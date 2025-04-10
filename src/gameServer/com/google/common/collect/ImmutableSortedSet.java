package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true, emulated = true)
public abstract class ImmutableSortedSet<E>
extends ImmutableSortedSetFauxverideShim<E>
implements SortedSet<E>, SortedIterable<E>
{
private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();

private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet<Comparable>(NATURAL_ORDER);

final transient Comparator<? super E> comparator;

private static <E> ImmutableSortedSet<E> emptySet() {
return (ImmutableSortedSet)NATURAL_EMPTY_SET;
}

static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
if (NATURAL_ORDER.equals(comparator)) {
return emptySet();
}
return new EmptyImmutableSortedSet<E>(comparator);
}

public static <E> ImmutableSortedSet<E> of() {
return emptySet();
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
return new RegularImmutableSortedSet<E>(ImmutableList.of(element), Ordering.natural());
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2 }));
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 }));
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 }));
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 }));
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
int size = remaining.length + 6;
List<E> all = new ArrayList<E>(size);
Collections.addAll(all, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5, (Comparable)e6 });
Collections.addAll(all, remaining);
return copyOf(Ordering.natural(), all);
}

@Deprecated
public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E[] elements) {
return copyOf(elements);
}

public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
return copyOf(Ordering.natural(), Arrays.asList(elements));
}

public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
Ordering<E> naturalOrder = Ordering.natural();
return copyOf(naturalOrder, elements);
}

public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
Ordering<E> naturalOrder = Ordering.natural();
return copyOf(naturalOrder, elements);
}

public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
Ordering<E> naturalOrder = Ordering.natural();
return copyOfInternal(naturalOrder, elements);
}

public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
Preconditions.checkNotNull(comparator);
return copyOfInternal(comparator, elements);
}

public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
Preconditions.checkNotNull(comparator);
return copyOfInternal(comparator, elements);
}

public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
Preconditions.checkNotNull(comparator);
return copyOfInternal(comparator, elements);
}

public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
Comparator<Comparable> comparator1;
Comparator<? super E> comparator = sortedSet.comparator();
if (comparator == null) {
comparator1 = NATURAL_ORDER;
}
return copyOfInternal((Comparator)comparator1, sortedSet);
}

private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterable<? extends E> elements) {
boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);

if (hasSameComparator && elements instanceof ImmutableSortedSet) {

ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
if (!original.isPartialView()) {
return original;
}
} 
ImmutableList<E> list = ImmutableList.copyOf(SortedIterables.sortedUnique(comparator, elements));

return list.isEmpty() ? emptySet(comparator) : new RegularImmutableSortedSet<E>(list, comparator);
}

private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> comparator, Iterator<? extends E> elements) {
ImmutableList<E> list = ImmutableList.copyOf(SortedIterables.sortedUnique(comparator, elements));

return list.isEmpty() ? emptySet(comparator) : new RegularImmutableSortedSet<E>(list, comparator);
}

public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
return new Builder<E>(comparator);
}

public static <E extends Comparable<E>> Builder<E> reverseOrder() {
return new Builder<E>(Ordering.<Comparable>natural().reverse());
}

public static <E extends Comparable<E>> Builder<E> naturalOrder() {
return new Builder<E>(Ordering.natural());
}

public static final class Builder<E>
extends ImmutableSet.Builder<E>
{
private final Comparator<? super E> comparator;

public Builder(Comparator<? super E> comparator) {
this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
}

public Builder<E> add(E element) {
super.add(element);
return this;
}

public Builder<E> add(E... elements) {
super.add(elements);
return this;
}

public Builder<E> addAll(Iterable<? extends E> elements) {
super.addAll(elements);
return this;
}

public Builder<E> addAll(Iterator<? extends E> elements) {
super.addAll(elements);
return this;
}

public ImmutableSortedSet<E> build() {
return ImmutableSortedSet.copyOfInternal(this.comparator, this.contents.iterator());
}
}

int unsafeCompare(Object a, Object b) {
return unsafeCompare(this.comparator, a, b);
}

static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
Comparator<Object> unsafeComparator = (Comparator)comparator;
return unsafeComparator.compare(a, b);
}

ImmutableSortedSet(Comparator<? super E> comparator) {
this.comparator = comparator;
}

public Comparator<? super E> comparator() {
return this.comparator;
}

public ImmutableSortedSet<E> headSet(E toElement) {
return headSet(toElement, false);
}

ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
}

public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
return subSet(fromElement, true, toElement, false);
}

ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
Preconditions.checkNotNull(fromElement);
Preconditions.checkNotNull(toElement);
Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
}

public ImmutableSortedSet<E> tailSet(E fromElement) {
return tailSet(fromElement, true);
}

ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
}

private static class SerializedForm<E>
implements Serializable
{
final Comparator<? super E> comparator;

final Object[] elements;

private static final long serialVersionUID = 0L;

public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
this.comparator = comparator;
this.elements = elements;
}

Object readResolve() {
return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
}
}

private void readObject(ObjectInputStream stream) throws InvalidObjectException {
throw new InvalidObjectException("Use SerializedForm");
}

Object writeReplace() {
return new SerializedForm<E>(this.comparator, toArray());
}

public abstract UnmodifiableIterator<E> iterator();

abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);

abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);

abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);

abstract int indexOf(@Nullable Object paramObject);
}

