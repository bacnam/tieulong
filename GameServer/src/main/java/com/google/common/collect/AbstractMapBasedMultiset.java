package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.*;

@GwtCompatible(emulated = true)
abstract class AbstractMapBasedMultiset<E>
        extends AbstractMultiset<E>
        implements Serializable {
    @GwtIncompatible("not needed in emulated source.")
    private static final long serialVersionUID = -2250766705698539974L;
    private transient Map<E, Count> backingMap;
    private transient long size;

    protected AbstractMapBasedMultiset(Map<E, Count> backingMap) {
        this.backingMap = (Map<E, Count>) Preconditions.checkNotNull(backingMap);
        this.size = super.size();
    }

    private static int getAndSet(Count i, int count) {
        if (i == null) {
            return 0;
        }

        return i.getAndSet(count);
    }

    Map<E, Count> backingMap() {
        return this.backingMap;
    }

    void setBackingMap(Map<E, Count> backingMap) {
        this.backingMap = backingMap;
    }

    public Set<Multiset.Entry<E>> entrySet() {
        return super.entrySet();
    }

    Iterator<Multiset.Entry<E>> entryIterator() {
        final Iterator<Map.Entry<E, Count>> backingEntries = this.backingMap.entrySet().iterator();

        return (Iterator) new Iterator<Multiset.Entry<Multiset.Entry<E>>>() {
            Map.Entry<E, Count> toRemove;

            public boolean hasNext() {
                return backingEntries.hasNext();
            }

            public Multiset.Entry<E> next() {
                final Map.Entry<E, Count> mapEntry = backingEntries.next();
                this.toRemove = mapEntry;
                return new Multisets.AbstractEntry<E>() {
                    public E getElement() {
                        return (E) mapEntry.getKey();
                    }

                    public int getCount() {
                        int count = ((Count) mapEntry.getValue()).get();
                        if (count == 0) {
                            Count frequency = (Count) AbstractMapBasedMultiset.this.backingMap.get(getElement());
                            if (frequency != null) {
                                count = frequency.get();
                            }
                        }
                        return count;
                    }
                };
            }

            public void remove() {
                Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");

                AbstractMapBasedMultiset.this.size -= ((Count) this.toRemove.getValue()).getAndSet(0);
                backingEntries.remove();
                this.toRemove = null;
            }
        };
    }

    public void clear() {
        for (Count frequency : this.backingMap.values()) {
            frequency.set(0);
        }
        this.backingMap.clear();
        this.size = 0L;
    }

    int distinctElements() {
        return this.backingMap.size();
    }

    public int size() {
        return Ints.saturatedCast(this.size);
    }

    public Iterator<E> iterator() {
        return new MapBasedMultisetIterator();
    }

    public int count(@Nullable Object element) {
        try {
            Count frequency = this.backingMap.get(element);
            return (frequency == null) ? 0 : frequency.get();
        } catch (NullPointerException e) {
            return 0;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public int add(@Nullable E element, int occurrences) {
        int oldCount;
        if (occurrences == 0) {
            return count(element);
        }
        Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[]{Integer.valueOf(occurrences)});

        Count frequency = this.backingMap.get(element);

        if (frequency == null) {
            oldCount = 0;
            this.backingMap.put(element, new Count(occurrences));
        } else {
            oldCount = frequency.get();
            long newCount = oldCount + occurrences;
            Preconditions.checkArgument((newCount <= 2147483647L), "too many occurrences: %s", new Object[]{Long.valueOf(newCount)});

            frequency.getAndAdd(occurrences);
        }
        this.size += occurrences;
        return oldCount;
    }

    public int remove(@Nullable Object element, int occurrences) {
        int numberRemoved;
        if (occurrences == 0) {
            return count(element);
        }
        Preconditions.checkArgument((occurrences > 0), "occurrences cannot be negative: %s", new Object[]{Integer.valueOf(occurrences)});

        Count frequency = this.backingMap.get(element);
        if (frequency == null) {
            return 0;
        }

        int oldCount = frequency.get();

        if (oldCount > occurrences) {
            numberRemoved = occurrences;
        } else {
            numberRemoved = oldCount;
            this.backingMap.remove(element);
        }

        frequency.addAndGet(-numberRemoved);
        this.size -= numberRemoved;
        return oldCount;
    }

    public int setCount(E element, int count) {
        int oldCount;
        Multisets.checkNonnegative(count, "count");

        if (count == 0) {
            Count existingCounter = this.backingMap.remove(element);
            oldCount = getAndSet(existingCounter, count);
        } else {
            Count existingCounter = this.backingMap.get(element);
            oldCount = getAndSet(existingCounter, count);

            if (existingCounter == null) {
                this.backingMap.put(element, new Count(count));
            }
        }

        this.size += (count - oldCount);
        return oldCount;
    }

    private int removeAllOccurrences(@Nullable Object element, Map<E, Count> map) {
        Count frequency = map.remove(element);
        if (frequency == null) {
            return 0;
        }
        int numberRemoved = frequency.getAndSet(0);
        this.size -= numberRemoved;
        return numberRemoved;
    }

    Set<E> createElementSet() {
        return new MapBasedElementSet(this.backingMap);
    }

    @GwtIncompatible("java.io.ObjectStreamException")
    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("Stream data required");
    }

    private class MapBasedMultisetIterator
            implements Iterator<E> {
        final Iterator<Map.Entry<E, Count>> entryIterator = AbstractMapBasedMultiset.this.backingMap.entrySet().iterator();

        Map.Entry<E, Count> currentEntry;
        int occurrencesLeft;
        boolean canRemove;

        public boolean hasNext() {
            return (this.occurrencesLeft > 0 || this.entryIterator.hasNext());
        }

        public E next() {
            if (this.occurrencesLeft == 0) {
                this.currentEntry = this.entryIterator.next();
                this.occurrencesLeft = ((Count) this.currentEntry.getValue()).get();
            }
            this.occurrencesLeft--;
            this.canRemove = true;
            return this.currentEntry.getKey();
        }

        public void remove() {
            Preconditions.checkState(this.canRemove, "no calls to next() since the last call to remove()");

            int frequency = ((Count) this.currentEntry.getValue()).get();
            if (frequency <= 0) {
                throw new ConcurrentModificationException();
            }
            if (((Count) this.currentEntry.getValue()).addAndGet(-1) == 0) {
                this.entryIterator.remove();
            }
            AbstractMapBasedMultiset.this.size--;
            this.canRemove = false;
        }
    }

    class MapBasedElementSet
            extends ForwardingSet<E> {
        private final Map<E, Count> map;

        private final Set<E> delegate;

        MapBasedElementSet(Map<E, Count> map) {
            this.map = map;
            this.delegate = map.keySet();
        }

        protected Set<E> delegate() {
            return this.delegate;
        }

        public Iterator<E> iterator() {
            final Iterator<Map.Entry<E, Count>> entries = this.map.entrySet().iterator();

            return new Iterator<E>() {
                Map.Entry<E, Count> toRemove;

                public boolean hasNext() {
                    return entries.hasNext();
                }

                public E next() {
                    this.toRemove = entries.next();
                    return this.toRemove.getKey();
                }

                public void remove() {
                    Preconditions.checkState((this.toRemove != null), "no calls to next() since the last call to remove()");

                    AbstractMapBasedMultiset.this.size -= ((Count) this.toRemove.getValue()).getAndSet(0);
                    entries.remove();
                    this.toRemove = null;
                }
            };
        }

        public boolean remove(Object element) {
            return (AbstractMapBasedMultiset.this.removeAllOccurrences(element, this.map) != 0);
        }

        public boolean removeAll(Collection<?> elementsToRemove) {
            return Iterators.removeAll(iterator(), elementsToRemove);
        }

        public boolean retainAll(Collection<?> elementsToRetain) {
            return Iterators.retainAll(iterator(), elementsToRetain);
        }

        public void clear() {
            if (this.map == AbstractMapBasedMultiset.this.backingMap) {
                AbstractMapBasedMultiset.this.clear();
            } else {
                Iterator<E> i = iterator();
                while (i.hasNext()) {
                    i.next();
                    i.remove();
                }
            }
        }

        public Map<E, Count> getMap() {
            return this.map;
        }
    }
}

