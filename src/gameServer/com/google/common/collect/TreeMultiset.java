package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class TreeMultiset<E>
extends AbstractMapBasedMultiset<E>
implements SortedIterable<E>
{
private final Comparator<? super E> comparator;
@GwtIncompatible("not needed in emulated source")
private static final long serialVersionUID = 0L;

public static <E extends Comparable> TreeMultiset<E> create() {
return new TreeMultiset<E>();
}

public static <E> TreeMultiset<E> create(@Nullable Comparator<? super E> comparator) {
return (comparator == null) ? new TreeMultiset<E>() : new TreeMultiset<E>(comparator);
}

public Iterator<E> iterator() {
return super.iterator();
}

public static <E extends Comparable> TreeMultiset<E> create(Iterable<? extends E> elements) {
TreeMultiset<E> multiset = create();
Iterables.addAll(multiset, elements);
return multiset;
}

private TreeMultiset() {
this(Ordering.natural());
}

private TreeMultiset(@Nullable Comparator<? super E> comparator) {
super(new TreeMap<E, Count>((Comparator<? super E>)Preconditions.checkNotNull(comparator)));
this.comparator = comparator;
}

public Comparator<? super E> comparator() {
return this.comparator;
}

public SortedSet<E> elementSet() {
return (SortedSet<E>)super.elementSet();
}

public int count(@Nullable Object element) {
try {
return super.count(element);
} catch (NullPointerException e) {
return 0;
} catch (ClassCastException e) {
return 0;
} 
}

public int add(E element, int occurrences) {
if (element == null) {
this.comparator.compare(element, element);
}
return super.add(element, occurrences);
}

Set<E> createElementSet() {
return new SortedMapBasedElementSet((SortedMap<E, Count>)backingMap());
}

private class SortedMapBasedElementSet
extends AbstractMapBasedMultiset<E>.MapBasedElementSet
implements SortedSet<E>, SortedIterable<E>
{
SortedMapBasedElementSet(SortedMap<E, Count> map) {
super(map);
}

SortedMap<E, Count> sortedMap() {
return (SortedMap<E, Count>)getMap();
}

public Comparator<? super E> comparator() {
return sortedMap().comparator();
}

public E first() {
return sortedMap().firstKey();
}

public E last() {
return sortedMap().lastKey();
}

public SortedSet<E> headSet(E toElement) {
return new SortedMapBasedElementSet(sortedMap().headMap(toElement));
}

public SortedSet<E> subSet(E fromElement, E toElement) {
return new SortedMapBasedElementSet(sortedMap().subMap(fromElement, toElement));
}

public SortedSet<E> tailSet(E fromElement) {
return new SortedMapBasedElementSet(sortedMap().tailMap(fromElement));
}

public boolean remove(Object element) {
try {
return super.remove(element);
} catch (NullPointerException e) {
return false;
} catch (ClassCastException e) {
return false;
} 
}
}

@GwtIncompatible("java.io.ObjectOutputStream")
private void writeObject(ObjectOutputStream stream) throws IOException {
stream.defaultWriteObject();
stream.writeObject(elementSet().comparator());
Serialization.writeMultiset(this, stream);
}

@GwtIncompatible("java.io.ObjectInputStream")
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();

Comparator<? super E> comparator = (Comparator<? super E>)stream.readObject();

setBackingMap(new TreeMap<E, Count>(comparator));
Serialization.populateMultiset(this, stream);
}
}

