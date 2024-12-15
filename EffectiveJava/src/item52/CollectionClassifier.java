package item52;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CollectionClassifier {

    public static String classify(Set<?> s) {
        return "집합";
    }

    public static String classify(List<?> list) {
        return "리스트";
    }

    public static String classify(Collection<?> c) {
        return "그 외";
    }

    public static String enhancedClassify(Collection<?> c) {
        return c instanceof Set ? "집합" : c instanceof List ? "리스트" : "그 외";
    }
}
