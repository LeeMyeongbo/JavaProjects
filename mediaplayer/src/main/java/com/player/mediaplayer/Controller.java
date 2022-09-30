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

public class Controller implements Initializable {
    private boolean isRepeating, isFinding, isPlaybarMouseOn, isPlayMouseOn, isForwardMouseOn, isBackwardMouseOn, isFileMouseOn, isFullScreen, isPlaying;
    private int mouse_stop;
    private double sound;
    private final String playbarStyle = "-fx-background-color: linear-gradient(to right, #0303f2 %f%%, #15ff00 %f%%);\n-fx-pref-height: %d;";
    private final String moveTime = "moveTime", vanishTime = "vanishTime", startVolume = "startVolume", opacity = "opacity";
    private Timer timer;
    private Tooltip tooltip;
    private HashMap<String, Integer> data;
    @FXML private StackPane appArea;
    @FXML private ImageView playButton, backButton, forwardButton, oneButton, repeatButton, volumeButton, fileButton, settingButton;
    @FXML private MediaView mediaArea;
    @FXML private HBox buttonArea, volumeArea, etcArea;
    @FXML private VBox bgArea;
    @FXML private Slider playSlider;
    @FXML private Slider volumeSlider;
    @FXML private Text volumeText;
    @FXML private Label curTimeLabel, endTimeLabel;

    /* 처음 app 실행 시 */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSetting();

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

        // 스페이스 바, 방향키로도 영상 제어할 수 있도록 함
        appArea.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE -> {
                    if (mediaArea.getMediaPlayer() == null) {
                        onMouseFileButtonReleased();
                        if (!isFileMouseOn)
                            onMouseFileButtonExited();
                    } else {
                        onMousePlaybuttonReleased();
                        if (!isPlayMouseOn) {
                            if (mediaArea.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
                                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play.png"))));
                            else
                                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("pause.png"))));
                        }
                    }
                } case RIGHT -> {
                    onMouseForwardbuttonReleased();
                    if (!isForwardMouseOn)
                        onMouseForwardbuttonExited();
                } case LEFT -> {
                    onMouseBackbuttonReleased();
                    if (!isBackwardMouseOn)
                        onMouseBackbuttonExited();
                } case ENTER -> {
                    isFullScreen = !isFullScreen;
                    ((Stage)mediaArea.getScene().getWindow()).setFullScreenExitHint("");
                    ((Stage)mediaArea.getScene().getWindow()).setFullScreen(isFullScreen);
                }
            }
        });

        // 볼륨 바 value, 드래그 관련 설정
        sound = data.get(startVolume);
        volumeSlider.setValue(sound);
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            onMouseMoved();

            volumeText.setText(Integer.toString(t1.intValue()));
            if (t1.intValue() == 0)
                volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute.png"))));
            else
                volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume.png"))));

            MediaPlayer player = mediaArea.getMediaPlayer();
            if (player != null)
                player.setVolume(t1.intValue() / 100.0);
        });
        volumeSlider.addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);      // 방향키로 slider 조작하는 거 막음

        // 재생 바 드래그 및 value 변화 시 관련 설정
        playSlider.setOnMouseDragged(e -> findPosition());
        playSlider.valueProperty().addListener((observableValue, number, t1) -> {
            if (isPlaybarMouseOn)
                playSlider.lookup(".track").setStyle(String.format(playbarStyle, t1.doubleValue() * 100, t1.doubleValue() * 100, 8));
            else
                playSlider.lookup(".track").setStyle(String.format(playbarStyle, t1.doubleValue() * 100, t1.doubleValue() * 100, 2));
        });
        playSlider.addEventFilter(KeyEvent.KEY_PRESSED, Event::consume);        // playSlider 도 방향키 조작 막음

        // 창 크기에 따라서 영상 사이즈 변경
        DoubleProperty width = mediaArea.fitWidthProperty();
        width.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "width"));
        DoubleProperty height = mediaArea.fitHeightProperty();
        height.bind(Bindings.selectDouble(mediaArea.sceneProperty(), "height"));

        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute.png"))));

        tooltip = new Tooltip();
    }

    /* 미디어 플레이어 실행 시 혹은 설정 수정 후 파일 저장 및 로드 */
    public void loadSetting() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Mediaplayer\\setting.dat"));
            data = (HashMap<String, Integer>) ois.readObject();
            ois.close();

            registerStartProgram();
        } catch (FileNotFoundException e) {                         // 경로에 설정 파일이 없을 때
            data = new HashMap<>();
            data.put(moveTime, 10);
            data.put(vanishTime, 3);
            data.put(startVolume, 20);
            data.put(opacity, 10);
            data.put("autoStart", 0);

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

    /* 시작 프로그램 등록 */
    public void registerStartProgram() {
        String regPath = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
        String regName = "MediaPlayer";
        String regValue = "\"" + Paths.get("").toAbsolutePath() + "\\MediaPlayer.exe\" /autorun";

        if (data.get("autoStart") == 1) {
            if (!Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, regPath, regName)) {    // 시작프로그램에 등록되어 있지 않을 때
                Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, regPath, regName);                 // 레지스트리 키 생성
                Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, regPath, regName, regValue);  // 레지스트리 값 설정
            }
        } else {
            if (Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, regPath, regName)) {     // 시작프로그램에 등록되어 있을 때
                Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, regPath, regName);               // 레지스트리 값 삭제
                Advapi32Util.registryDeleteKey(WinReg.HKEY_CURRENT_USER, regPath, regName);                 // 레지스트리 키 삭제(값 삭제가 선행)
            }
        }
    }

    /* 영상 재생 중일 때 마우스가 멈춰 있는 시간 측정 -> 3초 이상 움직이지 않았을 때 setVisibleOrNot 실행 */
    public void startTask() {
        timer = new Timer();
        mouse_stop = 0;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mouse_stop++;
                if (mouse_stop == data.get(vanishTime)) {
                    appArea.setCursor(Cursor.NONE);
                    setVisibleOrNot(1.0 - data.get(opacity) / 10.0);
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
    public void setVisibleOrNot(double v) {
        buttonArea.setOpacity(v);
        volumeArea.setOpacity(v);
        etcArea.setOpacity(v);
        playSlider.setOpacity(v);
        curTimeLabel.setOpacity(v);
        endTimeLabel.setOpacity(v);

        if (v < 1.0)
            bgArea.setStyle("-fx-background-color: transparent;");
        else
            bgArea.setStyle("-fx-background-color: linear-gradient(to top, rgba(18, 8, 65, 0.65) 20%, transparent);");
    }

    /* 영상 재생 시 */
    public void playMedia(File file) {
        try {
            Media media = new Media(file.toURI().toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(sound / 100.0);
            player.setOnReady(() -> {
                playSlider.setValue(0.0);
                if (!isFinding) {
                    player.currentTimeProperty().addListener((observableValue, duration, t1) -> {
                        double progress = player.getCurrentTime().toSeconds() / player.getTotalDuration().toSeconds();
                        playSlider.setValue(progress);
                        curTimeLabel.setText(getTime(player.getCurrentTime()));
                    });
                }
                endTimeLabel.setText(getTime(player.getTotalDuration()));
            });
            player.setOnEndOfMedia(() -> {
                player.stop();
                player.seek(Duration.ZERO);
                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play.png"))));
                appArea.setCursor(Cursor.DEFAULT);
                playSlider.setValue(0.0);
                isPlaying = isRepeating;
                timer.cancel();
                timer = null;
                setVisibleOrNot(1.0);
            });
            mediaArea.setMediaPlayer(player);
            isRepeating = false;
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-selected.png"))));
        } catch (NullPointerException ignored) {
            System.out.println("추가된 파일이 없습니다.");
        } catch (Exception e) {
            errorDialog();
        }
    }

    /* 재생 바로 현재 위치 직접 찾을 때 */
    public void findPosition() {
        onMouseMoved();
        double pos = playSlider.getValue();
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            player.pause();
            player.seek(Duration.seconds(pos * player.getTotalDuration().toSeconds()));
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    /* 영상 재생 시 시간 표시 */
    public String getTime(Duration d) {
        return String.format("%02d:%02d:%02d", (int)d.toHours(), (int)d.toMinutes() % 60, (int)d.toSeconds() % 60);
    }

    /* 마우스가 응용프로그램 내에서 움직일 때 or 프로그램 밖으로 나갈 때 or 더블 클릭 시 관련 이벤트 처리 */
    @FXML
    public void onMouseMoved() {            // 프로그램 내에서 마우스 움직일 땐 버튼들 및 slider 들 보이게 설정
        setVisibleOrNot(1.0);
        appArea.setCursor(Cursor.DEFAULT);
        mouse_stop = 0;
    }
    @FXML
    public void onMouseExited() {           // 프로그램 밖으로 마우스가 빠져나가면 안 보이게끔 설정
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            setVisibleOrNot(1.0 - data.get(opacity) / 10.0);
    }
    @FXML
    public void onMouseDoubleClick(MouseEvent mouseEvent) {         // 더블 클릭 시 전체화면
        if (mouseEvent.getClickCount() == 2) {
            isFullScreen = !isFullScreen;
            ((Stage)mediaArea.getScene().getWindow()).setFullScreenExitHint("");
            ((Stage)mediaArea.getScene().getWindow()).setFullScreen(isFullScreen);
        }
    }

    /* 재생 버튼 관련 이벤트 */
    @FXML
    public void onMousePlaybuttonEntered() {
        isPlayMouseOn = true;
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("pause-moused.png"))));
            tooltip.setText("일시 정지");
        } else {
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play-moused.png"))));
            tooltip.setText("재생");
        }
        Tooltip.install(playButton.getParent(), tooltip);
    }
    @FXML
    public void onMousePlaybuttonExited() {
        isPlayMouseOn = false;
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("pause.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play.png"))));
        Tooltip.uninstall(playButton.getParent(), tooltip);
    }
    @FXML
    public void onMousePlaybuttonPressed() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING)
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("pause-pressed.png"))));
        else
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play-pressed.png"))));
    }
    @FXML
    public void onMousePlaybuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                setVisibleOrNot(1.0);
                timer.cancel();
                timer = null;
                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play-moused.png"))));
                player.pause();
                isPlaying = false;
            }
            else {
                startTask();
                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("pause-moused.png"))));
                player.play();
                isPlaying = true;
            }
        } else {
            playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play-moused.png"))));
        }
    }

    /* 영상 N초 앞으로 이동 버튼 관련 이벤트 */
    @FXML
    public void onMouseForwardbuttonEntered() {
        isForwardMouseOn = true;
        tooltip.setText("앞으로 " + data.get(moveTime) + "초 이동");
        Tooltip.install(forwardButton.getParent(), tooltip);
        forwardButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("forward-moused.png"))));
    }
    @FXML
    public void onMouseForwardbuttonExited() {
        isForwardMouseOn = false;
        Tooltip.uninstall(forwardButton.getParent(), tooltip);
        forwardButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("forward.png"))));
    }
    @FXML
    public void onMouseForwardbuttonPressed() {
        forwardButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("forward-pressed.png"))));
    }
    @FXML
    public void onMouseForwardbuttonReleased() {
        forwardButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("forward-moused.png"))));
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() + data.get(moveTime);
            player.seek(Duration.seconds(time));
            playSlider.setValue(time / player.getTotalDuration().toSeconds());
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    /* 영상 N초 뒤로 이동 버튼 관련 이벤트 */
    @FXML
    public void onMouseBackbuttonEntered() {
        isBackwardMouseOn = true;
        tooltip.setText("뒤로 " + data.get(moveTime) + "초 이동");
        Tooltip.install(backButton.getParent(), tooltip);
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("back-moused.png"))));
    }
    @FXML
    public void onMouseBackbuttonExited() {
        isBackwardMouseOn = false;
        Tooltip.uninstall(backButton.getParent(), tooltip);
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("back.png"))));
    }
    @FXML
    public void onMouseBackbuttonPressed() {
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("back-pressed.png"))));
    }
    @FXML
    public void onMouseBackbuttonReleased() {
        backButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("back-moused.png"))));
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            double time = player.getCurrentTime().toSeconds() - data.get(moveTime);
            player.seek(Duration.seconds(time));
            playSlider.setValue(time / player.getTotalDuration().toSeconds());
            curTimeLabel.setText(getTime(player.getCurrentTime()));
        }
    }

    /* 영상 한 번만 재생 버튼 관련 이벤트 */
    @FXML
    public void onMouseOnebuttonEntered() {
        tooltip.setText("한 번만 재생");
        Tooltip.install(oneButton.getParent(), tooltip);
        oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-moused.png"))));
    }
    @FXML
    public void onMouseOnebuttonExited() {
        Tooltip.uninstall(oneButton.getParent(), tooltip);
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && !isRepeating)
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-selected.png"))));
        else
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one.png"))));
    }
    @FXML
    public void onMouseOnebuttonPressed() {
        oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-selected.png"))));
    }
    @FXML
    public void onMouseOnebuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = false;
            repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat.png"))));
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-selected.png"))));
            player.setOnEndOfMedia(() -> {
                player.stop();
                playButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("play.png"))));
                appArea.setCursor(Cursor.DEFAULT);
                timer.cancel();
                timer = null;
                setVisibleOrNot(1.0);
            });
        }
        else
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one-moused.png"))));
    }

    /* 영상 반복 재생 버튼 관련 이벤트 */
    @FXML
    public void onMouseRepeatbuttonEntered() {
        tooltip.setText("반복 재생");
        Tooltip.install(repeatButton.getParent(), tooltip);
        repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat-moused.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonExited() {
        Tooltip.uninstall(repeatButton.getParent(), tooltip);
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && isRepeating)
            repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat-selected.png"))));
        else
            repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonPressed() {
        repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat-selected.png"))));
    }
    @FXML
    public void onMouseRepeatbuttonReleased() {
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null) {
            isRepeating = true;
            repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat-selected.png"))));
            oneButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("one.png"))));
            player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        }
        else
            repeatButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("repeat-moused.png"))));
    }

    /* 파일 추가 버튼 관련 이벤트 */
    @FXML
    public void onMouseFileButtonEntered() {
        isFileMouseOn = true;
        tooltip.setText("동영상 추가");
        Tooltip.install(fileButton.getParent(), tooltip);
        fileButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder-moused.png"))));
    }
    @FXML
    public void onMouseFileButtonExited() {
        isFileMouseOn = false;
        Tooltip.uninstall(fileButton.getParent(), tooltip);
        fileButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder.png"))));
    }
    @FXML
    public void onMouseFileButtonPressed() {
        fileButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder-pressed.png"))));
    }
    @FXML
    public void onMouseFileButtonReleased() {       // 버튼을 눌었다 떼었을 때 파일 선택기 오픈
        fileButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder-moused.png"))));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("비디오 선택");
        fileChooser.setInitialDirectory(new File("C:/"));
        File file = fileChooser.showOpenDialog(null);
        playMedia(file);
    }

    /* 설정 버튼 관련 이벤트 */
    @FXML
    public void onMouseSettingButtonEntered() {
        tooltip.setText("설정");
        Tooltip.install(settingButton.getParent(), tooltip);
        settingButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("setting-moused.png"))));
    }
    @FXML
    public void onMouseSettingButtonExited() {
        Tooltip.uninstall(settingButton.getParent(), tooltip);
        settingButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("setting.png"))));
    }
    @FXML
    public void onMouseSettingButtonPressed() {
        settingButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("setting-pressed.png"))));
    }
    @FXML
    public void onMouseSettingButtonReleased() throws IOException {    // 버튼 눌렀다 떼었을 때 설정 창 오픈
        settingButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("setting-moused.png"))));
        SettingStage settingStage = new SettingStage();
        settingStage.showAndWait();
        loadSetting();
    }

    /* 볼륨 버튼 관련 이벤트 */
    @FXML
    public void onMouseVolumebuttonEntered() {
        if (sound > 0) {
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume-moused.png"))));
            tooltip.setText("볼륨");
        } else {
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute-moused.png"))));
            tooltip.setText("음소거");
        }
        Tooltip.install(volumeButton.getParent(), tooltip);
    }
    @FXML
    public void onMouseVolumebuttonExited() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute.png"))));
        Tooltip.uninstall(volumeButton.getParent(), tooltip);
    }
    @FXML
    public void onMouseVolumebuttonPressed() {
        if (sound > 0)
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume-pressed.png"))));
        else
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute-pressed.png"))));
    }
    @FXML
    public void onMouseVolumebuttonReleased() {
        if (sound == 0) {
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("volume-moused.png"))));
            sound = 20.0;
        }
        else {
            volumeButton.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("mute-moused.png"))));
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
        volumeText.setText(String.valueOf((int)volumeSlider.getValue()));
        volumeText.setVisible(true);
    }
    @FXML
    public void onMouseVolumebarExited() {
        volumeText.setVisible(false);
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
        MediaPlayer player = mediaArea.getMediaPlayer();
        if (player != null && isPlaying)
            player.play();
    }

    /* 동영상 플레이어 창 닫을 시 timer 동작하고 있다면 먼저 종료시킴 */
    public void shutdown() {
        if (timer != null)
            timer.cancel();
    }
}
