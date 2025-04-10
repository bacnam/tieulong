package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@GwtCompatible(serializable = true)
final class NaturalOrdering
extends Ordering<Comparable>
implements Serializable
{
static final NaturalOrdering INSTANCE = new NaturalOrdering();

public int compare(Comparable<Comparable> left, Comparable right) {
Preconditions.checkNotNull(right);
if (left == right) {
return 0;
}

return left.compareTo(right);
}
private static final long serialVersionUID = 0L;
public <S extends Comparable> Ordering<S> reverse() {
return ReverseNaturalOrdering.INSTANCE;
}

public int binarySearch(List<? extends Comparable> sortedList, Comparable key) {
return Collections.binarySearch((List)sortedList, key);
}

public <E extends Comparable> List<E> sortedCopy(Iterable<E> iterable) {
List<E> list = Lists.newArrayList(iterable);
Collections.sort(list);
return list;
}

private Object readResolve() {
return INSTANCE;
}

public String toString() {
return "Ordering.natural()";
}
}

