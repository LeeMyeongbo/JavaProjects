package com.sec.bestreviewer.util;

import java.util.Objects;

public class Pair<F, S> {

    public final F first;
    public final S second;

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair<?, ?> p)) {
            return false;
        }
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    @Override
    public String toString() {
        return "Pair{" + first + " " + second + "}";
    }

    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }
}
