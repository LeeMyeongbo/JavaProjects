package com.sec.bestreviewer.util;

import java.util.function.Supplier;

public class CachedSupplier<T> implements Supplier<T> {

    private final Supplier<T> wrapped;
    private volatile T cache;

    public CachedSupplier(Supplier<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public T get() {
        if (cache == null) {
            cache = wrapped.get();
        }
        return cache;

    }
}
