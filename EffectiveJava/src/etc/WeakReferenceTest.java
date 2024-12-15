package etc;

import java.lang.ref.WeakReference;

public class WeakReferenceTest {

    static class SubClass {
        private final int t = 1;
    }

    public static void main(String[] args) throws InterruptedException {
        testWeakReference(false);
    }

    private static void testWeakReference(boolean test) {
        SubClass c1 = new SubClass();
        if (test) {
            runSubWithWeakReference(new WeakReference<>(c1));
        } else {
            runSub(c1);
        }

        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("set c1 null and garbage collect");
        c1 = null;
        System.gc();
    }

    private static void runSub(SubClass subClass) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (subClass == null) {
                    System.out.println("subClass is null : " + i);
                } else {
                    System.out.println("subClass is not null : " + i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private static void runSubWithWeakReference(WeakReference<SubClass> weakReference) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (weakReference.get() == null) {
                    System.out.println("subClass is null : " + i);
                } else {
                    System.out.println("subClass is not null : " + i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
