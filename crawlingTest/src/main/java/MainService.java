import java.io.*;
import java.util.Scanner;

public class MainService {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("키워드 입력 : ");
        String keyword = sc.next();

        try (BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
            bs.write(new Crawling().crawl(keyword).getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("완료!");
    }
}
