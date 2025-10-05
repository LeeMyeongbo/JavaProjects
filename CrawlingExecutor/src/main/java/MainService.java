import java.io.*;
import java.util.*;

public class MainService {

    public static final int MAX_NEWS_COUNT = 5;

    public static void main(String[] args) {
        try (
            Scanner sc = new Scanner(System.in);
            BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("NewsTTS/result.txt"))
        ) {
            System.out.print("키워드 입력 : ");
            String article = new Crawler().crawl(sc.next());
            sc.nextLine();
            bs.write(article.getBytes());
            System.out.print(article);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생하였습니다...", e);
        }

        System.out.println("검색 완료! 뉴스를 재생합니다..");
        new SpeechExecutor().playNewsSpeech();
    }
}
