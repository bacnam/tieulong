package javolution.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import javolution.lang.Immutable;
import javolution.lang.Parallelizable;
import javolution.lang.Realtime;
import javolution.text.Cursor;
import javolution.text.DefaultTextFormat;
import javolution.text.TextContext;
import javolution.text.TextFormat;
import javolution.util.function.Consumer;
import javolution.util.function.Equality;
import javolution.util.function.Function;
import javolution.util.function.Predicate;
import javolution.util.function.Reducer;
import javolution.util.function.Reducers;
import javolution.util.internal.collection.AtomicCollectionImpl;
import javolution.util.internal.collection.DistinctCollectionImpl;
import javolution.util.internal.collection.FilteredCollectionImpl;
import javolution.util.internal.collection.MappedCollectionImpl;
import javolution.util.internal.collection.ParallelCollectionImpl;
import javolution.util.internal.collection.ReversedCollectionImpl;
import javolution.util.internal.collection.SequentialCollectionImpl;
import javolution.util.internal.collection.SharedCollectionImpl;
import javolution.util.internal.collection.SortedCollectionImpl;
import javolution.util.internal.collection.UnmodifiableCollectionImpl;
import javolution.util.service.CollectionService;

@Realtime
@DefaultTextFormat(FastCollection.Format.class)
public abstract class FastCollection<E>
implements Collection<E>, Serializable
{
private static final long serialVersionUID = 1536L;

@Parallelizable(mutexFree = true, comment = "Except for write operations, all read operations are mutex-free.")
public FastCollection<E> atomic() {
return (FastCollection<E>)new AtomicCollectionImpl(service());
}

@Parallelizable(mutexFree = false, comment = "Use multiple-readers/single-writer lock.")
public FastCollection<E> shared() {
return (FastCollection<E>)new SharedCollectionImpl(service());
}

public FastCollection<E> parallel() {
return (FastCollection<E>)new ParallelCollectionImpl(service());
}

public FastCollection<E> sequential() {
return (FastCollection<E>)new SequentialCollectionImpl(service());
}

public FastCollection<E> unmodifiable() {
return (FastCollection<E>)new UnmodifiableCollectionImpl(service());
}

public FastCollection<E> filtered(Predicate<? super E> filter) {
return (FastCollection<E>)new FilteredCollectionImpl(service(), filter);
}

public <R> FastCollection<R> mapped(Function<? super E, ? extends R> function) {
return (FastCollection<R>)new MappedCollectionImpl(service(), function);
}

public FastCollection<E> sorted() {
return (FastCollection<E>)new SortedCollectionImpl(service(), (Comparator)comparator());
}

public FastCollection<E> sorted(Comparator<? super E> cmp) {
return (FastCollection<E>)new SortedCollectionImpl(service(), cmp);
}

public FastCollection<E> reversed() {
return (FastCollection<E>)new ReversedCollectionImpl(service());
}

public FastCollection<E> distinct() {
return (FastCollection<E>)new DistinctCollectionImpl(service());
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void perform(Consumer<? extends Collection<E>> action) {
service().perform(action, service());
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void update(Consumer<? extends Collection<E>> action) {
service().update(action, service());
}

@Realtime(limit = Realtime.Limit.LINEAR)
public void forEach(final Consumer<? super E> consumer) {
perform(new Consumer<Collection<E>>() {
public void accept(Collection<E> view) {
Iterator<E> it = view.iterator();
while (it.hasNext()) {
consumer.accept(it.next());
}
}
});
}

@Realtime(limit = Realtime.Limit.LINEAR)
public boolean removeIf(final Predicate<? super E> filter) {
final boolean[] removed = new boolean[1];
update(new Consumer<Collection<E>>() {
public void accept(Collection<E> view) {
Iterator<E> it = view.iterator();
while (it.hasNext()) {
if (filter.test(it.next())) {
it.remove();
removed[0] = true;
} 
} 
}
});
return removed[0];
}

@Realtime(limit = Realtime.Limit.LINEAR)
public E reduce(Reducer<E> reducer) {
perform((Consumer)reducer);
return (E)reducer.get();
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "May have to search the whole collection (e.g. distinct view).")
public boolean add(E element) {
return service().add(element);
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "May actually iterates the whole collection (e.g. filtered view).")
public boolean isEmpty() {
return iterator().hasNext();
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially counts the elements.")
public int size() {
return service().size();
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially removes elements one at a time.")
public void clear() {
service().clear();
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially searches the whole collection.")
public boolean contains(Object searched) {
return service().contains(searched);
}

@Realtime(limit = Realtime.Limit.LINEAR, comment = "Potentially searches the whole collection.")
public boolean remove(Object searched) {
return service().remove(searched);
}

@Realtime(limit = Realtime.Limit.N_SQUARE, comment = "Construction of the iterator may require sorting the elements (e.g. sorted view)")
public Iterator<E> iterator() {
return service().iterator();
}

@Realtime(limit = Realtime.Limit.LINEAR)
public boolean addAll(Collection<? extends E> that) {
return service().addAll(that);
}

@Realtime(limit = Realtime.Limit.N_SQUARE)
public boolean containsAll(Collection<?> that) {
return service().containsAll(that);
}

@Realtime(limit = Realtime.Limit.N_SQUARE)
public boolean removeAll(Collection<?> that) {
return service().removeAll(that);
}

@Realtime(limit = Realtime.Limit.N_SQUARE)
public boolean retainAll(Collection<?> that) {
return service().retainAll(that);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public Object[] toArray() {
return service().toArray();
}

@Realtime(limit = Realtime.Limit.LINEAR)
public <T> T[] toArray(T[] array) {
return (T[])service().toArray((Object[])array);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public <T extends E> T any(Class<T> type) {
return (T)reduce(Reducers.any(type));
}

@Realtime(limit = Realtime.Limit.LINEAR)
public E min() {
return reduce(Reducers.min((Comparator)comparator()));
}

@Realtime(limit = Realtime.Limit.LINEAR)
public E max() {
return reduce(Reducers.max((Comparator)comparator()));
}

@Realtime(limit = Realtime.Limit.LINEAR)
public FastCollection<E> addAll(E... elements) {
for (E e : elements) {
add(e);
}
return this;
}

@Realtime(limit = Realtime.Limit.LINEAR)
public FastCollection<E> addAll(FastCollection<? extends E> that) {
addAll(that);
return this;
}

@Realtime(limit = Realtime.Limit.CONSTANT)
public Equality<? super E> comparator() {
return service().comparator();
}

@Realtime(limit = Realtime.Limit.CONSTANT)
public <T extends Collection<E>> Immutable<T> toImmutable() {
return new Immutable<T>() {
final T value = (T)FastCollection.this.unmodifiable();

public T value() {
return this.value;
}
};
}

@Realtime(limit = Realtime.Limit.LINEAR)
public boolean equals(Object obj) {
return service().equals(obj);
}

@Realtime(limit = Realtime.Limit.LINEAR)
public int hashCode() {
return service().hashCode();
}

@Realtime(limit = Realtime.Limit.LINEAR)
public String toString() {
return TextContext.getFormat(FastCollection.class).format(this);
}

protected abstract CollectionService<E> service();

protected static <E> CollectionService<E> serviceOf(FastCollection<E> collection) {
return collection.service();
}

@Parallelizable
public static class Format
extends TextFormat<FastCollection<?>>
{
public FastCollection<Object> parse(CharSequence csq, Cursor cursor) throws IllegalArgumentException {
throw new UnsupportedOperationException();
}

public Appendable format(FastCollection<?> that, Appendable dest) throws IOException {
dest.append('[');
Class<?> elementType = null;
TextFormat<Object> format = null;
for (Object element : that) {
if (elementType == null) { elementType = Void.class; }
else { dest.append(", "); }
if (element == null) {
dest.append("null");
continue;
} 
Class<?> cls = element.getClass();
if (elementType.equals(cls)) {
format.format(element, dest);
continue;
} 
elementType = cls;
format = TextContext.getFormat(cls);
format.format(element, dest);
} 
return dest.append(']');
}
}
}

