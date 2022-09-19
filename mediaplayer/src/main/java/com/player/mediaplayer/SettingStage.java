package com.player.mediaplayer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SettingStage extends Stage {
    public SettingStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(SettingStage.class.getResource("setting-view.fxml"));
        setScene(new Scene(loader.load()));
        setTitle("설정");
        resizableProperty().setValue(Boolean.FALSE);
        getIcons().add(new Image(Objects.requireNonNull(SettingStage.class.getResource("settingicon.png")).toExternalForm()));
        initModality(Modality.APPLICATION_MODAL);
    }
}
