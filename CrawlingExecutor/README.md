# CrawlerTest
  **crawling test using naver news search api and speak with MeloTTS**

## Naver news search api guide

### 1. Client id and secrets should be issued after registering application in Naver Developers Center
please refer : https://developers.naver.com/docs/serviceapi/search/news/news.md

### 2. You can request News search result with keyword.
#### java sample
```java
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiExamSearchBlog {

    public static void main(String[] args) {
        String clientId = "YOUR_CLIENT_ID"; // client id
        String clientSecret = "YOUR_CLIENT_SECRET"; // client secret

        String keyword = URLEncoder.encode("날씨", StandardCharsets.UTF_8);

        String apiURL = "https://openapi.naver.com/v1/search/news?query=" + keyword; // json result
        //String apiURL = "https://openapi.naver.com/v1/search/news.xml?query="+ text; // xml result

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);

        System.out.println(responseBody);
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("failed to get api result", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("wrong url : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("connection failed : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);
        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("failed to load api response", e);
        }
    }
}
```
※ reference : https://developers.naver.com/docs/serviceapi/search/blog/blog.md#%EA%B2%80%EC%83%89-api-%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B2%80%EC%83%89-%EA%B5%AC%ED%98%84-%EC%98%88%EC%A0%9C

## NewsTTS guide

### 1. create python project & build virtual env
- you should install python (3.8+) first.
```bash
mkdir NewsTTS
cd NewsTTS
python -m venv .venv
```

### 2. activate virtual env & install gTTS
```bash
pip install gTTS
```

#### ※ Internet should be connected to run
