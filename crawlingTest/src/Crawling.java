import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

import java.util.Objects;

public class Crawling {
    public static void main(String[] args) {
        try {
            String link = "website address here!!";
            Document doc = Jsoup.connect(link).get();
            Element e = doc.selectFirst("#dic_area");
            if (e == null)
                e = doc.selectFirst("#articeBody");
            if (e == null)
                e = doc.selectFirst("#newsEndContents");

            int num = Objects.requireNonNull(e).childrenSize();

            // 자식 요소 중 태그가 <font>, <span>, <b>가 아니거나 class="end_photo_org"인 경우 모두 제거
            for (int i = num - 1; i >= 0; i--) {
                String tag = e.child(i).tagName();
                if ((!tag.equals("font") && !tag.equals("span") && !tag.equals("b"))
                    || e.child(i).className().equals("end_photo_org"))
                    e.child(i).remove();
            }

            // 기사 본문에서 [], (), <>, &;로 싸여져 있는 부분 제거
            String content = e.text().replaceAll("([\\[(<&](.*?)[;>)\\]])", "");

            // #, ※, ▶, ⓒ 등의 특수문자가 오면 그 뒤의 내용은 필요없어서 잘라냄
            int idx = content.indexOf('#');
            if (idx == -1)
                idx = content.indexOf('※');
            if (idx == -1)
                idx = content.indexOf('▶');
            if (idx == -1)
                idx = content.indexOf('ⓒ');
            if (idx != -1)
                content = content.substring(0, idx);

            int send = content.length() / 100;
            for (int i = 0; i <= send; i++)
                System.out.println(content.substring(i * 100, Integer.min(i * 100 + 100, content.length())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
