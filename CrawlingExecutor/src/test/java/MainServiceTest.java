import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainServiceTest {

    private static boolean isNormal = true;

    private MockedConstruction<Scanner> scannerMockedConstruction;
    private MockedConstruction<FileOutputStream> fileOutputStreamMockedConstruction;
    private MockedConstruction<BufferedOutputStream> bufferedOutputStreamMockedConstruction;
    private MockedConstruction<Crawler> crawlerMockedConstruction;
    private MockedConstruction<SpeechExecutor> speechExecutorMockedConstruction;

    @BeforeEach
    void setUp() {
        isNormal = true;
        scannerMockedConstruction = mockConstruction(Scanner.class, (mock, c) ->
            when(mock.next()).thenReturn("junit")
        );
        fileOutputStreamMockedConstruction = mockConstruction(FileOutputStream.class, (mock, c) ->
            assertEquals("NewsTTS/result.txt", c.arguments().get(0))
        );
        bufferedOutputStreamMockedConstruction = mockConstruction(BufferedOutputStream.class, (mock, c) -> {
            assertEquals(fileOutputStreamMockedConstruction.constructed().get(0), c.arguments().get(0));
            if (!isNormal) {
                doThrow(IOException.class).when(mock).write(any());
            }
        });
        crawlerMockedConstruction = mockConstruction(Crawler.class, (mock, c) ->
            when(mock.crawl(anyString())).thenReturn("test")
        );
        speechExecutorMockedConstruction = mockConstruction(SpeechExecutor.class);
    }

    @AfterEach
    void tearDown() {
        scannerMockedConstruction.close();
        fileOutputStreamMockedConstruction.close();
        bufferedOutputStreamMockedConstruction.close();
        crawlerMockedConstruction.close();
        speechExecutorMockedConstruction.close();
    }

    @DisplayName("MainService IOException 테스트")
    @Test
    void testWhenIOException() throws IOException {
        isNormal = false;
        Throwable exception = assertThrowsExactly(RuntimeException.class, () -> MainService.main(new String[0]));

        assertEquals("파일 저장 중 오류가 발생하였습니다...", exception.getMessage());
        assertInstanceOf(IOException.class, exception.getCause());

        verify(crawlerMockedConstruction.constructed().get(0), times(1)).crawl("junit");
        verify(scannerMockedConstruction.constructed().get(0), times(1)).nextLine();
        verify(bufferedOutputStreamMockedConstruction.constructed().get(0), times(1)).write("test".getBytes());
        assertTrue(speechExecutorMockedConstruction.constructed().isEmpty());
    }

    @DisplayName("MainService 정상동작 테스트")
    @Test
    void test() throws IOException {
        assertDoesNotThrow(() -> MainService.main(new String[0]));

        verify(crawlerMockedConstruction.constructed().get(0), times(1)).crawl("junit");
        verify(scannerMockedConstruction.constructed().get(0), times(1)).nextLine();
        verify(bufferedOutputStreamMockedConstruction.constructed().get(0), times(1)).write("test".getBytes());
        verify(speechExecutorMockedConstruction.constructed().get(0), times(1)).playNewsSpeech();
    }
}
