package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
final class UsingToStringOrdering
extends Ordering<Object>
implements Serializable
{
static final UsingToStringOrdering INSTANCE = new UsingToStringOrdering();

public int compare(Object left, Object right) {
return left.toString().compareTo(right.toString());
}
private static final long serialVersionUID = 0L;

private Object readResolve() {
return INSTANCE;
}

public String toString() {
return "Ordering.usingToString()";
}
}

