package com.player.mediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class AppMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(AppMain.class.getResource("app-view.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(Objects.requireNonNull(AppMain.class.getResource("app-style.css")).toString());

        Controller controller = loader.getController();

        stage.setTitle("동영상 플레이어");
        stage.setMinWidth(600.0);
        stage.setMinHeight(400.0);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.getIcons().add(new Image(Objects.requireNonNull(AppMain.class.getResource("video_player_icon.png")).toExternalForm()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
