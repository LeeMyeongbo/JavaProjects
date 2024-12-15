import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;

class MP3Body extends JFrame implements ActionListener {

	@Serial private static final long serialVersionUID = 1L;
    private ArrayList<File> fileList = new ArrayList<>();
    private MP3Button playButton, pauseButton, prevButton, nextButton;
    private MP3Button onceModeButton, repeatModeButton, shuffleModeButton;
    private JButton addButton, deleteButton, savePlaylistButton, fetchPlayListButton, searchButton;
    private MP3Slider musicBarSlider;
    private MP3Slider volumeBarSlider;
	private final JLabel currentTimeLabel = new JLabel();
	private final JLabel musicLengthLabel = new JLabel();
	private final JLabel musicTitleLabel = new JLabel();
	private final JLabel volumeLabel = new JLabel();
	private final JTextField searchField = new JTextField();
	private final JFileChooser chooser = new JFileChooser("C:\\");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> musicList = new JList<>();
    private Media media = null;
	private MediaPlayer player = null;
	private int numOfMusic;
	private final LinkedList<Integer> MusicSequence = new LinkedList<>();

    private static final String PLAY_TOOLTIP = "PlayList에서 선택한 노래를 재생합니다.";
    private static final String PAUSE_TOOLTIP = "재생 중인 노래를 일시 정지합니다.";
    private static final String PREV_TOOLTIP = "PlayList에서 선택한 노래의 이전 곡을 재생합니다.";
    private static final String NEXT_TOOLTIP = "PlayList에서 선택한 노래의 다음 곡을 재생합니다.";
    private static final String ONCE_TOOLTIP = "PlayList에 있는 곡을 한 번씩만 재생합니다.";
    private static final String REPEAT_TOOLTIP = "PlayList에 있는 곡을 반복해서 재생합니다.";
    private static final String SHUFFLE_TOOLTIP = "PlayList에 있는 곡을 무작위 순서로 재생합니다.";

    private URL getResourceByPath(String path) {
        return Objects.requireNonNull(getClass().getClassLoader().getResource(path));
    }

	private final Timer timer = new Timer(100, e -> {
		Duration d1 = player.getCurrentTime();
		Duration d2 = player.getTotalDuration();
		String time1 = String.format("%02d:%02d%n", (int) d1.toMinutes(), (int) d1.toSeconds() % 60);
		String time2 = String.format("%02d:%02d%n", (int) d2.toMinutes(), (int) d2.toSeconds() % 60);
		currentTimeLabel.setText(time1);
		musicLengthLabel.setText(time2);
		musicBarSlider.setValue((int) (100 * (d1.toSeconds() / d2.toSeconds())));
		player.setOnEndOfMedia(() -> {
			int n = musicList.getSelectedIndex();
			if (!onceModeButton.isEnabled()) {
				if (n < listModel.getSize() - 1) {
					musicList.setSelectedIndex(n + 1);
					musicTitleLabel.setText(musicList.getSelectedValue());
					media = new Media(fileList.get(n + 1).toURI().toString());
					player = new MediaPlayer(media);
					player.play();
				}
			} else if (!repeatModeButton.isEnabled()) {
				if (n < listModel.getSize() - 1) {
					musicList.setSelectedIndex(n + 1);
					media = new Media(fileList.get(n + 1).toURI().toString());
				} else {
					musicList.setSelectedIndex(0);
					media = new Media(fileList.get(0).toURI().toString());
				}
				player = new MediaPlayer(media);
				musicTitleLabel.setText(musicList.getSelectedValue());
				player.play();
			} else {
				if (numOfMusic != listModel.getSize()) {
					MusicSequence.clear();
					numOfMusic = listModel.getSize();
					for (int i = 0; i < numOfMusic; i++)
						MusicSequence.add(i);
					int present = musicList.getSelectedIndex();
					MusicSequence.remove(present);
					Collections.shuffle(MusicSequence);
				}
				if (!MusicSequence.isEmpty()) {
					musicList.setSelectedIndex(MusicSequence.getFirst());
					media = new Media(fileList.get(MusicSequence.pop()).toURI().toString());
					musicTitleLabel.setText(musicList.getSelectedValue());
					player = new MediaPlayer(media);
					player.play();
				}
			}
		});
	});

    private final JPanel panel = new JPanel() {

        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            ImageIcon backgroundImage = new ImageIcon(getResourceByPath("background.png"));
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            super.paintComponents(g);
        }
    };

	class Mp3Stick extends MouseAdapter implements MouseListener {

		@Override
		public void mousePressed(MouseEvent e) {
			timer.stop();
			player.pause();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			double percent = p.getX() / (double) musicBarSlider.getWidth() * 100;
			modifySlide(percent);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			double percent = p.getX() / (double) musicBarSlider.getWidth() * 100;
			modifySlide(percent);
		}

        private void modifySlide(double percent) {
            musicBarSlider.setValue((int) percent);
            int pos = (int) player.getTotalDuration().toSeconds();
            player.seek(Duration.seconds(pos * (musicBarSlider.getValue() / 100.0)));
            if (pauseButton.isEnabled()) {
                player.play();
                timer.start();
            } else
                player.pause();
        }
	}

	class VolumeStick extends MouseAdapter implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			double percent = (volumeBarSlider.getHeight() - p.getY()) / (double) volumeBarSlider.getHeight() * 100;
			modifySlide(percent);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			volumeLabel.setVisible(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			volumeLabel.setVisible(false);
		}

        private void modifySlide(double percent) {
            volumeBarSlider.setValue((int)percent);
            if (player != null)
                if ((double) volumeBarSlider.getValue() / 100 >= 0.5)
                    player.setVolume((((double) volumeBarSlider.getValue() / 100) * 7.0 / 5.0) - 0.4);
                else
                    player.setVolume(Math.pow((double) volumeBarSlider.getValue() / 100 * 2, 2) * 0.3);
            volumeLabel.setText("Vol : " + volumeBarSlider.getValue());
        }
	}

	class selectListListener extends MouseAdapter implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int n = musicList.getSelectedIndex();
			deleteButton.setEnabled(true);
			if (e.getClickCount() == 2) {
				musicBarSlider.setVisible(true);
				timer.start();
				if (media != null)
					player.stop();
				media = new Media(fileList.get(n).toURI().toString());
				player = new MediaPlayer(media);
				player.play();
				player.setVolume(0.3);
				setPauseButtonEnabled();
				musicTitleLabel.setText(musicList.getSelectedValue());
			}
		}
	}

	public MP3Body() {
		super("MP3 플레이어");
        setIconImage(new ImageIcon(getResourceByPath("icon.png")).getImage());
		setLocation(600, 50);
		setSize(500, 700);
        add(panel);

        initButtonsEssentialToPlayingMusic();
        initAdditionalButtons();
        initSlider();
        initPlayListField();
        initSearchField();
        initMusicTitleLabel();
        initVolumeLabel();
        initCurrentTimeLabel();
        initMusicLengthLabel();
        initMusicFileChooser();

        panel.setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

    private void setPlayButtonEnabled() {
        playButton.setVisible(true);
        playButton.setEnabled(true);
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    private void setPauseButtonEnabled() {
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
        playButton.setVisible(false);
        playButton.setEnabled(false);
    }

    private ImageIcon setImageSize(ImageIcon icon, Dimension dimension) {
        Image image = icon.getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private void initButtonsEssentialToPlayingMusic() {
        setButtonsRelatedToPlayMusic();
        setButtonsRelatedToPlayMode();

        onceModeButton.setEnabled(false);
        setPlayButtonEnabled();
    }

    private void setButtonsRelatedToPlayMusic() {
        Dimension big = new Dimension(100, 100);
        Dimension small = new Dimension(50, 50);

        playButton = new MP3Button(this, setImageSize(new ImageIcon(getResourceByPath("play.png")), big), big,
            new Point(200, 20), PLAY_TOOLTIP);
        pauseButton = new MP3Button(this, setImageSize(new ImageIcon(getResourceByPath("pause.png")), big), big,
            new Point(200, 20), PAUSE_TOOLTIP);
        prevButton = new MP3Button(this, setImageSize(new ImageIcon(getResourceByPath("prev.png")), small), small,
            new Point(110, 45), PREV_TOOLTIP);
        nextButton = new MP3Button(this, setImageSize(new ImageIcon(getResourceByPath("next.png")), small), small,
            new Point(340, 45), NEXT_TOOLTIP);

        panel.add(playButton);
        panel.add(pauseButton);
        panel.add(prevButton);
        panel.add(nextButton);
    }

    private void setButtonsRelatedToPlayMode() {
        Dimension size = new Dimension(50, 40);

        onceModeButton = new MP3Button(this, new ImageIcon(getResourceByPath("all_once.png")), size,
            new Point(125, 170), ONCE_TOOLTIP);
        repeatModeButton = new MP3Button(this, new ImageIcon(getResourceByPath("all_repeat.png")), size,
            new Point(225, 170), REPEAT_TOOLTIP);
        shuffleModeButton = new MP3Button(this, new ImageIcon(getResourceByPath("shuffle.png")), size,
            new Point(325, 170), SHUFFLE_TOOLTIP);

        panel.add(onceModeButton);
        panel.add(repeatModeButton);
        panel.add(shuffleModeButton);
    }

    private void initAdditionalButtons() {
        initAddButton();
        initDeleteButton();
        initSearchButton();
        initSendPlaylistButton();
        initFetchPlaylistButton();
    }

    private void initAddButton() {
        addButton = new JButton("추가");
        addButton.setLocation(297, 620);
        addButton.setSize(70, 30);
        addButton.addActionListener(this);
        addButton.setToolTipText("노래를 PlayList에 추가합니다.");

        panel.add(addButton);
    }

    private void initDeleteButton() {
        deleteButton = new JButton("삭제");
        deleteButton.setLocation(373, 620);
        deleteButton.setSize(70, 30);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText("선택한 노래를 PlayList에서 삭제합니다.");

        panel.add(deleteButton);
    }

    private void initSearchButton() {
        searchButton = new JButton("검색");
        searchButton.setLocation(370, 220);
        searchButton.setSize(70, 30);
        searchButton.addActionListener(this);
        searchButton.setToolTipText("PlayList 안에 있는 노래들 중에서 검색합니다.");

        panel.add(searchButton);
    }

    private void initSendPlaylistButton() {
        savePlaylistButton = new JButton("목록 내보내기");
        savePlaylistButton.setLocation(52, 620);
        savePlaylistButton.setSize(117, 30);
        savePlaylistButton.addActionListener(this);
        savePlaylistButton.setToolTipText("PlayList를 파일로 저장합니다.");

        panel.add(savePlaylistButton);
    }

    private void initFetchPlaylistButton() {
        fetchPlayListButton = new JButton("목록 불러오기");
        fetchPlayListButton.setLocation(174, 620);
        fetchPlayListButton.setSize(117, 30);
        fetchPlayListButton.addActionListener(this);
        fetchPlayListButton.setToolTipText("저장된 PlayList를 불러옵니다.");

        panel.add(fetchPlayListButton);
    }

    private void initSlider() {
        musicBarSlider = new MP3Slider(JSlider.HORIZONTAL, new Point(50, 145), new Dimension(395, 30),
            "음악의 현재 재생 위치를 표시합니다.", new Mp3Stick());
        musicBarSlider.setVisible(false);

        volumeBarSlider = new MP3Slider(JSlider.VERTICAL, new Point(425, 34), new Dimension(30, 90),
            "볼륨 크기를 조절합니다.", new VolumeStick());
        volumeBarSlider.addChangeListener(e -> {
            if (player != null) {
                if ((double) volumeBarSlider.getValue() / 100 >= 0.5) {
                    player.setVolume((((double) volumeBarSlider.getValue() / 100) * 7.0 / 5.0) - 0.4);
                } else
                    player.setVolume(Math.pow((double) volumeBarSlider.getValue() / 100 * 2, 2) * 0.3);
            }
            volumeLabel.setText("Vol : " + volumeBarSlider.getValue());
        });

        panel.add(musicBarSlider);
        panel.add(volumeBarSlider);
    }

    private void initPlayListField() {
        musicList.setModel(listModel);
        musicList.setFont(new Font("HY엽서M", Font.PLAIN, 18));
        LineBorder lineBorder = new LineBorder(Color.gray, 1);
        TitledBorder titleBorder = new TitledBorder(lineBorder, "PlayLists", TitledBorder.CENTER, TitledBorder.TOP);
        titleBorder.setTitleFont(new Font("Algerian", Font.BOLD, 30));
        titleBorder.setTitleColor(Color.black);
        musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        musicList.setForeground(Color.GRAY);
        musicList.setSelectionForeground(Color.BLACK);
        musicList.addMouseListener(new selectListListener());
        JScrollPane scrollPane = new JScrollPane(musicList);
        scrollPane.setSize(395, 330);
        scrollPane.setLocation(50, 280);
        scrollPane.setBorder(titleBorder);
        scrollPane.setOpaque(false);

        panel.add(scrollPane);
    }

    private void initSearchField() {
        searchField.setLocation(52, 220);
        searchField.setSize(305, 30);
        searchField.registerKeyboardAction(this, "search",
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);

        panel.add(searchField);
    }

    private void initMusicTitleLabel() {
        musicTitleLabel.setText("");
        musicTitleLabel.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 20));
        musicTitleLabel.setForeground(Color.ORANGE);
        musicTitleLabel.setSize(500,20);
        musicTitleLabel.setLocation(0, 125);
        musicTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(musicTitleLabel);
    }

    private void initVolumeLabel() {
        volumeLabel.setVisible(false);
        volumeLabel.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 23));
        volumeLabel.setForeground(Color.RED);
        volumeLabel.setSize(100, 25);
        volumeLabel.setLocation(375, 12);
        volumeLabel.setText("Vol : " + volumeBarSlider.getValue());

        panel.add(volumeLabel);
    }

    private void initCurrentTimeLabel() {
        currentTimeLabel.setSize(55, 20);
        currentTimeLabel.setLocation(35, 180);
        currentTimeLabel.setFont(new Font("HY엽서M", Font.BOLD, 15));
        currentTimeLabel.setForeground(Color.BLUE);
        currentTimeLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(currentTimeLabel);
    }

    private void initMusicLengthLabel() {
        musicLengthLabel.setSize(55, 20);
        musicLengthLabel.setLocation(405, 180);
        musicLengthLabel.setFont(new Font("HY엽서M", Font.BOLD, 15));
        musicLengthLabel.setForeground(Color.BLUE);
        musicLengthLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(musicLengthLabel);
    }

    private void initMusicFileChooser() {
        chooser.setPreferredSize(new Dimension(700, 500));
        chooser.setFileFilter(new FileNameExtensionFilter(".mp3, .dat", "mp3", "dat"));
        chooser.setMultiSelectionEnabled(true);
        chooser.setDragEnabled(true);
        setFileChooserFont(chooser.getComponents());
    }

	private void setFileChooserFont(Component[] comp) {
        for (Component component : comp) {
            if (component instanceof Container)
                setFileChooserFont(((Container) component).getComponents());
            try {
                component.setFont(new Font("경기천년제목 Light", Font.BOLD, 15));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
        JButton pressedbutton = (JButton) e.getSource();
        if (pressedbutton.equals(addButton))
            addMusicToPlaylist();
        else if (pressedbutton.equals(deleteButton))
            deleteMusicFromPlaylist();
        else if (pressedbutton.equals(searchButton))
            searchMusic();
        else if (pressedbutton.equals(savePlaylistButton))
            savePlaylistAsFile();
        else if (pressedbutton.equals(fetchPlayListButton))
            fetchPlaylistFromFile();
        else if (pressedbutton.equals(playButton)) {
            playMusic();
        } else if (pressedbutton.equals(pauseButton)) {
            pauseMusic();
        } else if (pressedbutton.equals(prevButton)) {
            playPreviousMusic();
        } else if (pressedbutton.equals(nextButton)) {
            playNextMusic();
        } else {
            setPlayMode(pressedbutton);
        }
	}

    private void addMusicToPlaylist() {
        int option = chooser.showOpenDialog(new JFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File[] selectedfiles = chooser.getSelectedFiles();
            for (File selectedfile : selectedfiles) {
                try {
                    MP3File mp3 = (MP3File) AudioFileIO.read(selectedfile);
                    listModel.addElement(mp3.getFile().getName());
                    fileList.add(selectedfile);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "mp3 확장자만 지원합니다.",
                        "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteMusicFromPlaylist() {
        int[] selected = musicList.getSelectedIndices();
        for (int i = 0; i < selected.length; i++) {
            selected[i] -= i;
            File deletingfile = fileList.get(selected[i]);
            if (media != null)
                if (media.getSource().equals(deletingfile.toURI().toString())) {
                    timer.stop();
                    player.stop();
                    player = null;
                    media = null;
                    deleteButton.setEnabled(false);
                    setPlayButtonEnabled();
                    musicTitleLabel.setText("");
                    musicBarSlider.setVisible(false);
                    currentTimeLabel.setText("");
                    musicLengthLabel.setText("");
                } else {
                    if (media.getSource().equals(fileList.get(i).toURI().toString()))
                        musicList.setSelectedIndex(i);
                }
            else
                deleteButton.setEnabled(false);
            listModel.remove(selected[i]);
            fileList.remove(deletingfile);
        }
    }

    private void searchMusic() {
        String title = searchField.getText();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(null, "검색어를 입력해 주세요!", "찾기 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 0; i < listModel.getSize(); i++)
            if (listModel.getElementAt(i).contains(title))
                results.add(i);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "찾는 노래가 없습니다!", "찾기 오류",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        musicList.setSelectedIndices(results.stream().mapToInt(i -> i).toArray());
    }

    private void savePlaylistAsFile() {
        ObjectOutputStream oos;
        int choice = chooser.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                File path = chooser.getCurrentDirectory().getAbsoluteFile();
                oos = new ObjectOutputStream(new FileOutputStream(path + "\\PlayList.dat"));
                oos.writeObject(fileList);
                oos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void fetchPlaylistFromFile() {
        ObjectInputStream ois;
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                if (media != null)
                    player.stop();
                musicTitleLabel.setText("");
                musicBarSlider.setVisible(false);
                currentTimeLabel.setText("");
                musicLengthLabel.setText("");
                timer.stop();
                listModel.clear();
                player = null;
                media = null;
                setPlayButtonEnabled();
                deleteButton.setEnabled(false);
                ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
                fileList = (ArrayList<File>)ois.readObject();
                for (File file : fileList)
                    listModel.addElement(file.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "유효한 파일이 아닙니다!",
                    "파일 불러오기 오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void playMusic() {
        deleteButton.setEnabled(true);
        if (musicList.getSelectedIndices().length > 0)
            playWhenSelectMusic();
        else
            if (playWhenNotSelectMusic()) return;

        timer.start();
        player.play();
        musicBarSlider.setVisible(true);
        musicTitleLabel.setText(musicList.getSelectedValue());
        setPauseButtonEnabled();
    }

    private void playWhenSelectMusic() {
        int num = musicList.getSelectedIndices()[0];
        musicList.setSelectedIndex(num);
        if (media != null) {
            if (!media.getSource().equals(fileList.get(num).toURI().toString())) {
                media = new Media(fileList.get(num).toURI().toString());
                player = new MediaPlayer(media);
            }
        } else {
            media = new Media(fileList.get(num).toURI().toString());
            player = new MediaPlayer(media);
        }
    }

    private boolean playWhenNotSelectMusic() {
        if (listModel.getSize() > 0) {
            musicList.setSelectedIndex(0);
            media = new Media(fileList.get(0).toURI().toString());
            player = new MediaPlayer(media);
        } else {
            deleteButton.setEnabled(false);
            JOptionPane.showMessageDialog(null, "노래를 추가하세요!", "재생 오류",
                JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void pauseMusic() {
        player.pause();
        setPlayButtonEnabled();
    }

    private void playPreviousMusic() {
        if (media != null) {
            int n = musicList.getSelectedIndex();
            if (n > 0) {
                player.stop();
                musicList.setSelectedIndex(n - 1);
                media = new Media(fileList.get(n - 1).toURI().toString());
                player = new MediaPlayer(media);
                player.play();
                timer.start();
            }
            musicTitleLabel.setText(musicList.getSelectedValue());
        }
    }

    private void playNextMusic() {
        if (media != null) {
            int n = musicList.getSelectedIndex();
            if (n < musicList.getLastVisibleIndex()) {
                player.stop();
                musicList.setSelectedIndex(n + 1);
                media = new Media(fileList.get(n + 1).toURI().toString());
                player = new MediaPlayer(media);
                player.play();
                timer.start();
            }
            musicTitleLabel.setText(musicList.getSelectedValue());
        }
    }

    private void setPlayMode(JButton pressedbutton) {
        pressedbutton.setEnabled(false);
        if (pressedbutton.equals(onceModeButton)) {
            repeatModeButton.setEnabled(true);
            shuffleModeButton.setEnabled(true);
        } else if (pressedbutton.equals(repeatModeButton)) {
            onceModeButton.setEnabled(true);
            shuffleModeButton.setEnabled(true);
        } else {
            onceModeButton.setEnabled(true);
            repeatModeButton.setEnabled(true);
        }
    }
}
