import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Objects;

public class Crawling {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://n.news.naver.com/mnews/article/007/0000007181").get();
        Element e = doc.selectFirst("#dic_area");
        if (e == null)
            e = doc.selectFirst("#articeBody");
        if (e == null)
            e = doc.selectFirst("#newsEndContents");

        int num = Objects.requireNonNull(e).childrenSize();

        for (int i = num - 1; i >= 0; i--) {
            String tag = e.child(i).tagName();
            if ((!tag.equals("font") && !tag.equals("span") && !tag.equals("b")) || e.child(i).className().equals("end_photo_org"))
                e.child(i).remove();
        }

        String content = e.text().replaceAll("([\\[(<&](.*?)[;>)\\]])", "");

        int idx = content.indexOf('#');
        if (idx == -1)
            idx = content.indexOf('※');
        if (idx == -1)
            idx = content.indexOf('▶');
        if (idx == -1)
            idx = content.indexOf('ⓒ');
        if (idx != -1)
            content = content.substring(0, idx);

        if (content.length() > 3000) {
            int send = content.length() / 3000;
            for (int i = 0; i <= send; i++) {
                sendMessage(content.substring(3000 * i, Integer.min(3000 * i + 3000, content.length())));
            }
        }
    }

    public static void sendMessage(String str) {
        System.out.println(str);
    }
}
