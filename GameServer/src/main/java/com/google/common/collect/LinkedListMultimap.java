package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

@GwtCompatible(serializable = true, emulated = true)
public class LinkedListMultimap<K, V>
        implements ListMultimap<K, V>, Serializable {
    @GwtIncompatible("java serialization not supported")
    private static final long serialVersionUID = 0L;
    private transient Node<K, V> head;
    private transient Node<K, V> tail;
    private transient Multiset<K> keyCount;
    private transient Map<K, Node<K, V>> keyToKeyHead;
    private transient Map<K, Node<K, V>> keyToKeyTail;
    private transient Set<K> keySet;
    private transient Multiset<K> keys;
    private transient List<V> valuesList;
    private transient List<Map.Entry<K, V>> entries;
    private transient Map<K, Collection<V>> map;

    LinkedListMultimap() {
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
    }

    private LinkedListMultimap(int expectedKeys) {
        this.keyCount = LinkedHashMultiset.create(expectedKeys);
        this.keyToKeyHead = Maps.newHashMapWithExpectedSize(expectedKeys);
        this.keyToKeyTail = Maps.newHashMapWithExpectedSize(expectedKeys);
    }

    private LinkedListMultimap(Multimap<? extends K, ? extends V> multimap) {
        this(multimap.keySet().size());
        putAll(multimap);
    }

    public static <K, V> LinkedListMultimap<K, V> create() {
        return new LinkedListMultimap<K, V>();
    }

    public static <K, V> LinkedListMultimap<K, V> create(int expectedKeys) {
        return new LinkedListMultimap<K, V>(expectedKeys);
    }

    public static <K, V> LinkedListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
        return new LinkedListMultimap<K, V>(multimap);
    }

    private static void checkElement(@Nullable Object node) {
        if (node == null)
            throw new NoSuchElementException();
    }

    private static <K, V> Map.Entry<K, V> createEntry(final Node<K, V> node) {
        return new AbstractMapEntry<K, V>() {
            public K getKey() {
                return node.key;
            }

            public V getValue() {
                return node.value;
            }

            public V setValue(V value) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }
        };
    }

    private Node<K, V> addNode(@Nullable K key, @Nullable V value, @Nullable Node<K, V> nextSibling) {
        Node<K, V> node = new Node<K, V>(key, value);
        if (this.head == null) {
            this.head = this.tail = node;
            this.keyToKeyHead.put(key, node);
            this.keyToKeyTail.put(key, node);
        } else if (nextSibling == null) {
            this.tail.next = node;
            node.previous = this.tail;
            Node<K, V> keyTail = this.keyToKeyTail.get(key);
            if (keyTail == null) {
                this.keyToKeyHead.put(key, node);
            } else {
                keyTail.nextSibling = node;
                node.previousSibling = keyTail;
            }
            this.keyToKeyTail.put(key, node);
            this.tail = node;
        } else {
            node.previous = nextSibling.previous;
            node.previousSibling = nextSibling.previousSibling;
            node.next = nextSibling;
            node.nextSibling = nextSibling;
            if (nextSibling.previousSibling == null) {
                this.keyToKeyHead.put(key, node);
            } else {
                nextSibling.previousSibling.nextSibling = node;
            }
            if (nextSibling.previous == null) {
                this.head = node;
            } else {
                nextSibling.previous.next = node;
            }
            nextSibling.previous = node;
            nextSibling.previousSibling = node;
        }
        this.keyCount.add(key);
        return node;
    }

    private void removeNode(Node<K, V> node) {
        if (node.previous != null) {
            node.previous.next = node.next;
        } else {
            this.head = node.next;
        }
        if (node.next != null) {
            node.next.previous = node.previous;
        } else {
            this.tail = node.previous;
        }
        if (node.previousSibling != null) {
            node.previousSibling.nextSibling = node.nextSibling;
        } else if (node.nextSibling != null) {
            this.keyToKeyHead.put(node.key, node.nextSibling);
        } else {
            this.keyToKeyHead.remove(node.key);
        }
        if (node.nextSibling != null) {
            node.nextSibling.previousSibling = node.previousSibling;
        } else if (node.previousSibling != null) {
            this.keyToKeyTail.put(node.key, node.previousSibling);
        } else {
            this.keyToKeyTail.remove(node.key);
        }
        this.keyCount.remove(node.key);
    }

    private void removeAllNodes(@Nullable Object key) {
        for (Iterator<V> i = new ValueForKeyIterator(key); i.hasNext(); ) {
            i.next();
            i.remove();
        }
    }

    public int size() {
        return this.keyCount.size();
    }

    public boolean isEmpty() {
        return (this.head == null);
    }

    public boolean containsKey(@Nullable Object key) {
        return this.keyToKeyHead.containsKey(key);
    }

    public boolean containsValue(@Nullable Object value) {
        for (Iterator<Node<K, V>> i = new NodeIterator(); i.hasNext(); ) {
            if (Objects.equal(((Node) i.next()).value, value)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
        for (Iterator<V> i = new ValueForKeyIterator(key); i.hasNext(); ) {
            if (Objects.equal(i.next(), value)) {
                return true;
            }
        }
        return false;
    }

    public boolean put(@Nullable K key, @Nullable V value) {
        addNode(key, value, null);
        return true;
    }

    public boolean remove(@Nullable Object key, @Nullable Object value) {
        Iterator<V> values = new ValueForKeyIterator(key);
        while (values.hasNext()) {
            if (Objects.equal(values.next(), value)) {
                values.remove();
                return true;
            }
        }
        return false;
    }

    public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
        boolean changed = false;
        for (V value : values) {
            changed |= put(key, value);
        }
        return changed;
    }

    public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
        boolean changed = false;
        for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
            changed |= put(entry.getKey(), entry.getValue());
        }
        return changed;
    }

    public List<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
        List<V> oldValues = getCopy(key);
        ListIterator<V> keyValues = new ValueForKeyIterator(key);
        Iterator<? extends V> newValues = values.iterator();

        while (keyValues.hasNext() && newValues.hasNext()) {
            keyValues.next();
            keyValues.set(newValues.next());
        }

        while (keyValues.hasNext()) {
            keyValues.next();
            keyValues.remove();
        }

        while (newValues.hasNext()) {
            keyValues.add(newValues.next());
        }

        return oldValues;
    }

    private List<V> getCopy(@Nullable Object key) {
        return Collections.unmodifiableList(Lists.newArrayList(new ValueForKeyIterator(key)));
    }

    public List<V> removeAll(@Nullable Object key) {
        List<V> oldValues = getCopy(key);
        removeAllNodes(key);
        return oldValues;
    }

    public void clear() {
        this.head = null;
        this.tail = null;
        this.keyCount.clear();
        this.keyToKeyHead.clear();
        this.keyToKeyTail.clear();
    }

    public List<V> get(@Nullable final K key) {
        return new AbstractSequentialList<V>() {
            public int size() {
                return LinkedListMultimap.this.keyCount.count(key);
            }

            public ListIterator<V> listIterator(int index) {
                return new LinkedListMultimap.ValueForKeyIterator(key, index);
            }

            public boolean removeAll(Collection<?> c) {
                return Iterators.removeAll(iterator(), c);
            }

            public boolean retainAll(Collection<?> c) {
                return Iterators.retainAll(iterator(), c);
            }
        };
    }

    public Set<K> keySet() {
        Set<K> result = this.keySet;
        if (result == null) {
            this.keySet = result = new AbstractSet<K>() {
                public int size() {
                    return LinkedListMultimap.this.keyCount.elementSet().size();
                }

                public Iterator<K> iterator() {
                    return new LinkedListMultimap.DistinctKeyIterator();
                }

                public boolean contains(Object key) {
                    return LinkedListMultimap.this.keyCount.contains(key);
                }

                public boolean removeAll(Collection<?> c) {
                    Preconditions.checkNotNull(c);
                    return super.removeAll(c);
                }
            };
        }
        return result;
    }

    public Multiset<K> keys() {
        Multiset<K> result = this.keys;
        if (result == null) {
            this.keys = result = new MultisetView();
        }
        return result;
    }

    public List<V> values() {
        List<V> result = this.valuesList;
        if (result == null) {
            this.valuesList = result = new AbstractSequentialList<V>() {
                public int size() {
                    return LinkedListMultimap.this.keyCount.size();
                }

                public ListIterator<V> listIterator(int index) {
                    final LinkedListMultimap<K, V>.NodeIterator nodes = new LinkedListMultimap.NodeIterator(index);
                    return new ListIterator<V>() {
                        public boolean hasNext() {
                            return nodes.hasNext();
                        }

                        public V next() {
                            return (nodes.next()).value;
                        }

                        public boolean hasPrevious() {
                            return nodes.hasPrevious();
                        }

                        public V previous() {
                            return (nodes.previous()).value;
                        }

                        public int nextIndex() {
                            return nodes.nextIndex();
                        }

                        public int previousIndex() {
                            return nodes.previousIndex();
                        }

                        public void remove() {
                            nodes.remove();
                        }

                        public void set(V e) {
                            nodes.setValue(e);
                        }

                        public void add(V e) {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };
        }
        return result;
    }

    public List<Map.Entry<K, V>> entries() {
        List<Map.Entry<K, V>> result = this.entries;
        if (result == null) {
            this.entries = result = new AbstractSequentialList<Map.Entry<K, V>>() {
                public int size() {
                    return LinkedListMultimap.this.keyCount.size();
                }

                public ListIterator<Map.Entry<K, V>> listIterator(int index) {
                    final ListIterator<LinkedListMultimap.Node<K, V>> nodes = new LinkedListMultimap.NodeIterator(index);
                    return new ListIterator<Map.Entry<K, V>>() {
                        public boolean hasNext() {
                            return nodes.hasNext();
                        }

                        public Map.Entry<K, V> next() {
                            return LinkedListMultimap.createEntry(nodes.next());
                        }

                        public void remove() {
                            nodes.remove();
                        }

                        public boolean hasPrevious() {
                            return nodes.hasPrevious();
                        }

                        public Map.Entry<K, V> previous() {
                            return LinkedListMultimap.createEntry(nodes.previous());
                        }

                        public int nextIndex() {
                            return nodes.nextIndex();
                        }

                        public int previousIndex() {
                            return nodes.previousIndex();
                        }

                        public void set(Map.Entry<K, V> e) {
                            throw new UnsupportedOperationException();
                        }

                        public void add(Map.Entry<K, V> e) {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };
        }
        return result;
    }

    public Map<K, Collection<V>> asMap() {
        Map<K, Collection<V>> result = this.map;
        if (result == null) {
            this.map = result = (Map) new AbstractMap<K, Collection<Collection<Collection<Collection<V>>>>>() {
                Set<Map.Entry<K, Collection<V>>> entrySet;

                public Set<Map.Entry<K, Collection<V>>> entrySet() {
                    Set<Map.Entry<K, Collection<V>>> result = this.entrySet;
                    if (result == null) {
                        this.entrySet = result = new LinkedListMultimap.AsMapEntries();
                    }
                    return result;
                }

                public boolean containsKey(@Nullable Object key) {
                    return LinkedListMultimap.this.containsKey(key);
                }

                public Collection<V> get(@Nullable Object key) {
                    Collection<V> collection = LinkedListMultimap.this.get(key);
                    return collection.isEmpty() ? null : collection;
                }

                public Collection<V> remove(@Nullable Object key) {
                    Collection<V> collection = LinkedListMultimap.this.removeAll(key);
                    return collection.isEmpty() ? null : collection;
                }
            };
        }

        return result;
    }

    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof Multimap) {
            Multimap<?, ?> that = (Multimap<?, ?>) other;
            return asMap().equals(that.asMap());
        }
        return false;
    }

    public int hashCode() {
        return asMap().hashCode();
    }

    public String toString() {
        return asMap().toString();
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(size());
        for (Map.Entry<K, V> entry : entries()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.keyCount = LinkedHashMultiset.create();
        this.keyToKeyHead = Maps.newHashMap();
        this.keyToKeyTail = Maps.newHashMap();
        int size = stream.readInt();
        for (int i = 0; i < size; i++) {

            K key = (K) stream.readObject();

            V value = (V) stream.readObject();
            put(key, value);
        }
    }

    private static final class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;
        Node<K, V> previous;
        Node<K, V> nextSibling;
        Node<K, V> previousSibling;

        Node(@Nullable K key, @Nullable V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return (new StringBuilder()).append(this.key).append("=").append(this.value).toString();
        }
    }

    private class NodeIterator
            implements ListIterator<Node<K, V>> {
        int nextIndex;
        LinkedListMultimap.Node<K, V> next;
        LinkedListMultimap.Node<K, V> current;
        LinkedListMultimap.Node<K, V> previous;

        NodeIterator() {
            this.next = LinkedListMultimap.this.head;
        }

        NodeIterator(int index) {
            int size = LinkedListMultimap.this.size();
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = LinkedListMultimap.this.tail;
                this.nextIndex = size;
                while (index++ < size) {
                    previous();
                }
            } else {
                this.next = LinkedListMultimap.this.head;
                while (index-- > 0) {
                    next();
                }
            }
            this.current = null;
        }

        public boolean hasNext() {
            return (this.next != null);
        }

        public LinkedListMultimap.Node<K, V> next() {
            LinkedListMultimap.checkElement(this.next);
            this.previous = this.current = this.next;
            this.next = this.next.next;
            this.nextIndex++;
            return this.current;
        }

        public void remove() {
            Preconditions.checkState((this.current != null));
            if (this.current != this.next) {
                this.previous = this.current.previous;
                this.nextIndex--;
            } else {
                this.next = this.current.next;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        public boolean hasPrevious() {
            return (this.previous != null);
        }

        public LinkedListMultimap.Node<K, V> previous() {
            LinkedListMultimap.checkElement(this.previous);
            this.next = this.current = this.previous;
            this.previous = this.previous.previous;
            this.nextIndex--;
            return this.current;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void set(LinkedListMultimap.Node<K, V> e) {
            throw new UnsupportedOperationException();
        }

        public void add(LinkedListMultimap.Node<K, V> e) {
            throw new UnsupportedOperationException();
        }

        void setValue(V value) {
            Preconditions.checkState((this.current != null));
            this.current.value = value;
        }
    }

    private class DistinctKeyIterator
            implements Iterator<K> {
        final Set<K> seenKeys = Sets.newHashSetWithExpectedSize(LinkedListMultimap.this.keySet().size());
        LinkedListMultimap.Node<K, V> next = LinkedListMultimap.this.head;

        LinkedListMultimap.Node<K, V> current;

        private DistinctKeyIterator() {
        }

        public boolean hasNext() {
            return (this.next != null);
        }

        public K next() {
            LinkedListMultimap.checkElement(this.next);
            this.current = this.next;
            this.seenKeys.add(this.current.key);
            do {
                this.next = this.next.next;
            } while (this.next != null && !this.seenKeys.add(this.next.key));
            return this.current.key;
        }

        public void remove() {
            Preconditions.checkState((this.current != null));
            LinkedListMultimap.this.removeAllNodes(this.current.key);
            this.current = null;
        }
    }

    private class ValueForKeyIterator implements ListIterator<V> {
        final Object key;
        int nextIndex;
        LinkedListMultimap.Node<K, V> next;
        LinkedListMultimap.Node<K, V> current;
        LinkedListMultimap.Node<K, V> previous;

        ValueForKeyIterator(Object key) {
            this.key = key;
            this.next = (LinkedListMultimap.Node<K, V>) LinkedListMultimap.this.keyToKeyHead.get(key);
        }

        public ValueForKeyIterator(Object key, int index) {
            int size = LinkedListMultimap.this.keyCount.count(key);
            Preconditions.checkPositionIndex(index, size);
            if (index >= size / 2) {
                this.previous = (LinkedListMultimap.Node<K, V>) LinkedListMultimap.this.keyToKeyTail.get(key);
                this.nextIndex = size;
                while (index++ < size) {
                    previous();
                }
            } else {
                this.next = (LinkedListMultimap.Node<K, V>) LinkedListMultimap.this.keyToKeyHead.get(key);
                while (index-- > 0) {
                    next();
                }
            }
            this.key = key;
            this.current = null;
        }

        public boolean hasNext() {
            return (this.next != null);
        }

        public V next() {
            LinkedListMultimap.checkElement(this.next);
            this.previous = this.current = this.next;
            this.next = this.next.nextSibling;
            this.nextIndex++;
            return this.current.value;
        }

        public boolean hasPrevious() {
            return (this.previous != null);
        }

        public V previous() {
            LinkedListMultimap.checkElement(this.previous);
            this.next = this.current = this.previous;
            this.previous = this.previous.previousSibling;
            this.nextIndex--;
            return this.current.value;
        }

        public int nextIndex() {
            return this.nextIndex;
        }

        public int previousIndex() {
            return this.nextIndex - 1;
        }

        public void remove() {
            Preconditions.checkState((this.current != null));
            if (this.current != this.next) {
                this.previous = this.current.previousSibling;
                this.nextIndex--;
            } else {
                this.next = this.current.nextSibling;
            }
            LinkedListMultimap.this.removeNode(this.current);
            this.current = null;
        }

        public void set(V value) {
            Preconditions.checkState((this.current != null));
            this.current.value = value;
        }

        public void add(V value) {
            this.previous = LinkedListMultimap.this.addNode((K) this.key, value, this.next);
            this.nextIndex++;
            this.current = null;
        }
    }

    private class MultisetView extends AbstractCollection<K> implements Multiset<K> {
        private MultisetView() {
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.size();
        }

        public Iterator<K> iterator() {
            final Iterator<LinkedListMultimap.Node<K, V>> nodes = new LinkedListMultimap.NodeIterator();
            return new Iterator<K>() {
                public boolean hasNext() {
                    return nodes.hasNext();
                }

                public K next() {
                    return ((LinkedListMultimap.Node) nodes.next()).key;
                }

                public void remove() {
                    nodes.remove();
                }
            };
        }

        public int count(@Nullable Object key) {
            return LinkedListMultimap.this.keyCount.count(key);
        }

        public int add(@Nullable K key, int occurrences) {
            throw new UnsupportedOperationException();
        }

        public int remove(@Nullable Object key, int occurrences) {
            Preconditions.checkArgument((occurrences >= 0));
            int oldCount = count(key);
            Iterator<V> values = new LinkedListMultimap.ValueForKeyIterator(key);
            while (occurrences-- > 0 && values.hasNext()) {
                values.next();
                values.remove();
            }
            return oldCount;
        }

        public int setCount(K element, int count) {
            return Multisets.setCountImpl(this, element, count);
        }

        public boolean setCount(K element, int oldCount, int newCount) {
            return Multisets.setCountImpl(this, element, oldCount, newCount);
        }

        public boolean removeAll(Collection<?> c) {
            return Iterators.removeAll(iterator(), c);
        }

        public boolean retainAll(Collection<?> c) {
            return Iterators.retainAll(iterator(), c);
        }

        public Set<K> elementSet() {
            return LinkedListMultimap.this.keySet();
        }

        public Set<Multiset.Entry<K>> entrySet() {
            return new AbstractSet<Multiset.Entry<K>>() {
                public int size() {
                    return LinkedListMultimap.this.keyCount.elementSet().size();
                }

                public Iterator<Multiset.Entry<K>> iterator() {
                    final Iterator<K> keyIterator = new LinkedListMultimap.DistinctKeyIterator();
                    return new Iterator<Multiset.Entry<K>>() {
                        public boolean hasNext() {
                            return keyIterator.hasNext();
                        }

                        public Multiset.Entry<K> next() {
                            final K key = keyIterator.next();
                            return new Multisets.AbstractEntry<K>() {
                                public K getElement() {
                                    return (K) key;
                                }

                                public int getCount() {
                                    return LinkedListMultimap.this.keyCount.count(key);
                                }
                            };
                        }

                        public void remove() {
                            keyIterator.remove();
                        }
                    };
                }
            };
        }

        public boolean equals(@Nullable Object object) {
            return LinkedListMultimap.this.keyCount.equals(object);
        }

        public int hashCode() {
            return LinkedListMultimap.this.keyCount.hashCode();
        }

        public String toString() {
            return LinkedListMultimap.this.keyCount.toString();
        }
    }

    private class AsMapEntries extends AbstractSet<Map.Entry<K, Collection<V>>> {
        private AsMapEntries() {
        }

        public int size() {
            return LinkedListMultimap.this.keyCount.elementSet().size();
        }

        public Iterator<Map.Entry<K, Collection<V>>> iterator() {
            final Iterator<K> keyIterator = new LinkedListMultimap.DistinctKeyIterator();
            return new Iterator<Map.Entry<K, Collection<V>>>() {
                public boolean hasNext() {
                    return keyIterator.hasNext();
                }

                public Map.Entry<K, Collection<V>> next() {
                    final K key = keyIterator.next();
                    return (Map.Entry) new AbstractMapEntry<K, Collection<Collection<V>>>() {
                        public K getKey() {
                            return (K) key;
                        }

                        public Collection<V> getValue() {
                            return LinkedListMultimap.this.get(key);
                        }
                    };
                }

                public void remove() {
                    keyIterator.remove();
                }
            };
        }
    }
}

