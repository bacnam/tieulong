package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

@GwtCompatible(serializable = true, emulated = true)
final class ImmutableAsList<E>
extends RegularImmutableList<E>
{
private final transient ImmutableCollection<E> collection;

ImmutableAsList(Object[] array, ImmutableCollection<E> collection) {
super(array, 0, array.length);
this.collection = collection;
}

public boolean contains(Object target) {
return this.collection.contains(target);
}

static class SerializedForm
implements Serializable {
final ImmutableCollection<?> collection;
private static final long serialVersionUID = 0L;

SerializedForm(ImmutableCollection<?> collection) {
this.collection = collection;
}
Object readResolve() {
return this.collection.asList();
}
}

private void readObject(ObjectInputStream stream) throws InvalidObjectException {
throw new InvalidObjectException("Use SerializedForm");
}

Object writeReplace() {
return new SerializedForm(this.collection);
}
}

