package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML private AnchorPane pane;
    @FXML private CheckBox autoStartCheck;
    @FXML private Label startVolumeLabel, opacityLabel, warningLabel;
    @FXML private Slider startVolumeSlider, opacitySlider;
    @FXML private ChoiceBox<Integer> moveTimeBox, vanishTimeBox;
    private HashMap<String, Integer> data;
    private final String moveTime = "moveTime", vanishTime = "vanishTime", startVolume = "startVolume", opacity = "opacity", autoStart = "autoStart";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSetting();

        moveTimeBox.getItems().addAll(5, 10, 15, 20, 30);
        moveTimeBox.setValue(data.get(moveTime));

        vanishTimeBox.getItems().addAll(3, 5, 10, 15, 20);
        vanishTimeBox.setValue(data.get(vanishTime));

        startVolumeSlider.setValue(data.get(startVolume));
        startVolumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            startVolumeSlider.setValue(t1.intValue());
            startVolumeLabel.setText(String.valueOf(t1.intValue()));
        });
        startVolumeLabel.setText(String.valueOf(data.get(startVolume)));

        opacitySlider.setValue(data.get(opacity) / 10.0);
        opacitySlider.valueProperty().addListener((observableValue, number, t1) -> {
            double val = Math.round(t1.doubleValue() * 10) / 10.0;
            opacitySlider.setValue(val);
            opacityLabel.setText(String.valueOf(val));
        });
        opacityLabel.setText(String.valueOf(data.get(opacity) / 10.0));

        autoStartCheck.setSelected(data.get(autoStart) != 0);
        autoStartCheck.selectedProperty().addListener((observableValue, aBoolean, t1) -> warningLabel.setVisible(t1));
        warningLabel.setVisible(autoStartCheck.isSelected());
    }

    public void loadSetting() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Mediaplayer\\setting.dat"));
            data = (HashMap<String, Integer>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {                         // 경로에 설정 파일이 없을 때
            data = new HashMap<>();
            data.put(moveTime, 10);
            data.put(vanishTime, 3);
            data.put(startVolume, 20);
            data.put(opacity, 10);
            data.put(autoStart, 0);

            try {                                                   // C 드라이브에 Mediaplayer 폴더 만들고 설정 파일 저장
                File path = new File("C:\\Mediaplayer");
                if (path.mkdirs()) {
                    File savedFile = new File(path, "setting.dat");
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedFile.getAbsolutePath()));
                    oos.writeObject(data);
                    oos.flush();
                    oos.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* 확인 버튼 눌렀을 때 */
    @FXML
    public void saveSetting() {
        applySetting();
        close();
    }

    /* 취소 버튼 눌렀을 때 */
    @FXML
    public void cancelSetting() {
        close();
    }

    /* 적용 버튼 눌렀을 때 */
    @FXML
    public void applySetting() {
        data.put(moveTime, moveTimeBox.getValue());
        data.put(vanishTime, vanishTimeBox.getValue());
        data.put(startVolume, (int)startVolumeSlider.getValue());
        data.put(opacity, (int)(opacitySlider.getValue() * 10));
        data.put(autoStart, autoStartCheck.isSelected() ? 1 : 0);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Mediaplayer\\setting.dat"));
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 창 닫을 때 */
    public void close() {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }
}
