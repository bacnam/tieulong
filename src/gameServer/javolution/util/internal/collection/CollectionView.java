package javolution.util.internal.collection;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javolution.util.FastCollection;
import javolution.util.function.Consumer;
import javolution.util.function.Equalities;
import javolution.util.function.Equality;
import javolution.util.service.CollectionService;

public abstract class CollectionView<E>
extends FastCollection<E>
implements CollectionService<E>
{
private static final long serialVersionUID = 1536L;
private CollectionService<E> target;

public CollectionView(CollectionService<E> target) {
this.target = target;
}

public boolean addAll(Collection<? extends E> c) {
boolean changed = false;
Iterator<? extends E> it = c.iterator();
while (it.hasNext()) {
if (add(it.next())) changed = true; 
} 
return changed;
}

public void clear() {
Iterator<? extends E> it = iterator();
while (it.hasNext()) {
it.next();
it.remove();
} 
}

public CollectionView<E> clone() {
try {
CollectionView<E> copy = (CollectionView<E>)super.clone();
if (this.target != null) {
copy.target = this.target.clone();
}
return copy;
} catch (CloneNotSupportedException e) {
throw new Error("Should not happen since target is cloneable");
} 
}

public boolean contains(Object obj) {
Iterator<? extends E> it = iterator();
Equality<Object> cmp = (Equality)comparator();
while (it.hasNext()) {
if (cmp.areEqual(obj, it.next())) return true; 
} 
return false;
}

public boolean containsAll(Collection<?> c) {
for (Object e : c) {
if (!contains(e)) return false; 
} 
return true;
}

public boolean equals(Object o) {
if (this == o) return true;

if (o instanceof CollectionService)
{ if (!comparator().equals(((CollectionService)o).comparator())) return false;
}
else if (!comparator().equals(Equalities.STANDARD)) { return false; }

if (this instanceof Set) {
if (!(o instanceof Set)) return false; 
Set<E> set = (Set<E>)o;
return (size() == set.size() && containsAll(set));
}  if (this instanceof List) {
if (!(o instanceof List)) return false; 
List<E> list = (List<E>)o;
if (size() != list.size()) return false; 
Equality<? super E> cmp = comparator();
Iterator<E> it1 = iterator();
Iterator<E> it2 = list.iterator();
while (it1.hasNext()) {
if (!it2.hasNext()) return false; 
if (!cmp.areEqual(it1.next(), it2.next())) return false; 
} 
if (it2.hasNext()) return false; 
return true;
} 
return false;
}

public int hashCode() {
Equality<? super E> cmp = comparator();
Iterator<E> it = iterator();
int hash = 0;
if (this instanceof Set) {
while (it.hasNext()) {
hash += cmp.hashCodeOf(it.next());
}
} else if (this instanceof List) {
while (it.hasNext()) {
hash += 31 * hash + cmp.hashCodeOf(it.next());
}
} else {
hash = super.hashCode();
} 
return hash;
}

public boolean isEmpty() {
return iterator().hasNext();
}

public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
if (this.target == null) {
action.accept(view);
} else {
this.target.perform(action, view);
} 
}

public boolean remove(Object obj) {
Iterator<? extends E> it = iterator();
Equality<Object> cmp = (Equality)comparator();
while (it.hasNext()) {
if (cmp.areEqual(obj, it.next())) {
it.remove();
return true;
} 
} 
return false;
}

public boolean removeAll(Collection<?> c) {
boolean changed = false;
Iterator<? extends E> it = iterator();
while (it.hasNext()) {
if (c.contains(it.next())) {
it.remove();
changed = true;
} 
} 
return changed;
}

public boolean retainAll(Collection<?> c) {
boolean changed = false;
Iterator<? extends E> it = iterator();
while (it.hasNext()) {
if (!c.contains(it.next())) {
it.remove();
changed = true;
} 
} 
return changed;
}

public int size() {
int count = 0;
Iterator<? extends E> it = iterator();
while (it.hasNext()) {
count++;
it.next();
} 
return count;
}

public CollectionService<E>[] split(int n) {
if (this.target == null) return (CollectionService<E>[])new CollectionService[] { this }; 
CollectionService[] arrayOfCollectionService1 = (CollectionService[])this.target.split(n);
CollectionService[] arrayOfCollectionService2 = new CollectionService[arrayOfCollectionService1.length];
for (int i = 0; i < arrayOfCollectionService1.length; i++) {
CollectionView<E> copy = clone();
copy.target = arrayOfCollectionService1[i];
arrayOfCollectionService2[i] = copy;
} 
return (CollectionService<E>[])arrayOfCollectionService2;
}

public CollectionService<E> threadSafe() {
return new SharedCollectionImpl<E>(this);
}

public Object[] toArray() {
return toArray(new Object[size()]);
}

public <T> T[] toArray(T[] a) {
int size = size();
T[] result = (size <= a.length) ? a : (T[])Array.newInstance(a.getClass().getComponentType(), size);

int i = 0;
Iterator<E> it = iterator();
while (it.hasNext()) {
result[i++] = (T)it.next();
}
if (result.length > size) {
result[size] = null;
}
return result;
}

public void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
if (this.target == null) {
action.accept(view);
} else {
this.target.perform(action, view);
} 
}

protected CollectionService<E> service() {
return this;
}

protected CollectionService<E> target() {
return this.target;
}

public abstract boolean add(E paramE);

public abstract Equality<? super E> comparator();

public abstract Iterator<E> iterator();
}

