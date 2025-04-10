package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableList<E>
extends ImmutableCollection<E>
implements List<E>, RandomAccess
{
public static <E> ImmutableList<E> of() {
return EmptyImmutableList.INSTANCE;
}

public static <E> ImmutableList<E> of(E element) {
return new SingletonImmutableList<E>(element);
}

public static <E> ImmutableList<E> of(E e1, E e2) {
return construct(new Object[] { e1, e2 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
return construct(new Object[] { e1, e2, e3 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
return construct(new Object[] { e1, e2, e3, e4 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
return construct(new Object[] { e1, e2, e3, e4, e5 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
}

public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
Object[] array = new Object[12 + others.length];
array[0] = e1;
array[1] = e2;
array[2] = e3;
array[3] = e4;
array[4] = e5;
array[5] = e6;
array[6] = e7;
array[7] = e8;
array[8] = e9;
array[9] = e10;
array[10] = e11;
array[11] = e12;
System.arraycopy(others, 0, array, 12, others.length);
return construct(array);
}

@Deprecated
public static <E> ImmutableList<E> of(E[] elements) {
return copyOf(elements);
}

public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
Preconditions.checkNotNull(elements);
return (elements instanceof Collection) ? copyOf(Collections2.cast(elements)) : copyOf(elements.iterator());
}

public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
if (elements instanceof ImmutableCollection) {

ImmutableList<E> list = ((ImmutableCollection)elements).asList();
return list.isPartialView() ? copyFromCollection(list) : list;
} 
return copyFromCollection(elements);
}

public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
return copyFromCollection(Lists.newArrayList(elements));
}

public static <E> ImmutableList<E> copyOf(E[] elements) {
switch (elements.length) {
case 0:
return of();
case 1:
return new SingletonImmutableList<E>(elements[0]);
} 
return construct((Object[])elements.clone());
}

private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> collection) {
ImmutableList<E> list;
Object[] elements = collection.toArray();
switch (elements.length) {
case 0:
return of();

case 1:
list = new SingletonImmutableList<E>((E)elements[0]);
return list;
} 

return construct(elements);
}

private static <E> ImmutableList<E> construct(Object... elements) {
for (int i = 0; i < elements.length; i++) {
checkElementNotNull(elements[i], i);
}
return new RegularImmutableList<E>(elements);
}

private static Object checkElementNotNull(Object element, int index) {
if (element == null) {
throw new NullPointerException("at index " + index);
}
return element;
}

public UnmodifiableIterator<E> iterator() {
return listIterator();
}

public UnmodifiableListIterator<E> listIterator() {
return listIterator(0);
}

public final boolean addAll(int index, Collection<? extends E> newElements) {
throw new UnsupportedOperationException();
}

public final E set(int index, E element) {
throw new UnsupportedOperationException();
}

public final void add(int index, E element) {
throw new UnsupportedOperationException();
}

public final E remove(int index) {
throw new UnsupportedOperationException();
}

public ImmutableList<E> asList() {
return this;
}

public ImmutableList<E> reverse() {
return new ReverseImmutableList<E>(this);
}

private static class ReverseImmutableList<E> extends ImmutableList<E> {
private final transient ImmutableList<E> forwardList;
private final transient int size;

ReverseImmutableList(ImmutableList<E> backingList) {
this.forwardList = backingList;
this.size = backingList.size();
}

private int reverseIndex(int index) {
return this.size - 1 - index;
}

private int reversePosition(int index) {
return this.size - index;
}

public ImmutableList<E> reverse() {
return this.forwardList;
}

public boolean contains(@Nullable Object object) {
return this.forwardList.contains(object);
}

public boolean containsAll(Collection<?> targets) {
return this.forwardList.containsAll(targets);
}

public int indexOf(@Nullable Object object) {
int index = this.forwardList.lastIndexOf(object);
return (index >= 0) ? reverseIndex(index) : -1;
}

public int lastIndexOf(@Nullable Object object) {
int index = this.forwardList.indexOf(object);
return (index >= 0) ? reverseIndex(index) : -1;
}

public ImmutableList<E> subList(int fromIndex, int toIndex) {
Preconditions.checkPositionIndexes(fromIndex, toIndex, this.size);
return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
}

public E get(int index) {
Preconditions.checkElementIndex(index, this.size);
return this.forwardList.get(reverseIndex(index));
}

public UnmodifiableListIterator<E> listIterator(int index) {
Preconditions.checkPositionIndex(index, this.size);
final UnmodifiableListIterator<E> forward = this.forwardList.listIterator(reversePosition(index));

return new UnmodifiableListIterator<E>() {
public boolean hasNext() {
return forward.hasPrevious();
}

public boolean hasPrevious() {
return forward.hasNext();
}

public E next() {
return forward.previous();
}

public int nextIndex() {
return ImmutableList.ReverseImmutableList.this.reverseIndex(forward.previousIndex());
}

public E previous() {
return forward.next();
}

public int previousIndex() {
return ImmutableList.ReverseImmutableList.this.reverseIndex(forward.nextIndex());
}
};
}

public int size() {
return this.size;
}

public boolean isEmpty() {
return this.forwardList.isEmpty();
}

boolean isPartialView() {
return this.forwardList.isPartialView();
}
}

public boolean equals(Object obj) {
return Lists.equalsImpl(this, obj);
}

public int hashCode() {
return Lists.hashCodeImpl(this);
}

private static class SerializedForm
implements Serializable
{
final Object[] elements;
private static final long serialVersionUID = 0L;

SerializedForm(Object[] elements) {
this.elements = elements;
}
Object readResolve() {
return ImmutableList.copyOf(this.elements);
}
}

private void readObject(ObjectInputStream stream) throws InvalidObjectException {
throw new InvalidObjectException("Use SerializedForm");
}

Object writeReplace() {
return new SerializedForm(toArray());
}

public static <E> Builder<E> builder() {
return new Builder<E>();
}

public abstract UnmodifiableListIterator<E> listIterator(int paramInt);

public abstract int indexOf(@Nullable Object paramObject);

public abstract int lastIndexOf(@Nullable Object paramObject);

public abstract ImmutableList<E> subList(int paramInt1, int paramInt2);

public static final class Builder<E>
extends ImmutableCollection.Builder<E>
{
private final ArrayList<E> contents = Lists.newArrayList();

public Builder<E> add(E element) {
this.contents.add((E)Preconditions.checkNotNull(element));
return this;
}

public Builder<E> addAll(Iterable<? extends E> elements) {
if (elements instanceof Collection) {
Collection<?> collection = (Collection)elements;
this.contents.ensureCapacity(this.contents.size() + collection.size());
} 
super.addAll(elements);
return this;
}

public Builder<E> add(E... elements) {
this.contents.ensureCapacity(this.contents.size() + elements.length);
super.add(elements);
return this;
}

public Builder<E> addAll(Iterator<? extends E> elements) {
super.addAll(elements);
return this;
}

public ImmutableList<E> build() {
return ImmutableList.copyOf(this.contents);
}
}
}

