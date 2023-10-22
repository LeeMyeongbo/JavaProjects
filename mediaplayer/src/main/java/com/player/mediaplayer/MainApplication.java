package com.player.mediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.player.mediaplayer.Utils.getResource;

public class MainApplication extends Application {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void start(Stage stage) {
        LOG.info("MediaPlayer started..");
        FXMLLoader loader = new FXMLLoader(getResource(getClass(), "app-view.fxml"));
        Scene scene = createScene(loader);
        scene.getStylesheets().add(getResource(getClass(), "app-style.css").toString());
        MainController controller = loader.getController();

        setStage(stage, scene, controller);
        LOG.info("MediaPlayer is being showed...");
        stage.show();
    }

    private Scene createScene(FXMLLoader loader) {
        try {
            Scene scene = new Scene(loader.load());
            LOG.info("Scene is created successfully!!");
            return scene;
        } catch (IOException e) {
            LOG.fatal("Scene is not created by this error : \n" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void setStage(Stage stage, Scene scene, MainController controller) {
        setStageBasicInfo(stage);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.shutdown());
        stage.getIcons().add(new Image(getResource(getClass(), "video_player_icon.png").toExternalForm()));
    }

    private void setStageBasicInfo(Stage stage) {
        String title = "동영상 플레이어";
        double minWidth = 900.0, minHeight = 600.0;

        stage.setTitle(title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        LOG.info("MediaPlayer title : " + title + ", minWidth : " + minWidth + ", minHeight : " + minHeight);
    }

    public static void main(String[] args) {
        launch();
    }
}
