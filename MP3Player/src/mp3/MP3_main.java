package mp3;

import javafx.embed.swing.*;

public class MP3_main {
	public static void main(String[] args) {
		new JFXPanel(); // Media 클래스가 속한 javafx 는 실행 전에 특수한 초기화 과정을 거쳐야 하는데 이 JFXPanel 이 그 초기화를 해줌
		new MP3_play();
	}
}
