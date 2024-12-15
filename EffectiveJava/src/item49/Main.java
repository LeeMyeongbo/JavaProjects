package item49;

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        BigInteger integer = BigInteger.valueOf(199992929);
        BigInteger val = integer.mod(BigInteger.valueOf(900));

        System.out.println(val);

        BigInteger i = Objects.requireNonNull(BigInteger.valueOf(100000));
        Collections.sort(new ArrayList<Integer>());
    }

    private static void sort(long[] a, int offset, int length) {
        assert a != null;
        assert offset >= 0 && offset < a.length;
        assert length >= 0 && length < a.length - offset;

        // start sort...
    }
}
