package item54;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum Cheese {ORIGINAL, STILTON}

class CheeseStock {

    private final List<Cheese> cheesesInStock;
    private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];

    public CheeseStock() {
        cheesesInStock = new ArrayList<>();
    }

    /**
     * @return 모든 치즈 목록을 반환함. 단, 비어있다면 null 반환
     */
    public List<Cheese> getCheese() {
        return cheesesInStock.isEmpty() ? null : new ArrayList<>(cheesesInStock);
    }

    public List<Cheese> getCheesesInStock() {
        return new ArrayList<>(cheesesInStock);
    }

    public List<Cheese> enhancedGetCheesesInStock() {
        return cheesesInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(cheesesInStock);
    }

    public Cheese[] getCheeses() {
        return cheesesInStock.toArray(new Cheese[0]);
    }

    public Cheese[] enhancedGetCheeses() {
        return cheesesInStock.toArray(EMPTY_CHEESE_ARRAY);
    }
}
