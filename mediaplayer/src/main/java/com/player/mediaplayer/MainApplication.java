package com.player.mediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import static com.player.mediaplayer.Utils.getResource;

public class MainApplication extends Application {
    private MainController mainController;

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getResource(getClass(), "app-view.fxml"));
        Scene scene = createScene(loader);
        scene.getStylesheets().add(getResource(getClass(), "app-style.css").toString());
        mainController = loader.getController();

        setStage(stage, scene);
        stage.show();
    }

    Scene createScene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setStage(Stage stage, Scene scene) {
        stage.setTitle("동영상 플레이어");
        stage.setMinWidth(900.0);
        stage.setMinHeight(600.0);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> mainController.shutdown());
        stage.getIcons().add(new Image(getResource(getClass(), "video_player_icon.png").toExternalForm()));
    }

    public static void main(String[] args) {
        launch();
    }
}
