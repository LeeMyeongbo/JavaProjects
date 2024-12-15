package etc;

public class RunnableThreadTest {

    public static void main(String[] args) {
        runnableAndThreadTest();
    }

    private static void runnableAndThreadTest() {
        Runnable r1 = () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable r2 = () -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        // 동기적으로 실행
        long before = System.currentTimeMillis();
        r1.run();
        r2.run();
        long after = System.currentTimeMillis();
        System.out.println("elapsed time : " + (after - before));

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        // 비동기적으로 동시에 실행
        before = System.currentTimeMillis();
        try {
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        after = System.currentTimeMillis();
        System.out.println("elapsed time : " + (after - before));
    }
}
