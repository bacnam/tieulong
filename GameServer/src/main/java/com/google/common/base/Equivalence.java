package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;

@Beta
@GwtCompatible
public abstract class Equivalence<T> {
    public final boolean equivalent(@Nullable T a, @Nullable T b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return doEquivalent(a, b);
    }

    protected abstract boolean doEquivalent(T paramT1, T paramT2);

    public final int hash(@Nullable T t) {
        if (t == null) {
            return 0;
        }
        return doHash(t);
    }

    protected abstract int doHash(T paramT);

    public final <F> Equivalence<F> onResultOf(Function<F, ? extends T> function) {
        return new FunctionalEquivalence<F, T>(function, this);
    }

    public final <S extends T> Wrapper<S> wrap(@Nullable S reference) {
        return new Wrapper<S>(this, reference);
    }

    @GwtCompatible(serializable = true)
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return new PairwiseEquivalence<S>(this);
    }

    public final Predicate<T> equivalentTo(@Nullable T target) {
        return new EquivalentToPredicate<T>(this, target);
    }

    @Beta
    public static final class Wrapper<T>
            implements Serializable {
        private static final long serialVersionUID = 0L;
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;

        private Wrapper(Equivalence<? super T> equivalence, @Nullable T reference) {
            this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
            this.reference = reference;
        }

        @Nullable
        public T get() {
            return this.reference;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this)
                return true;
            if (obj instanceof Wrapper) {
                Wrapper<?> that = (Wrapper) obj;

                Equivalence<Object> equivalence = (Equivalence) this.equivalence;
                return (equivalence.equals(that.equivalence) && equivalence.equivalent(this.reference, that.reference));
            }

            return false;
        }

        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }

        public String toString() {
            return this.equivalence + ".wrap(" + this.reference + ")";
        }
    }

    private static final class EquivalentToPredicate<T>
            implements Predicate<T>, Serializable {
        private static final long serialVersionUID = 0L;
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;
        EquivalentToPredicate(Equivalence<T> equivalence, @Nullable T target) {
            this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
            this.target = target;
        }

        public boolean apply(@Nullable T input) {
            return this.equivalence.equivalent(input, this.target);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof EquivalentToPredicate) {
                EquivalentToPredicate<?> that = (EquivalentToPredicate) obj;
                return (this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target));
            }

            return false;
        }

        public int hashCode() {
            return Objects.hashCode(new Object[]{this.equivalence, this.target});
        }

        public String toString() {
            return this.equivalence + ".equivalentTo(" + this.target + ")";
        }
    }
}

