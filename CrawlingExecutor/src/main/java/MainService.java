import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import static javax.sound.sampled.AudioSystem.*;

public class MainService {

    public static final int MAX_NEWS_COUNT = 5;

    public static void main(String[] args) {
        String article;
        try (
            Scanner sc = new Scanner(System.in);
            BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("Output.txt"))
        ) {
            System.out.print("키워드 입력 : ");
            article = new Crawler().crawl(sc.next());
            bs.write(article.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("검색 완료! 뉴스를 재생합니다..");
        new SpeechExecutor().speak(article);
    }
}
