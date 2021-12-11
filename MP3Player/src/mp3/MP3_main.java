package mp3;

import javafx.embed.swing.*;

public class MP3_main {
	public static void main(String[] args) {
		new JFXPanel(); // Media클래스가 속한 javafx는 실행 전에 특수한 초기화 과정을 거쳐야 하는데 이 JFXPanel이 그 초기화를 해줌
		new MP3_play(); // MP3_play클래스 실행 (참조 변수 없이 바로 heap 영역에 클래스 생성 가능)
		
		System.out.println("성공했어요.");
	}
}
