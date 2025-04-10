package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class ComparatorOrdering<T>
extends Ordering<T>
implements Serializable
{
final Comparator<T> comparator;
private static final long serialVersionUID = 0L;

ComparatorOrdering(Comparator<T> comparator) {
this.comparator = (Comparator<T>)Preconditions.checkNotNull(comparator);
}

public int compare(T a, T b) {
return this.comparator.compare(a, b);
}

public int binarySearch(List<? extends T> sortedList, T key) {
return Collections.binarySearch(sortedList, key, this.comparator);
}

public <E extends T> List<E> sortedCopy(Iterable<E> iterable) {
List<E> list = Lists.newArrayList(iterable);
Collections.sort(list, this.comparator);
return list;
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof ComparatorOrdering) {
ComparatorOrdering<?> that = (ComparatorOrdering)object;
return this.comparator.equals(that.comparator);
} 
return false;
}

public int hashCode() {
return this.comparator.hashCode();
}

public String toString() {
return this.comparator.toString();
}
}

