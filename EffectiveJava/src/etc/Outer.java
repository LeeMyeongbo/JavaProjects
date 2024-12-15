package etc;

public class Outer {

    class Inner {}

    private int[] data;

    public Outer(int size) {
        data = new int[size];
    }

    Inner getInnerObject() {
        return new Inner();
    }
}
