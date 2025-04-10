package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
public abstract class ImmutableMultiset<E>
extends ImmutableCollection<E>
implements Multiset<E>
{
private transient ImmutableSet<Multiset.Entry<E>> entrySet;

public static <E> ImmutableMultiset<E> of() {
return EmptyImmutableMultiset.INSTANCE;
}

public static <E> ImmutableMultiset<E> of(E element) {
return copyOfInternal((E[])new Object[] { element });
}

public static <E> ImmutableMultiset<E> of(E e1, E e2) {
return copyOfInternal((E[])new Object[] { e1, e2 });
}

public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3) {
return copyOfInternal((E[])new Object[] { e1, e2, e3 });
}

public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4) {
return copyOfInternal((E[])new Object[] { e1, e2, e3, e4 });
}

public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
return copyOfInternal((E[])new Object[] { e1, e2, e3, e4, e5 });
}

public static <E> ImmutableMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... others) {
int size = others.length + 6;
List<E> all = new ArrayList<E>(size);
Collections.addAll(all, (E[])new Object[] { e1, e2, e3, e4, e5, e6 });
Collections.addAll(all, others);
return copyOf(all);
}

@Deprecated
public static <E> ImmutableMultiset<E> of(E[] elements) {
return copyOf(Arrays.asList(elements));
}

public static <E> ImmutableMultiset<E> copyOf(E[] elements) {
return copyOf(Arrays.asList(elements));
}

public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> elements) {
if (elements instanceof ImmutableMultiset) {

ImmutableMultiset<E> result = (ImmutableMultiset)elements;
if (!result.isPartialView()) {
return result;
}
} 

Multiset<? extends E> multiset = (elements instanceof Multiset) ? Multisets.<E>cast(elements) : LinkedHashMultiset.<E>create(elements);

return copyOfInternal(multiset);
}

private static <E> ImmutableMultiset<E> copyOfInternal(E... elements) {
return copyOf(Arrays.asList(elements));
}

private static <E> ImmutableMultiset<E> copyOfInternal(Multiset<? extends E> multiset) {
long size = 0L;
ImmutableMap.Builder<E, Integer> builder = ImmutableMap.builder();

for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
int count = entry.getCount();
if (count > 0) {

builder.put(entry.getElement(), Integer.valueOf(count));
size += count;
} 
} 

if (size == 0L) {
return of();
}
return new RegularImmutableMultiset<E>(builder.build(), Ints.saturatedCast(size));
}

public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> elements) {
Multiset<E> multiset = LinkedHashMultiset.create();
Iterators.addAll(multiset, elements);
return copyOfInternal(multiset);
}

public UnmodifiableIterator<E> iterator() {
final Iterator<Multiset.Entry<E>> entryIterator = entryIterator();

return new UnmodifiableIterator<E>()
{
int remaining;
E element;

public boolean hasNext() {
return (this.remaining > 0 || entryIterator.hasNext());
}

public E next() {
if (this.remaining <= 0) {
Multiset.Entry<E> entry = entryIterator.next();
this.element = entry.getElement();
this.remaining = entry.getCount();
} 
this.remaining--;
return this.element;
}
};
}

public boolean contains(@Nullable Object object) {
return (count(object) > 0);
}

public boolean containsAll(Collection<?> targets) {
return elementSet().containsAll(targets);
}

public final int add(E element, int occurrences) {
throw new UnsupportedOperationException();
}

public final int remove(Object element, int occurrences) {
throw new UnsupportedOperationException();
}

public final int setCount(E element, int count) {
throw new UnsupportedOperationException();
}

public final boolean setCount(E element, int oldCount, int newCount) {
throw new UnsupportedOperationException();
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof Multiset) {
Multiset<?> that = (Multiset)object;
if (size() != that.size()) {
return false;
}
for (Multiset.Entry<?> entry : that.entrySet()) {
if (count(entry.getElement()) != entry.getCount()) {
return false;
}
} 
return true;
} 
return false;
}

public int hashCode() {
return Sets.hashCodeImpl(entrySet());
}

public String toString() {
return entrySet().toString();
}

public Set<Multiset.Entry<E>> entrySet() {
ImmutableSet<Multiset.Entry<E>> es = this.entrySet;
return (es == null) ? (this.entrySet = createEntrySet()) : es;
}

ImmutableSet<Multiset.Entry<E>> createEntrySet() {
return new EntrySet<E>(this);
}

static class EntrySet<E> extends ImmutableSet<Multiset.Entry<E>> {
final transient ImmutableMultiset<E> multiset;

public EntrySet(ImmutableMultiset<E> multiset) {
this.multiset = multiset;
}
private static final long serialVersionUID = 0L;

public UnmodifiableIterator<Multiset.Entry<E>> iterator() {
return this.multiset.entryIterator();
}

public int size() {
return this.multiset.distinctElements();
}

boolean isPartialView() {
return this.multiset.isPartialView();
}

public boolean contains(Object o) {
if (o instanceof Multiset.Entry) {
Multiset.Entry<?> entry = (Multiset.Entry)o;
if (entry.getCount() <= 0) {
return false;
}
int count = this.multiset.count(entry.getElement());
return (count == entry.getCount());
} 
return false;
}

public Object[] toArray() {
Object[] newArray = new Object[size()];
return toArray(newArray);
}

public <T> T[] toArray(T[] other) {
int size = size();
if (other.length < size) {
other = ObjectArrays.newArray(other, size);
} else if (other.length > size) {
other[size] = null;
} 

T[] arrayOfT = other;
int index = 0;
for (Multiset.Entry<?> element : this) {
arrayOfT[index++] = (T)element;
}
return other;
}

public int hashCode() {
return this.multiset.hashCode();
}

Object writeReplace() {
return new EntrySetSerializedForm<E>(this.multiset);
}

static class EntrySetSerializedForm<E> implements Serializable {
final ImmutableMultiset<E> multiset;

EntrySetSerializedForm(ImmutableMultiset<E> multiset) {
this.multiset = multiset;
}

Object readResolve() {
return this.multiset.entrySet();
}
}
}

private static class SerializedForm
implements Serializable {
final Object[] elements;
final int[] counts;
private static final long serialVersionUID = 0L;

SerializedForm(Multiset<?> multiset) {
int distinct = multiset.entrySet().size();
this.elements = new Object[distinct];
this.counts = new int[distinct];
int i = 0;
for (Multiset.Entry<?> entry : multiset.entrySet()) {
this.elements[i] = entry.getElement();
this.counts[i] = entry.getCount();
i++;
} 
}

Object readResolve() {
LinkedHashMultiset<Object> multiset = LinkedHashMultiset.create(this.elements.length);

for (int i = 0; i < this.elements.length; i++) {
multiset.add(this.elements[i], this.counts[i]);
}
return ImmutableMultiset.copyOf(multiset);
}
}

Object writeReplace() {
return new SerializedForm(this);
}

public static <E> Builder<E> builder() {
return new Builder<E>();
}

abstract UnmodifiableIterator<Multiset.Entry<E>> entryIterator();

abstract int distinctElements();

public static class Builder<E>
extends ImmutableCollection.Builder<E>
{
final Multiset<E> contents;

public Builder() {
this(LinkedHashMultiset.create());
}

Builder(Multiset<E> contents) {
this.contents = contents;
}

public Builder<E> add(E element) {
this.contents.add((E)Preconditions.checkNotNull(element));
return this;
}

public Builder<E> addCopies(E element, int occurrences) {
this.contents.add((E)Preconditions.checkNotNull(element), occurrences);
return this;
}

public Builder<E> setCount(E element, int count) {
this.contents.setCount((E)Preconditions.checkNotNull(element), count);
return this;
}

public Builder<E> add(E... elements) {
super.add(elements);
return this;
}

public Builder<E> addAll(Iterable<? extends E> elements) {
if (elements instanceof Multiset) {
Multiset<? extends E> multiset = Multisets.cast(elements);
for (Multiset.Entry<? extends E> entry : multiset.entrySet()) {
addCopies(entry.getElement(), entry.getCount());
}
} else {
super.addAll(elements);
} 
return this;
}

public Builder<E> addAll(Iterator<? extends E> elements) {
super.addAll(elements);
return this;
}

public ImmutableMultiset<E> build() {
return ImmutableMultiset.copyOf(this.contents);
}
}
}

