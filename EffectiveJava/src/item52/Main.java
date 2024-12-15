package item52;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Collection<?>[] collections = {
            new HashSet<String>(), new ArrayList<Integer>(), new HashMap<String, String>().values()
        };
        for (Collection<?> c : collections)
            System.out.println(CollectionClassifier.classify(c));   // 컴파일 타임 때 매개변수의 타입에 의해 결정

        List<Wine> wineList = List.of(new Wine(), new SparklingWine(), new Champagne());
        for (Wine wine : wineList)
            System.out.println(wine.name());        // 항상 가장 하위에서 재정의한 메소드가 실행

        for (Collection<?> c : collections)
            System.out.println(CollectionClassifier.enhancedClassify(c));

        Set<Integer> set = new TreeSet<>();
        List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        for (int i = 0; i < 3; i++) {
            set.remove(i);
            list.remove(i);             // remove(Object)와 remove(int)를 다중정의해서 이러한 오해가 발생
        }
        System.out.println(set + " " + list);

        Set<Integer> set1 = new TreeSet<>();
        List<Integer> list1 = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            set1.add(i);
            list1.add(i);
        }

        for (int i = 0; i < 3; i++) {
            set1.remove(i);
            list1.remove((Integer)i);
        }
        System.out.println(set1 + " " + list1);

        // 1) Thread 생성자 호출
        new Thread(System.out::println).start();

        // 2) ExecutorService 내 submit 메소드 호출
        ExecutorService exec = Executors.newCachedThreadPool();
        //exec.submit(System.out::println);       // 서로 다른 함수형 인터페이스라도 인수 위치가 같으면 혼란이 생김
        exec.submit(() -> System.out.println());
    }
}
