package com.player.mediaplayer;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Timer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class ControllerTest {
    private static Controller testTarget, mockController;
    private static Timer mockTimer;
    private static Tooltip mockTooltip;
    private static HashMap<String, Integer> mockData;
    private static StackPane mockAppArea;
    private static ImageView mockPlayButton, mockBackButton, mockForwardButton, mockOneButton, mockRepeatButton,
        mockVolumeButton, mockFileButton, mockSettingButton;
    private static MediaView mockMediaArea;
    private static HBox mockButtonArea, mockVolumeArea, mockEtcArea;
    private static VBox mockBgArea;
    private static Slider mockPlayBar, mockVolumeBar;
    private static Text mockVolumeText;
    private static Label mockCurTimeLabel, mockEndTimeLabel;

    private static void setupTest() {
        testTarget = new Controller();
        mockController = mock(Controller.class);
        mockTimer = mock(Timer.class);
        mockTooltip = mock(Tooltip.class);
        mockData = mock(HashMap.class);
        mockAppArea = mock(StackPane.class);
        mockPlayButton = mock(ImageView.class);
        mockBackButton = mock(ImageView.class);
        mockForwardButton = mock(ImageView.class);
        mockOneButton = mock(ImageView.class);
        mockRepeatButton = mock(ImageView.class);
        mockVolumeButton = mock(ImageView.class);
        mockFileButton = mock(ImageView.class);
        mockSettingButton = mock(ImageView.class);
        mockMediaArea = mock(MediaView.class);
        mockButtonArea = mock(HBox.class);
        mockVolumeArea = mock(HBox.class);
        mockEtcArea = mock(HBox.class);
        mockBgArea = mock(VBox.class);
        mockPlayBar = mock(Slider.class);
        mockVolumeBar = mock(Slider.class);
        mockVolumeText = mock(Text.class);
        mockCurTimeLabel = mock(Label.class);
        mockEndTimeLabel = mock(Label.class);

        lenient().doCallRealMethod().when(mockController).initialize(any(), any());
        lenient().when(mockController.createTooltip()).thenReturn(mockTooltip);
    }

    private static void teardownTest() { }

    @RunWith(MockitoJUnitRunner.class)
    public static class TestInitialize {

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
            mockController.initialize(null, null);

            verify(mockController, times(1)).loadSetting();
            verify(mockController, times(1)).enableMediaReadyByDragAndDrop();
            verify(mockController, times(1)).enableMediaPlayAfterDragAndDrop();
            verify(mockController, times(1)).enableMediaControlWithKeyboard();
            verify(mockController, times(1)).setInitialVolume();
            verify(mockController, times(1)).setVolumeBarEvent();
            verify(mockController, times(1)).setPlayBarEvent();
            verify(mockController, times(1)).setMediaSizeByWindowSize();
            verify(mockController, times(1)).setInitialVolumeButton();
            verify(mockController, times(1)).createTooltip();
        }
    }
}
