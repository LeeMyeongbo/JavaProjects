package com.player.mediaplayer;

import com.google.common.annotations.VisibleForTesting;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AppMain extends Application {
    private Controller controller;

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(getResource("app-view.fxml"));
        Scene scene = createScene(loader);
        scene.getStylesheets().add(getResource("app-style.css").toString());
        controller = loader.getController();

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
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.getIcons().add(new Image(getResource("video_player_icon.png").toExternalForm()));
    }

    URL getResource(String source) {
        return Objects.requireNonNull(getClass().getResource(source));
    }

    public static void main(String[] args) {
        launch();
    }

    @VisibleForTesting
    final Controller getController() {
        return controller;
    }
}
