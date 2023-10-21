package com.player.mediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static com.player.mediaplayer.Utils.getResource;

public class SettingApplication extends Application {

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getResource(getClass(), "setting-view.fxml"));
        stage.setScene(createScene(loader));
        stage.setTitle("설정");
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.getIcons().add(new Image(getResource(getClass(), "settingicon.png").toExternalForm()));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private Scene createScene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
