package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML private CheckBox autoStartCheck;
    @FXML private Button confirmButton, cancelButton, applyButton;
    @FXML private Label startVolumeLabel, opacityLabel;
    @FXML private Slider startVolumeSlider, opacitySlider;
    @FXML private ChoiceBox<Integer> moveTimeBox, vanishTimeBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
