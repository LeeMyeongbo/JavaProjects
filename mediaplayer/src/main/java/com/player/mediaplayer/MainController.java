package com.player.mediaplayer;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static com.player.mediaplayer.SettingDataManager.*;
import static com.player.mediaplayer.Utils.*;

public class MainController implements Initializable {
    private final String PLAYBAR_STYLE = "-fx-background-color: linear-gradient(to right, #0303f2 %f%%, #15ff00 %f%%);" +
        "\n-fx-pref-height: %d;";
    private boolean isRepeating, isFinding, isFullScreen, isPlaying;
    private boolean isPlayBarMouseOn, isPlayButtonMouseOn, isForwardButtonMouseOn, isBackwardButtonMouseOn, isFileButtonMouseOn;
    private int mouse_stop;
    private double sound;
    private Timer timer;
    private Tooltip tooltip;
    @FXML private StackPane appArea;
    @FXML private ImageView playButton, backButton, forwardButton, oneButton, repeatButton, volumeButton, fileButton, settingButton;
    @FXML private MediaView mediaArea;
    @FXML private HBox buttonArea, volumeArea, etcArea;
    @FXML private VBox bgArea;
    @FXML private Slider playBar, volumeBar;
    @FXML private Text volumeText;
    @FXML private Label curTimeLabel, endTimeLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSettingsFromFile();
        manageStartProgramRegistration();
        enableMediaReadyByDragAndDrop();
        enableMediaPlayAfterDragAndDrop();
        enableMediaControlWithKeyboard();
        setInitialVolume();
        setVolumeBarEvent();
        setPlayBarEvent();
        setMediaSizeByWindowSize();
        setInitialVolumeButton();

        tooltip = new Tooltip();
    }

    private void manageStartProgramRegistration() {
        String regPath = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
        String regName = "MediaPlayer";
        String regValue = "\"" + Paths.get("").toAbsolutePath() + "\\MediaPlayer.exe\" /autorun";

        if (getData("autoStart") == 1) {
            registerStartProgramWhenNotRegistered(regPath, regName, regValue);
        } else {
            deleteStartProgramWhenRegistered(regPath, regName);
        }
    }

    private void registerStartProgramWhenNotRegistered(String regPath, String regName, String regValue) {
        if (!Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, regPath, regName)) {
            Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, regPath, regName);
            Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, regPath, regName, regValue);
        }
    }

    private void deleteStartProgramWhenRegistered(String regPath, String regName) {
        if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, regPath, regName)) {
            Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, regPath, regName);
            Advapi32Util.registryDeleteKey(WinReg.HKEY_CURRENT_USER, regPath, regName);
        }
    }

    private void enableMediaReadyByDragAndDrop() {
        mediaArea.setOnDragOver(e -> {
            System.out.println("dragOver");
            if (e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.ANY);
            e.consume();
        });
    }

    private void enableMediaPlayAfterDragAndDrop() {
        mediaArea.setOnDragDropped(e -> {
            System.out.println("dragDropped");
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                success = true;
                setupMedia(db.getFiles().get(0));
            }
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void enableMediaControlWithKeyboard() {
        appArea.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE -> {
                    if (mediaArea.getMediaPlayer() == null) {
                        onMouseFileButtonReleased();
                        if (!isFileButtonMouseOn)
                            onMouseFileButtonExited();
                    } else {
                        onMousePlayButtonReleased();
                        if (!isPlayButtonMouseOn)
                            modifyPlayButton();
                    }
                } case RIGHT -> {
                    onMouseForwardButtonReleased();
                    if (!isForwardButtonMouseOn)
                        onMouseForwardButtonExited();
                } case LEFT -> {
                    onMouseBackButtonReleased();
                    if (!isBackwardButtonMouseOn)
                        onMouseBackButtonExited();
                } case ENTER -> {
                    isFullScreen = !isFullScreen;
                    ((Stage)mediaArea.getScene().getWindow()).setFullScreenExitHint("");
                    ((Stage)mediaArea.getScene().getWindow()).setFullScreen(isFullScreen);
                }
            }
        });
    }

    private void modifyPlayButton() {
        if (mediaArea.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(getStreamBySource(getClass(), "play.png")));
        else
            playButton.setImage(new Image(getStreamBySource(getClass(), "pause.png")));
    }

    private void setInitialVolume() {
        sound = getData("startVolume");
        volumeBar.setValue(sound);
    }

    private void setVolumeBarEvent() {
        volumeBar.valueProperty().addListener((observableValue, number, t1) -> {
            onMouseMoved();
            volumeText.setText(Integer.toString(t1.intValue()));
            if (t1.intValue() == 0)
                volumeButton.setImage(new Image(getStreamBySource(getClass(), "mute.png")));
            else
                volumeButton.setImage(new Image(getStreamBySource(getClass(), "volume.png")));

            MediaPlayer player = mediaArea.getMediaPlayer();
            if (player != null)
                player.setVolume(t1.intValue() / 100.0);
        });
        volumeBar.addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);
    }

    private void setPlayBarEvent() {
        playBar.setOnMouseDragged(e -> findPosition());
        playBar.valueProperty().addListener((observableValue, number, t1) -> {
            if (isPlayBarMouseOn)
                playBar.lookup(".track").setStyle(String.format(PLAYBAR_STYLE, t1.doubleValue() * 100, t1.doubleValue() * 100, 8));
            else
                playBar.lookup(".track").setStyle(String.format(PLAYBAR_STYLE, t1.doubleValue() * 100, t1.doubleValue() * 100, 2));
        });
        playBar.addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);
    }

    private void setMediaSizeByWindowSize() {
        DoubleProperty width = mediaArea.fitWidthProperty();
        width.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "width"));
        DoubleProperty height = mediaArea.fitHeightProperty();
        height.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "height"));
    }

    private void setInitialVolumeButton() {
        if (sound > 0)
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "volume.png")));
        else
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "mute.png")));
    }

    private void startTask() {
        timer = new Timer();
        mouse_stop = 0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mouse_stop++;
                if (mouse_stop == getData("vanishTime")) {
                    appArea.setCursor(Cursor.NONE);
                    setComponentVisibility(1.0 - getData("opacity") / 10.0);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    private void errorDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("형식 오류!");
        dialog.setWidth(400);
        dialog.setHeight(300);
        dialog.showAndWait();
    }

    private void setComponentVisibility(double v) {
        buttonArea.setOpacity(v);
        volumeArea.setOpacity(v);
        etcArea.setOpacity(v);
        playBar.setOpacity(v);
        curTimeLabel.setOpacity(v);
        endTimeLabel.setOpacity(v);

        if (v < 1.0)
            bgArea.setStyle("-fx-background-color: transparent;");
        else
            bgArea.setStyle("-fx-background-color: linear-gradient(to top, rgba(18, 8, 65, 0.65) 20%, transparent);");
    }

    private void setupMedia(File file) {
        try {
            disposePriorMedia();
            MediaPlayer player = new MediaPlayer(new Media(file.toURI().toString()));
            player.setOnReady(() -> {
                setMediaReadyToPlay(player);
                if (!isFinding)
                    updatePlayBarInRealTime(player);
                endTimeLabel.setText(getTime(player.getTotalDuration()));
            });
            player.setOnEndOfMedia(() -> setMediaReadyToPlay(player));
            player.setVolume(sound / 100.0);
            prepareInitialPlaying(player);
        } catch (NullPointerException ignored) {
            System.out.println("추가된 파일이 없습니다.");
        } catch (Exception e) {
            errorDialog();
        }
    }

    private void disposePriorMedia() {
        if (mediaArea.getMediaPlayer() != null) {
            MediaPlayer exMediaPlayer = mediaArea.getMediaPlayer();
            exMediaPlayer.stop();
            exMediaPlayer.dispose();
        }
    }

    private void setMediaReadyToPlay(MediaPlayer player) {
        player.stop();
        player.seek(Duration.ZERO);
        playButton.setImage(new Image(getStreamBySource(getClass(), "play.png")));
        appArea.setCursor(Cursor.DEFAULT);
        playBar.setValue(0.0);
        curTimeLabel.setText("00:00:00");
        isPlaying = isRepeating;
        if (timer != null)
            timer.cancel();
        timer = null;
        setComponentVisibility(1.0);
    }

    private void updatePlayBarInRealTime(MediaPlayer player) {
        player.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            double progress = player.getCurrentTime().toSeconds() / player.getTotalDuration().toSeconds();
            playBar.setValue(progress);
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        });
    }

    private void prepareInitialPlaying(MediaPlayer player) {
        mediaArea.setMediaPlayer(player);
        isRepeating = false;
        oneButton.setImage(new Image(getStreamBySource(getClass(), "one-selected.png")));
    }

    private void findPosition() {
        onMouseMoved();
        double pos = playBar.getValue();
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            player.pause();
            player.seek(Duration.seconds(pos * player.getTotalDuration().toSeconds()));
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    private String getTime(Duration d) {
        return String.format("%02d:%02d:%02d", (int)d.toHours(), (int)d.toMinutes() % 60, (int)d.toSeconds() % 60);
    }

    @FXML
    public void onMouseMoved() {
        setComponentVisibility(1.0);
        appArea.setCursor(Cursor.DEFAULT);
        mouse_stop = 0;
    }
    @FXML
    public void onMouseExited() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            setComponentVisibility(1.0 - getData("opacity") / 10.0);
    }
    @FXML
    public void onMouseDoubleClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            isFullScreen = !isFullScreen;
            ((Stage)mediaArea.getScene().getWindow()).setFullScreenExitHint("");
            ((Stage)mediaArea.getScene().getWindow()).setFullScreen(isFullScreen);
        }
    }

    @FXML
    public void onMousePlayButtonEntered() {
        isPlayButtonMouseOn = true;
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
            playButton.setImage(new Image(getStreamBySource(getClass(), "pause-moused.png")));
            tooltip.setText("일시 정지");
        } else {
            playButton.setImage(new Image(getStreamBySource(getClass(), "play-moused.png")));
            tooltip.setText("재생");
        }
        Tooltip.install(playButton.getParent(), tooltip);
    }
    @FXML
    public void onMousePlayButtonExited() {
        isPlayButtonMouseOn = false;
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(getStreamBySource(getClass(), "pause.png")));
        else
            playButton.setImage(new Image(getStreamBySource(getClass(), "play.png")));
        Tooltip.uninstall(playButton.getParent(), tooltip);
    }
    @FXML
    public void onMousePlayButtonPressed() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(getStreamBySource(getClass(), "pause-pressed.png")));
        else
            playButton.setImage(new Image(getStreamBySource(getClass(), "play-pressed.png")));
    }
    @FXML
    public void onMousePlayButtonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                setComponentVisibility(1.0);
                timer.cancel();
                timer = null;
                playButton.setImage(new Image(getStreamBySource(getClass(), "play-moused.png")));
                player.pause();
                isPlaying = false;
            }
            else {
                startTask();
                playButton.setImage(new Image(getStreamBySource(getClass(), "pause-moused.png")));
                player.play();
                isPlaying = true;
            }
        } else {
            playButton.setImage(new Image(getStreamBySource(getClass(), "play-moused.png")));
        }
    }

    @FXML
    public void onMouseForwardButtonEntered() {
        isForwardButtonMouseOn = true;
        tooltip.setText("앞으로 " + getData("moveTime") + "초 이동");
        Tooltip.install(forwardButton.getParent(), tooltip);
        forwardButton.setImage(new Image(getStreamBySource(getClass(), "forward-moused.png")));
    }
    @FXML
    public void onMouseForwardButtonExited() {
        isForwardButtonMouseOn = false;
        Tooltip.uninstall(forwardButton.getParent(), tooltip);
        forwardButton.setImage(new Image(getStreamBySource(getClass(), "forward.png")));
    }
    @FXML
    public void onMouseForwardButtonPressed() {
        forwardButton.setImage(new Image(getStreamBySource(getClass(), "forward-pressed.png")));
    }
    @FXML
    public void onMouseForwardButtonReleased() {
        forwardButton.setImage(new Image(getStreamBySource(getClass(), "forward-moused.png")));
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() + getData("moveTime");
            player.seek(Duration.seconds(time));
            playBar.setValue(time / player.getTotalDuration().toSeconds());
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    @FXML
    public void onMouseBackButtonEntered() {
        isBackwardButtonMouseOn = true;
        tooltip.setText("뒤로 " + getData("moveTime") + "초 이동");
        Tooltip.install(backButton.getParent(), tooltip);
        backButton.setImage(new Image(getStreamBySource(getClass(), "back-moused.png")));
    }
    @FXML
    public void onMouseBackButtonExited() {
        isBackwardButtonMouseOn = false;
        Tooltip.uninstall(backButton.getParent(), tooltip);
        backButton.setImage(new Image(getStreamBySource(getClass(), "back.png")));
    }
    @FXML
    public void onMouseBackButtonPressed() {
        backButton.setImage(new Image(getStreamBySource(getClass(), "back-pressed.png")));
    }
    @FXML
    public void onMouseBackButtonReleased() {
        backButton.setImage(new Image(getStreamBySource(getClass(), "back-moused.png")));
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() - getData("moveTime");
            player.seek(Duration.seconds(time));
            playBar.setValue(time / player.getTotalDuration().toSeconds());
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    @FXML
    public void onMouseOneButtonEntered() {
        tooltip.setText("한 번만 재생");
        Tooltip.install(oneButton.getParent(), tooltip);
        oneButton.setImage(new Image(getStreamBySource(getClass(), "one-moused.png")));
    }
    @FXML
    public void onMouseOneButtonExited() {
        Tooltip.uninstall(oneButton.getParent(), tooltip);
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && !isRepeating)
            oneButton.setImage(new Image(getStreamBySource(getClass(), "one-selected.png")));
        else
            oneButton.setImage(new Image(getStreamBySource(getClass(), "one.png")));
    }
    @FXML
    public void onMouseOneButtonPressed() {
        oneButton.setImage(new Image(getStreamBySource(getClass(), "one-selected.png")));
    }
    @FXML
    public void onMouseOneButtonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = false;
            repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat.png")));
            oneButton.setImage(new Image(getStreamBySource(getClass(), "one-selected.png")));
            player.setOnEndOfMedia(() -> setMediaReadyToPlay(player));
        }
        else
            oneButton.setImage(new Image(getStreamBySource(getClass(), "one-moused.png")));
    }

    @FXML
    public void onMouseRepeatButtonEntered() {
        tooltip.setText("반복 재생");
        Tooltip.install(repeatButton.getParent(), tooltip);
        repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat-moused.png")));
    }
    @FXML
    public void onMouseRepeatButtonExited() {
        Tooltip.uninstall(repeatButton.getParent(), tooltip);
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && isRepeating)
            repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat-selected.png")));
        else
            repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat.png")));
    }
    @FXML
    public void onMouseRepeatButtonPressed() {
        repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat-selected.png")));
    }
    @FXML
    public void onMouseRepeatButtonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = true;
            repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat-selected.png")));
            oneButton.setImage(new Image(getStreamBySource(getClass(), "one.png")));
            player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        }
        else
            repeatButton.setImage(new Image(getStreamBySource(getClass(), "repeat-moused.png")));
    }

    @FXML
    public void onMouseFileButtonEntered() {
        isFileButtonMouseOn = true;
        tooltip.setText("동영상 추가");
        Tooltip.install(fileButton.getParent(), tooltip);
        fileButton.setImage(new Image(getStreamBySource(getClass(), "folder-moused.png")));
    }
    @FXML
    public void onMouseFileButtonExited() {
        isFileButtonMouseOn = false;
        Tooltip.uninstall(fileButton.getParent(), tooltip);
        fileButton.setImage(new Image(getStreamBySource(getClass(), "folder.png")));
    }
    @FXML
    public void onMouseFileButtonPressed() {
        fileButton.setImage(new Image(getStreamBySource(getClass(), "folder-pressed.png")));
    }
    @FXML
    public void onMouseFileButtonReleased() {
        fileButton.setImage(new Image(getStreamBySource(getClass(), "folder-moused.png")));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("비디오 선택");
        fileChooser.setInitialDirectory(new File("C:/"));
        File file = fileChooser.showOpenDialog(null);
        setupMedia(file);
    }

    @FXML
    public void onMouseSettingButtonEntered() {
        tooltip.setText("설정");
        Tooltip.install(settingButton.getParent(), tooltip);
        settingButton.setImage(new Image(getStreamBySource(getClass(), "setting-moused.png")));
    }
    @FXML
    public void onMouseSettingButtonExited() {
        Tooltip.uninstall(settingButton.getParent(), tooltip);
        settingButton.setImage(new Image(getStreamBySource(getClass(), "setting.png")));
    }
    @FXML
    public void onMouseSettingButtonPressed() {
        settingButton.setImage(new Image(getStreamBySource(getClass(), "setting-pressed.png")));
    }
    @FXML
    public void onMouseSettingButtonReleased() {
        settingButton.setImage(new Image(getStreamBySource(getClass(), "setting-moused.png")));
        new SettingApplication().start(new Stage());
        manageStartProgramRegistration();
    }

    @FXML
    public void onMouseVolumeButtonEntered() {
        if (sound > 0) {
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "volume-moused.png")));
            tooltip.setText("볼륨");
        } else {
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "mute-moused.png")));
            tooltip.setText("음소거");
        }
        Tooltip.install(volumeButton.getParent(), tooltip);
    }
    @FXML
    public void onMouseVolumeButtonExited() {
        setInitialVolumeButton();
        Tooltip.uninstall(volumeButton.getParent(), tooltip);
    }
    @FXML
    public void onMouseVolumeButtonPressed() {
        if (sound > 0)
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "volume-pressed.png")));
        else
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "mute-pressed.png")));
    }
    @FXML
    public void onMouseVolumeButtonReleased() {
        if (Double.compare(sound, 0.0) == 0) {
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "volume-moused.png")));
            sound = 20.0;
        }
        else {
            volumeButton.setImage(new Image(getStreamBySource(getClass(), "mute-moused.png")));
            sound = 0.0;
        }
        volumeText.setText(Integer.toString((int)sound));
        volumeBar.setValue(sound);

        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null)
            player.setVolume(sound / 100);
    }

    @FXML
    public void onMouseVolumeBarEntered() {
        volumeText.setText(String.valueOf((int)volumeBar.getValue()));
        volumeText.setVisible(true);
    }
    @FXML
    public void onMouseVolumeBarExited() {
        volumeText.setVisible(false);
    }

    @FXML
    public void onMousePlayBarEntered() {
        isPlayBarMouseOn = true;
        playBar.lookup(".track").setStyle(String.format(PLAYBAR_STYLE, playBar.getValue() * 100, playBar.getValue() * 100, 8));
    }
    @FXML
    public void onMousePlayBarExited() {
        isPlayBarMouseOn = false;
        playBar.lookup(".track").setStyle(String.format(PLAYBAR_STYLE, playBar.getValue() * 100, playBar.getValue() * 100, 2));
    }
    @FXML
    public void onMousePlayBarPressed() {
        isFinding = true;
        findPosition();
    }
    @FXML
    public void onMousePlayBarReleased() {
        isFinding = false;
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && isPlaying)
            player.play();
    }

    public void shutdown() {
        if (timer != null)
            timer.cancel();
        timer = null;
    }
}
