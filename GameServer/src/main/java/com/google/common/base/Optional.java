package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;

@Beta
@GwtCompatible
public abstract class Optional<T>
        implements Serializable {
    private static final long serialVersionUID = 0L;

    private Optional() {
    }

    public static <T> Optional<T> absent() {
        return Absent.INSTANCE;
    }

    public static <T> Optional<T> of(T reference) {
        return new Present<T>(Preconditions.checkNotNull(reference));
    }

    public static <T> Optional<T> fromNullable(@Nullable T nullableReference) {
        return (nullableReference == null) ? absent() : new Present<T>(nullableReference);
    }

    public abstract boolean isPresent();

    public abstract T get();

    public abstract T or(T paramT);

    public abstract Optional<T> or(Optional<? extends T> paramOptional);

    @Nullable
    public abstract T or(Supplier<? extends T> paramSupplier);

    @Nullable
    public abstract T orNull();

    public abstract boolean equals(@Nullable Object paramObject);

    public abstract int hashCode();

    public abstract String toString();

    private static final class Present<T>
            extends Optional<T> {
        private static final long serialVersionUID = 0L;
        private final T reference;

        Present(T reference) {
            this.reference = reference;
        }

        public boolean isPresent() {
            return true;
        }

        public T get() {
            return this.reference;
        }

        public T or(T defaultValue) {
            Preconditions.checkNotNull(defaultValue, "use orNull() instead of or(null)");
            return this.reference;
        }

        public Optional<T> or(Optional<? extends T> secondChoice) {
            Preconditions.checkNotNull(secondChoice);
            return this;
        }

        public T or(Supplier<? extends T> supplier) {
            Preconditions.checkNotNull(supplier);
            return this.reference;
        }

        public T orNull() {
            return this.reference;
        }

        public boolean equals(@Nullable Object object) {
            if (object instanceof Present) {
                Present<?> other = (Present) object;
                return this.reference.equals(other.reference);
            }
            return false;
        }

        public int hashCode() {
            return 1502476572 + this.reference.hashCode();
        }

        public String toString() {
            return "Optional.of(" + this.reference + ")";
        }
    }

    private static final class Absent
            extends Optional<Object> {
        private static final Absent INSTANCE = new Absent();
        private static final long serialVersionUID = 0L;

        public boolean isPresent() {
            return false;
        }

        public Object get() {
            throw new IllegalStateException("value is absent");
        }

        public Object or(Object defaultValue) {
            return Preconditions.checkNotNull(defaultValue, "use orNull() instead of or(null)");
        }

        public Optional<Object> or(Optional<?> secondChoice) {
            return (Optional<Object>) Preconditions.<Optional<?>>checkNotNull(secondChoice);
        }

        @Nullable
        public Object or(Supplier<?> supplier) {
            return supplier.get();
        }

        @Nullable
        public Object orNull() {
            return null;
        }

        public boolean equals(@Nullable Object object) {
            return (object == this);
        }

        public int hashCode() {
            return 1502476572;
        }

        public String toString() {
            return "Optional.absent()";
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

