package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Iterator;

@GwtCompatible(serializable = true)
final class LexicographicalOrdering<T>
        extends Ordering<Iterable<T>>
        implements Serializable {
    private static final long serialVersionUID = 0L;
    final Ordering<? super T> elementOrder;

    LexicographicalOrdering(Ordering<? super T> elementOrder) {
        this.elementOrder = elementOrder;
    }

    public int compare(Iterable<T> leftIterable, Iterable<T> rightIterable) {
        Iterator<T> left = leftIterable.iterator();
        Iterator<T> right = rightIterable.iterator();
        while (left.hasNext()) {
            if (!right.hasNext()) {
                return 1;
            }
            int result = this.elementOrder.compare(left.next(), right.next());
            if (result != 0) {
                return result;
            }
        }
        if (right.hasNext()) {
            return -1;
        }
        return 0;
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof LexicographicalOrdering) {
            LexicographicalOrdering<?> that = (LexicographicalOrdering) object;
            return this.elementOrder.equals(that.elementOrder);
        }
        return false;
    }

    public int hashCode() {
        return this.elementOrder.hashCode() ^ 0x7BB78CF5;
    }

    public String toString() {
        return this.elementOrder + ".lexicographical()";
    }
}

