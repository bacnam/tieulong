package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
final class EmptyImmutableSet
extends ImmutableSet<Object>
{
static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();

public int size() {
return 0;
}

public boolean isEmpty() {
return true;
}

public boolean contains(Object target) {
return false;
}

public UnmodifiableIterator<Object> iterator() {
return Iterators.emptyIterator();
}

boolean isPartialView() {
return false;
}

private static final Object[] EMPTY_ARRAY = new Object[0];

public Object[] toArray() {
return EMPTY_ARRAY;
}
private static final long serialVersionUID = 0L;
public <T> T[] toArray(T[] a) {
if (a.length > 0) {
a[0] = null;
}
return a;
}

public boolean containsAll(Collection<?> targets) {
return targets.isEmpty();
}

public boolean equals(@Nullable Object object) {
if (object instanceof Set) {
Set<?> that = (Set)object;
return that.isEmpty();
} 
return false;
}

public final int hashCode() {
return 0;
}

boolean isHashCodeFast() {
return true;
}

public String toString() {
return "[]";
}

Object readResolve() {
return INSTANCE;
}
}

