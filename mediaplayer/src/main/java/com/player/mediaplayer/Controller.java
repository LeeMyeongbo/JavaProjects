package com.player.mediaplayer;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import java.util.Objects;

public class Controller {
    private boolean playing;
    private int sound = 20;
    @FXML private ImageView playButton, backButton, forwardButton, preButton, nextButton, volumeButton, settingButton;
    @FXML private MediaView mediaArea;

    @FXML
    public void onMouseSettingsEntered() {
        settingButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("settings-moused.png"))));
    }
    @FXML
    public void onMouseSettingsExited() {
        settingButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("settings.png"))));
    }
    @FXML
    public void onMouseSettingsPressed() {
        settingButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("settings-pressed.png"))));
    }
    @FXML
    public void onMouseSettingsReleased() {
        settingButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("settings-moused.png"))));
    }

    @FXML
    public void onMousePrebuttonEntered() {
        preButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pre-moused.png"))));
    }
    @FXML
    public void onMousePrebuttonExited() {
        preButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pre.png"))));
    }
    @FXML
    public void onMousePrebuttonPressed() {
        preButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pre-pressed.png"))));
    }
    @FXML
    public void onMousePrebuttonReleased() {
        preButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pre-moused.png"))));
    }

    @FXML
    public void onMouseBackbuttonEntered() {
        backButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("back-moused.png"))));
    }
    @FXML
    public void onMouseBackbuttonExited() {
        backButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("back.png"))));
    }
    @FXML
    public void onMouseBackbuttonPressed() {
        backButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("back-pressed.png"))));
    }
    @FXML
    public void onMouseBackbuttonReleased() {
        backButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("back-moused.png"))));
    }

    @FXML
    public void onMousePlaybuttonEntered() {
        if (playing)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-moused.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-moused.png"))));
    }
    @FXML
    public void onMousePlaybuttonExited() {
        if (playing)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play.png"))));
    }
    @FXML
    public void onMousePlaybuttonPressed() {
        if (playing)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-pressed.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-pressed.png"))));
    }
    @FXML
    public void onMousePlaybuttonReleased() {
        if (playing) {
            playing = false;
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-moused.png"))));
        }
        else {
            playing = true;
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-moused.png"))));
        }
    }

    @FXML
    public void onMouseForwardbuttonEntered() {
        forwardButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("forward-moused.png"))));
    }
    @FXML
    public void onMouseForwardbuttonExited() {
        forwardButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("forward.png"))));
    }
    @FXML
    public void onMouseForwardbuttonPressed() {
        forwardButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("forward-pressed.png"))));
    }
    @FXML
    public void onMouseForwardbuttonReleased() {
        forwardButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("forward-moused.png"))));
    }

    @FXML
    public void onMouseNextbuttonEntered() {
        nextButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("next-moused.png"))));
    }
    @FXML
    public void onMouseNextbuttonExited() {
        nextButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("next.png"))));
    }
    @FXML
    public void onMouseNextbuttonPressed() {
        nextButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("next-pressed.png"))));
    }
    @FXML
    public void onMouseNextbuttonReleased() {
        nextButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("next-moused.png"))));
    }

    @FXML
    public void onMouseVolumebuttonEntered() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume-moused.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute-moused.png"))));
    }
    @FXML
    public void onMouseVolumebuttonExited() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute.png"))));
    }
    @FXML
    public void onMouseVolumebuttonPressed() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume-pressed.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute-pressed.png"))));
    }
    @FXML
    public void onMouseVolumebuttonReleased() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume-moused.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute-moused.png"))));
    }
}
