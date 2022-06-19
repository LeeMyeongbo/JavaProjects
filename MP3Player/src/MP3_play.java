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

class MP3_play extends JFrame implements ActionListener { // JFrame 상속 및 ActionListener 구현
	@Serial
    private static final long serialVersionUID = 1L;
	private final ImageIcon image = new ImageIcon("images/background.png"); // 배경
    private ArrayList<File> filelists = new ArrayList<>(); // mp3 파일 저장용 ArrayList
    private final ImageIcon[] buttonimgs = new ImageIcon[4]; // 재생, 일시정지, 이전곡, 다음곡 버튼 이미지
	private final ImageIcon[] optionimgs = new ImageIcon[3]; // 전체 한 번 재생, 전체 반복 재생, 셔플 재생 버튼 이미지
	private final JButton[] jb = new JButton[3]; // 재생, 일시정지, 이전곡, 다음곡 버튼
	private final JButton[] options = new JButton[3]; // 전체 한 번 재생, 전체 반복 재생, 셔플 재생 버튼
	private final JSlider js1 = new JSlider(JSlider.HORIZONTAL); // 노래 재생 상태를 나타내는 js1 슬라이더
	private final JSlider js2 = new JSlider(JSlider.VERTICAL); // 볼륨 크기를 나타내는 js2 슬라이더
	private final JLabel presentimes = new JLabel(); // 현재 재생 중인 시간 표시
	private final JLabel lastimes = new JLabel(); // 총 노래 길이 표시
	private final JLabel song = new JLabel(); // 재생 중인 노래 제목 표시
	private final JLabel vol = new JLabel(); // 볼륨 표시
	private final JTextField jt = new JTextField(); // 검색창
    private final JButton addb = new JButton("추가"); // 추가 버튼(디렉토리에서 노래를 PlayList에 추가함)
	private final JButton deleteb = new JButton("삭제"); // 삭제 버튼(PlayList에서 노래 삭제)
	private final JButton savelists = new JButton("목록 내보내기"); // 재생목록 내보내기 버튼
	private final JButton openlists = new JButton("목록 불러오기"); // 재생목록 불러오기 버튼
	private final JFileChooser chooser = new JFileChooser("C:\\"); // 추가 버튼을 눌렀을 때 파일 디렉토리를 나타냄
    private final DefaultListModel<String> listmodel = new DefaultListModel<>(); // JList가 리스트화할 기본 ListModel 설정
	private final JList<String> jl = new JList<>(); // PlayList에 추가된 노래 목록 나타냄
    private Media m = null; // mp3파일 실행을 위한 Media클래스 타입의 변수 m
	private MediaPlayer player = null; // Media클래스에 있는 mp3파일을 실제로 실행하기 위한 MediaPlayer 클래스타입의 변수 player
	private int size; // PlayList에 저장된 노래의 개수를 실시간으로 파악
	private final LinkedList<Integer> order = new LinkedList<>(); // 셔플 재생일 때 재생 순서를 정하기 위함
	// 노래가 시작되었을 때 주기적으로 처리할 작업 명시
	private final Timer t = new Timer(100, e -> { // 0.1초 동안 반복해서 해당 actionPerformed를 호출 (t.start() 메소드 호출 시작)
		Duration d1 = player.getCurrentTime();
		Duration d2 = player.getTotalDuration();
		String time1 = String.format("%02d:%02d%n", (int) d1.toMinutes(), (int) d1.toSeconds() % 60);
		String time2 = String.format("%02d:%02d%n", (int) d2.toMinutes(), (int) d2.toSeconds() % 60); 
		presentimes.setText(time1);
		lastimes.setText(time2);
		js1.setValue((int) (100 * (d1.toSeconds() / d2.toSeconds()))); 
		player.setOnEndOfMedia(() -> {
			int n = jl.getSelectedIndex();
			if(!options[0].isEnabled()) { // 전체 한 번 재생일 경우
				if(n < listmodel.getSize() - 1) {
					jl.setSelectedIndex(n + 1);
					song.setText(jl.getSelectedValue());
					m = new Media(filelists.get(n + 1).toURI().toString());
					player = new MediaPlayer(m);
					player.play();
				}
			} else if(!options[1].isEnabled()) { // 전체 반복 재생일 경우
				if(n < listmodel.getSize() - 1) {
					jl.setSelectedIndex(n + 1);
					m = new Media(filelists.get(n + 1).toURI().toString());
				} else {
					jl.setSelectedIndex(0);
					m = new Media(filelists.get(0).toURI().toString());
				}
				player = new MediaPlayer(m);
				song.setText(jl.getSelectedValue());
				player.play();
			} else { // 셔플 재생일 경우
				if(size != listmodel.getSize()) { // 리스트 내의 노래 개수가 바뀌었을 경우
					order.clear();
					size = listmodel.getSize();
					for(int i = 0; i < size; i++)
						order.add(i);
					int present = jl.getSelectedIndex();
					order.remove(present);
					Collections.shuffle(order);
				}
				if(order.size() > 0) {
					jl.setSelectedIndex(order.getFirst());
					m = new Media(filelists.get(order.pop()).toURI().toString());
					song.setText(jl.getSelectedValue());
					player = new MediaPlayer(m);
					player.play();
				}
			}
		});
	});
	// mp3 노래 재생 슬라이더 설정
	class Mp3Stick extends MouseAdapter implements MouseListener {
		public void modifySlide(double percent) {
			js1.setValue((int) percent);
			int pos = (int) player.getTotalDuration().toSeconds();
			player.seek(Duration.seconds(pos * (js1.getValue() / 100.0)));
			if (jb[0].getIcon().equals(buttonimgs[3])) {
				player.play();
				t.start();
			} else
				player.pause();
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			t.stop();
			player.pause();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point p = e.getPoint();
			double percent = p.getX() / (double) js1.getWidth() * 100;
			modifySlide(percent);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			double percent = p.getX() / (double) js1.getWidth() * 100;
			modifySlide(percent);
		}
	}
	// 소리 크기 슬라이더 설정
	class VolumeStick extends MouseAdapter implements MouseListener {
		public void modifySlide(double percent) {
			js2.setValue((int)percent);
			if (player != null)
				if ((double) js2.getValue() / 100 >= 0.5) {
					player.setVolume((((double) js2.getValue() / 100) * 7.0 / 5.0) - 0.4);
				} else
					player.setVolume(((double) js2.getValue() / 100 * 2 * (double) js2.getValue() / 100 * 2) * 0.3);
			vol.setText("Vol : " + js2.getValue());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			double percent = (js2.getHeight() - p.getY()) / (double)js2.getHeight() * 100;
			modifySlide(percent);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			vol.setVisible(true);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			vol.setVisible(false);
		}
	}
	// 목록에 있는 노래 선택했을 때 이벤트 처리
	class Listselect extends MouseAdapter implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			int n = jl.getSelectedIndex();
			deleteb.setEnabled(true);
			if(e.getClickCount() == 2) {
				js1.setVisible(true);
				t.start();
				if (m != null)
					player.stop();
				m = new Media(filelists.get(n).toURI().toString());
				player = new MediaPlayer(m);
				player.play();
				player.setVolume(0.3);
				jb[0].setIcon(buttonimgs[3]);
				song.setText(jl.getSelectedValue());
			}
		}
	}
	// 클래스 생성자(MP3 Player 기본 세팅)
	public MP3_play() {
		super("MP3 플레이어"); // 상위 클래스인 JFrame 클래스의 생성자 호출
        // 4가지 버튼 이미지 파일 이름
        String[] but = {"images/재생.png", "images/이전곡.png", "images/다음곡.png", "images/일시정지.png"};
        // 패널에다 위의 배경을 그림
        JPanel jp = new JPanel() { // 패널에다 위의 배경을 그림
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paint(Graphics g) {
                g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
                super.paintComponents(g);
            }
        };
        for (int i = 0; i < 3; i++) { // 재생(i = 0), 이전곡(i = 1), 다음곡(i = 2)
			buttonimgs[i] = new ImageIcon(but[i]); // 파일의 상대경로 입력(파일 이름만 적으면 상대 경로에 위치해 있는 파일을 받음)
			jb[i] = new JButton(buttonimgs[i]); // 버튼 이미지를 가지는 버튼 생성
			jb[i].setBorderPainted(false); // 버튼 경계선 제거
			jb[i].setContentAreaFilled(false); // 버튼 내부 색 제거
			jb[i].setFocusPainted(false); // 이미지 경계선 제거
			jb[i].setSize(100, 100); // 버튼 크기를 100*100 으로 설정
			jb[i].addActionListener(this);
			jp.add(jb[i]);
            // 3가지 재생 옵션 파일 이름
            String[] opt = {"images/전체 한 번씩.png", "images/전체반복.png", "images/셔플.png"};
            optionimgs[i] = new ImageIcon(opt[i]);
			options[i] = new JButton(optionimgs[i]);
			options[i].setBorderPainted(false);
			options[i].setContentAreaFilled(false);
			options[i].setFocusPainted(false);
			options[i].setSize(45, 40);
			options[i].setLocation(140 + i * 84, 170);
			options[i].addActionListener(this);
			jp.add(options[i]);
		}
		buttonimgs[3] = new ImageIcon(but[3]); // 일시정지 버튼 이미지를 buttonimgs의 3번 index에 저장
		setLocation(600, 50); // (x, y) -> 왼쪽에서 오른쪽으로 갈수록 x 증가, 위에서 아래로 갈수록 y 증가
		setSize(500, 700); // MP3 창 크기를 500*700으로 설정
		add(jp);
		jb[0].setLocation(200, 30);
		jb[0].setToolTipText("PlayList에서 선택한 노래를 재생합니다.");
		jb[1].setLocation(100, 30);
		jb[1].setToolTipText("PlayList에서 선택한 노래의 이전곡을 재생합니다.");
		jb[2].setLocation(292, 30);
		jb[2].setToolTipText("PlayList에서 선택한 노래의 다음곡을 재생합니다.");
		options[0].setEnabled(false);
		options[0].setToolTipText("PlayList에 있는 곡을 한 번씩만 재생합니다.");
		options[1].setToolTipText("PlayList에 있는 곡을 반복해서 재생합니다.");
		options[2].setToolTipText("PlayList에 있는 곡을 무작위로 재생합니다.");
		jp.add(js1);
		js1.setVisible(false);
		jp.add(js2);
        // 검색 버튼
        JButton searchb = new JButton("검색");
        searchb.setLocation(370, 220);
		searchb.setSize(70, 30);
		searchb.addActionListener(this);
		searchb.setActionCommand("search"); // 검색 버튼을 눌렀을 때의 발생되는 이벤트 이름을 "search"라 설정
		searchb.setToolTipText("PlayList 안에 있는 노래들 중에서 검색합니다.");
		jp.add(searchb);
		addb.setLocation(297, 620);
		addb.setSize(70, 30);
		addb.addActionListener(this);
		addb.setToolTipText("노래를 PlayList에 추가합니다.");
		jp.add(addb);
		deleteb.setLocation(373, 620);
		deleteb.setSize(70, 30);
		deleteb.setEnabled(false);
		deleteb.addActionListener(this);
		deleteb.setToolTipText("선택한 노래를 PlayList에서 삭제합니다.");
		jp.add(deleteb);
		jl.setModel(listmodel);
		jl.setFont(new Font("HY엽서M", Font.PLAIN, 18));
		LineBorder lborder = new LineBorder(Color.gray, 1);
		TitledBorder border = new TitledBorder(lborder, "PlayLists", TitledBorder.CENTER, TitledBorder.TOP);
		border.setTitleFont(new Font("Algerian", Font.BOLD, 30));
		border.setTitleColor(Color.black);
		jl.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jl.setForeground(Color.GRAY);
		jl.setSelectionForeground(Color.BLACK);
		jl.addMouseListener(new Listselect());
        JScrollPane scroll = new JScrollPane(jl);   // PlayList에 스크롤바를 추가(목록이 많아지면 스크롤바로 보이지 않는 목록들까지 볼 수 있음)
		scroll.setSize(395, 330);
		scroll.setLocation(50, 280);
		scroll.setBorder(border);
		scroll.setOpaque(false);
		jp.add(scroll);
		jt.setLocation(52, 220);
		jt.setSize(305, 30);
		jt.registerKeyboardAction(this, "search", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED); // 엔터 눌렀을 때 "search" 이벤트 발생
		jp.add(jt);
		js1.setLocation(50, 145);
		js1.setSize(395, 30);
		js1.setBackground(Color.white);
		js1.setOpaque(false);
		js1.addMouseListener(new Mp3Stick());
		js2.setLocation(400, 34);
		js2.setSize(30, 90);
		js2.setBackground(Color.white);
		js2.setOpaque(false);
		js2.setToolTipText("볼륨 크기를 조절합니다.");
		js2.addMouseListener(new VolumeStick());
        // 볼륨 조절 슬라이더의 노브에 변화가 일어났을 때 발생되는 이벤트 처리
        js2.addChangeListener(e -> {
            if (player != null) {
                if ((double) js2.getValue() / 100 >= 0.5) {
                    player.setVolume((((double) js2.getValue() / 100) * 7.0 / 5.0) - 0.4);
                } else
                    player.setVolume(((double) js2.getValue() / 100 * 2 * (double) js2.getValue() / 100 * 2) * 0.3);
            }
            vol.setText("Vol : " + js2.getValue());
        });
		jp.add(presentimes);
		song.setText("");
		song.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 20));
		song.setForeground(Color.ORANGE);
		song.setSize(500,20);
		song.setLocation(0, 123);
		song.setHorizontalAlignment(JLabel.CENTER);
		jp.add(song);
		vol.setVisible(false);
		vol.setFont(new Font("경기천년제목 Medium", Font.PLAIN, 23));
		vol.setForeground(Color.RED);
		vol.setSize(100, 25);
		vol.setLocation(375, 12);
		vol.setText("Vol : " + js2.getValue());
		jp.add(vol);
		presentimes.setSize(55, 20);
		presentimes.setLocation(45, 180);
		presentimes.setFont(new Font("HY엽서M", Font.BOLD, 15));
		presentimes.setForeground(Color.BLUE);
		jp.add(lastimes);
		lastimes.setSize(55, 20);
		lastimes.setLocation(386, 180);
		lastimes.setFont(new Font("HY엽서M", Font.BOLD, 15));
		lastimes.setForeground(Color.BLUE);
		chooser.setPreferredSize(new Dimension(700, 500));
		setFileChooserFont(chooser.getComponents());
        // JFileChooser에서 .mp3, .dat 파일만 필터링
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".mp3, .dat", "mp3", "dat");
        chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);
		chooser.setDragEnabled(true);
		savelists.setLocation(52, 620);
		savelists.setSize(117, 30);
		savelists.addActionListener(this);
		savelists.setToolTipText("PlayList를 파일로 저장합니다.");
		jp.add(savelists);
		openlists.setLocation(174, 620);
		openlists.setSize(117, 30);
		openlists.addActionListener(this);
		openlists.setToolTipText("저장된 PlayList를 불러옵니다.");
		jp.add(openlists);
		jp.setLayout(null); // setLayout을 null로 해야지 내가 설정한 대로 됨
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫으면 프로세스도 같이 종료
	}
	// JFileChooser 다이얼로그 창 설정
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
			String title = jt.getText();
			if(title.equals("")) { // 아무것도 입력하지 않고 검색하면
				JOptionPane.showMessageDialog(null, "검색어를 입력해 주세요!", "찾기 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			ArrayList<Integer> results = new ArrayList<>();
			for(int i = 0; i < listmodel.getSize(); i++)
				if(listmodel.getElementAt(i).contains(title)) // 리스트의 i번째 index가 검색어를 포함하고 있을 경우
					results.add(i);
			int[] result = new int[results.size()];
			for(int i = 0; i < result.length; i++)
				result[i] = results.get(i);
			if(result.length == 0) { // result 배열에 아무것도 저장이 안 되었을 때
				JOptionPane.showMessageDialog(null, "찾는 노래가 없습니다!", "찾기 오류", JOptionPane.WARNING_MESSAGE);
				return;
			}
			jl.setSelectedIndices(result);
		} else { // 그게 아니라면 
			JButton pressedbutton = (JButton) e.getSource(); // 이 actionPerformed 메소드가 실행되었을 때 클릭된 버튼을 불러옴
			if (pressedbutton.equals(addb)) { // 추가 버튼 클릭했을 때
				int option = chooser.showOpenDialog(new JFrame());
				if(option == JFileChooser.APPROVE_OPTION) {
					File[] selectedfiles = chooser.getSelectedFiles(); // 파일 여러 개 선택하고 확인 누르면 그 파일들이 selectedfiles 배열에 저장됨
                    for (File selectedfile : selectedfiles) {
                        try {
                            MP3File mp3 = (MP3File) AudioFileIO.read(selectedfile);
                            Tag tag = mp3.getTag();
                            listmodel.addElement(tag.getFirst(FieldKey.TITLE)); // 해당 mp3 파일의 제목을 추출해서 list에 추가
                            filelists.add(selectedfile); // filelists에 파일 추가
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "mp3 확장자만 지원합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
				}
			} else if(pressedbutton.equals(deleteb)) { // 삭제 버튼 눌렀을 때
				int[] selected = jl.getSelectedIndices();
				for(int i = 0; i < selected.length; i++) {
					selected[i] -= i; // 하나 삭제할 때마다 index가 1씩 당겨지므로
					File deletingfile = filelists.get(selected[i]);
					if(m != null) // 노래를 재생 중이었을 때
						if(m.getSource().equals(deletingfile.toURI().toString())) { // 삭제할려는 노래가 재생 중 일때
							t.stop();
							player.stop();
							player = null;
							m = null;
							deleteb.setEnabled(false);
							jb[0].setIcon(buttonimgs[0]); // 재생 버튼으로 바꿈
							song.setText("");
							js1.setVisible(false);
							presentimes.setText("");
							lastimes.setText("");
						} else { // 그렇지 않을 때 원래 재생 중이던 노래 그대로 선택된 대로 표시
							if(m.getSource().equals(filelists.get(i).toURI().toString()))
								jl.setSelectedIndex(i);
						}
					else
						deleteb.setEnabled(false);
					listmodel.remove(selected[i]);
					filelists.remove(deletingfile);
				}
			} else if(pressedbutton.equals(savelists)) { // 목록 내보내기 버튼 클릭했을 때
				ObjectOutputStream oos;
				int choice = chooser.showSaveDialog(null);
				if(choice == JFileChooser.APPROVE_OPTION) {
					try {
						File path = chooser.getCurrentDirectory().getAbsoluteFile(); // JFileChooser에서 사용자가 정한 절대 경로를 path에 저장
						oos = new ObjectOutputStream(new FileOutputStream(path + "\\PlayList.dat")); // 그 경로에 PlayList.dat파일 생성
						oos.writeObject(filelists); // 그 PlayList.dat파일에 ArrayList<File> 타입의 객체 filelists 저장
						oos.flush();
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			} else if(pressedbutton.equals(openlists)) { // 목록 불러오기 버튼 클릭했을 때
				ObjectInputStream ois;
				int choice = chooser.showOpenDialog(null);
				if(choice == JFileChooser.APPROVE_OPTION) {
					try {
						if(m != null)
							player.stop();
						song.setText("");
						js1.setVisible(false);
						presentimes.setText("");
						lastimes.setText("");
						t.stop();
						listmodel.clear(); // 현재 PlayList 초기화
						player = null;
						m = null;
						jb[0].setIcon(buttonimgs[0]); // 재생 버튼으로 바꿈
						deleteb.setEnabled(false);
						ois = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile())); 
						filelists = (ArrayList<File>)ois.readObject(); // 사용자가 선택한 파일에 저장된 객체 정보를 읽어서 filelists에 저장
                        for (File file : filelists) {
                            MP3File mp3 = (MP3File) AudioFileIO.read(file);
                            Tag tag = mp3.getTag();
                            listmodel.addElement(tag.getFirst(FieldKey.TITLE));
                        }
					} catch (Exception e3) {
						JOptionPane.showMessageDialog(null, "유효한 파일이 아닙니다!", "파일 불러오기 오류", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (pressedbutton.getIcon().equals(buttonimgs[0])) { // 재생 버튼 클릭했을 때
				deleteb.setEnabled(true);
				if(jl.getSelectedIndices().length > 0) { // 재생 목록에 항목을 하나 이상 선택했을 경우
					int num = jl.getSelectedIndices()[0]; 
					jl.setSelectedIndex(num); // 그 항목만 선택된 걸로 표시
					if(m != null) { // 한 번 이상 재생 버튼을 누른 적이 있다면
						if(!m.getSource().equals(filelists.get(num).toURI().toString())) {
							m = new Media(filelists.get(num).toURI().toString());
							player = new MediaPlayer(m);
						} // 플레이 중이었던 노래와 선택한 노래가 다를 때 선택한 노래 재생
					} else { // 재생 버튼 누른 적 없다면 (MP3 플레이어를 실행하고 원하는 노래 선택한 다음 재생 버튼 누른 경우) 선택한 노래 재생
						m = new Media(filelists.get(num).toURI().toString());
						player = new MediaPlayer(m);
					} 
				} else { // 재생 목록에 항목을 선택하지 않았을 겅우 (MP3 플레이어 실행하고 바로 재생 버튼 누르거나 재생 중이었던 노래 삭제 후 재생 버튼 누름)
					if(listmodel.getSize() > 0) { // 재생 목록에 노래가 하나 이상 있다면
						jl.setSelectedIndex(0); 
						m = new Media(filelists.get(0).toURI().toString());
						player = new MediaPlayer(m);
					} else { // 재생 목록에 노래가 없다면
						deleteb.setEnabled(false);
						JOptionPane.showMessageDialog(null, "노래를 추가하세요!", "재생 오류", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				t.start();
				js1.setVisible(true);
				player.play();
				song.setText(jl.getSelectedValue());
				pressedbutton.setToolTipText("노래를 잠시 멈춥니다.");
				pressedbutton.setIcon(buttonimgs[3]); // 버튼의 이미지를 "일시정지.png"로 바꿈
			} else if (pressedbutton.getIcon().equals(buttonimgs[3])) { // 일시 정지 버튼 클릭했을 때
				player.pause(); // 노래 일시 정지
				pressedbutton.setToolTipText("PlayList에서 선택한 노래를 재생합니다.");
				pressedbutton.setIcon(buttonimgs[0]); // 버튼 이미지를 "재생.png"로 바꿈
			} else if (pressedbutton.getIcon().equals(buttonimgs[1]) || 
					pressedbutton.getIcon().equals(buttonimgs[2])) { // 이전곡 또는 다음곡 버튼 클릭했을 때
				if (m != null) {
					int n = jl.getSelectedIndex();
					if(pressedbutton.getIcon().equals(buttonimgs[1])) { // 이전곡 클릭 시
						if (n > 0) {
							player.stop();
							jl.setSelectedIndex(n - 1);
							m = new Media(filelists.get(n - 1).toURI().toString());
							player = new MediaPlayer(m);
							player.play();
							t.start();
						}
					} else { // 다음곡 클릭 시
						if (n < jl.getLastVisibleIndex()) {
							player.stop();
							jl.setSelectedIndex(n + 1);
							m = new Media(filelists.get(n + 1).toURI().toString());
							player = new MediaPlayer(m);
							player.play();
							t.start();
						}
					}
					song.setText(jl.getSelectedValue());
				}
			} else { // 전체 한 번 재생 버튼, 전체 반복 재생 버튼 또는 셔플 재생 버튼 클릭 시
				pressedbutton.setEnabled(false); // 클릭된 버튼 비활성화
				if(pressedbutton.getIcon().equals(optionimgs[0])) { // 전체 한 번 재생 버튼 클릭했을 경우
					options[1].setEnabled(true);
					options[2].setEnabled(true);
				} else if(pressedbutton.getIcon().equals(optionimgs[1])) { // 전체 반복 재생 버튼 클릭했을 경우
					options[0].setEnabled(true);
					options[2].setEnabled(true);
				} else { // 셔플 재생 클릭했을 경우
					options[0].setEnabled(true);
					options[1].setEnabled(true);
				}
			}
		}
	}
}
