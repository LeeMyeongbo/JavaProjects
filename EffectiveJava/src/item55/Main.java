package item55;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class Main {

    public static <E extends Comparable<E>> E max(Collection<E> c) {
        if (c.isEmpty())
            throw new IllegalArgumentException("빈 컬렉션");

        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);

        return result;
    }

    public static <E extends Comparable<E>> Optional<E> improvedMax(Collection<E> c) {
        if (c.isEmpty())
            return Optional.empty();

        E result = null;
        for (E e : c)
            if (result == null || e.compareTo(result) > 0)
                result = Objects.requireNonNull(e);

        return Optional.of(result);
    }

    public static <E extends Comparable<E>> Optional<E> streamedMax(Collection<E> c) {
        return c.stream().max(Comparator.naturalOrder());
    }

    public static OptionalInt upperbound(int pivot, Collection<Integer> c) {
        int result = Integer.MAX_VALUE;
        for (int i : c) {
            if (pivot < i && i <= result)
                result = i;
        }

        if (result > c.stream().max(Comparator.naturalOrder()).orElse(Integer.MIN_VALUE))
            return OptionalInt.empty();

        return OptionalInt.of(result);
    }

    public static void main(String[] args) {
        //System.out.println(max(new ArrayList<String>()));     // Exception 발생
        ArrayList<Integer> list = new ArrayList<>();
        System.out.println(improvedMax(list));
        System.out.println(streamedMax(list));

        list.add(1);
        list.add(4);
        list.add(3);
        System.out.println(improvedMax(list));
        System.out.println(streamedMax(list));
        // Optional.of(null);   // nullPointerException 발생

        ArrayList<String> words = new ArrayList<>();
        System.out.println(streamedMax(words).orElse("단어 없음..."));
        //String maxWord = streamedMax(words).orElseThrow(NoSuchElementException::new);

        words.add("wow");
        words.add("samsung");
        words.add("LG");
        System.out.println(streamedMax(words).orElse("단어 없음..."));
        System.out.println(streamedMax(words).get());   // isPresent() 체크해야 된다고 경고가 표시

        ProcessHandle ph = new ProcessHandle() {
            @Override
            public long pid() {
                return 0;
            }

            @Override
            public Optional<ProcessHandle> parent() {
                return Optional.empty();
            }

            @Override
            public Stream<ProcessHandle> children() {
                return null;
            }

            @Override
            public Stream<ProcessHandle> descendants() {
                return null;
            }

            @Override
            public Info info() {
                return null;
            }

            @Override
            public CompletableFuture<ProcessHandle> onExit() {
                return null;
            }

            @Override
            public boolean supportsNormalTermination() {
                return false;
            }

            @Override
            public boolean destroy() {
                return false;
            }

            @Override
            public boolean destroyForcibly() {
                return false;
            }

            @Override
            public boolean isAlive() {
                return false;
            }

            @Override
            public int compareTo(ProcessHandle other) {
                return 0;
            }
        };

        Optional<ProcessHandle> parentProcess = ph.parent();
        System.out.println("부모 PID : " + (parentProcess.isPresent() ? String.valueOf(parentProcess.get().pid()) : "N/A"));
        System.out.println("부모 PID : " + (parentProcess.map(h -> String.valueOf(h.pid())).orElse("N/A")));

        System.out.println(upperbound(-9, list).orElseThrow());
        list.add(8);
        list.add(5);
        list.add(9);
        System.out.println(upperbound(5, list).orElseThrow());
        list.clear();
        list.add(Integer.MAX_VALUE);
        System.out.println(upperbound(Integer.MAX_VALUE - 1, list).orElseThrow());
        list.clear();
        System.out.println(upperbound(Integer.MAX_VALUE, list).orElseThrow());
    }
}
