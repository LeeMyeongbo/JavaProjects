package com.player.mediaplayer;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

public class MainControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        MainApplication mainApplication = new MainApplication();
        mainApplication.start(new Stage());
    }

    @After
    public void teardown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}
