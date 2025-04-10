package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class Iterables
{
public static <T> Iterable<T> unmodifiableIterable(Iterable<T> iterable) {
Preconditions.checkNotNull(iterable);
if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection)
{
return iterable;
}
return new UnmodifiableIterable<T>(iterable);
}

@Deprecated
public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
return (Iterable<E>)Preconditions.checkNotNull(iterable);
}

private static final class UnmodifiableIterable<T> implements Iterable<T> {
private final Iterable<T> iterable;

private UnmodifiableIterable(Iterable<T> iterable) {
this.iterable = iterable;
}

public Iterator<T> iterator() {
return Iterators.unmodifiableIterator(this.iterable.iterator());
}

public String toString() {
return this.iterable.toString();
}
}

public static int size(Iterable<?> iterable) {
return (iterable instanceof Collection) ? ((Collection)iterable).size() : Iterators.size(iterable.iterator());
}

public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
if (iterable instanceof Collection) {
Collection<?> collection = (Collection)iterable;
try {
return collection.contains(element);
} catch (NullPointerException e) {
return false;
} catch (ClassCastException e) {
return false;
} 
} 
return Iterators.contains(iterable.iterator(), element);
}

public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
return (removeFrom instanceof Collection) ? ((Collection)removeFrom).removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
}

public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
return (removeFrom instanceof Collection) ? ((Collection)removeFrom).retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
}

public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
if (removeFrom instanceof java.util.RandomAccess && removeFrom instanceof List) {
return removeIfFromRandomAccessList((List)removeFrom, (Predicate)Preconditions.checkNotNull(predicate));
}

return Iterators.removeIf(removeFrom.iterator(), predicate);
}

private static <T> boolean removeIfFromRandomAccessList(List<T> list, Predicate<? super T> predicate) {
int from = 0;
int to = 0;

for (; from < list.size(); from++) {
T element = list.get(from);
if (!predicate.apply(element)) {
if (from > to) {
try {
list.set(to, element);
} catch (UnsupportedOperationException e) {
slowRemoveIfForRemainingElements(list, predicate, to, from);
return true;
} 
}
to++;
} 
} 

list.subList(to, list.size()).clear();
return (from != to);
}

private static <T> void slowRemoveIfForRemainingElements(List<T> list, Predicate<? super T> predicate, int to, int from) {
int n;
for (n = list.size() - 1; n > from; n--) {
if (predicate.apply(list.get(n))) {
list.remove(n);
}
} 

for (n = from - 1; n >= to; n--) {
list.remove(n);
}
}

public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
}

public static String toString(Iterable<?> iterable) {
return Iterators.toString(iterable.iterator());
}

public static <T> T getOnlyElement(Iterable<T> iterable) {
return Iterators.getOnlyElement(iterable.iterator());
}

public static <T> T getOnlyElement(Iterable<T> iterable, @Nullable T defaultValue) {
return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
}

@GwtIncompatible("Array.newInstance(Class, int)")
public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
Collection<? extends T> collection = toCollection(iterable);
T[] array = ObjectArrays.newArray(type, collection.size());
return collection.toArray(array);
}

static Object[] toArray(Iterable<?> iterable) {
return toCollection(iterable).toArray();
}

private static <E> Collection<E> toCollection(Iterable<E> iterable) {
return (iterable instanceof Collection) ? (Collection<E>)iterable : Lists.<E>newArrayList(iterable.iterator());
}

public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
if (elementsToAdd instanceof Collection) {
Collection<? extends T> c = Collections2.cast(elementsToAdd);
return addTo.addAll(c);
} 
return Iterators.addAll(addTo, elementsToAdd.iterator());
}

public static int frequency(Iterable<?> iterable, @Nullable Object element) {
if (iterable instanceof Multiset) {
return ((Multiset)iterable).count(element);
}
if (iterable instanceof Set) {
return ((Set)iterable).contains(element) ? 1 : 0;
}
return Iterators.frequency(iterable.iterator(), element);
}

public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
Preconditions.checkNotNull(iterable);
return new Iterable<T>()
{
public Iterator<T> iterator() {
return Iterators.cycle(iterable);
}
public String toString() {
return iterable.toString() + " (cycled)";
}
};
}

public static <T> Iterable<T> cycle(T... elements) {
return cycle(Lists.newArrayList(elements));
}

public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
Preconditions.checkNotNull(a);
Preconditions.checkNotNull(b);
return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b }));
}

public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
Preconditions.checkNotNull(a);
Preconditions.checkNotNull(b);
Preconditions.checkNotNull(c);
return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b, c }));
}

public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
Preconditions.checkNotNull(a);
Preconditions.checkNotNull(b);
Preconditions.checkNotNull(c);
Preconditions.checkNotNull(d);
return concat(Arrays.asList((Iterable<? extends T>[])new Iterable[] { a, b, c, d }));
}

public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
return concat(ImmutableList.copyOf(inputs));
}

public static <T> Iterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
Preconditions.checkNotNull(inputs);
return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
return Iterators.concat(Iterables.iterators(inputs));
}
};
}

private static <T> UnmodifiableIterator<Iterator<? extends T>> iterators(Iterable<? extends Iterable<? extends T>> iterables) {
final Iterator<? extends Iterable<? extends T>> iterableIterator = iterables.iterator();

return new UnmodifiableIterator<Iterator<? extends T>>()
{
public boolean hasNext() {
return iterableIterator.hasNext();
}

public Iterator<? extends T> next() {
return ((Iterable<? extends T>)iterableIterator.next()).iterator();
}
};
}

public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
Preconditions.checkNotNull(iterable);
Preconditions.checkArgument((size > 0));
return new IterableWithToString<List<T>>()
{
public Iterator<List<T>> iterator() {
return Iterators.partition(iterable.iterator(), size);
}
};
}

public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
Preconditions.checkNotNull(iterable);
Preconditions.checkArgument((size > 0));
return new IterableWithToString<List<T>>()
{
public Iterator<List<T>> iterator() {
return Iterators.paddedPartition(iterable.iterator(), size);
}
};
}

public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> predicate) {
Preconditions.checkNotNull(unfiltered);
Preconditions.checkNotNull(predicate);
return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
return Iterators.filter(unfiltered.iterator(), predicate);
}
};
}

@GwtIncompatible("Class.isInstance")
public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> type) {
Preconditions.checkNotNull(unfiltered);
Preconditions.checkNotNull(type);
return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
return Iterators.filter(unfiltered.iterator(), type);
}
};
}

public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
return Iterators.any(iterable.iterator(), predicate);
}

public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
return Iterators.all(iterable.iterator(), predicate);
}

public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
return Iterators.find(iterable.iterator(), predicate);
}

public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate, @Nullable T defaultValue) {
return Iterators.find(iterable.iterator(), predicate, defaultValue);
}

public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
return Iterators.indexOf(iterable.iterator(), predicate);
}

public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
Preconditions.checkNotNull(fromIterable);
Preconditions.checkNotNull(function);
return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
return Iterators.transform(fromIterable.iterator(), function);
}
};
}

public static <T> T get(Iterable<T> iterable, int position) {
Preconditions.checkNotNull(iterable);
if (iterable instanceof List) {
return ((List<T>)iterable).get(position);
}

if (iterable instanceof Collection) {

Collection<T> collection = (Collection<T>)iterable;
Preconditions.checkElementIndex(position, collection.size());
} else {

checkNonnegativeIndex(position);
} 
return Iterators.get(iterable.iterator(), position);
}

private static void checkNonnegativeIndex(int position) {
if (position < 0) {
throw new IndexOutOfBoundsException("position cannot be negative: " + position);
}
}

public static <T> T get(Iterable<T> iterable, int position, @Nullable T defaultValue) {
Preconditions.checkNotNull(iterable);
checkNonnegativeIndex(position);

try {
return get(iterable, position);
} catch (IndexOutOfBoundsException e) {
return defaultValue;
} 
}

public static <T> T getFirst(Iterable<T> iterable, @Nullable T defaultValue) {
return Iterators.getNext(iterable.iterator(), defaultValue);
}

public static <T> T getLast(Iterable<T> iterable) {
if (iterable instanceof List) {
List<T> list = (List<T>)iterable;
if (list.isEmpty()) {
throw new NoSuchElementException();
}
return getLastInNonemptyList(list);
} 

if (iterable instanceof SortedSet) {
SortedSet<T> sortedSet = (SortedSet<T>)iterable;
return sortedSet.last();
} 

return Iterators.getLast(iterable.iterator());
}

public static <T> T getLast(Iterable<T> iterable, @Nullable T defaultValue) {
if (iterable instanceof Collection) {
Collection<T> collection = (Collection<T>)iterable;
if (collection.isEmpty()) {
return defaultValue;
}
} 

if (iterable instanceof List) {
List<T> list = (List<T>)iterable;
return getLastInNonemptyList(list);
} 

if (iterable instanceof SortedSet) {
SortedSet<T> sortedSet = (SortedSet<T>)iterable;
return sortedSet.last();
} 

return Iterators.getLast(iterable.iterator(), defaultValue);
}

private static <T> T getLastInNonemptyList(List<T> list) {
return list.get(list.size() - 1);
}

public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
Preconditions.checkNotNull(iterable);
Preconditions.checkArgument((numberToSkip >= 0), "number to skip cannot be negative");

if (iterable instanceof List) {
final List<T> list = (List<T>)iterable;
return new IterableWithToString<T>()
{
public Iterator<T> iterator()
{
return (numberToSkip >= list.size()) ? Iterators.<T>emptyIterator() : list.subList(numberToSkip, list.size()).iterator();
}
};
} 

return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
final Iterator<T> iterator = iterable.iterator();

Iterators.skip(iterator, numberToSkip);

return new Iterator()
{
boolean atStart = true;

public boolean hasNext() {
return iterator.hasNext();
}

public T next() {
if (!hasNext()) {
throw new NoSuchElementException();
}

try {
return (T)iterator.next();
} finally {
this.atStart = false;
} 
}

public void remove() {
if (this.atStart) {
throw new IllegalStateException();
}
iterator.remove();
}
};
}
};
}

public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
Preconditions.checkNotNull(iterable);
Preconditions.checkArgument((limitSize >= 0), "limit is negative");
return new IterableWithToString<T>()
{
public Iterator<T> iterator() {
return Iterators.limit(iterable.iterator(), limitSize);
}
};
}

public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
if (iterable instanceof Queue) {
return new Iterable<T>()
{
public Iterator<T> iterator() {
return new Iterables.ConsumingQueueIterator<T>((Queue)iterable);
}
};
}

Preconditions.checkNotNull(iterable);

return new Iterable<T>()
{
public Iterator<T> iterator() {
return Iterators.consumingIterator(iterable.iterator());
}
};
}

private static class ConsumingQueueIterator<T> extends AbstractIterator<T> {
private final Queue<T> queue;

private ConsumingQueueIterator(Queue<T> queue) {
this.queue = queue;
}

public T computeNext() {
try {
return this.queue.remove();
} catch (NoSuchElementException e) {
return endOfData();
} 
}
}

@Deprecated
public static <T> Iterable<T> reverse(List<T> list) {
return Lists.reverse(list);
}

public static <T> boolean isEmpty(Iterable<T> iterable) {
return !iterable.iterator().hasNext();
}

static boolean remove(Iterable<?> iterable, @Nullable Object o) {
Iterator<?> i = iterable.iterator();
while (i.hasNext()) {
if (Objects.equal(i.next(), o)) {
i.remove();
return true;
} 
} 
return false;
}

static abstract class IterableWithToString<E> implements Iterable<E> {
public String toString() {
return Iterables.toString(this);
}
}
}

