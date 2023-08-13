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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

class MP3_play extends JFrame implements ActionListener { // JFrame 상속 및 ActionListener 구현
	@Serial
    private static final long serialVersionUID = 1L;
	private final ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
        .getResource("background.png"))); // 배경
    private ArrayList<File> fileList = new ArrayList<>();
    private final ImageIcon[] playButtonImage = new ImageIcon[4];
	private final ImageIcon[] optionButtonImage = new ImageIcon[3];
	private MP3Button[] playButton;
	private MP3Button[] optionButton;
	private final JSlider musicBarSlider = new JSlider(JSlider.HORIZONTAL);
	private final JSlider volumeBarSlider = new JSlider(JSlider.VERTICAL);
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
			if(!optionButton[0].isEnabled()) { // 전체 한 번 재생일 경우
				if(n < listModel.getSize() - 1) {
					musicList.setSelectedIndex(n + 1);
					musicTitleLabel.setText(musicList.getSelectedValue());
					media = new Media(fileList.get(n + 1).toURI().toString());
					player = new MediaPlayer(media);
					player.play();
				}
			} else if(!optionButton[1].isEnabled()) { // 전체 반복 재생일 경우
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
			} else { // 셔플 재생일 경우
				if(numOfMusic != listModel.getSize()) { // 리스트 내의 노래 개수가 바뀌었을 경우
					MusicSequence.clear();
					numOfMusic = listModel.getSize();
					for(int i = 0; i < numOfMusic; i++)
						MusicSequence.add(i);
					int present = musicList.getSelectedIndex();
					MusicSequence.remove(present);
					Collections.shuffle(MusicSequence);
				}
				if(MusicSequence.size() > 0) {
					musicList.setSelectedIndex(MusicSequence.getFirst());
					media = new Media(fileList.get(MusicSequence.pop()).toURI().toString());
					musicTitleLabel.setText(musicList.getSelectedValue());
					player = new MediaPlayer(media);
					player.play();
				}
			}
		});
	});

    private final JPanel panel = new JPanel() { // 패널에다 위의 배경을 그림
        @Serial
        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), null);
            super.paintComponents(g);
        }
    };

	// mp3 노래 재생 슬라이더 설정
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
	// 소리 크기 슬라이더 설정
	class VolumeStick extends MouseAdapter implements MouseListener {
		public void modifySlide(double percent) {
			volumeBarSlider.setValue((int)percent);
			if (player != null)
				if ((double) volumeBarSlider.getValue() / 100 >= 0.5) {
					player.setVolume((((double) volumeBarSlider.getValue() / 100) * 7.0 / 5.0) - 0.4);
				} else
					player.setVolume(((double) volumeBarSlider.getValue() / 100 * 2 * (double) volumeBarSlider.getValue() / 100 * 2) * 0.3);
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
	// 목록에 있는 노래 선택했을 때 이벤트 처리
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

	public MP3_play() {
		super("MP3 플레이어");
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
            .getResource("Mp3Playericon.png")));
        setIconImage(icon.getImage());
		setLocation(600, 50); // (x, y) -> 왼쪽에서 오른쪽으로 갈수록 x 증가, 위에서 아래로 갈수록 y 증가
		setSize(500, 700); // MP3 창 크기를 500*700으로 설정
        setButtons();
		add(panel);
		panel.add(musicBarSlider);
		musicBarSlider.setVisible(false);
		panel.add(volumeBarSlider);
        // 검색 버튼
        JButton searchb = new JButton("검색");
        searchb.setLocation(370, 220);
		searchb.setSize(70, 30);
		searchb.addActionListener(this);
		searchb.setActionCommand("search"); // 검색 버튼을 눌렀을 때의 발생되는 이벤트 이름을 "search"라 설정
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
		musicBarSlider.setLocation(50, 145);
		musicBarSlider.setSize(395, 30);
		musicBarSlider.setBackground(Color.white);
		musicBarSlider.setOpaque(false);
		musicBarSlider.addMouseListener(new Mp3Stick());
		volumeBarSlider.setLocation(400, 34);
		volumeBarSlider.setSize(30, 90);
		volumeBarSlider.setBackground(Color.white);
		volumeBarSlider.setOpaque(false);
		volumeBarSlider.setToolTipText("볼륨 크기를 조절합니다.");
		volumeBarSlider.addMouseListener(new VolumeStick());
        // 볼륨 조절 슬라이더의 노브에 변화가 일어났을 때 발생되는 이벤트 처리
        volumeBarSlider.addChangeListener(e -> {
            if (player != null) {
                if ((double) volumeBarSlider.getValue() / 100 >= 0.5) {
                    player.setVolume((((double) volumeBarSlider.getValue() / 100) * 7.0 / 5.0) - 0.4);
                } else
                    player.setVolume(((double) volumeBarSlider.getValue() / 100 * 2 * (double) volumeBarSlider.getValue() / 100 * 2) * 0.3);
            }
            volumeLabel.setText("Vol : " + volumeBarSlider.getValue());
        });
		panel.add(currentTimeLabel);
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
		panel.add(musicLengthLabel);
		musicLengthLabel.setSize(55, 20);
		musicLengthLabel.setLocation(386, 180);
		musicLengthLabel.setFont(new Font("HY엽서M", Font.BOLD, 15));
		musicLengthLabel.setForeground(Color.BLUE);
		chooser.setPreferredSize(new Dimension(700, 500));
		setFileChooserFont(chooser.getComponents());
        // JFileChooser에서 .mp3, .dat 파일만 필터링
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
		panel.setLayout(null); // setLayout을 null로 해야지 내가 설정한 대로 됨
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫으면 프로세스도 같이 종료
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
        Point[] playButtonPos = {new Point(200, 30), new Point(100, 20), new Point(292, 30)};

        for (int i = 0; i < 3; i++) { // 재생(i = 0), 이전곡(i = 1), 다음곡(i = 2)
            playButtonImage[i] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(playButtonFileName[i])));
            playButton[i] = new MP3Button(this, playButtonImage[i], new Dimension(100, 100),
                playButtonPos[i], playButtonToolTip[i]);
            panel.add(playButton[i]);

            optionButtonImage[i] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource(optionButtonFileName[i])));
            optionButton[i] = new MP3Button(this, optionButtonImage[i], new Dimension(45, 40),
                new Point(40 + i * 84, 170), optionButtonToolTip[i]);
            panel.add(optionButton[i]);
        }
        playButtonImage[3] = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
            .getResource(playButtonFileName[3])));
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
	// 각 버튼마다 추가한 액션 이벤트 처리(검색 버튼, 추가 버튼, 삭제 버튼, 목록 내보내기 버튼, 목록 불러오기 버튼, 재생/일시정지 버튼, 이전곡 버튼, 다음곡 버튼)
	@SuppressWarnings("unchecked")
	@Override // ActionListener를 구현하기 위해선 추상 메소드 actionPerformed를 오버라이딩하면 됨!
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("search")) { // 검색 버튼 누르거나 검색어 입력하고 엔터 입력했을 때
			String title = searchField.getText();
			if(title.equals("")) { // 아무것도 입력하지 않고 검색하면
				JOptionPane.showMessageDialog(null, "검색어를 입력해 주세요!", "찾기 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ArrayList<Integer> results = new ArrayList<>();
			for(int i = 0; i < listModel.getSize(); i++)
				if(listModel.getElementAt(i).contains(title)) // 리스트의 i번째 index가 검색어를 포함하고 있을 경우
					results.add(i);
			int[] result = new int[results.size()];
			for(int i = 0; i < result.length; i++)
				result[i] = results.get(i);
			if(result.length == 0) { // result 배열에 아무것도 저장이 안 되었을 때
				JOptionPane.showMessageDialog(null, "찾는 노래가 없습니다!", "찾기 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			musicList.setSelectedIndices(result);
		} else { // 그게 아니라면 
			JButton pressedbutton = (JButton) e.getSource(); // 이 actionPerformed 메소드가 실행되었을 때 클릭된 버튼을 불러옴
			if (pressedbutton.equals(addButton)) { // 추가 버튼 클릭했을 때
				int option = chooser.showOpenDialog(new JFrame());
				if(option == JFileChooser.APPROVE_OPTION) {
					File[] selectedfiles = chooser.getSelectedFiles(); // 파일 여러 개 선택하고 확인 누르면 그 파일들이 selectedfiles 배열에 저장됨
                    for (File selectedfile : selectedfiles) {
                        try {
                            MP3File mp3 = (MP3File) AudioFileIO.read(selectedfile);
                            Tag tag = mp3.getTag();
                            listModel.addElement(tag.getFirst(FieldKey.TITLE)); // 해당 mp3 파일의 제목을 추출해서 list에 추가
                            fileList.add(selectedfile); // filelists에 파일 추가
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "mp3 확장자만 지원합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
				}
			} else if(pressedbutton.equals(deleteButton)) { // 삭제 버튼 눌렀을 때
				int[] selected = musicList.getSelectedIndices();
				for(int i = 0; i < selected.length; i++) {
					selected[i] -= i; // 하나 삭제할 때마다 index가 1씩 당겨지므로
					File deletingfile = fileList.get(selected[i]);
					if(media != null) // 노래를 재생 중이었을 때
						if(media.getSource().equals(deletingfile.toURI().toString())) { // 삭제할려는 노래가 재생 중 일때
							timer.stop();
							player.stop();
							player = null;
							media = null;
							deleteButton.setEnabled(false);
							playButton[0].setIcon(playButtonImage[0]); // 재생 버튼으로 바꿈
							musicTitleLabel.setText("");
							musicBarSlider.setVisible(false);
							currentTimeLabel.setText("");
							musicLengthLabel.setText("");
						} else { // 그렇지 않을 때 원래 재생 중이던 노래 그대로 선택된 대로 표시
							if(media.getSource().equals(fileList.get(i).toURI().toString()))
								musicList.setSelectedIndex(i);
						}
					else
						deleteButton.setEnabled(false);
					listModel.remove(selected[i]);
					fileList.remove(deletingfile);
				}
			} else if(pressedbutton.equals(sendPlaylistButton)) { // 목록 내보내기 버튼 클릭했을 때
				ObjectOutputStream oos;
				int choice = chooser.showSaveDialog(null);
				if(choice == JFileChooser.APPROVE_OPTION) {
					try {
						File path = chooser.getCurrentDirectory().getAbsoluteFile(); // JFileChooser에서 사용자가 정한 절대 경로를 path에 저장
						oos = new ObjectOutputStream(new FileOutputStream(path + "\\PlayList.dat")); // 그 경로에 PlayList.dat파일 생성
						oos.writeObject(fileList); // 그 PlayList.dat파일에 ArrayList<File> 타입의 객체 filelists 저장
						oos.flush();
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			} else if(pressedbutton.equals(fetchPlayListButton)) { // 목록 불러오기 버튼 클릭했을 때
				ObjectInputStream ois;
				int choice = chooser.showOpenDialog(null);
				if(choice == JFileChooser.APPROVE_OPTION) {
					try {
						if(media != null)
							player.stop();
						musicTitleLabel.setText("");
						musicBarSlider.setVisible(false);
						currentTimeLabel.setText("");
						musicLengthLabel.setText("");
						timer.stop();
						listModel.clear(); // 현재 PlayList 초기화
						player = null;
						media = null;
						playButton[0].setIcon(playButtonImage[0]); // 재생 버튼으로 바꿈
						deleteButton.setEnabled(false);
						ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile())); 
						fileList = (ArrayList<File>)ois.readObject(); // 사용자가 선택한 파일에 저장된 객체 정보를 읽어서 filelists에 저장
                        for (File file : fileList) {
                            MP3File mp3 = (MP3File) AudioFileIO.read(file);
                            Tag tag = mp3.getTag();
                            listModel.addElement(tag.getFirst(FieldKey.TITLE));
                        }
					} catch (Exception e3) {
						JOptionPane.showMessageDialog(null, "유효한 파일이 아닙니다!", "파일 불러오기 오류", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (pressedbutton.getIcon().equals(playButtonImage[0])) { // 재생 버튼 클릭했을 때
				deleteButton.setEnabled(true);
				if(musicList.getSelectedIndices().length > 0) { // 재생 목록에 항목을 하나 이상 선택했을 경우
					int num = musicList.getSelectedIndices()[0]; 
					musicList.setSelectedIndex(num); // 그 항목만 선택된 걸로 표시
					if(media != null) { // 한 번 이상 재생 버튼을 누른 적이 있다면
						if(!media.getSource().equals(fileList.get(num).toURI().toString())) {
							media = new Media(fileList.get(num).toURI().toString());
							player = new MediaPlayer(media);
						} // 플레이 중이었던 노래와 선택한 노래가 다를 때 선택한 노래 재생
					} else { // 재생 버튼 누른 적 없다면 (MP3 플레이어를 실행하고 원하는 노래 선택한 다음 재생 버튼 누른 경우) 선택한 노래 재생
						media = new Media(fileList.get(num).toURI().toString());
						player = new MediaPlayer(media);
					} 
				} else { // 재생 목록에 항목을 선택하지 않았을 겅우 (MP3 플레이어 실행하고 바로 재생 버튼 누르거나 재생 중이었던 노래 삭제 후 재생 버튼 누름)
					if(listModel.getSize() > 0) { // 재생 목록에 노래가 하나 이상 있다면
						musicList.setSelectedIndex(0); 
						media = new Media(fileList.get(0).toURI().toString());
						player = new MediaPlayer(media);
					} else { // 재생 목록에 노래가 없다면
						deleteButton.setEnabled(false);
						JOptionPane.showMessageDialog(null, "노래를 추가하세요!", "재생 오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				timer.start();
				musicBarSlider.setVisible(true);
				player.play();
				musicTitleLabel.setText(musicList.getSelectedValue());
				pressedbutton.setToolTipText("노래를 잠시 멈춥니다.");
				pressedbutton.setIcon(playButtonImage[3]); // 버튼의 이미지를 "일시정지.png"로 바꿈
			} else if (pressedbutton.getIcon().equals(playButtonImage[3])) { // 일시 정지 버튼 클릭했을 때
				player.pause(); // 노래 일시 정지
				pressedbutton.setToolTipText("PlayList에서 선택한 노래를 재생합니다.");
				pressedbutton.setIcon(playButtonImage[0]); // 버튼 이미지를 "재생.png"로 바꿈
			} else if (pressedbutton.getIcon().equals(playButtonImage[1]) ||
					pressedbutton.getIcon().equals(playButtonImage[2])) { // 이전곡 또는 다음곡 버튼 클릭했을 때
				if (media != null) {
					int n = musicList.getSelectedIndex();
					if(pressedbutton.getIcon().equals(playButtonImage[1])) { // 이전곡 클릭 시
						if (n > 0) {
							player.stop();
							musicList.setSelectedIndex(n - 1);
							media = new Media(fileList.get(n - 1).toURI().toString());
							player = new MediaPlayer(media);
							player.play();
							timer.start();
						}
					} else { // 다음곡 클릭 시
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
			} else { // 전체 한 번 재생 버튼, 전체 반복 재생 버튼 또는 셔플 재생 버튼 클릭 시
				pressedbutton.setEnabled(false); // 클릭된 버튼 비활성화
				if(pressedbutton.getIcon().equals(optionButtonImage[0])) { // 전체 한 번 재생 버튼 클릭했을 경우
					optionButton[1].setEnabled(true);
					optionButton[2].setEnabled(true);
				} else if(pressedbutton.getIcon().equals(optionButtonImage[1])) { // 전체 반복 재생 버튼 클릭했을 경우
					optionButton[0].setEnabled(true);
					optionButton[2].setEnabled(true);
				} else { // 셔플 재생 클릭했을 경우
					optionButton[0].setEnabled(true);
					optionButton[1].setEnabled(true);
				}
			}
		}
	}
}
