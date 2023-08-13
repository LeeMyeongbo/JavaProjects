import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

class MP3Body extends JFrame implements ActionListener {
	@Serial
    private static final long serialVersionUID = 1L;
    private ArrayList<File> fileList = new ArrayList<>();
    private final ImageIcon[] playButtonImage = new ImageIcon[4];
	private final ImageIcon[] optionButtonImage = new ImageIcon[3];
	private MP3Button[] playButton;
	private MP3Button[] optionButton;
	private MP3Slider musicBarSlider;
	private MP3Slider volumeBarSlider;
	private final JLabel currentTimeLabel = new JLabel();
	private final JLabel musicLengthLabel = new JLabel();
	private final JLabel musicTitleLabel = new JLabel();
	private final JLabel volumeLabel = new JLabel();
	private final JTextField searchField = new JTextField();
    private final JButton addButton = new JButton("추가");
	private final JButton deleteButton = new JButton("삭제");
	private final JButton sendPlaylistButton = new JButton("목록 내보내기");
	private final JButton fetchPlayListButton = new JButton("목록 불러오기");
	private final JFileChooser chooser = new JFileChooser("C:\\");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
	private final JList<String> musicList = new JList<>();
    private Media media = null;
	private MediaPlayer player = null;
	private int numOfMusic;
	private final LinkedList<Integer> MusicSequence = new LinkedList<>();

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
			if (!optionButton[0].isEnabled()) {
				if (n < listModel.getSize() - 1) {
					musicList.setSelectedIndex(n + 1);
					musicTitleLabel.setText(musicList.getSelectedValue());
					media = new Media(fileList.get(n + 1).toURI().toString());
					player = new MediaPlayer(media);
					player.play();
				}
			} else if (!optionButton[1].isEnabled()) {
				if(n < listModel.getSize() - 1) {
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
					for(int i = 0; i < numOfMusic; i++)
						MusicSequence.add(i);
					int present = musicList.getSelectedIndex();
					MusicSequence.remove(present);
					Collections.shuffle(MusicSequence);
				}
				if (MusicSequence.size() > 0) {
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
		public void modifySlide(double percent) {
			musicBarSlider.setValue((int) percent);
			int pos = (int) player.getTotalDuration().toSeconds();
			player.seek(Duration.seconds(pos * (musicBarSlider.getValue() / 100.0)));
			if (playButton[0].getIcon().equals(playButtonImage[3])) {
				player.play();
				timer.start();
			} else
				player.pause();
		}

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
	}

	class VolumeStick extends MouseAdapter implements MouseListener {
		public void modifySlide(double percent) {
			volumeBarSlider.setValue((int)percent);
			if (player != null)
				if ((double) volumeBarSlider.getValue() / 100 >= 0.5) {
					player.setVolume((((double) volumeBarSlider.getValue() / 100) * 7.0 / 5.0) - 0.4);
				} else
					player.setVolume(Math.pow((double) volumeBarSlider.getValue() / 100 * 2, 2) * 0.3);
			volumeLabel.setText("Vol : " + volumeBarSlider.getValue());
		}
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
	}

	class Listselect extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			int n = musicList.getSelectedIndex();
			deleteButton.setEnabled(true);
			if(e.getClickCount() == 2) {
				musicBarSlider.setVisible(true);
				timer.start();
				if (media != null)
					player.stop();
				media = new Media(fileList.get(n).toURI().toString());
				player = new MediaPlayer(media);
				player.play();
				player.setVolume(0.3);
				playButton[0].setIcon(playButtonImage[3]);
				musicTitleLabel.setText(musicList.getSelectedValue());
			}
		}
	}

	public MP3Body() {
		super("MP3 플레이어");
        ImageIcon icon = new ImageIcon(getResourceByPath("Mp3Playericon.png"));
        setIconImage(icon.getImage());
		setLocation(600, 50);
		setSize(500, 700);
        add(panel);
        setButtons();
        setSlider();

        JButton searchb = new JButton("검색");
        searchb.setLocation(370, 220);
		searchb.setSize(70, 30);
		searchb.addActionListener(this);
		searchb.setActionCommand("search");
		searchb.setToolTipText("PlayList 안에 있는 노래들 중에서 검색합니다.");
		panel.add(searchb);

		addButton.setLocation(297, 620);
		addButton.setSize(70, 30);
		addButton.addActionListener(this);
		addButton.setToolTipText("노래를 PlayList에 추가합니다.");
		panel.add(addButton);

		deleteButton.setLocation(373, 620);
		deleteButton.setSize(70, 30);
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("선택한 노래를 PlayList에서 삭제합니다.");
		panel.add(deleteButton);

		musicList.setModel(listModel);
		musicList.setFont(new Font("HY엽서M", Font.PLAIN, 18));
		LineBorder lborder = new LineBorder(Color.gray, 1);
		TitledBorder border = new TitledBorder(lborder, "PlayLists", TitledBorder.CENTER, TitledBorder.TOP);
		border.setTitleFont(new Font("Algerian", Font.BOLD, 30));
		border.setTitleColor(Color.black);
		musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		musicList.setForeground(Color.GRAY);
		musicList.setSelectionForeground(Color.BLACK);
		musicList.addMouseListener(new Listselect());
        JScrollPane scroll = new JScrollPane(musicList);
		scroll.setSize(395, 330);
		scroll.setLocation(50, 280);
		scroll.setBorder(border);
		scroll.setOpaque(false);
		panel.add(scroll);

		searchField.setLocation(52, 220);
		searchField.setSize(305, 30);
		searchField.registerKeyboardAction(this, "search",
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
		panel.add(searchField);

		musicTitleLabel.setText("");
		musicTitleLabel.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 20));
		musicTitleLabel.setForeground(Color.ORANGE);
		musicTitleLabel.setSize(500,20);
		musicTitleLabel.setLocation(0, 123);
		musicTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(musicTitleLabel);

		volumeLabel.setVisible(false);
		volumeLabel.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 23));
		volumeLabel.setForeground(Color.RED);
		volumeLabel.setSize(100, 25);
		volumeLabel.setLocation(375, 12);
		volumeLabel.setText("Vol : " + volumeBarSlider.getValue());
		panel.add(volumeLabel);

		currentTimeLabel.setSize(55, 20);
		currentTimeLabel.setLocation(45, 180);
		currentTimeLabel.setFont(new Font("HY엽서M", Font.BOLD, 15));
		currentTimeLabel.setForeground(Color.BLUE);
        panel.add(currentTimeLabel);

		musicLengthLabel.setSize(55, 20);
		musicLengthLabel.setLocation(386, 180);
		musicLengthLabel.setFont(new Font("HY엽서M", Font.BOLD, 15));
		musicLengthLabel.setForeground(Color.BLUE);
        panel.add(musicLengthLabel);

		chooser.setPreferredSize(new Dimension(700, 500));
		setFileChooserFont(chooser.getComponents());

        FileNameExtensionFilter filter = new FileNameExtensionFilter(".mp3, .dat", "mp3", "dat");
        chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDragEnabled(true);

		sendPlaylistButton.setLocation(52, 620);
		sendPlaylistButton.setSize(117, 30);
		sendPlaylistButton.addActionListener(this);
		sendPlaylistButton.setToolTipText("PlayList를 파일로 저장합니다.");
		panel.add(sendPlaylistButton);

		fetchPlayListButton.setLocation(174, 620);
		fetchPlayListButton.setSize(117, 30);
		fetchPlayListButton.addActionListener(this);
		fetchPlayListButton.setToolTipText("저장된 PlayList를 불러옵니다.");
		panel.add(fetchPlayListButton);

		panel.setLayout(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

    public void setButtons() {
        playButton = new MP3Button[3];
        optionButton = new MP3Button[3];

        String[] playButtonFileName = {"재생.png", "이전곡.png", "다음곡.png", "일시정지.png"};
        String[] optionButtonFileName = {"전체 한 번씩.png", "전체반복.png", "셔플.png"};
        String[] playButtonToolTip = {
            "PlayList에서 선택한 노래를 재생합니다.",
            "PlayList에서 선택한 노래의 이전곡을 재생합니다.",
            "PlayList에서 선택한 노래의 다음곡을 재생합니다."
        };
        String[] optionButtonToolTip = {
            "PlayList에 있는 곡을 한 번씩만 재생합니다.",
            "PlayList에 있는 곡을 반복해서 재생합니다.",
            "PlayList에 있는 곡을 무작위로 재생합니다."
        };
        Point[] playButtonPos = {new Point(200, 30), new Point(80, 30), new Point(315, 30)};

        for (int i = 0; i < 3; i++) {
            playButtonImage[i] = new ImageIcon(getResourceByPath(playButtonFileName[i]));
            playButton[i] = new MP3Button(this, playButtonImage[i], new Dimension(100, 100),
                playButtonPos[i], playButtonToolTip[i]);
            panel.add(playButton[i]);

            optionButtonImage[i] = new ImageIcon(getResourceByPath(optionButtonFileName[i]));
            optionButton[i] = new MP3Button(this, optionButtonImage[i], new Dimension(45, 40),
                new Point(140 + i * 84, 170), optionButtonToolTip[i]);
            panel.add(optionButton[i]);
        }

        optionButton[0].setEnabled(false);
        playButtonImage[3] = new ImageIcon(getResourceByPath(playButtonFileName[3]));
    }

    public void setSlider() {
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

	public void setFileChooserFont(Component[] comp) {
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

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("search")) {
			String title = searchField.getText();
			if (title.equals("")) {
				JOptionPane.showMessageDialog(null, "검색어를 입력해 주세요!", "찾기 오류",
                    JOptionPane.WARNING_MESSAGE);
				return;
			}
			ArrayList<Integer> results = new ArrayList<>();
			for (int i = 0; i < listModel.getSize(); i++)
				if (listModel.getElementAt(i).contains(title))
					results.add(i);
			int[] result = new int[results.size()];
			for (int i = 0; i < result.length; i++)
				result[i] = results.get(i);
			if (result.length == 0) {
				JOptionPane.showMessageDialog(null, "찾는 노래가 없습니다!", "찾기 오류",
                    JOptionPane.WARNING_MESSAGE);
				return;
			}
			musicList.setSelectedIndices(result);
		} else {
			JButton pressedbutton = (JButton) e.getSource();
			if (pressedbutton.equals(addButton)) {
				int option = chooser.showOpenDialog(new JFrame());
				if (option == JFileChooser.APPROVE_OPTION) {
					File[] selectedfiles = chooser.getSelectedFiles();
                    for (File selectedfile : selectedfiles) {
                        try {
                            MP3File mp3 = (MP3File) AudioFileIO.read(selectedfile);
                            Tag tag = mp3.getTag();
                            listModel.addElement(tag.getFirst(FieldKey.TITLE));
                            fileList.add(selectedfile);
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "mp3 확장자만 지원합니다.",
                                "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
				}
			} else if (pressedbutton.equals(deleteButton)) {
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
							playButton[0].setIcon(playButtonImage[0]);
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
			} else if (pressedbutton.equals(sendPlaylistButton)) {
				ObjectOutputStream oos;
				int choice = chooser.showSaveDialog(null);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						File path = chooser.getCurrentDirectory().getAbsoluteFile();
						oos = new ObjectOutputStream(new FileOutputStream(path + "\\PlayList.dat"));
						oos.writeObject(fileList);
						oos.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} else if (pressedbutton.equals(fetchPlayListButton)) {
				ObjectInputStream ois;
				int choice = chooser.showOpenDialog(null);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						if(media != null)
							player.stop();
						musicTitleLabel.setText("");
						musicBarSlider.setVisible(false);
						currentTimeLabel.setText("");
						musicLengthLabel.setText("");
						timer.stop();
						listModel.clear();
						player = null;
						media = null;
						playButton[0].setIcon(playButtonImage[0]);
						deleteButton.setEnabled(false);
						ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
						fileList = (ArrayList<File>)ois.readObject();
                        for (File file : fileList) {
                            MP3File mp3 = (MP3File) AudioFileIO.read(file);
                            Tag tag = mp3.getTag();
                            listModel.addElement(tag.getFirst(FieldKey.TITLE));
                        }
					} catch (Exception e3) {
						JOptionPane.showMessageDialog(null, "유효한 파일이 아닙니다!",
                            "파일 불러오기 오류", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (pressedbutton.getIcon().equals(playButtonImage[0])) {
				deleteButton.setEnabled(true);
				if (musicList.getSelectedIndices().length > 0) {
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
				} else {
					if (listModel.getSize() > 0) {
						musicList.setSelectedIndex(0);
						media = new Media(fileList.get(0).toURI().toString());
						player = new MediaPlayer(media);
					} else {
						deleteButton.setEnabled(false);
						JOptionPane.showMessageDialog(null, "노래를 추가하세요!", "재생 오류",
                            JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				timer.start();
				musicBarSlider.setVisible(true);
				player.play();
				musicTitleLabel.setText(musicList.getSelectedValue());
				pressedbutton.setToolTipText("노래를 잠시 멈춥니다.");
				pressedbutton.setIcon(playButtonImage[3]);
			} else if (pressedbutton.getIcon().equals(playButtonImage[3])) {
				player.pause();
				pressedbutton.setToolTipText("PlayList에서 선택한 노래를 재생합니다.");
				pressedbutton.setIcon(playButtonImage[0]);
			} else if (pressedbutton.getIcon().equals(playButtonImage[1]) ||
					pressedbutton.getIcon().equals(playButtonImage[2])) {
				if (media != null) {
					int n = musicList.getSelectedIndex();
					if(pressedbutton.getIcon().equals(playButtonImage[1])) {
						if (n > 0) {
							player.stop();
							musicList.setSelectedIndex(n - 1);
							media = new Media(fileList.get(n - 1).toURI().toString());
							player = new MediaPlayer(media);
							player.play();
							timer.start();
						}
					} else {
						if (n < musicList.getLastVisibleIndex()) {
							player.stop();
							musicList.setSelectedIndex(n + 1);
							media = new Media(fileList.get(n + 1).toURI().toString());
							player = new MediaPlayer(media);
							player.play();
							timer.start();
						}
					}
					musicTitleLabel.setText(musicList.getSelectedValue());
				}
			} else {
				pressedbutton.setEnabled(false);
				if (pressedbutton.getIcon().equals(optionButtonImage[0])) {
					optionButton[1].setEnabled(true);
					optionButton[2].setEnabled(true);
				} else if (pressedbutton.getIcon().equals(optionButtonImage[1])) {
					optionButton[0].setEnabled(true);
					optionButton[2].setEnabled(true);
				} else {
					optionButton[0].setEnabled(true);
					optionButton[1].setEnabled(true);
				}
			}
		}
	}
}
