package com.player.mediaplayer;

import com.google.common.annotations.VisibleForTesting;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = createLoader();
        Scene scene = createScene(loader);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("app-style.css")).toString());
        Controller controller = loader.getController();

        setStage(stage, scene, controller);
        stage.show();
    }

    FXMLLoader createLoader() {
        return new FXMLLoader(getClass().getResource("app-view.fxml"));
    }

    Scene createScene(FXMLLoader loader) {
        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Image createMediaPlayerIcon() {
        return new Image(Objects.requireNonNull(getClass().getResource("video_player_icon.png")).toExternalForm());
    }

    void setStage(Stage stage, Scene scene, Controller controller) {
        stage.setTitle("동영상 플레이어");
        stage.setMinWidth(900.0);
        stage.setMinHeight(600.0);
        stage.setScene(scene);

        CloseHandler handler = createCloseHandler();
        handler.setController(controller);
        stage.setOnCloseRequest(handler);
        stage.getIcons().add(createMediaPlayerIcon());
    }

    CloseHandler createCloseHandler() {
        return new CloseHandler();
    }

    static class CloseHandler implements EventHandler<WindowEvent> {
        private Controller controller;

        void setController(Controller controller) {
            this.controller = controller;
        }

        @Override
        public void handle(WindowEvent windowEvent) {
            controller.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @VisibleForTesting
    void runApp() {
        main(null);
    }
}
