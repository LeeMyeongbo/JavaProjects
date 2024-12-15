package item52;

class Wine {

    String name() {
        return "와인";
    }
}

class SparklingWine extends Wine {

    @Override
    String name() {
        return "스파클링 와인";
    }
}

class Champagne extends SparklingWine {

    @Override
    String name() {
        return "샴페인";
    }
}
