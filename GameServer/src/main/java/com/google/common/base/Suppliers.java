package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@GwtCompatible
public final class Suppliers {
    public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) {
        Preconditions.checkNotNull(function);
        Preconditions.checkNotNull(supplier);
        return new SupplierComposition<F, T>(function, supplier);
    }

    public static <T> Supplier<T> memoize(Supplier<T> delegate) {
        return (delegate instanceof MemoizingSupplier) ? delegate : new MemoizingSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
    }

    public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit) {
        return new ExpiringMemoizingSupplier<T>(delegate, duration, unit);
    }

    public static <T> Supplier<T> ofInstance(@Nullable T instance) {
        return new SupplierOfInstance<T>(instance);
    }

    public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate) {
        return new ThreadSafeSupplier<T>(Preconditions.<Supplier<T>>checkNotNull(delegate));
    }

    @Beta
    public static <T> Function<Supplier<T>, T> supplierFunction() {
        return SupplierFunction.INSTANCE;
    }

    private enum SupplierFunction implements Function<Supplier<?>, Object> {
        INSTANCE;

        public Object apply(Supplier<?> input) {
            return input.get();
        }
    }

    private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0L;
        final Function<? super F, T> function;
        final Supplier<F> supplier;

        SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
            this.function = function;
            this.supplier = supplier;
        }

        public T get() {
            return this.function.apply(this.supplier.get());
        }
    }

    @VisibleForTesting
    static class MemoizingSupplier<T>
            implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0L;
        final Supplier<T> delegate;
        volatile transient boolean initialized;
        transient T value;

        MemoizingSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        public T get() {
            if (!this.initialized) {
                synchronized (this) {
                    if (!this.initialized) {
                        T t = this.delegate.get();
                        this.value = t;
                        this.initialized = true;
                        return t;
                    }
                }
            }
            return this.value;
        }
    }

    @VisibleForTesting
    static class ExpiringMemoizingSupplier<T>
            implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0L;
        final Supplier<T> delegate;
        final long durationNanos;
        volatile transient T value;
        volatile transient long expirationNanos;

        ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
            this.delegate = Preconditions.<Supplier<T>>checkNotNull(delegate);
            this.durationNanos = unit.toNanos(duration);
            Preconditions.checkArgument((duration > 0L));
        }

        public T get() {
            long nanos = this.expirationNanos;
            long now = Platform.systemNanoTime();
            if (nanos == 0L || now - nanos >= 0L) {
                synchronized (this) {
                    if (nanos == this.expirationNanos) {
                        T t = this.delegate.get();
                        this.value = t;
                        nanos = now + this.durationNanos;

                        this.expirationNanos = (nanos == 0L) ? 1L : nanos;
                        return t;
                    }
                }
            }
            return this.value;
        }
    }

    private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0L;
        final T instance;

        SupplierOfInstance(@Nullable T instance) {
            this.instance = instance;
        }

        public T get() {
            return this.instance;
        }
    }

    private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
        private static final long serialVersionUID = 0L;
        final Supplier<T> delegate;

        ThreadSafeSupplier(Supplier<T> delegate) {
            this.delegate = delegate;
        }

        public T get() {
            synchronized (this.delegate) {
                return this.delegate.get();
            }
        }
    }
}

