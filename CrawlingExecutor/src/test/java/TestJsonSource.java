public class TestJsonSource {

    public static final String source =
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
                "title" : "  test..  test..[&90;] <;>",
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
                "link" : "https://news.naver.com/89",
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
            },
            {
                "link" : "https://news.navers.com/809",
                "title" : "  test.. test..",
                "passage" : "wow2..."
            },
            {
                "link" : "https://news.naver.com/&456",
                "title" : "  [&484];  test..   test..  <AJ>",
                "passage" : "wow2...  "
            },
            {
                "link" : "https://news.naver.com/&456",
                "title" : "  [&484];  test..   test..  <DKfe;>",
                "passage" : "wow2...  "
            }
        ]
        """;
}
