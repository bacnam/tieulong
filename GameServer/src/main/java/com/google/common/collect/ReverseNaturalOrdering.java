package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class ReverseNaturalOrdering
extends Ordering<Comparable>
implements Serializable
{
static final ReverseNaturalOrdering INSTANCE = new ReverseNaturalOrdering();

public int compare(Comparable left, Comparable<Comparable> right) {
Preconditions.checkNotNull(left);
if (left == right) {
return 0;
}

return right.compareTo(left);
}
private static final long serialVersionUID = 0L;
public <S extends Comparable> Ordering<S> reverse() {
return Ordering.natural();
}

public <E extends Comparable> E min(E a, E b) {
return (E)NaturalOrdering.INSTANCE.max(a, b);
}

public <E extends Comparable> E min(E a, E b, E c, E... rest) {
return (E)NaturalOrdering.INSTANCE.max(a, b, c, (Object[])rest);
}

public <E extends Comparable> E min(Iterable<E> iterable) {
return (E)NaturalOrdering.INSTANCE.max(iterable);
}

public <E extends Comparable> E max(E a, E b) {
return (E)NaturalOrdering.INSTANCE.min(a, b);
}

public <E extends Comparable> E max(E a, E b, E c, E... rest) {
return (E)NaturalOrdering.INSTANCE.min(a, b, c, (Object[])rest);
}

public <E extends Comparable> E max(Iterable<E> iterable) {
return (E)NaturalOrdering.INSTANCE.min(iterable);
}

private Object readResolve() {
return INSTANCE;
}

public String toString() {
return "Ordering.natural().reverse()";
}
}

