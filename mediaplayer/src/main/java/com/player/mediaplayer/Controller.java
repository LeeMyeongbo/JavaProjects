package com.player.mediaplayer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Slider;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    private boolean isRepeating, isFinding, isPlaybarMouseOn;
    private int mouse_stop, move_into = 10;
    private double sound = 20.0;
    private Timer timer;
    private final String playbarStyle = "-fx-background-color: linear-gradient(to right, #0303f2 %f%%, #15ff00 %f%%);\n-fx-pref-height: %d;";
    @FXML private ToolBar toolbar;
    @FXML private StackPane appArea;
    @FXML private ImageView playButton, backButton, forwardButton, oneButton, repeatButton, volumeButton;
    @FXML private MediaView mediaArea;
    @FXML private HBox buttonArea, volumeArea;
    @FXML private Slider playSlider;
    @FXML private Slider volumeSlider;
    @FXML private Text volumeText;

    /* 처음 app 실행 시 */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // 동영상을 드래그 앤 드롭으로 재생할 수 있도록 설정
        mediaArea.setOnDragOver(mouseEvent -> {
            Dragboard db = mouseEvent.getDragboard();
            if (db.hasFiles())
                mouseEvent.acceptTransferModes(TransferMode.ANY);
            mouseEvent.consume();
        });

        // 끌어온 동영상 파일을 app 내에 drop 하였을 때 바로 재생할 수 있도록 준비
        mediaArea.setOnDragDropped(mouseEvent -> {
            Dragboard db = mouseEvent.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().iterator().next();
                playMedia(file);
            }
            mouseEvent.setDropCompleted(success);
            mouseEvent.consume();
        });

        volumeSlider.setValue(sound);
        volumeSlider.setOnMouseDragged(e -> adjustVolume());
        playSlider.setOnMouseDragged(e -> findPosition());
        playSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if (isPlaybarMouseOn)
                playSlider.lookup(".track").setStyle(String.format(playbarStyle, t1.doubleValue() * 100, t1.doubleValue() * 100, 8));
            else
                playSlider.lookup(".track").setStyle(String.format(playbarStyle, t1.doubleValue() * 100, t1.doubleValue() * 100, 2));
        });

        // 창 크기에 따라서 영상 사이즈 변경
        DoubleProperty width = mediaArea.fitWidthProperty();
        width.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "width"));
        DoubleProperty height = mediaArea.fitHeightProperty();
        height.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "height"));

        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute.png"))));
    }

    /* 영상 재생 중일 때 마우스가 멈춰 있는 시간 측정 -> 3초 이상 움직이지 않았을 때 setVisibleOrNot 실행 */
    public void startTask() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mouse_stop++;
                if (mouse_stop == 3) {
                    appArea.setCursor(Cursor.NONE);
                    setVisibleOrNot(false);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /* 지원되는 동영상 파일 확장자 이외의 파일을 열 경우 */
    public void errorDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("형식 오류!");
        dialog.setWidth(400);
        dialog.setHeight(300);
        dialog.showAndWait();
    }

    /* 컴포넌트들 보이거나 안 보이도록 함 */
    public void setVisibleOrNot(boolean v) {
        toolbar.setVisible(v);
        buttonArea.setVisible(v);
        volumeArea.setVisible(v);
        playSlider.setVisible(v);
    }

    /* 영상 재생 시 */
    public void playMedia(File file) {
        try {
            isRepeating = false;
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-selected.png"))));

            Media media = new Media(file.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(sound / 100.0);
            player.setOnReady(() -> {
                if (!isFinding) {
                    player.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                        double progress = player.getCurrentTime().toSeconds() / player.getTotalDuration().toSeconds();
                        playSlider.setValue(progress);
                    });
                }
            });
            player.setOnEndOfMedia(() -> {
                player.stop();
                player.seek(Duration.ZERO);
                playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play.png"))));
                appArea.setCursor(Cursor.DEFAULT);
                mouse_stop = 0;
                playSlider.setValue(0.0);
                timer.cancel();
                timer = null;
                setVisibleOrNot(true);
            });
            mediaArea.setMediaPlayer(player);
        } catch (NullPointerException ignored) {

        } catch (Exception e) {
            errorDialog();
        }
    }

    /* 볼륨 조절 시 */
    public void adjustVolume() {
        onMouseMoved();

        sound = volumeSlider.getValue();
        volumeText.setText(Integer.toString((int)sound));
        if (sound == 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume.png"))));

        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null)
            player.setVolume(sound / 100.0);
    }

    /* 재생 바로 현재 위치 직접 찾을 때 */
    public void findPosition() {
        onMouseMoved();

        double pos = playSlider.getValue();
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null)
            player.seek(Duration.seconds(pos * player.getTotalDuration().toSeconds()));
    }

    /* 툴바 내 동영상 추가 버튼 눌렀을 때 directory 에서 영상 추가할 수 있도록 함 */
    @FXML
    public void openMedia() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("비디오 선택");
        fileChooser.setInitialDirectory(new File("C:/"));
        File file = fileChooser.showOpenDialog(null);
        playMedia(file);
    }

    /* 설정 버튼 눌렀을 때 설정 창 엶 */
    @FXML
    public void openSetting() {
        Stage setting = new Stage();
        setting.setTitle("설정");
        setting.initModality(Modality.APPLICATION_MODAL);
        setting.setWidth(400);
        setting.setHeight(300);
        setting.showAndWait();
        // TBD...
    }

    /* 마우스가 응용프로그램 내에서 움직일 때 or 프로그램 밖으로 나갈 때 관련 이벤트 처리 */
    @FXML
    public void onMouseMoved() {
        setVisibleOrNot(true);
        appArea.setCursor(Cursor.DEFAULT);
        mouse_stop = 0;
    }
    @FXML
    public void onMouseExited() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            setVisibleOrNot(false);
    }

    /* 재생 버튼 관련 이벤트 */
    @FXML
    public void onMousePlaybuttonEntered() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-moused.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-moused.png"))));
    }
    @FXML
    public void onMousePlaybuttonExited() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play.png"))));
    }
    @FXML
    public void onMousePlaybuttonPressed() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-pressed.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-pressed.png"))));
    }
    @FXML
    public void onMousePlaybuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                buttonArea.setVisible(true);
                timer.cancel();
                timer = null;
                playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-moused.png"))));
                player.pause();
            }
            else {
                startTask();
                playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("pause-moused.png"))));
                player.play();
            }
        }
        else {
            playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play-moused.png"))));
        }
    }

    /* 영상 N초 앞으로 이동 버튼 관련 이벤트 */
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
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() + move_into;
            player.seek(Duration.seconds(time));
            playSlider.setValue(time / player.getTotalDuration().toSeconds());
        }
    }

    /* 영상 N초 뒤로 이동 버튼 관련 이벤트 */
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
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() - move_into;
            player.seek(Duration.seconds(time));
            playSlider.setValue(time / player.getTotalDuration().toSeconds());
        }
    }

    /* 영상 한 번만 재생 버튼 관련 이벤트 */
    @FXML
    public void onMouseOnebuttonEntered() {
        oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-moused.png"))));
    }
    @FXML
    public void onMouseOnebuttonExited() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && !isRepeating)
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-selected.png"))));
        else
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one.png"))));
    }
    @FXML
    public void onMouseOnebuttonPressed() {
        oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-selected.png"))));
    }
    @FXML
    public void onMouseOnebuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = false;
            repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat.png"))));
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-selected.png"))));
            player.setOnEndOfMedia(() -> {
                player.stop();
                playButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("play.png"))));
                appArea.setCursor(Cursor.DEFAULT);
                mouse_stop = 0;
                timer.cancel();
                timer = null;
                setVisibleOrNot(true);
            });
        }
        else
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one-moused.png"))));
    }

    /* 영상 반복 재생 버튼 관련 이벤트 */
    @FXML
    public void onMouseRepeatbuttonEntered() {
        repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat-moused.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonExited() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && isRepeating)
            repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat-selected.png"))));
        else
            repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonPressed() {
        repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat-selected.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = true;
            repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat-selected.png"))));
            oneButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("one.png"))));
            player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        }
        else
            repeatButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("repeat-moused.png"))));
    }

    /* 볼륨 버튼 관련 이벤트 */
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
        if (sound == 0) {
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("volume-moused.png"))));
            sound = 20.0;
        }
        else {
            volumeButton.setImage(new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("mute-moused.png"))));
            sound = 0.0;
        }
        volumeText.setText(Integer.toString((int)sound));
        volumeSlider.setValue(sound);

        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null)
            player.setVolume(sound);
    }

    /* 볼륨 바 관련 이벤트 */
    @FXML
    public void onMouseVolumebarEntered() {
        volumeText.setVisible(true);
    }
    @FXML
    public void onMouseVolumebarExited() {
        volumeText.setVisible(false);
    }
    @FXML
    public void onMouseVolumebarPressed() {
    }
    @FXML
    public void onMouseVolumebarReleased() {
        adjustVolume();
    }

    /* 재생 바 관련 이벤트 */
    @FXML
    public void onMousePlaybarEntered() {
        isPlaybarMouseOn = true;
        playSlider.lookup(".track").setStyle(String.format(playbarStyle, playSlider.getValue() * 100, playSlider.getValue() * 100, 8));
    }
    @FXML
    public void onMousePlaybarExited() {
        isPlaybarMouseOn = false;
        playSlider.lookup(".track").setStyle(String.format(playbarStyle, playSlider.getValue() * 100, playSlider.getValue() * 100, 2));
    }
    @FXML
    public void onMousePlaybarPressed() {
        isFinding = true;
        findPosition();
    }
    @FXML
    public void onMousePlaybarReleased() {
        isFinding = false;
    }

    /* 동영상 플레이어 창 닫을 시 timer 동작하고 있다면 먼저 종료시킴 */
    public void shutdown() {
        if (timer != null)
            timer.cancel();
    }
}
