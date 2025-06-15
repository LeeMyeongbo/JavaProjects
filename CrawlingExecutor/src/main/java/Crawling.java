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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawling {

    public String crawl(String keyword) {
        return processJson(convert2JSON(getResponse(keyword)));
    }

    private String getResponse(String keyword) {
        String text = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text + "&display=20";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", getKey("Id.txt"));
        requestHeaders.put("X-Naver-Client-Secret", getKey("Secret.txt"));

        return get(apiURL, requestHeaders);
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
            throw new RuntimeException("API 요청 및 응답 실패하였습니다...", e);
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = URI.create(apiUrl).toURL();
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다 - " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결에 실패했습니다 - " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다...", e);
        }
    }

    private List<JSONObject> convert2JSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);

            return filterNaverNews((JSONArray) jsonObject.get("items"));
        } catch (JSONException e) {
            throw new RuntimeException("Json 형식으로 파싱할 수 없습니다...", e);
        }
    }

    private List<JSONObject> filterNaverNews(JSONArray arrayArticles) {
        Set<String> appendedLink = new HashSet<>();
        List<JSONObject> jsonArticleList = new ArrayList<>();

        for (Object jsonArticle : arrayArticles) {
            JSONObject articleObject = (JSONObject) jsonArticle;
            String link = (String) articleObject.get("link");
            if (link.contains("news.naver.com") && !appendedLink.contains(link)) {
                appendedLink.add(link);
                jsonArticleList.add(articleObject);
            }
            if (jsonArticleList.size() == 5) {
                break;
            }
        }
        return jsonArticleList;
    }

    private String processJson(List<JSONObject> jsonArticles) {
        StringBuilder ret = new StringBuilder();

        for (JSONObject j : jsonArticles) {
            String title = ((String) j.get("title")).replaceAll("([\\[(<&](.*?)[;>)\\]])", "");
            ret.append("<").append(eraseInitialSpace(title)).append(">\n");

            String link = (String) j.get("link");
            String content = modifyText(crawlPassage(link));
            ret.append(content).append("\n\n");
        }

        return ret.toString();
    }

    private Element crawlPassage(String link) {
        try {
            Element e = getElement(Jsoup.connect(link).get());
            eraseUselessTagsInElement(e);

            return e;
        } catch (IOException e) {
            throw new RuntimeException("네이버 뉴스 웹페이지를 읽어올 수 없습니다...", e);
        }
    }

    private Element getElement(Document doc) {
        Element e = doc.selectFirst("#dic_area");
        if (e == null) {
            e = doc.selectFirst("#articeBody");
        }
        if (e == null) {
            e = doc.selectFirst("#newsEndContents");
        }
        return e;
    }

    private void eraseUselessTagsInElement(Element e) {
        int num = Objects.requireNonNull(e).childrenSize();
        for (int i = num - 1; i >= 0; i--) {
            String tag = e.child(i).tagName();
            if ((!tag.equals("font") && !tag.equals("span") && !tag.equals("b"))
                    || e.child(i).className().equals("end_photo_org")) {
                e.child(i).remove();
            }
        }
    }

    private String modifyText(Element e) {
        String content = appendSpaceAfterFullStop(e.text().replaceAll("([\\[(<&](.*?)[;>)\\]])", ""));

        char[] cutoutChar = {'#', '※', '▶', 'ⓒ'};
        for (char c : cutoutChar) {
            int idx = content.indexOf(c);
            if (idx != -1) {
                content = content.substring(0, idx);
            }
        }

        return eraseInitialSpace(content);
    }

    private String appendSpaceAfterFullStop(String content) {
        StringBuilder builder = new StringBuilder(content);
        Pattern pattern = Pattern.compile("[가-힣]\\.[^ ]");
        Matcher matcher = pattern.matcher(content);

        int weight = 0;
        while (matcher.find()) {
            builder.insert(matcher.end() - 1 + weight++, " ");
        }
        return builder.toString();
    }

    private String eraseInitialSpace(String text) {
        int start = 0;
        while (text.length() > start && text.charAt(start) == ' ') {
            start++;
        }

        return text.substring(start).replace("  ", " ");
    }
}
