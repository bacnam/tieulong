package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

@GwtCompatible
public final class Functions {
    public static Function<Object, String> toStringFunction() {
        return ToStringFunction.INSTANCE;
    }

    public static <E> Function<E, E> identity() {
        return IdentityFunction.INSTANCE;
    }

    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return new FunctionForMapNoDefault<K, V>(map);
    }

    public static <K, V> Function<K, V> forMap(Map<K, ? extends V> map, @Nullable V defaultValue) {
        return new ForMapWithDefault<K, V>(map, defaultValue);
    }

    public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, ? extends B> f) {
        return new FunctionComposition<A, B, C>(g, f);
    }

    public static <T> Function<T, Boolean> forPredicate(Predicate<T> predicate) {
        return new PredicateFunction<T>(predicate);
    }

    public static <E> Function<Object, E> constant(@Nullable E value) {
        return new ConstantFunction<E>(value);
    }

    @Beta
    public static <T> Function<Object, T> forSupplier(Supplier<T> supplier) {
        return new SupplierFunction<T>(supplier);
    }

    private enum ToStringFunction
            implements Function<Object, String> {
        INSTANCE;

        public String apply(Object o) {
            Preconditions.checkNotNull(o);
            return o.toString();
        }

        public String toString() {
            return "toString";
        }
    }

    private enum IdentityFunction
            implements Function<Object, Object> {
        INSTANCE;

        public Object apply(Object o) {
            return o;
        }

        public String toString() {
            return "identity";
        }
    }

    private static class FunctionForMapNoDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0L;
        final Map<K, V> map;

        FunctionForMapNoDefault(Map<K, V> map) {
            this.map = Preconditions.<Map<K, V>>checkNotNull(map);
        }

        public V apply(K key) {
            V result = this.map.get(key);
            Preconditions.checkArgument((result != null || this.map.containsKey(key)), "Key '%s' not present in map", new Object[]{key});
            return result;
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof FunctionForMapNoDefault) {
                FunctionForMapNoDefault<?, ?> that = (FunctionForMapNoDefault<?, ?>) o;
                return this.map.equals(that.map);
            }
            return false;
        }

        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return "forMap(" + this.map + ")";
        }
    }

    private static class ForMapWithDefault<K, V> implements Function<K, V>, Serializable {
        private static final long serialVersionUID = 0L;
        final Map<K, ? extends V> map;
        final V defaultValue;

        ForMapWithDefault(Map<K, ? extends V> map, @Nullable V defaultValue) {
            this.map = Preconditions.<Map<K, ? extends V>>checkNotNull(map);
            this.defaultValue = defaultValue;
        }

        public V apply(K key) {
            V result = this.map.get(key);
            return (result != null || this.map.containsKey(key)) ? result : this.defaultValue;
        }

        public boolean equals(@Nullable Object o) {
            if (o instanceof ForMapWithDefault) {
                ForMapWithDefault<?, ?> that = (ForMapWithDefault<?, ?>) o;
                return (this.map.equals(that.map) && Objects.equal(this.defaultValue, that.defaultValue));
            }
            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.map, this.defaultValue});
        }

        public String toString() {
            return "forMap(" + this.map + ", defaultValue=" + this.defaultValue + ")";
        }
    }

    private static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {
        private static final long serialVersionUID = 0L;
        private final Function<B, C> g;
        private final Function<A, ? extends B> f;

        public FunctionComposition(Function<B, C> g, Function<A, ? extends B> f) {
            this.g = Preconditions.<Function<B, C>>checkNotNull(g);
            this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
        }

        public C apply(A a) {
            return this.g.apply(this.f.apply(a));
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof FunctionComposition) {
                FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>) obj;
                return (this.f.equals(that.f) && this.g.equals(that.g));
            }
            return false;
        }

        public int hashCode() {
            return this.f.hashCode() ^ this.g.hashCode();
        }

        public String toString() {
            return this.g.toString() + "(" + this.f.toString() + ")";
        }
    }

    private static class PredicateFunction<T> implements Function<T, Boolean>, Serializable {
        private static final long serialVersionUID = 0L;
        private final Predicate<T> predicate;

        private PredicateFunction(Predicate<T> predicate) {
            this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
        }

        public Boolean apply(T t) {
            return Boolean.valueOf(this.predicate.apply(t));
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof PredicateFunction) {
                PredicateFunction<?> that = (PredicateFunction) obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }

        public int hashCode() {
            return this.predicate.hashCode();
        }

        public String toString() {
            return "forPredicate(" + this.predicate + ")";
        }
    }

    private static class ConstantFunction<E> implements Function<Object, E>, Serializable {
        private static final long serialVersionUID = 0L;
        private final E value;

        public ConstantFunction(@Nullable E value) {
            this.value = value;
        }

        public E apply(@Nullable Object from) {
            return this.value;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof ConstantFunction) {
                ConstantFunction<?> that = (ConstantFunction) obj;
                return Objects.equal(this.value, that.value);
            }
            return false;
        }

        public int hashCode() {
            return (this.value == null) ? 0 : this.value.hashCode();
        }

        public String toString() {
            return "constant(" + this.value + ")";
        }
    }

    private static class SupplierFunction<T>
            implements Function<Object, T>, Serializable {
        private static final long serialVersionUID = 0L;
        private final Supplier<T> supplier;

        private SupplierFunction(Supplier<T> supplier) {
            this.supplier = Preconditions.<Supplier<T>>checkNotNull(supplier);
        }

        public T apply(@Nullable Object input) {
            return this.supplier.get();
        }

        public boolean equals(@Nullable Object obj) {
            if (obj instanceof SupplierFunction) {
                SupplierFunction<?> that = (SupplierFunction) obj;
                return this.supplier.equals(that.supplier);
            }
            return false;
        }

        public int hashCode() {
            return this.supplier.hashCode();
        }

        public String toString() {
            return "forSupplier(" + this.supplier + ")";
        }
    }
}

