package com.player.mediaplayer;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.testfx.api.FxAssert.verifyThat;

public class MainApplicationTest extends ApplicationTest {
    private Stage testStage;
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void start(Stage stage) {
        LOG.info("start : MainApplicationTest started..");
        testStage = new Stage();
        new MainApplication().start(testStage);
    }

    @After
    public void teardown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        LOG.info("teardown : MainApplicationTest ended..");
    }

    @Test
    public void testStart() {
        clickOn("#appArea");

        assert testStage.getScene().getStylesheets().contains(Objects.requireNonNull(
            getClass().getResource("app-style.css")).toString());
    }

    @Test
    public void testSetStage() {
        clickOn("#appArea");

        verifyThat("#appArea", Node::isResizable);

        assert testStage.getTitle().equals("동영상 플레이어");
        assert testStage.getMinWidth() == 900.0;
        assert testStage.getMinHeight() == 600.0;

        ObservableList<Image> list = testStage.getIcons();
        for (Image image : list) {
            if (Objects.equals(image.getUrl(), Objects.requireNonNull(
                getClass().getResource("video_player_icon.png")).toExternalForm())) {
                LOG.info("testSetStage : MediaPlayer icon is applied correctly!!");
                return;
            }
        }
        LOG.info("testSetStage : MediaPlayer icon is wrong!!");
        throw new RuntimeException("failed : MediaPlayer icon is wrong!!");
    }
}
