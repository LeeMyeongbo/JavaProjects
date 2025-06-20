import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CrawlingTest {

    private MockedStatic<URLEncoder> mockStaticEncoder;
    private MockedStatic<URI> mockStaticURI;
    private MockedStatic<Jsoup> mockStaticJsoup;

    private final URI uri = mock(URI.class);
    private final URL url = mock(URL.class);
    private final HttpURLConnection httpURLConnection = mock(HttpURLConnection.class);
    private final InputStream inputStream = mock(InputStream.class);
    private final InputStream errorStream = mock(InputStream.class);
    private final Connection connection = mock(Connection.class);
    private final Document document = mock(Document.class);
    private final Element element = mock(Element.class);
    private final JSONArray jsonArray = new JSONArray(
        """
        [
            {
                "link" : "https://news.daum.com/14",
                "title" : "[계엄] Hyundai",
                "passage" : "wow..."
            },
            {
                "link" : "https://news.naver.com/34",
                "title" : "'얼라이드' junit",
                "passage" : "wow1..."
            },
            {
                "link" : "https://news.naver.com/89",
                "title" : "  test.. test..",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.naver.com/89",
                "title" : "  test.. test..",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.naver.com/89",
                "title" : "  test.. test..",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.navers.com/809",
                "title" : "  test.. test..",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.naver.com/90",
                "title" : "  test.. test???  ",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.naver.com/&454",
                "title" : " &484;  test.. test..  <미래>",
                "passage" : "wow2..."
            }
        ]
        """
    );

    @BeforeEach
    void setUp() throws IOException {
        mockStaticEncoder = mockStatic(URLEncoder.class);
        mockStaticURI = mockStatic(URI.class);
        mockStaticJsoup = mockStatic(Jsoup.class);

        when(URLEncoder.encode(anyString(), any(Charset.class))).thenReturn("test");
        when(URI.create(anyString())).thenReturn(uri);
        when(uri.toURL()).thenReturn(url);
        when(url.openConnection()).thenReturn(httpURLConnection);
        when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpURLConnection.getInputStream()).thenReturn(inputStream);
        when(httpURLConnection.getErrorStream()).thenReturn(errorStream);
        when(Jsoup.connect(anyString())).thenReturn(connection);
        when(connection.get()).thenReturn(document);
    }

    @AfterEach
    void tearDown() {
        mockStaticEncoder.close();
        mockStaticURI.close();
        mockStaticJsoup.close();
    }

    private MockedConstruction<FileReader> getFReaderMockConstruction() {
        return mockConstruction(FileReader.class, (reader, context) -> {
            if ("Id.txt".equals(context.arguments().get(0))) {
                when(reader.read()).thenReturn((int) 'u').thenReturn((int) 'n').thenReturn((int) 'i')
                    .thenReturn((int) 't').thenReturn(-1);
            } else if ("Secret.txt".equals(context.arguments().get(0))) {
                when(reader.read()).thenReturn((int) 't').thenReturn((int) 'e').thenReturn((int) 's')
                    .thenReturn((int) 't').thenReturn(-1);
            } else {
                throw new FileNotFoundException();
            }
        });
    }

    private MockedConstruction<BufferedReader> getBReaderMockConstruction() {
        return mockConstruction(BufferedReader.class, (bufferedReader, context) -> {
            assertInstanceOf(InputStreamReader.class, context.arguments().get(0));
            when(bufferedReader.readLine()).thenReturn("hello! ").thenReturn("junit").thenReturn(null);
        });
    }

    private MockedConstruction<JSONObject> getJsonMockConstruction() {
        return mockConstruction(JSONObject.class, (jsonObject, context) -> {
            assertEquals("hello! junit", context.arguments().get(0));
            when(jsonObject.get("items")).thenReturn(jsonArray);
        });
    }

    @DisplayName("id 및 비번이 없을 때 IOException 체크")
    @Test
    void testIOExceptionWhenGetKey() {
        IOException e = new IOException();
        try (
            MockedConstruction<FileReader> mockConstructionFReader = mockConstruction(FileReader.class,
                (reader, context) -> when(reader.read()).thenReturn(0).thenThrow(e))
        ) {
            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("키 파일이 없습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            mockStaticEncoder.verify(() -> URLEncoder.encode("test", StandardCharsets.UTF_8), times(1));
        }
    }

    @DisplayName("url 형식이 잘못됐을 때 MalformedURLException 체크")
    @Test
    void testMalformedURLExceptionWhenConnect() {
        try (MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction()) {
            MalformedURLException e = new MalformedURLException();
            when(uri.toURL()).thenThrow(e);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("API URL이 잘못되었습니다 - https://openapi.naver.com/v1/search/news?query=test&display=20",
                exception.getMessage());
            assertEquals(e, exception.getCause());
            mockStaticURI.verify(() -> URI.create("https://openapi.naver.com/v1/search/news?query=test&display=20"),
                times(1));
            verify(uri, times(1)).toURL();
            verify(url, times(0)).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("url 연결에 실패하였을 때 IOException 체크")
    @Test
    void testIOExceptionWhenConnect() {
        try (MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction()) {
            IOException e = new IOException();
            when(url.openConnection()).thenThrow(e);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("연결에 실패했습니다 - https://openapi.naver.com/v1/search/news?query=test&display=20",
                exception.getMessage());
            assertEquals(e, exception.getCause());
            mockStaticURI.verify(() -> URI.create("https://openapi.naver.com/v1/search/news?query=test&display=20"),
                times(1));
            verify(uri, times(1)).toURL();
            verify(url, times(1)).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("url GET 요청 실패 시 ProtocolException 체크")
    @Test
    void testProtocolExceptionWhenGetArticlesFromApiUrl() {
        try (MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction()) {
            ProtocolException e = new ProtocolException();
            doThrow(e).when(httpURLConnection).setRequestMethod(anyString());

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("API 요청 및 응답 실패하였습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(0)).setRequestProperty(anyString(), anyString());
            verify(httpURLConnection, times(0)).getResponseCode();
            verify(httpURLConnection, times(0)).getInputStream();
            verify(httpURLConnection, times(0)).getErrorStream();
            verify(httpURLConnection, times(0)).disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("url 응답 정상적으로 받지 못했을 시 IOException 체크")
    @Test
    void testIOExceptionWhenGetResponse() {
        try (MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction()) {
            IOException e = new IOException();
            when(httpURLConnection.getResponseCode()).thenThrow(e);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("API 요청 및 응답 실패하였습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Id", "unit");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Secret", "test");
            verify(httpURLConnection, times(1)).getResponseCode();
            verify(httpURLConnection, times(0)).getInputStream();
            verify(httpURLConnection, times(0)).getErrorStream();
            verify(httpURLConnection, times(0)).disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("url 응답코드가 OK가 아니면서 응답 읽지 못했을 때 IOException 체크")
    @Test
    void testGetUrlResponseWhenResultCodeIsNotOK() {
        IOException e = new IOException();
        try (
            MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction();
            MockedConstruction<InputStreamReader> mockConstructionISReader = mockConstruction(InputStreamReader.class,
                (inputStreamReader, context) -> assertEquals(errorStream, context.arguments().get(0))
            );
            MockedConstruction<BufferedReader> mockConstructionBReader = mockConstruction(BufferedReader.class,
                (bufferedReader, context) -> {
                    assertEquals(mockConstructionISReader.constructed().get(0), context.arguments().get(0));
                    when(bufferedReader.readLine()).thenThrow(e);
                }
            )
        ) {
            when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("API 응답을 읽는 데 실패했습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Id", "unit");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Secret", "test");
            verify(httpURLConnection, times(1)).getResponseCode();
            verify(httpURLConnection, times(0)).getInputStream();
            verify(httpURLConnection, times(1)).getErrorStream();
            verify(httpURLConnection, times(0)).disconnect();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @DisplayName("url 응답코드가 OK 인데 응답을 읽지는 못했을 때 IOException 체크")
    @Test
    void testGetUrlResponseWhenResultCodeIsOK() {
        IOException e = new IOException();
        try (
            MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction();
            MockedConstruction<InputStreamReader> mockConstructionISReader = mockConstruction(InputStreamReader.class,
                (inputStreamReader, context) -> assertEquals(inputStream, context.arguments().get(0))
            );
            MockedConstruction<BufferedReader> mockConstructionBReader = mockConstruction(BufferedReader.class,
                (bufferedReader, context) -> {
                    assertEquals(mockConstructionISReader.constructed().get(0), context.arguments().get(0));
                    when(bufferedReader.readLine()).thenThrow(e);
                }
            )
        ) {
            when(httpURLConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("API 응답을 읽는 데 실패했습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Id", "unit");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Secret", "test");
            verify(httpURLConnection, times(1)).getResponseCode();
            verify(httpURLConnection, times(1)).getInputStream();
            verify(httpURLConnection, times(0)).getErrorStream();
            verify(httpURLConnection, times(0)).disconnect();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @DisplayName("url 응답 읽어온 후 JSON 으로 파싱 실패했을 때 JSONException 체크")
    @Test
    void testJSONExceptionWhenFailedToConvertResponseToJSON() {
        try (
            MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction();
            MockedConstruction<BufferedReader> mockConstructionBReader = getBReaderMockConstruction();
        ) {
            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("Json 형식으로 파싱할 수 없습니다...", exception.getMessage());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Id", "unit");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Secret", "test");
            verify(httpURLConnection, times(1)).getResponseCode();
            verify(httpURLConnection, times(1)).getInputStream();
            verify(httpURLConnection, times(0)).getErrorStream();
            verify(httpURLConnection, times(1)).disconnect();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @DisplayName("url 응답을 JSON 으로 파싱 했는데 뉴스 기사 항목이 없을 때 JSONException 체크")
    @Test
    void testJSONExceptionWithNoItemsWhenConvertResponseToJSON() {
        JSONException e = new JSONException("error");
        try (
            MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction();
            MockedConstruction<BufferedReader> mockConstructionBReader = getBReaderMockConstruction();
            MockedConstruction<JSONObject> mockConstructionJson = mockConstruction(JSONObject.class,
                (jsonObject, context) -> {
                    assertEquals("hello! junit", context.arguments().get(0));
                    when(jsonObject.get("items")).thenThrow(e);
                }
            )
        ) {
            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("Json 형식으로 파싱할 수 없습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            verify(httpURLConnection, times(1)).setRequestMethod("GET");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Id", "unit");
            verify(httpURLConnection, times(1)).setRequestProperty("X-Naver-Client-Secret", "test");
            verify(httpURLConnection, times(1)).getResponseCode();
            verify(httpURLConnection, times(1)).getInputStream();
            verify(httpURLConnection, times(0)).getErrorStream();
            verify(httpURLConnection, times(1)).disconnect();
            verify(mockConstructionJson.constructed().get(0), times(1)).get("items");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @DisplayName("응답으로 받은 뉴스 기사들 중 네이버 기사들만 필터링하고 크롤링 중 에러 발생 시 IOException 체크")
    @Test
    void testFilterNaverNews() {
        IOException e = new IOException();
        try (
            MockedConstruction<FileReader> mockConstructionFReader = getFReaderMockConstruction();
            MockedConstruction<BufferedReader> mockConstructionBReader = getBReaderMockConstruction();
            MockedConstruction<JSONObject> mockConstructionJson = getJsonMockConstruction()
        ) {
            when(connection.get()).thenThrow(e);

            Throwable exception = assertThrowsExactly(RuntimeException.class, () -> new Crawling().crawl("test"));

            assertEquals("네이버 뉴스 웹페이지를 읽어올 수 없습니다...", exception.getMessage());
            assertEquals(e, exception.getCause());
            mockStaticJsoup.verify(() -> Jsoup.connect("https://news.naver.com/34"), times(1));
            verify(connection, times(1)).get();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
