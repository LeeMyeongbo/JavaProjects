package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML private AnchorPane pane;
    @FXML private CheckBox autoStartCheck;
    @FXML private Label startVolumeLabel, opacityLabel, warningLabel;
    @FXML private Slider startVolumeSlider, opacitySlider;
    @FXML private ChoiceBox<Integer> moveTimeBox, vanishTimeBox;
    private HashMap<String, Integer> data;
    private final String moveTimeKey = "moveTime", vanishTimeKey = "vanishTime", startVolumeKey = "startVolume", opacityKey = "opacity", autoStartKey = "autoStart";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(Objects.requireNonNull(getClass().getResource("setting.dat")).getFile()));
            data = (HashMap<String, Integer>) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        moveTimeBox.getItems().addAll(5, 10, 15, 20, 30);
        moveTimeBox.setValue(data.get(moveTimeKey));

        vanishTimeBox.getItems().addAll(3, 5, 10, 15, 20);
        vanishTimeBox.setValue(data.get(vanishTimeKey));

        startVolumeSlider.setValue(data.get(startVolumeKey));
        startVolumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            startVolumeSlider.setValue(t1.intValue());
            startVolumeLabel.setText(String.valueOf(t1.intValue()));
        });
        startVolumeLabel.setText(String.valueOf(data.get(startVolumeKey)));

        opacitySlider.setValue(data.get(opacityKey) / 10.0);
        opacitySlider.valueProperty().addListener((observableValue, number, t1) -> {
            double val = Math.round(t1.doubleValue() * 10) / 10.0;
            opacitySlider.setValue(val);
            opacityLabel.setText(String.valueOf(val));
        });
        opacityLabel.setText(String.valueOf(data.get(opacityKey) / 10.0));

        autoStartCheck.setSelected(data.get(autoStartKey) != 0);
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
        data.put(moveTimeKey, moveTimeBox.getValue());
        data.put(vanishTimeKey, vanishTimeBox.getValue());
        data.put(startVolumeKey, (int)startVolumeSlider.getValue());
        data.put(opacityKey, (int)(opacitySlider.getValue() * 10));
        data.put(autoStartKey, autoStartCheck.isSelected() ? 1 : 0);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(Objects.requireNonNull(getClass().getResource("setting.dat")).getFile()));
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }
}
