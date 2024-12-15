package item54;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        CheeseStock cheeseStock = new CheeseStock();
        List<Cheese> cheeses = cheeseStock.getCheese();

        // null 상황을 처리하는 코드 추가로 작성해야 함
        if (cheeses != null && cheeses.contains(Cheese.STILTON)) {
            System.out.println("That's it!!");
        }
    }
}
