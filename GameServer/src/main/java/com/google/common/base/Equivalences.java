package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;

@Beta
@GwtCompatible
public final class Equivalences {
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }

    @Deprecated
    @GwtCompatible(serializable = true)
    public static <T> Equivalence<Iterable<T>> pairwise(Equivalence<? super T> elementEquivalence) {
        return new PairwiseEquivalence<T>(elementEquivalence);
    }

    private static final class Equals
            extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE = new Equals();
        private static final long serialVersionUID = 1L;

        protected boolean doEquivalent(Object a, Object b) {
            return a.equals(b);
        }

        public int doHash(Object o) {
            return o.hashCode();
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    private static final class Identity
            extends Equivalence<Object>
            implements Serializable {
        static final Identity INSTANCE = new Identity();
        private static final long serialVersionUID = 1L;

        protected boolean doEquivalent(Object a, Object b) {
            return false;
        }

        protected int doHash(Object o) {
            return System.identityHashCode(o);
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }
}

