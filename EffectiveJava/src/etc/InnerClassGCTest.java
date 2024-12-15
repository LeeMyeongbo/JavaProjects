package etc;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;

public class InnerClassGCTest {

    public static void main(String[] args) {
        innerClassTest();
    }

    public static void innerClassTest() {
        ArrayList<Object> list = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            list.add(new Outer(100000005).getInnerObject());
            System.out.println(getMemory()[0]);
        }
    }

    private static String[] getMemory() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryMXBean.getNonHeapMemoryUsage();
        long heapInit = heap.getInit();
        long heapUsed = heap.getUsed();
        long heapCommit = heap.getCommitted();
        long heapMax = heap.getMax();

        long nonHeapUsed = nonHeap.getUsed();
        String[] list = new String[5];
//		list[0] = String.valueOf(heapInit);
        list[0] = String.valueOf(heapUsed);
        list[1] = String.valueOf(nonHeapUsed);
        list[2] = String.valueOf(heapCommit);
        list[3] = String.valueOf(heapMax);
        return list;
    }
}
