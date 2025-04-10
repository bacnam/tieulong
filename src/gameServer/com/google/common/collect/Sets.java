package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.primitives.Ints;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Sets
{
@GwtCompatible(serializable = true)
public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(E anElement, E... otherElements) {
return new ImmutableEnumSet<E>(EnumSet.of(anElement, otherElements));
}

@GwtCompatible(serializable = true)
public static <E extends Enum<E>> ImmutableSet<E> immutableEnumSet(Iterable<E> elements) {
Iterator<E> iterator = elements.iterator();
if (!iterator.hasNext()) {
return ImmutableSet.of();
}
if (elements instanceof EnumSet) {
EnumSet<E> enumSetClone = EnumSet.copyOf((EnumSet<E>)elements);
return new ImmutableEnumSet<E>(enumSetClone);
} 
Enum enum_ = (Enum)iterator.next();
EnumSet<E> set = EnumSet.of((E)enum_);
while (iterator.hasNext()) {
set.add(iterator.next());
}
return new ImmutableEnumSet<E>(set);
}

public static <E extends Enum<E>> EnumSet<E> newEnumSet(Iterable<E> iterable, Class<E> elementType) {
Preconditions.checkNotNull(iterable);
EnumSet<E> set = EnumSet.noneOf(elementType);
Iterables.addAll(set, iterable);
return set;
}

public static <E> HashSet<E> newHashSet() {
return new HashSet<E>();
}

public static <E> HashSet<E> newHashSet(E... elements) {
HashSet<E> set = newHashSetWithExpectedSize(elements.length);
Collections.addAll(set, elements);
return set;
}

public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
return new HashSet<E>(Maps.capacity(expectedSize));
}

public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
return (elements instanceof Collection) ? new HashSet<E>(Collections2.cast(elements)) : newHashSet(elements.iterator());
}

public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
HashSet<E> set = newHashSet();
while (elements.hasNext()) {
set.add(elements.next());
}
return set;
}

public static <E> LinkedHashSet<E> newLinkedHashSet() {
return new LinkedHashSet<E>();
}

public static <E> LinkedHashSet<E> newLinkedHashSet(Iterable<? extends E> elements) {
if (elements instanceof Collection) {
return new LinkedHashSet<E>(Collections2.cast(elements));
}
LinkedHashSet<E> set = newLinkedHashSet();
for (E element : elements) {
set.add(element);
}
return set;
}

public static <E extends Comparable> TreeSet<E> newTreeSet() {
return new TreeSet<E>();
}

public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
TreeSet<E> set = newTreeSet();
for (Comparable comparable : elements) {
set.add((E)comparable);
}
return set;
}

public static <E> TreeSet<E> newTreeSet(Comparator<? super E> comparator) {
return new TreeSet<E>((Comparator<? super E>)Preconditions.checkNotNull(comparator));
}

public static <E> Set<E> newIdentityHashSet() {
return newSetFromMap(Maps.newIdentityHashMap());
}

public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection) {
if (collection instanceof EnumSet) {
return EnumSet.complementOf((EnumSet<E>)collection);
}
Preconditions.checkArgument(!collection.isEmpty(), "collection is empty; use the other version of this method");

Class<E> type = ((Enum<E>)collection.iterator().next()).getDeclaringClass();
return makeComplementByHand(collection, type);
}

public static <E extends Enum<E>> EnumSet<E> complementOf(Collection<E> collection, Class<E> type) {
Preconditions.checkNotNull(collection);
return (collection instanceof EnumSet) ? EnumSet.<E>complementOf((EnumSet<E>)collection) : makeComplementByHand(collection, type);
}

private static <E extends Enum<E>> EnumSet<E> makeComplementByHand(Collection<E> collection, Class<E> type) {
EnumSet<E> result = EnumSet.allOf(type);
result.removeAll(collection);
return result;
}

public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
return new SetFromMap<E>(map);
}

private static class SetFromMap<E>
extends AbstractSet<E>
implements Set<E>, Serializable {
private final Map<E, Boolean> m;

SetFromMap(Map<E, Boolean> map) {
Preconditions.checkArgument(map.isEmpty(), "Map is non-empty");
this.m = map;
this.s = map.keySet();
} private transient Set<E> s; @GwtIncompatible("not needed in emulated source")
private static final long serialVersionUID = 0L;
public void clear() {
this.m.clear();
}
public int size() {
return this.m.size();
}
public boolean isEmpty() {
return this.m.isEmpty();
}
public boolean contains(Object o) {
return this.m.containsKey(o);
}
public boolean remove(Object o) {
return (this.m.remove(o) != null);
}
public boolean add(E e) {
return (this.m.put(e, Boolean.TRUE) == null);
}
public Iterator<E> iterator() {
return this.s.iterator();
}
public Object[] toArray() {
return this.s.toArray();
}
public <T> T[] toArray(T[] a) {
return this.s.toArray(a);
}
public String toString() {
return this.s.toString();
}
public int hashCode() {
return this.s.hashCode();
}
public boolean equals(@Nullable Object object) {
return (this == object || this.s.equals(object));
}
public boolean containsAll(Collection<?> c) {
return this.s.containsAll(c);
}
public boolean removeAll(Collection<?> c) {
return this.s.removeAll(c);
}
public boolean retainAll(Collection<?> c) {
return this.s.retainAll(c);
}

@GwtIncompatible("java.io.ObjectInputStream")
private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
stream.defaultReadObject();
this.s = this.m.keySet();
}
}

public static abstract class SetView<E>
extends AbstractSet<E>
{
private SetView() {}

public ImmutableSet<E> immutableCopy() {
return ImmutableSet.copyOf(this);
}

public <S extends Set<E>> S copyInto(S set) {
set.addAll(this);
return set;
}
}

public static <E> SetView<E> union(final Set<? extends E> set1, final Set<? extends E> set2) {
Preconditions.checkNotNull(set1, "set1");
Preconditions.checkNotNull(set2, "set2");

final Set<? extends E> set2minus1 = difference(set2, set1);

return new SetView<E>() {
public int size() {
return set1.size() + set2minus1.size();
}
public boolean isEmpty() {
return (set1.isEmpty() && set2.isEmpty());
}
public Iterator<E> iterator() {
return Iterators.unmodifiableIterator(Iterators.concat(set1.iterator(), set2minus1.iterator()));
}

public boolean contains(Object object) {
return (set1.contains(object) || set2.contains(object));
}
public <S extends Set<E>> S copyInto(S set) {
set.addAll(set1);
set.addAll(set2);
return set;
}
public ImmutableSet<E> immutableCopy() {
return (new ImmutableSet.Builder<E>()).addAll(set1).addAll(set2).build();
}
};
}

public static <E> SetView<E> intersection(final Set<E> set1, final Set<?> set2) {
Preconditions.checkNotNull(set1, "set1");
Preconditions.checkNotNull(set2, "set2");

final Predicate<Object> inSet2 = Predicates.in(set2);
return new SetView<E>() {
public Iterator<E> iterator() {
return Iterators.filter(set1.iterator(), inSet2);
}
public int size() {
return Iterators.size(iterator());
}
public boolean isEmpty() {
return !iterator().hasNext();
}
public boolean contains(Object object) {
return (set1.contains(object) && set2.contains(object));
}
public boolean containsAll(Collection<?> collection) {
return (set1.containsAll(collection) && set2.containsAll(collection));
}
};
}

public static <E> SetView<E> difference(final Set<E> set1, final Set<?> set2) {
Preconditions.checkNotNull(set1, "set1");
Preconditions.checkNotNull(set2, "set2");

final Predicate<Object> notInSet2 = Predicates.not(Predicates.in(set2));
return new SetView<E>() {
public Iterator<E> iterator() {
return Iterators.filter(set1.iterator(), notInSet2);
}
public int size() {
return Iterators.size(iterator());
}
public boolean isEmpty() {
return set2.containsAll(set1);
}
public boolean contains(Object element) {
return (set1.contains(element) && !set2.contains(element));
}
};
}

public static <E> SetView<E> symmetricDifference(Set<? extends E> set1, Set<? extends E> set2) {
Preconditions.checkNotNull(set1, "set1");
Preconditions.checkNotNull(set2, "set2");

return difference(union(set1, set2), intersection(set1, set2));
}

public static <E> Set<E> filter(Set<E> unfiltered, Predicate<? super E> predicate) {
if (unfiltered instanceof FilteredSet) {

FilteredSet<E> filtered = (FilteredSet<E>)unfiltered;
Predicate<E> combinedPredicate = Predicates.and(filtered.predicate, predicate);

return new FilteredSet<E>((Set<E>)filtered.unfiltered, combinedPredicate);
} 

return new FilteredSet<E>((Set<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
}

private static class FilteredSet<E>
extends Collections2.FilteredCollection<E>
implements Set<E> {
FilteredSet(Set<E> unfiltered, Predicate<? super E> predicate) {
super(unfiltered, predicate);
}

public boolean equals(@Nullable Object object) {
return Sets.equalsImpl(this, object);
}

public int hashCode() {
return Sets.hashCodeImpl(this);
}
}

public static <B> Set<List<B>> cartesianProduct(List<? extends Set<? extends B>> sets) {
for (Set<? extends B> set : sets) {
if (set.isEmpty()) {
return ImmutableSet.of();
}
} 
CartesianSet<B> cartesianSet = new CartesianSet<B>(sets);
return cartesianSet;
}

public static <B> Set<List<B>> cartesianProduct(Set<? extends B>... sets) {
return cartesianProduct(Arrays.asList(sets));
}

private static class CartesianSet<B> extends AbstractSet<List<B>> {
final ImmutableList<Axis> axes;
final int size;

CartesianSet(List<? extends Set<? extends B>> sets) {
long dividend = 1L;
ImmutableList.Builder<Axis> builder = ImmutableList.builder();
for (Set<? extends B> set : sets) {
Axis axis = new Axis(set, (int)dividend);
builder.add(axis);
dividend *= axis.size();
Preconditions.checkArgument((dividend <= 2147483647L), "cartesian product is too big");
} 

this.axes = builder.build();
this.size = Ints.checkedCast(dividend);
}

public int size() {
return this.size;
}

public UnmodifiableIterator<List<B>> iterator() {
return new UnmodifiableIterator<List<B>>()
{
int index;

public boolean hasNext() {
return (this.index < Sets.CartesianSet.this.size);
}

public List<B> next() {

}
};
}

public boolean contains(Object element) {
if (!(element instanceof List)) {
return false;
}
List<?> tuple = (List)element;
int dimensions = this.axes.size();
if (tuple.size() != dimensions) {
return false;
}
for (int i = 0; i < dimensions; i++) {
if (!((Axis)this.axes.get(i)).contains(tuple.get(i))) {
return false;
}
} 
return true;
}

public boolean equals(@Nullable Object object) {
if (object instanceof CartesianSet) {
CartesianSet<?> that = (CartesianSet)object;
return this.axes.equals(that.axes);
} 
return super.equals(object);
}

public int hashCode() {
int adjust = this.size - 1;
for (int i = 0; i < this.axes.size(); i++) {
adjust *= 31;
}
return this.axes.hashCode() + adjust;
}

private class Axis {
final ImmutableSet<? extends B> choices;
final ImmutableList<? extends B> choicesList;
final int dividend;

Axis(Set<? extends B> set, int dividend) {
this.choices = ImmutableSet.copyOf(set);
this.choicesList = this.choices.asList();
this.dividend = dividend;
}

int size() {
return this.choices.size();
}

B getForIndex(int index) {
return this.choicesList.get(index / this.dividend % size());
}

boolean contains(Object target) {
return this.choices.contains(target);
}

public boolean equals(Object obj) {
if (obj instanceof Axis) {
Axis that = (Axis)obj;
return this.choices.equals(that.choices);
} 

return false;
}

public int hashCode() {
return Sets.CartesianSet.this.size / this.choices.size() * this.choices.hashCode();
}
}
}

@GwtCompatible(serializable = false)
public static <E> Set<Set<E>> powerSet(Set<E> set) {
ImmutableSet<E> input = ImmutableSet.copyOf(set);
Preconditions.checkArgument((input.size() <= 30), "Too many elements to create power set: %s > 30", new Object[] { Integer.valueOf(input.size()) });

return new PowerSet<E>(input);
}

private static final class PowerSet<E> extends AbstractSet<Set<E>> {
final ImmutableSet<E> inputSet;
final ImmutableList<E> inputList;
final int powerSetSize;

PowerSet(ImmutableSet<E> input) {
this.inputSet = input;
this.inputList = input.asList();
this.powerSetSize = 1 << input.size();
}

public int size() {
return this.powerSetSize;
}

public boolean isEmpty() {
return false;
}

public Iterator<Set<E>> iterator() {
return (Iterator)new AbstractIndexedListIterator<Set<Set<E>>>(this.powerSetSize) {
protected Set<E> get(final int setBits) {
return new AbstractSet<E>() {
public int size() {
return Integer.bitCount(setBits);
}
public Iterator<E> iterator() {
return new Sets.PowerSet.BitFilteredSetIterator<E>(Sets.PowerSet.this.inputList, setBits);
}
};
}
};
}

private static final class BitFilteredSetIterator<E>
extends UnmodifiableIterator<E> {
final ImmutableList<E> input;
int remainingSetBits;

BitFilteredSetIterator(ImmutableList<E> input, int allSetBits) {
this.input = input;
this.remainingSetBits = allSetBits;
}

public boolean hasNext() {
return (this.remainingSetBits != 0);
}

public E next() {
int index = Integer.numberOfTrailingZeros(this.remainingSetBits);
if (index == 32) {
throw new NoSuchElementException();
}

int currentElementMask = 1 << index;
this.remainingSetBits &= currentElementMask ^ 0xFFFFFFFF;
return this.input.get(index);
}
}

public boolean contains(@Nullable Object obj) {
if (obj instanceof Set) {
Set<?> set = (Set)obj;
return this.inputSet.containsAll(set);
} 
return false;
}

public boolean equals(@Nullable Object obj) {
if (obj instanceof PowerSet) {
PowerSet<?> that = (PowerSet)obj;
return this.inputSet.equals(that.inputSet);
} 
return super.equals(obj);
}

public int hashCode() {
return this.inputSet.hashCode() << this.inputSet.size() - 1;
}

public String toString() {
return "powerSet(" + this.inputSet + ")";
}
}

static int hashCodeImpl(Set<?> s) {
int hashCode = 0;
for (Object o : s) {
hashCode += (o != null) ? o.hashCode() : 0;
}
return hashCode;
}

static boolean equalsImpl(Set<?> s, @Nullable Object object) {
if (s == object) {
return true;
}
if (object instanceof Set) {
Set<?> o = (Set)object;

try {
return (s.size() == o.size() && s.containsAll(o));
} catch (NullPointerException ignored) {
return false;
} catch (ClassCastException ignored) {
return false;
} 
} 
return false;
}

static <A, B> Set<B> transform(Set<A> set, InvertibleFunction<A, B> bijection) {
return new TransformedSet<Object, B>((Set)Preconditions.checkNotNull(set, "set"), (InvertibleFunction<?, B>)Preconditions.checkNotNull(bijection, "bijection"));
}

static abstract class InvertibleFunction<A, B>
implements Function<A, B>
{
abstract A invert(B param1B);

public InvertibleFunction<B, A> inverse() {
return new InvertibleFunction<B, A>() {
public A apply(B b) {
return Sets.InvertibleFunction.this.invert(b);
}

B invert(A a) {
return (B)Sets.InvertibleFunction.this.apply(a);
}

public Sets.InvertibleFunction<A, B> inverse() {
return Sets.InvertibleFunction.this;
}
};
}
}

private static class TransformedSet<A, B> extends AbstractSet<B> {
final Set<A> delegate;
final Sets.InvertibleFunction<A, B> bijection;

TransformedSet(Set<A> delegate, Sets.InvertibleFunction<A, B> bijection) {
this.delegate = delegate;
this.bijection = bijection;
}

public Iterator<B> iterator() {
return Iterators.transform(this.delegate.iterator(), this.bijection);
}

public int size() {
return this.delegate.size();
}

public boolean contains(Object o) {
B b = (B)o;
A a = this.bijection.invert(b);

return (this.delegate.contains(a) && Objects.equal(this.bijection.apply(a), o));
}

public boolean add(B b) {
return this.delegate.add(this.bijection.invert(b));
}

public boolean remove(Object o) {
return (contains(o) && this.delegate.remove(this.bijection.invert((B)o)));
}

public void clear() {
this.delegate.clear();
}
}
}

