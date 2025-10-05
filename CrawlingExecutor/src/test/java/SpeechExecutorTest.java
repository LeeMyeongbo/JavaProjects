import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpeechExecutorTest {

    private static int status = 0;

    private final SpeechExecutor testTarget = new SpeechExecutor();
    private final Process process = mock(Process.class);

    private MockedConstruction<ProcessBuilder> processBuilderMockedConstruction;
    private MockedConstruction<FileInputStream> fileInputStreamMockedConstruction;

    @BeforeEach
    void setUp() throws InterruptedException {
        status = 0;

        processBuilderMockedConstruction = mockConstruction(ProcessBuilder.class, (mock, c) ->
            when(mock.start()).thenReturn(process)
        );
        fileInputStreamMockedConstruction = mockConstruction(FileInputStream.class, (mock, c) ->
            assertEquals("NewsTTS/news.mp3", c.arguments().get(0))
        );
        when(process.waitFor()).thenAnswer(invocationOnMock -> {
            if (status == 2) {
                throw new InterruptedException();
            } else {
                return status;
            }
        });
    }

    @AfterEach
    void tearDown() {
        processBuilderMockedConstruction.close();
        fileInputStreamMockedConstruction.close();
    }

    @DisplayName("음성 합성 시 Exception 테스트")
    @Test
    void testInterruptedExceptionWhenSynthesize() throws IOException, InterruptedException {
        status = 2;
        Throwable exception = assertThrowsExactly(RuntimeException.class, testTarget::playNewsSpeech);

        assertEquals("음성 합성 중 오류가 발생하였습니다...", exception.getMessage());
        assertInstanceOf(InterruptedException.class, exception.getCause());

        ProcessBuilder builder = processBuilderMockedConstruction.constructed().get(0);
        verify(builder, times(1)).directory(any(File.class));
        verify(builder, times(1)).command(anyString(), anyString(), anyString());
        verify(builder, times(1)).inheritIO();
        verify(builder, times(1)).start();
        verify(process, times(1)).waitFor();
        assertTrue(fileInputStreamMockedConstruction.constructed().isEmpty());
    }

    @DisplayName("음성 합성이 성공적으로 완료되지 못하고 종료되었을 때 테스트")
    @Test
    void testAbnormalExitWhenSynthesize() throws IOException, InterruptedException {
        status = 1;
        Throwable exception = assertThrowsExactly(RuntimeException.class, testTarget::playNewsSpeech);

        assertEquals("음성 합성이 정상적으로 이루어지지 못했습니다...", exception.getMessage());

        ProcessBuilder builder = processBuilderMockedConstruction.constructed().get(0);
        verify(builder, times(1)).directory(any(File.class));
        verify(builder, times(1)).command(anyString(), anyString(), anyString());
        verify(builder, times(1)).inheritIO();
        verify(builder, times(1)).start();
        verify(process, times(1)).waitFor();
        assertTrue(fileInputStreamMockedConstruction.constructed().isEmpty());
    }

    @DisplayName("음성 재생 중 Exception 테스트")
    @Test
    void testUnsupportedAudioFileExceptionWhenPlayNewsSpeech() {
        try (MockedConstruction<Player> playerMockedConstruction = mockConstruction(Player.class, (mock, c) -> {
            assertEquals(fileInputStreamMockedConstruction.constructed().get(0), c.arguments().get(0));
            doThrow(JavaLayerException.class).when(mock).play();
        })) {
            Throwable exception = assertThrowsExactly(RuntimeException.class, testTarget::playNewsSpeech);

            assertEquals("음성 재생 중 오류가 발생하였습니다...", exception.getMessage());
            assertInstanceOf(JavaLayerException.class, exception.getCause());

            verify(playerMockedConstruction.constructed().get(0), times(1)).play();
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("SpeechExecutor 정상 동작 시 테스트")
    @Test
    void testWhenNormal() {
        try (MockedConstruction<Player> playerMockedConstruction = mockConstruction(Player.class, (mock, c) ->
            assertEquals(fileInputStreamMockedConstruction.constructed().get(0), c.arguments().get(0))
        )) {
            assertDoesNotThrow(testTarget::playNewsSpeech);

            verify(playerMockedConstruction.constructed().get(0), times(1)).play();
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }
    }
}
