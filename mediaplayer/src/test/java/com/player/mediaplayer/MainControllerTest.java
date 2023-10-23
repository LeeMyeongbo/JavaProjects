package com.player.mediaplayer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

public class MainControllerTest extends ApplicationTest {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void start(Stage stage) {
        LOG.info("start : MainApplicationTest started..");
        MainApplication mainApplication = new MainApplication();
        mainApplication.start(stage);
    }

    @After
    public void teardown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        LOG.info("teardown : MainApplicationTest ended..");
    }
}
