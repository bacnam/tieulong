package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible(serializable = true, emulated = true)
final class RegularImmutableSet<E>
        extends ImmutableSet.ArrayImmutableSet<E> {
    @VisibleForTesting
    final transient Object[] table;
    private final transient int mask;
    private final transient int hashCode;

    RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
        super(elements);
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }

    public boolean contains(Object target) {
        if (target == null) {
            return false;
        }
        for (int i = Hashing.smear(target.hashCode()); ; i++) {
            Object candidate = this.table[i & this.mask];
            if (candidate == null) {
                return false;
            }
            if (candidate.equals(target)) {
                return true;
            }
        }
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}

