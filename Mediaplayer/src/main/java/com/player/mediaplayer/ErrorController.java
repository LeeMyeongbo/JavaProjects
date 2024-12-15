package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ErrorController {

    @FXML private AnchorPane errorArea;

    @FXML
    public void onMouseClickOKButton() {
        Stage stage = (Stage) errorArea.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onKeyReleased(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            onMouseClickOKButton();
        }
    }
}
