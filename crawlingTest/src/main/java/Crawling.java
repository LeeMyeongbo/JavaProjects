import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Crawling {

    public String crawl(String keyword) {
        return processJson(convert2JSON(getResponse(keyword)));
    }

    private String getResponse(String keyword) {
        String text = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=20";    // JSON 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", getKey("Id.txt"));
        requestHeaders.put("X-Naver-Client-Secret", getKey("Secret.txt"));

        return get(apiURL,requestHeaders);
    }

    private String getKey(String textKeyFile) {
        try (FileReader reader = new FileReader(textKeyFile)) {
            StringBuilder key = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                key.append((char) ch);
            }

            return key.toString();
        } catch (IOException e) {
            throw new RuntimeException("키 파일이 없습니다...", e);
        }
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            String ret = con.getResponseCode() == HttpURLConnection.HTTP_OK
                    ? readBody(con.getInputStream()) : readBody(con.getErrorStream());

            con.disconnect();
            return ret;
        } catch (IOException e) {
            throw new RuntimeException("API 요청 및 응답 실패", e);
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = URI.create(apiUrl).toURL();
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결에 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private Set<JSONObject> convert2JSON(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            throw new RuntimeException("Json 형식으로 파싱할 수 없습니다...", e);
        }

        JSONArray arrayArticles = (JSONArray) jsonObject.get("items");
        Set<JSONObject> jsonObjects = new HashSet<>();

        for (Object arrayArticle : arrayArticles) {
            JSONObject articleObject = (JSONObject) arrayArticle;
            String link = (String) articleObject.get("link");
            if (link.contains("news.naver.com")) jsonObjects.add(articleObject);
            if (jsonObjects.size() == 5) break;
        }

        return jsonObjects;
    }

    private String processJson(Set<JSONObject> jsonArticles) {
        StringBuilder ret = new StringBuilder();

        for (JSONObject j : jsonArticles) {
            String title = (String) j.get("title");
            title = title.replaceAll("([\\[(<&](.*?)[;>)\\]])", "");
            ret.append("<").append(title).append(">\n");

            String link = (String) j.get("link");
            String content = modifyText(crawlPassage(link));
            ret.append(content).append("\n\n");
        }

        return ret.toString();
    }

    private Element crawlPassage(String link) {
        Document doc;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            throw new RuntimeException("웹페이지를 읽어올 수 없습니다..", e);
        }

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
        return e;
    }

    private String modifyText(Element e) {
        // [], (), <>, &;로 싸여져 있는 부분 제거
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

        return content;
    }
}
