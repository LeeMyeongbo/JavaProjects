import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawling {
    public static void main(String[] args) throws IOException {
        byte[] k = "코로나".getBytes("euc-kr");
        String keyword = new String(k, StandardCharsets.UTF_8);
        String url = "https://openapi.naver.com/v1/search/news?query=" + keyword + "&display=20";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", "77ZHNWgvaXOrqCOeo3s5");
        requestHeaders.put("X-Naver-Client-Secret", "x_SI3JImaI");
        String responseBody = get(url,requestHeaders);
        System.out.println(responseBody);

        byte[] tmp = responseBody.getBytes(StandardCharsets.UTF_8);
        String response = new String(tmp, "euc-kr");
        System.out.println(response);
        /*
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
        } */
    }

    public static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet())
                con.setRequestProperty(header.getKey(), header.getValue());

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    public static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null)
                responseBody.append(line);

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}
