package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultimap<K, V>
implements Multimap<K, V>, Serializable
{
private transient Map<K, Collection<V>> map;
private transient int totalSize;
private transient Set<K> keySet;
private transient Multiset<K> multiset;
private transient Collection<V> valuesCollection;
private transient Collection<Map.Entry<K, V>> entries;
private transient Map<K, Collection<V>> asMap;
private static final long serialVersionUID = 2447537837011683357L;

protected AbstractMultimap(Map<K, Collection<V>> map) {
Preconditions.checkArgument(map.isEmpty());
this.map = map;
}

final void setMap(Map<K, Collection<V>> map) {
this.map = map;
this.totalSize = 0;
for (Collection<V> values : map.values()) {
Preconditions.checkArgument(!values.isEmpty());
this.totalSize += values.size();
} 
}

Collection<V> createCollection(@Nullable K key) {
return createCollection();
}

Map<K, Collection<V>> backingMap() {
return this.map;
}

public int size() {
return this.totalSize;
}

public boolean isEmpty() {
return (this.totalSize == 0);
}

public boolean containsKey(@Nullable Object key) {
return this.map.containsKey(key);
}

public boolean containsValue(@Nullable Object value) {
for (Collection<V> collection : this.map.values()) {
if (collection.contains(value)) {
return true;
}
} 

return false;
}

public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
Collection<V> collection = this.map.get(key);
return (collection != null && collection.contains(value));
}

public boolean put(@Nullable K key, @Nullable V value) {
Collection<V> collection = getOrCreateCollection(key);

if (collection.add(value)) {
this.totalSize++;
return true;
} 
return false;
}

private Collection<V> getOrCreateCollection(@Nullable K key) {
Collection<V> collection = this.map.get(key);
if (collection == null) {
collection = createCollection(key);
this.map.put(key, collection);
} 
return collection;
}

public boolean remove(@Nullable Object key, @Nullable Object value) {
Collection<V> collection = this.map.get(key);
if (collection == null) {
return false;
}

boolean changed = collection.remove(value);
if (changed) {
this.totalSize--;
if (collection.isEmpty()) {
this.map.remove(key);
}
} 
return changed;
}

public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
if (!values.iterator().hasNext()) {
return false;
}
Collection<V> collection = getOrCreateCollection(key);
int oldSize = collection.size();

boolean changed = false;
if (values instanceof Collection) {
Collection<? extends V> c = Collections2.cast(values);
changed = collection.addAll(c);
} else {
for (V value : values) {
changed |= collection.add(value);
}
} 

this.totalSize += collection.size() - oldSize;
return changed;
}

public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
boolean changed = false;
for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
changed |= put(entry.getKey(), entry.getValue());
}
return changed;
}

public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
Iterator<? extends V> iterator = values.iterator();
if (!iterator.hasNext()) {
return removeAll(key);
}

Collection<V> collection = getOrCreateCollection(key);
Collection<V> oldValues = createCollection();
oldValues.addAll(collection);

this.totalSize -= collection.size();
collection.clear();

while (iterator.hasNext()) {
if (collection.add(iterator.next())) {
this.totalSize++;
}
} 

return unmodifiableCollectionSubclass(oldValues);
}

public Collection<V> removeAll(@Nullable Object key) {
Collection<V> collection = this.map.remove(key);
Collection<V> output = createCollection();

if (collection != null) {
output.addAll(collection);
this.totalSize -= collection.size();
collection.clear();
} 

return unmodifiableCollectionSubclass(output);
}

private Collection<V> unmodifiableCollectionSubclass(Collection<V> collection) {
if (collection instanceof SortedSet)
return Collections.unmodifiableSortedSet((SortedSet<V>)collection); 
if (collection instanceof Set)
return Collections.unmodifiableSet((Set<? extends V>)collection); 
if (collection instanceof List) {
return Collections.unmodifiableList((List<? extends V>)collection);
}
return Collections.unmodifiableCollection(collection);
}

public void clear() {
for (Collection<V> collection : this.map.values()) {
collection.clear();
}
this.map.clear();
this.totalSize = 0;
}

public Collection<V> get(@Nullable K key) {
Collection<V> collection = this.map.get(key);
if (collection == null) {
collection = createCollection(key);
}
return wrapCollection(key, collection);
}

private Collection<V> wrapCollection(@Nullable K key, Collection<V> collection) {
if (collection instanceof SortedSet)
return new WrappedSortedSet(key, (SortedSet<V>)collection, null); 
if (collection instanceof Set)
return new WrappedSet(key, (Set<V>)collection); 
if (collection instanceof List) {
return wrapList(key, (List<V>)collection, null);
}
return new WrappedCollection(key, collection, null);
}

private List<V> wrapList(@Nullable K key, List<V> list, @Nullable WrappedCollection ancestor) {
return (list instanceof RandomAccess) ? new RandomAccessWrappedList(key, list, ancestor) : new WrappedList(key, list, ancestor);
}

private class WrappedCollection
extends AbstractCollection<V>
{
final K key;

Collection<V> delegate;

final WrappedCollection ancestor;

final Collection<V> ancestorDelegate;

WrappedCollection(K key, @Nullable Collection<V> delegate, WrappedCollection ancestor) {
this.key = key;
this.delegate = delegate;
this.ancestor = ancestor;
this.ancestorDelegate = (ancestor == null) ? null : ancestor.getDelegate();
}

void refreshIfEmpty() {
if (this.ancestor != null) {
this.ancestor.refreshIfEmpty();
if (this.ancestor.getDelegate() != this.ancestorDelegate) {
throw new ConcurrentModificationException();
}
} else if (this.delegate.isEmpty()) {
Collection<V> newDelegate = (Collection<V>)AbstractMultimap.this.map.get(this.key);
if (newDelegate != null) {
this.delegate = newDelegate;
}
} 
}

void removeIfEmpty() {
if (this.ancestor != null) {
this.ancestor.removeIfEmpty();
} else if (this.delegate.isEmpty()) {
AbstractMultimap.this.map.remove(this.key);
} 
}

K getKey() {
return this.key;
}

void addToMap() {
if (this.ancestor != null) {
this.ancestor.addToMap();
} else {
AbstractMultimap.this.map.put(this.key, this.delegate);
} 
}

public int size() {
refreshIfEmpty();
return this.delegate.size();
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
refreshIfEmpty();
return this.delegate.equals(object);
}

public int hashCode() {
refreshIfEmpty();
return this.delegate.hashCode();
}

public String toString() {
refreshIfEmpty();
return this.delegate.toString();
}

Collection<V> getDelegate() {
return this.delegate;
}

public Iterator<V> iterator() {
refreshIfEmpty();
return new WrappedIterator();
}

class WrappedIterator
implements Iterator<V> {
final Iterator<V> delegateIterator;
final Collection<V> originalDelegate = AbstractMultimap.WrappedCollection.this.delegate;

WrappedIterator() {
this.delegateIterator = AbstractMultimap.this.iteratorOrListIterator(AbstractMultimap.WrappedCollection.this.delegate);
}

WrappedIterator(Iterator<V> delegateIterator) {
this.delegateIterator = delegateIterator;
}

void validateIterator() {
AbstractMultimap.WrappedCollection.this.refreshIfEmpty();
if (AbstractMultimap.WrappedCollection.this.delegate != this.originalDelegate) {
throw new ConcurrentModificationException();
}
}

public boolean hasNext() {
validateIterator();
return this.delegateIterator.hasNext();
}

public V next() {
validateIterator();
return this.delegateIterator.next();
}

public void remove() {
this.delegateIterator.remove();
AbstractMultimap.this.totalSize--;
AbstractMultimap.WrappedCollection.this.removeIfEmpty();
}

Iterator<V> getDelegateIterator() {
validateIterator();
return this.delegateIterator;
}
}

public boolean add(V value) {
refreshIfEmpty();
boolean wasEmpty = this.delegate.isEmpty();
boolean changed = this.delegate.add(value);
if (changed) {
AbstractMultimap.this.totalSize++;
if (wasEmpty) {
addToMap();
}
} 
return changed;
}

WrappedCollection getAncestor() {
return this.ancestor;
}

public boolean addAll(Collection<? extends V> collection) {
if (collection.isEmpty()) {
return false;
}
int oldSize = size();
boolean changed = this.delegate.addAll(collection);
if (changed) {
int newSize = this.delegate.size();
AbstractMultimap.this.totalSize += newSize - oldSize;
if (oldSize == 0) {
addToMap();
}
} 
return changed;
}

public boolean contains(Object o) {
refreshIfEmpty();
return this.delegate.contains(o);
}

public boolean containsAll(Collection<?> c) {
refreshIfEmpty();
return this.delegate.containsAll(c);
}

public void clear() {
int oldSize = size();
if (oldSize == 0) {
return;
}
this.delegate.clear();
AbstractMultimap.this.totalSize -= oldSize;
removeIfEmpty();
}

public boolean remove(Object o) {
refreshIfEmpty();
boolean changed = this.delegate.remove(o);
if (changed) {
AbstractMultimap.this.totalSize--;
removeIfEmpty();
} 
return changed;
}

public boolean removeAll(Collection<?> c) {
if (c.isEmpty()) {
return false;
}
int oldSize = size();
boolean changed = this.delegate.removeAll(c);
if (changed) {
int newSize = this.delegate.size();
AbstractMultimap.this.totalSize += newSize - oldSize;
removeIfEmpty();
} 
return changed;
}

public boolean retainAll(Collection<?> c) {
Preconditions.checkNotNull(c);
int oldSize = size();
boolean changed = this.delegate.retainAll(c);
if (changed) {
int newSize = this.delegate.size();
AbstractMultimap.this.totalSize += newSize - oldSize;
removeIfEmpty();
} 
return changed;
}
}

private Iterator<V> iteratorOrListIterator(Collection<V> collection) {
return (collection instanceof List) ? ((List<V>)collection).listIterator() : collection.iterator();
}

private class WrappedSet
extends WrappedCollection
implements Set<V>
{
WrappedSet(K key, Set<V> delegate) {
super(key, delegate, null);
}
}

private class WrappedSortedSet
extends WrappedCollection
implements SortedSet<V>
{
WrappedSortedSet(K key, @Nullable SortedSet<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
super(key, delegate, ancestor);
}

SortedSet<V> getSortedSetDelegate() {
return (SortedSet<V>)getDelegate();
}

public Comparator<? super V> comparator() {
return getSortedSetDelegate().comparator();
}

public V first() {
refreshIfEmpty();
return getSortedSetDelegate().first();
}

public V last() {
refreshIfEmpty();
return getSortedSetDelegate().last();
}

public SortedSet<V> headSet(V toElement) {
refreshIfEmpty();
return new WrappedSortedSet(getKey(), getSortedSetDelegate().headSet(toElement), (getAncestor() == null) ? this : getAncestor());
}

public SortedSet<V> subSet(V fromElement, V toElement) {
refreshIfEmpty();
return new WrappedSortedSet(getKey(), getSortedSetDelegate().subSet(fromElement, toElement), (getAncestor() == null) ? this : getAncestor());
}

public SortedSet<V> tailSet(V fromElement) {
refreshIfEmpty();
return new WrappedSortedSet(getKey(), getSortedSetDelegate().tailSet(fromElement), (getAncestor() == null) ? this : getAncestor());
}
}

private class WrappedList
extends WrappedCollection
implements List<V>
{
WrappedList(K key, @Nullable List<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
super(key, delegate, ancestor);
}

List<V> getListDelegate() {
return (List<V>)getDelegate();
}

public boolean addAll(int index, Collection<? extends V> c) {
if (c.isEmpty()) {
return false;
}
int oldSize = size();
boolean changed = getListDelegate().addAll(index, c);
if (changed) {
int newSize = getDelegate().size();
AbstractMultimap.this.totalSize += newSize - oldSize;
if (oldSize == 0) {
addToMap();
}
} 
return changed;
}

public V get(int index) {
refreshIfEmpty();
return getListDelegate().get(index);
}

public V set(int index, V element) {
refreshIfEmpty();
return getListDelegate().set(index, element);
}

public void add(int index, V element) {
refreshIfEmpty();
boolean wasEmpty = getDelegate().isEmpty();
getListDelegate().add(index, element);
AbstractMultimap.this.totalSize++;
if (wasEmpty) {
addToMap();
}
}

public V remove(int index) {
refreshIfEmpty();
V value = getListDelegate().remove(index);
AbstractMultimap.this.totalSize--;
removeIfEmpty();
return value;
}

public int indexOf(Object o) {
refreshIfEmpty();
return getListDelegate().indexOf(o);
}

public int lastIndexOf(Object o) {
refreshIfEmpty();
return getListDelegate().lastIndexOf(o);
}

public ListIterator<V> listIterator() {
refreshIfEmpty();
return new WrappedListIterator();
}

public ListIterator<V> listIterator(int index) {
refreshIfEmpty();
return new WrappedListIterator(index);
}

public List<V> subList(int fromIndex, int toIndex) {
refreshIfEmpty();
return AbstractMultimap.this.wrapList(getKey(), getListDelegate().subList(fromIndex, toIndex), (getAncestor() == null) ? this : getAncestor());
}

private class WrappedListIterator
extends AbstractMultimap<K, V>.WrappedCollection.WrappedIterator
implements ListIterator<V>
{
WrappedListIterator() {}

public WrappedListIterator(int index) {
super(AbstractMultimap.WrappedList.this.getListDelegate().listIterator(index));
}

private ListIterator<V> getDelegateListIterator() {
return (ListIterator<V>)getDelegateIterator();
}

public boolean hasPrevious() {
return getDelegateListIterator().hasPrevious();
}

public V previous() {
return getDelegateListIterator().previous();
}

public int nextIndex() {
return getDelegateListIterator().nextIndex();
}

public int previousIndex() {
return getDelegateListIterator().previousIndex();
}

public void set(V value) {
getDelegateListIterator().set(value);
}

public void add(V value) {
boolean wasEmpty = AbstractMultimap.WrappedList.this.isEmpty();
getDelegateListIterator().add(value);
AbstractMultimap.this.totalSize++;
if (wasEmpty) {
AbstractMultimap.WrappedList.this.addToMap();
}
}
}
}

private class RandomAccessWrappedList
extends WrappedList
implements RandomAccess
{
RandomAccessWrappedList(K key, @Nullable List<V> delegate, AbstractMultimap<K, V>.WrappedCollection ancestor) {
super(key, delegate, ancestor);
}
}

public Set<K> keySet() {
Set<K> result = this.keySet;
return (result == null) ? (this.keySet = createKeySet()) : result;
}

private Set<K> createKeySet() {
return (this.map instanceof SortedMap) ? new SortedKeySet((SortedMap<K, Collection<V>>)this.map) : new KeySet(this.map);
}

private class KeySet
extends Maps.KeySet<K, Collection<V>>
{
final Map<K, Collection<V>> subMap;

KeySet(Map<K, Collection<V>> subMap) {
this.subMap = subMap;
}

Map<K, Collection<V>> map() {
return this.subMap;
}

public Iterator<K> iterator() {
return new Iterator<K>() {
final Iterator<Map.Entry<K, Collection<V>>> entryIterator = AbstractMultimap.KeySet.this.subMap.entrySet().iterator();

Map.Entry<K, Collection<V>> entry;

public boolean hasNext() {
return this.entryIterator.hasNext();
}

public K next() {
this.entry = this.entryIterator.next();
return this.entry.getKey();
}

public void remove() {
Preconditions.checkState((this.entry != null));
Collection<V> collection = this.entry.getValue();
this.entryIterator.remove();
AbstractMultimap.this.totalSize -= collection.size();
collection.clear();
}
};
}

public boolean remove(Object key) {
int count = 0;
Collection<V> collection = this.subMap.remove(key);
if (collection != null) {
count = collection.size();
collection.clear();
AbstractMultimap.this.totalSize -= count;
} 
return (count > 0);
}

public void clear() {
Iterators.clear(iterator());
}

public boolean containsAll(Collection<?> c) {
return this.subMap.keySet().containsAll(c);
}

public boolean equals(@Nullable Object object) {
return (this == object || this.subMap.keySet().equals(object));
}

public int hashCode() {
return this.subMap.keySet().hashCode();
}
}

private class SortedKeySet
extends KeySet implements SortedSet<K> {
SortedKeySet(SortedMap<K, Collection<V>> subMap) {
super(subMap);
}

SortedMap<K, Collection<V>> sortedMap() {
return (SortedMap<K, Collection<V>>)this.subMap;
}

public Comparator<? super K> comparator() {
return sortedMap().comparator();
}

public K first() {
return sortedMap().firstKey();
}

public SortedSet<K> headSet(K toElement) {
return new SortedKeySet(sortedMap().headMap(toElement));
}

public K last() {
return sortedMap().lastKey();
}

public SortedSet<K> subSet(K fromElement, K toElement) {
return new SortedKeySet(sortedMap().subMap(fromElement, toElement));
}

public SortedSet<K> tailSet(K fromElement) {
return new SortedKeySet(sortedMap().tailMap(fromElement));
}
}

public Multiset<K> keys() {
Multiset<K> result = this.multiset;
if (result == null) {
return this.multiset = new Multimaps.Keys<K, V>() {
Multimap<K, V> multimap() {
return AbstractMultimap.this;
}
};
}
return result;
}

private int removeValuesForKey(Object key) {
Collection<V> collection;
try {
collection = this.map.remove(key);
} catch (NullPointerException e) {
return 0;
} catch (ClassCastException e) {
return 0;
} 

int count = 0;
if (collection != null) {
count = collection.size();
collection.clear();
this.totalSize -= count;
} 
return count;
}

public Collection<V> values() {
Collection<V> result = this.valuesCollection;
if (result == null) {
return this.valuesCollection = new Multimaps.Values<K, V>() {
Multimap<K, V> multimap() {
return AbstractMultimap.this;
}
};
}
return result;
}

public Collection<Map.Entry<K, V>> entries() {
Collection<Map.Entry<K, V>> result = this.entries;
return (result == null) ? (this.entries = createEntries()) : result;
}

Collection<Map.Entry<K, V>> createEntries() {
if (this instanceof SetMultimap) {
return new Multimaps.EntrySet<K, V>() {
Multimap<K, V> multimap() {
return AbstractMultimap.this;
}

public Iterator<Map.Entry<K, V>> iterator() {
return AbstractMultimap.this.createEntryIterator();
}
};
}
return new Multimaps.Entries<K, V>() {
Multimap<K, V> multimap() {
return AbstractMultimap.this;
}

public Iterator<Map.Entry<K, V>> iterator() {
return AbstractMultimap.this.createEntryIterator();
}
};
}

Iterator<Map.Entry<K, V>> createEntryIterator() {
return new EntryIterator();
}

private class EntryIterator
implements Iterator<Map.Entry<K, V>>
{
final Iterator<Map.Entry<K, Collection<V>>> keyIterator = AbstractMultimap.this.map.entrySet().iterator(); K key; EntryIterator() {
if (this.keyIterator.hasNext()) {
findValueIteratorAndKey();
} else {
this.valueIterator = Iterators.emptyModifiableIterator();
} 
}
Collection<V> collection; Iterator<V> valueIterator;
void findValueIteratorAndKey() {
Map.Entry<K, Collection<V>> entry = this.keyIterator.next();
this.key = entry.getKey();
this.collection = entry.getValue();
this.valueIterator = this.collection.iterator();
}

public boolean hasNext() {
return (this.keyIterator.hasNext() || this.valueIterator.hasNext());
}

public Map.Entry<K, V> next() {
if (!this.valueIterator.hasNext()) {
findValueIteratorAndKey();
}
return Maps.immutableEntry(this.key, this.valueIterator.next());
}

public void remove() {
this.valueIterator.remove();
if (this.collection.isEmpty()) {
this.keyIterator.remove();
}
AbstractMultimap.this.totalSize--;
}
}

public Map<K, Collection<V>> asMap() {
Map<K, Collection<V>> result = this.asMap;
return (result == null) ? (this.asMap = createAsMap()) : result;
}

private Map<K, Collection<V>> createAsMap() {
return (this.map instanceof SortedMap) ? new SortedAsMap((SortedMap<K, Collection<V>>)this.map) : new AsMap(this.map);
}

private class AsMap
extends AbstractMap<K, Collection<V>>
{
final transient Map<K, Collection<V>> submap;

transient Set<Map.Entry<K, Collection<V>>> entrySet;

AsMap(Map<K, Collection<V>> submap) {
this.submap = submap;
}

public Set<Map.Entry<K, Collection<V>>> entrySet() {
Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
return (result == null) ? (this.entrySet = new AsMapEntries()) : result;
}

public boolean containsKey(Object key) {
return Maps.safeContainsKey(this.submap, key);
}

public Collection<V> get(Object key) {
Collection<V> collection = Maps.<Collection<V>>safeGet(this.submap, key);
if (collection == null) {
return null;
}

K k = (K)key;
return AbstractMultimap.this.wrapCollection(k, collection);
}

public Set<K> keySet() {
return AbstractMultimap.this.keySet();
}

public int size() {
return this.submap.size();
}

public Collection<V> remove(Object key) {
Collection<V> collection = this.submap.remove(key);
if (collection == null) {
return null;
}

Collection<V> output = AbstractMultimap.this.createCollection();
output.addAll(collection);
AbstractMultimap.this.totalSize -= collection.size();
collection.clear();
return output;
}

public boolean equals(@Nullable Object object) {
return (this == object || this.submap.equals(object));
}

public int hashCode() {
return this.submap.hashCode();
}

public String toString() {
return this.submap.toString();
}

public void clear() {
if (this.submap == AbstractMultimap.this.map) {
AbstractMultimap.this.clear();
} else {

Iterators.clear(new AsMapIterator());
} 
}

class AsMapEntries
extends Maps.EntrySet<K, Collection<V>> {
Map<K, Collection<V>> map() {
return AbstractMultimap.AsMap.this;
}

public Iterator<Map.Entry<K, Collection<V>>> iterator() {
return new AbstractMultimap.AsMap.AsMapIterator();
}

public boolean contains(Object o) {
return Collections2.safeContains(AbstractMultimap.AsMap.this.submap.entrySet(), o);
}

public boolean remove(Object o) {
if (!contains(o)) {
return false;
}
Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
AbstractMultimap.this.removeValuesForKey(entry.getKey());
return true;
}
}

class AsMapIterator
implements Iterator<Map.Entry<K, Collection<V>>> {
final Iterator<Map.Entry<K, Collection<V>>> delegateIterator = AbstractMultimap.AsMap.this.submap.entrySet().iterator();

Collection<V> collection;

public boolean hasNext() {
return this.delegateIterator.hasNext();
}

public Map.Entry<K, Collection<V>> next() {
Map.Entry<K, Collection<V>> entry = this.delegateIterator.next();
K key = entry.getKey();
this.collection = entry.getValue();
return Maps.immutableEntry(key, AbstractMultimap.this.wrapCollection(key, this.collection));
}

public void remove() {
this.delegateIterator.remove();
AbstractMultimap.this.totalSize -= this.collection.size();
this.collection.clear();
} }
}

private class SortedAsMap extends AsMap implements SortedMap<K, Collection<V>> {
SortedSet<K> sortedKeySet;

SortedAsMap(SortedMap<K, Collection<V>> submap) {
super(submap);
}

SortedMap<K, Collection<V>> sortedMap() {
return (SortedMap<K, Collection<V>>)this.submap;
}

public Comparator<? super K> comparator() {
return sortedMap().comparator();
}

public K firstKey() {
return sortedMap().firstKey();
}

public K lastKey() {
return sortedMap().lastKey();
}

public SortedMap<K, Collection<V>> headMap(K toKey) {
return new SortedAsMap(sortedMap().headMap(toKey));
}

public SortedMap<K, Collection<V>> subMap(K fromKey, K toKey) {
return new SortedAsMap(sortedMap().subMap(fromKey, toKey));
}

public SortedMap<K, Collection<V>> tailMap(K fromKey) {
return new SortedAsMap(sortedMap().tailMap(fromKey));
}

public SortedSet<K> keySet() {
SortedSet<K> result = this.sortedKeySet;
return (result == null) ? (this.sortedKeySet = new AbstractMultimap.SortedKeySet(sortedMap())) : result;
}
}

public boolean equals(@Nullable Object object) {
if (object == this) {
return true;
}
if (object instanceof Multimap) {
Multimap<?, ?> that = (Multimap<?, ?>)object;
return this.map.equals(that.asMap());
} 
return false;
}

public int hashCode() {
return this.map.hashCode();
}

public String toString() {
return this.map.toString();
}

abstract Collection<V> createCollection();
}

