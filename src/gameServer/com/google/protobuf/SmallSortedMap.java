

package com.google.protobuf;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {

  static <FieldDescriptorType extends
      FieldSet.FieldDescriptorLite<FieldDescriptorType>>
      SmallSortedMap<FieldDescriptorType, Object> newFieldMap(int arraySize) {
    return new SmallSortedMap<FieldDescriptorType, Object>(arraySize) {
      @Override
      @SuppressWarnings("unchecked")
      public void makeImmutable() {
        if (!isImmutable()) {
          for (int i = 0; i < getNumArrayEntries(); i++) {
            final Map.Entry<FieldDescriptorType, Object> entry =
                getArrayEntryAt(i);
            if (entry.getKey().isRepeated()) {
              final List value = (List) entry.getValue();
              entry.setValue(Collections.unmodifiableList(value));
            }
          }
          for (Map.Entry<FieldDescriptorType, Object> entry :
                   getOverflowEntries()) {
            if (entry.getKey().isRepeated()) {
              final List value = (List) entry.getValue();
              entry.setValue(Collections.unmodifiableList(value));
            }
          }
        }
        super.makeImmutable();
      }
    };
  }

  static <K extends Comparable<K>, V> SmallSortedMap<K, V> newInstanceForTest(
      int arraySize) {
    return new SmallSortedMap<K, V>(arraySize);
  }

  private final int maxArraySize;

  private List<Entry> entryList;
  private Map<K, V> overflowEntries;
  private boolean isImmutable;

  private volatile EntrySet lazyEntrySet;

  private SmallSortedMap(int arraySize) {
    this.maxArraySize = arraySize;
    this.entryList = Collections.emptyList();
    this.overflowEntries = Collections.emptyMap();
  }

  public void makeImmutable() {
    if (!isImmutable) {

      overflowEntries = overflowEntries.isEmpty() ?
          Collections.<K, V>emptyMap() :
          Collections.unmodifiableMap(overflowEntries);
      isImmutable = true;
    }
  }

  public boolean isImmutable() {
    return isImmutable;
  }

  public int getNumArrayEntries() {
    return entryList.size();
  }

  public Map.Entry<K, V> getArrayEntryAt(int index) {
    return entryList.get(index);
  }

  public int getNumOverflowEntries() {
    return overflowEntries.size();
  }

  public Iterable<Map.Entry<K, V>> getOverflowEntries() {
    return overflowEntries.isEmpty() ?
        EmptySet.<Map.Entry<K, V>>iterable() :
        overflowEntries.entrySet();
  }

  @Override
  public int size() {
    return entryList.size() + overflowEntries.size();
  }

  @Override
  public boolean containsKey(Object o) {
    @SuppressWarnings("unchecked")
    final K key = (K) o;
    return binarySearchInArray(key) >= 0 || overflowEntries.containsKey(key);
  }

  @Override
  public V get(Object o) {
    @SuppressWarnings("unchecked")
    final K key = (K) o;
    final int index = binarySearchInArray(key);
    if (index >= 0) {
      return entryList.get(index).getValue();
    }
    return overflowEntries.get(key);
  }

  @Override
  public V put(K key, V value) {
    checkMutable();
    final int index = binarySearchInArray(key);
    if (index >= 0) {

      return entryList.get(index).setValue(value);
    }
    ensureEntryArrayMutable();
    final int insertionPoint = -(index + 1);
    if (insertionPoint >= maxArraySize) {

      return getOverflowEntriesMutable().put(key, value);
    }

    if (entryList.size() == maxArraySize) {

      final Entry lastEntryInArray = entryList.remove(maxArraySize - 1);
      getOverflowEntriesMutable().put(lastEntryInArray.getKey(),
                                      lastEntryInArray.getValue());
    }
    entryList.add(insertionPoint, new Entry(key, value));
    return null;
  }

  @Override
  public void clear() {
    checkMutable();
    if (!entryList.isEmpty()) {
      entryList.clear();
    }
    if (!overflowEntries.isEmpty()) {
      overflowEntries.clear();
    }
  }

  @Override
  public V remove(Object o) {
    checkMutable();
    @SuppressWarnings("unchecked")
    final K key = (K) o;
    final int index = binarySearchInArray(key);
    if (index >= 0) {
      return removeArrayEntryAt(index);
    }

    if (overflowEntries.isEmpty()) {
      return null;
    } else {
      return overflowEntries.remove(key);
    }
  }

  private V removeArrayEntryAt(int index) {
    checkMutable();
    final V removed = entryList.remove(index).getValue();
    if (!overflowEntries.isEmpty()) {

      final Iterator<Map.Entry<K, V>> iterator =
          getOverflowEntriesMutable().entrySet().iterator();
      entryList.add(new Entry(iterator.next()));
      iterator.remove();
    }
    return removed;
  }

  private int binarySearchInArray(K key) {
    int left = 0;
    int right = entryList.size() - 1;

    if (right >= 0) {
      int cmp = key.compareTo(entryList.get(right).getKey());
      if (cmp > 0) {
        return -(right + 2);  
      } else if (cmp == 0) {
        return right;
      }
    }

    while (left <= right) {
      int mid = (left + right) / 2;
      int cmp = key.compareTo(entryList.get(mid).getKey());
      if (cmp < 0) {
        right = mid - 1;
      } else if (cmp > 0) {
        left = mid + 1;
      } else {
        return mid;
      }
    }
    return -(left + 1);
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    if (lazyEntrySet == null) {
      lazyEntrySet = new EntrySet();
    }
    return lazyEntrySet;
  }

  private void checkMutable() {
    if (isImmutable) {
      throw new UnsupportedOperationException();
    }
  }

  @SuppressWarnings("unchecked")
  private SortedMap<K, V> getOverflowEntriesMutable() {
    checkMutable();
    if (overflowEntries.isEmpty() && !(overflowEntries instanceof TreeMap)) {
      overflowEntries = new TreeMap<K, V>();
    }
    return (SortedMap<K, V>) overflowEntries;
  }

  private void ensureEntryArrayMutable() {
    checkMutable();
    if (entryList.isEmpty() && !(entryList instanceof ArrayList)) {
      entryList = new ArrayList<Entry>(maxArraySize);
    }
  }

  private class Entry implements Map.Entry<K, V>, Comparable<Entry> {

    private final K key;
    private V value;

    Entry(Map.Entry<K, V> copy) {
      this(copy.getKey(), copy.getValue());
    }

    Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    public int compareTo(Entry other) {
      return getKey().compareTo(other.getKey());
    }

    public V setValue(V newValue) {
      checkMutable();
      final V oldValue = this.value;
      this.value = newValue;
      return oldValue;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      @SuppressWarnings("unchecked")
      Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
      return equals(key, other.getKey()) && equals(value, other.getValue());
    }

    @Override
    public int hashCode() {
      return (key == null ? 0 : key.hashCode()) ^
          (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString() {
      return key + "=" + value;
    }

    private boolean equals(Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals(o2);
    }
  }

  private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
      return new EntryIterator();
    }

    @Override
    public int size() {
      return SmallSortedMap.this.size();
    }

    @Override
    public boolean contains(Object o) {
      @SuppressWarnings("unchecked")
      final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
      final V existing = get(entry.getKey());
      final V value = entry.getValue();
      return existing == value ||
          (existing != null && existing.equals(value));
    }

    @Override
    public boolean add(Map.Entry<K, V> entry) {
      if (!contains(entry)) {
        put(entry.getKey(), entry.getValue());
        return true;
      }
      return false;
    }

    @Override
    public boolean remove(Object o) {
      @SuppressWarnings("unchecked")
      final Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
      if (contains(entry)) {
        SmallSortedMap.this.remove(entry.getKey());
        return true;
      }
      return false;
    }

    @Override
    public void clear() {
      SmallSortedMap.this.clear();
    }
  }

  private class EntryIterator implements Iterator<Map.Entry<K, V>> {

    private int pos = -1;
    private boolean nextCalledBeforeRemove;
    private Iterator<Map.Entry<K, V>> lazyOverflowIterator;

    public boolean hasNext() {
      return (pos + 1) < entryList.size() ||
          getOverflowIterator().hasNext();
    }

    public Map.Entry<K, V> next() {
      nextCalledBeforeRemove = true;

      if (++pos < entryList.size()) {
        return entryList.get(pos);
      }
      return getOverflowIterator().next();
    }

    public void remove() {
      if (!nextCalledBeforeRemove) {
        throw new IllegalStateException("remove() was called before next()");
      }
      nextCalledBeforeRemove = false;
      checkMutable();

      if (pos < entryList.size()) {
        removeArrayEntryAt(pos--);
      } else {
        getOverflowIterator().remove();
      }
    }

    private Iterator<Map.Entry<K, V>> getOverflowIterator() {
      if (lazyOverflowIterator == null) {
        lazyOverflowIterator = overflowEntries.entrySet().iterator();
      }
      return lazyOverflowIterator;
    }
  }

  private static class EmptySet {

    private static final Iterator<Object> ITERATOR = new Iterator<Object>() {

      public boolean hasNext() {
        return false;
      }

      public Object next() {
        throw new NoSuchElementException();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };

    private static final Iterable<Object> ITERABLE = new Iterable<Object>() {

      public Iterator<Object> iterator() {
        return ITERATOR;
      }
    };

    @SuppressWarnings("unchecked")
    static <T> Iterable<T> iterable() {
      return (Iterable<T>) ITERABLE;
    }
  }
}
