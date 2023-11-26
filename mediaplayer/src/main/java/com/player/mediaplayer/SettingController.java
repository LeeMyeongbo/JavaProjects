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
    @FXML private AnchorPane settingArea;
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
        moveTimeBox.setValue(getMoveTimeValue());
    }

    private void setVanishTimeCheckComponent() {
        vanishTimeBox.getItems().addAll(3, 5, 10, 15, 20);
        vanishTimeBox.setValue(getVanishTimeValue());
    }

    private void setStartVolumeCheckComponent() {
        startVolumeSlider.setValue(getStartVolumeValue());
        startVolumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            startVolumeSlider.setValue(t1.intValue());
            startVolumeLabel.setText(String.valueOf(t1.intValue()));
        });
        startVolumeLabel.setText(String.valueOf(getStartVolumeValue()));
    }

    private void setOpacityCheckComponent() {
        opacitySlider.setValue(getOpacityValue() / 10.0);
        opacitySlider.valueProperty().addListener((observableValue, number, t1) -> {
            double val = Math.round(t1.doubleValue() * 10) / 10.0;
            opacitySlider.setValue(val);
            opacityLabel.setText(String.valueOf(val));
        });
        opacityLabel.setText(String.valueOf(getOpacityValue() / 10.0));
    }

    private void setAutoStartCheckComponent() {
        autoStartCheck.setSelected(getAutoStartValue() != 0);
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
        putMoveTimeValue(moveTimeBox.getValue());
        putVanishTimeValue(vanishTimeBox.getValue());
        putStartVolumeValue((int) startVolumeSlider.getValue());
        putOpacityValue((int) (opacitySlider.getValue() * 10));
        putAutoStartValue(autoStartCheck.isSelected() ? 1 : 0);

        saveSettingsOnFile();
    }

    public void close() {
        Stage stage = (Stage) settingArea.getScene().getWindow();
        stage.close();
    }
}
