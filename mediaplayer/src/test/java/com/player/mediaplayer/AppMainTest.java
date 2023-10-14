package com.player.mediaplayer;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class AppMainTest {
    private static AppMain mockTestTarget;
    private static FXMLLoader mockLoader;
    private static Scene mockScene;
    private static ObservableList<String> mockStringList;
    private static ObservableList<Image> mockImageList;
    private static Controller mockController;
    private static Stage mockStage;
    private static Image mockImage;
    private static AppMain.CloseHandler mockHandler;

    private static MockedStatic<AppMain> mockedStaticTestTarget;

    private static void setupTest() {
        mockTestTarget = mock(AppMain.class);
        mockLoader = mock(FXMLLoader.class);
        mockScene = mock(Scene.class);
        mockStringList = mock(ObservableList.class);
        mockImageList = mock(ObservableList.class);
        mockController = mock(Controller.class);
        mockStage = mock(Stage.class);
        mockImage = mock(Image.class);
        mockHandler = mock(AppMain.CloseHandler.class);

        mockedStaticTestTarget = mockStatic(AppMain.class);

        lenient().doCallRealMethod().when(mockTestTarget).runApp();
        lenient().doCallRealMethod().when(mockTestTarget).start(mockStage);
        lenient().doCallRealMethod().when(mockTestTarget).setStage(mockStage, mockScene, mockController);
        lenient().doCallRealMethod().when(mockHandler).setController(mockController);
        lenient().doCallRealMethod().when(mockHandler).handle(any());

        lenient().when(mockTestTarget.createLoader()).thenReturn(mockLoader);
        lenient().when(mockTestTarget.createMediaPlayerIcon()).thenReturn(mockImage);
        lenient().when(mockTestTarget.createCloseHandler()).thenReturn(mockHandler);
        lenient().when(mockScene.getStylesheets()).thenReturn(mockStringList);
        lenient().when(mockLoader.getController()).thenReturn(mockController);
        lenient().when(mockStage.getIcons()).thenReturn(mockImageList);
    }

    private static void teardownTest() {
        mockedStaticTestTarget.close();
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class TestMain {
        @Before
        public void setup() {
            setupTest();
        }

        @After
        public void teardown() {
            teardownTest();
        }

        @Test
        public void test() {
            mockTestTarget.runApp();

            mockedStaticTestTarget.verify(() -> AppMain.main(null), times(1));
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class TestStart {
        @Before
        public void setup() {
            setupTest();
        }

        @After
        public void teardown() {
            teardownTest();
        }

        @Test
        public void testWhenExceptionIsNotThrown() {
            when(mockTestTarget.createScene(mockLoader)).thenReturn(mockScene);

            mockTestTarget.start(mockStage);
            verify(mockTestTarget, times(1)).createLoader();
            verify(mockTestTarget, times(1)).createScene(mockLoader);
            verify(mockScene, times(1)).getStylesheets();
            verify(mockStringList, times(1)).add(anyString());
            verify(mockLoader, times(1)).getController();
            verify(mockTestTarget, times(1)).setStage(mockStage, mockScene, mockController);
            verify(mockStage, times(1)).show();
        }

        @Test
        public void testWhenExceptionIsThrown() {
            when(mockTestTarget.createScene(mockLoader)).thenThrow(new RuntimeException(new IOException()));

            Assert.assertThrows(RuntimeException.class, () -> mockTestTarget.start(mockStage));
            verify(mockTestTarget, times(1)).createLoader();
            verify(mockTestTarget, times(1)).createScene(mockLoader);
            verify(mockScene, times(0)).getStylesheets();
            verify(mockStringList, times(0)).add(anyString());
            verify(mockLoader, times(0)).getController();
            verify(mockTestTarget, times(0)).setStage(mockStage, mockScene, mockController);
            verify(mockStage, times(0)).show();
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public static class TestSetStage {
        @Before
        public void setup() {
            setupTest();
        }

        @After
        public void teardown() {
            teardownTest();
        }

        @Test
        public void test() {
            mockTestTarget.setStage(mockStage, mockScene, mockController);

            verify(mockStage, times(1)).setTitle("동영상 플레이어");
            verify(mockStage, times(1)).setMinWidth(900.0);
            verify(mockStage, times(1)).setMinHeight(600.0);
            verify(mockStage, times(1)).setScene(mockScene);
            verify(mockTestTarget, times(1)).createCloseHandler();
            verify(mockHandler, times(1)).setController(mockController);
            verify(mockStage, times(1)).setOnCloseRequest(mockHandler);
            verify(mockStage, times(1)).getIcons();
            verify(mockImageList, times(1)).add(mockImage);

            mockHandler.handle(null);
            verify(mockController, times(1)).shutdown();
        }
    }
}
