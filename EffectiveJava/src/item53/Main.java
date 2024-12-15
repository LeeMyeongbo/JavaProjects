package item53;

public class Main {

    public static void main(String[] args) {
        System.out.println(sum());
        System.out.println(sum(1));
        System.out.println(sum(6, 7, 8, 9));

        //System.out.println(min());            // 런타임 시 실패
        System.out.println(min(7, 8, 5, 6));
    }

    static int sum(int... args) {
        int sum = 0;
        for (int arg : args)
            sum += arg;
        return sum;
    }

//    static int min(int... args) {
//        if (args.length == 0)
//            throw new IllegalArgumentException("인수가 1개 이상 필요합니다!");
//        int min = args[0];
//        for (int i = 1; i < args.length; i++)
//            if (args[i] < min)
//                min = args[i];
//        return min;
//    }

    static int min(int firstArg, int... remainingArgs) {
        int min = firstArg;
        for (int arg : remainingArgs)
            if (arg < min)
                min = arg;
        return min;
    }
}
