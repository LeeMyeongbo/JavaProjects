package com.player.mediaplayer;

import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.util.concurrent.TimeoutException;

public class ControllerTest extends ApplicationTest {
    private Controller controller;

    @Override
    public void start(Stage stage) {
        AppMain appMain = new AppMain();
        appMain.start(new Stage());
        controller = appMain.getController();
    }

    @After
    public void teardown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    final public Controller getController() {
        return controller;
    }
}
