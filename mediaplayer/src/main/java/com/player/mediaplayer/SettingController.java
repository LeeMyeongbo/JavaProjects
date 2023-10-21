package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static com.player.mediaplayer.SettingDataManager.*;

public class SettingController implements Initializable {
    @FXML private AnchorPane pane;
    @FXML private CheckBox autoStartCheck;
    @FXML private Label startVolumeLabel, opacityLabel, warningLabel;
    @FXML private Slider startVolumeSlider, opacitySlider;
    @FXML private ChoiceBox<Integer> moveTimeBox, vanishTimeBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMoveTimeCheckComponent();
        setVanishTimeCheckComponent();
        setStartVolumeCheckComponent();
        setOpacityCheckComponent();
        setAutoStartCheckComponent();
    }

    private void setMoveTimeCheckComponent() {
        moveTimeBox.getItems().addAll(5, 10, 15, 20, 30);
        moveTimeBox.setValue(getData("moveTime"));
    }

    private void setVanishTimeCheckComponent() {
        vanishTimeBox.getItems().addAll(3, 5, 10, 15, 20);
        vanishTimeBox.setValue(getData("vanishTime"));
    }

    private void setStartVolumeCheckComponent() {
        startVolumeSlider.setValue(getData("startVolume"));
        startVolumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            startVolumeSlider.setValue(t1.intValue());
            startVolumeLabel.setText(String.valueOf(t1.intValue()));
        });
        startVolumeLabel.setText(String.valueOf(getData("startVolume")));
    }

    private void setOpacityCheckComponent() {
        opacitySlider.setValue(getData("opacity") / 10.0);
        opacitySlider.valueProperty().addListener((observableValue, number, t1) -> {
            double val = Math.round(t1.doubleValue() * 10) / 10.0;
            opacitySlider.setValue(val);
            opacityLabel.setText(String.valueOf(val));
        });
        opacityLabel.setText(String.valueOf(getData("opacity") / 10.0));
    }

    private void setAutoStartCheckComponent() {
        autoStartCheck.setSelected(getData("autoStart") != 0);
        autoStartCheck.selectedProperty().addListener((observableValue, aBoolean, t1) -> warningLabel.setVisible(t1));
        warningLabel.setVisible(autoStartCheck.isSelected());
    }

    @FXML
    public void saveSetting() {
        applySetting();
        close();
    }

    @FXML
    public void cancelSetting() {
        close();
    }

    @FXML
    public void applySetting() {
        putData("moveTime", moveTimeBox.getValue());
        putData("vanishTime", vanishTimeBox.getValue());
        putData("startVolume", (int)startVolumeSlider.getValue());
        putData("opacity", (int)(opacitySlider.getValue() * 10));
        putData("autoStart", autoStartCheck.isSelected() ? 1 : 0);

        saveSettingsOnFile();
    }

    public void close() {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }
}
