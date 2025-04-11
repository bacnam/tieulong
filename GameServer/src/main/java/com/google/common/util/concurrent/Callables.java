package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public final class Callables {
    public static <T> Callable<T> returning(@Nullable final T value) {
        return new Callable<T>() {
            public T call() {
                return (T) value;
            }
        };
    }
}

