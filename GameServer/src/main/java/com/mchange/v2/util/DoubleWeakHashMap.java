package com.mchange.v2.util;

import com.mchange.v1.util.AbstractMapEntry;
import com.mchange.v1.util.WrapperIterator;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

public class DoubleWeakHashMap
        implements Map {
    HashMap inner;
    ReferenceQueue keyQ = new ReferenceQueue();
    ReferenceQueue valQ = new ReferenceQueue();

    CheckKeyHolder holder = new CheckKeyHolder();

    Set userKeySet = null;
    Collection valuesCollection = null;

    public DoubleWeakHashMap() {
        this.inner = new HashMap<Object, Object>();
    }

    public DoubleWeakHashMap(int paramInt) {
        this.inner = new HashMap<Object, Object>(paramInt);
    }

    public DoubleWeakHashMap(int paramInt, float paramFloat) {
        this.inner = new HashMap<Object, Object>(paramInt, paramFloat);
    }

    public DoubleWeakHashMap(Map paramMap) {
        this();
        putAll(paramMap);
    }

    public void cleanCleared() {
        WKey wKey;
        while ((wKey = (WKey) this.keyQ.poll()) != null) {
            this.inner.remove(wKey);
        }
        WVal wVal;
        while ((wVal = (WVal) this.valQ.poll()) != null) {
            this.inner.remove(wVal.getWKey());
        }
    }

    public void clear() {
        cleanCleared();
        this.inner.clear();
    }

    public boolean containsKey(Object paramObject) {
        cleanCleared();
        try {
            return this.inner.containsKey(this.holder.set(paramObject));
        } finally {
            this.holder.clear();
        }
    }

    public boolean containsValue(Object paramObject) {
        for (WVal wVal : this.inner.values()) {

            if (paramObject.equals(wVal.get()))
                return true;
        }
        return false;
    }

    public Set entrySet() {
        cleanCleared();
        return new UserEntrySet();
    }

    public Object get(Object paramObject) {
        try {
            cleanCleared();
            WVal wVal = (WVal) this.inner.get(this.holder.set(paramObject));
            return (wVal == null) ? null : wVal.get();
        } finally {

            this.holder.clear();
        }
    }

    public boolean isEmpty() {
        cleanCleared();
        return this.inner.isEmpty();
    }

    public Set keySet() {
        cleanCleared();
        if (this.userKeySet == null)
            this.userKeySet = new UserKeySet();
        return this.userKeySet;
    }

    public Object put(Object paramObject1, Object paramObject2) {
        cleanCleared();
        WVal wVal = doPut(paramObject1, paramObject2);
        if (wVal != null) {
            return wVal.get();
        }
        return null;
    }

    private WVal doPut(Object paramObject1, Object paramObject2) {
        WKey wKey = new WKey(paramObject1, this.keyQ);
        WVal wVal = new WVal(wKey, paramObject2, this.valQ);
        return this.inner.put(wKey, wVal);
    }

    public void putAll(Map paramMap) {
        cleanCleared();
        for (Map.Entry entry : paramMap.entrySet()) {

            doPut(entry.getKey(), entry.getValue());
        }
    }

    public Object remove(Object paramObject) {
        try {
            cleanCleared();
            WVal wVal = (WVal) this.inner.remove(this.holder.set(paramObject));
            return (wVal == null) ? null : wVal.get();
        } finally {

            this.holder.clear();
        }
    }

    public int size() {
        cleanCleared();
        return this.inner.size();
    }

    public Collection values() {
        if (this.valuesCollection == null)
            this.valuesCollection = new ValuesCollection();
        return this.valuesCollection;
    }

    static final class CheckKeyHolder {
        Object checkKey;

        public Object get() {
            return this.checkKey;
        }

        public CheckKeyHolder set(Object param1Object) {
            assert this.checkKey == null : "Illegal concurrenct use of DoubleWeakHashMap!";

            this.checkKey = param1Object;
            return this;
        }

        public void clear() {
            this.checkKey = null;
        }

        public int hashCode() {
            return this.checkKey.hashCode();
        }

        public boolean equals(Object param1Object) {
            assert get() != null : "CheckedKeyHolder should never do an equality check while its value is null.";

            if (this == param1Object)
                return true;
            if (param1Object instanceof CheckKeyHolder)
                return get().equals(((CheckKeyHolder) param1Object).get());
            if (param1Object instanceof DoubleWeakHashMap.WKey) {
                return get().equals(((DoubleWeakHashMap.WKey) param1Object).get());
            }
            return false;
        }
    }

    static final class WKey
            extends WeakReference {
        int cachedHash;

        WKey(Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
            super((T) param1Object, param1ReferenceQueue);
            this.cachedHash = param1Object.hashCode();
        }

        public int hashCode() {
            return this.cachedHash;
        }

        public boolean equals(Object param1Object) {
            if (this == param1Object)
                return true;
            if (param1Object instanceof WKey) {

                WKey wKey = (WKey) param1Object;
                T t1 = get();
                T t2 = wKey.get();
                if (t1 == null || t2 == null) {
                    return false;
                }
                return t1.equals(t2);
            }
            if (param1Object instanceof DoubleWeakHashMap.CheckKeyHolder) {

                DoubleWeakHashMap.CheckKeyHolder checkKeyHolder = (DoubleWeakHashMap.CheckKeyHolder) param1Object;
                T t = get();
                Object object = checkKeyHolder.get();
                if (t == null || object == null) {
                    return false;
                }
                return t.equals(object);
            }

            return false;
        }
    }

    static final class WVal
            extends WeakReference {
        DoubleWeakHashMap.WKey key;

        WVal(DoubleWeakHashMap.WKey param1WKey, Object param1Object, ReferenceQueue<? super T> param1ReferenceQueue) {
            super((T) param1Object, param1ReferenceQueue);
            this.key = param1WKey;
        }

        public DoubleWeakHashMap.WKey getWKey() {
            return this.key;
        }
    }

    private final class UserEntrySet extends AbstractSet {
        private UserEntrySet() {
        }

        private Set innerEntrySet() {
            DoubleWeakHashMap.this.cleanCleared();
            return DoubleWeakHashMap.this.inner.entrySet();
        }

        public Iterator iterator() {
            return (Iterator) new WrapperIterator(innerEntrySet().iterator(), true) {
                protected Object transformObject(Object param2Object) {
                    Map.Entry entry = (Map.Entry) param2Object;
                    T t1 = ((DoubleWeakHashMap.WKey) entry.getKey()).get();
                    T t2 = ((DoubleWeakHashMap.WVal) entry.getValue()).get();

                    if (t1 == null || t2 == null) {
                        return WrapperIterator.SKIP_TOKEN;
                    }
                    return new DoubleWeakHashMap.UserEntry(entry, t1, t2);
                }
            };
        }

        public int size() {
            return innerEntrySet().size();
        }
    }

    class UserEntry
            extends AbstractMapEntry {
        Map.Entry innerEntry;
        Object key;
        Object val;

        UserEntry(Map.Entry param1Entry, Object param1Object1, Object param1Object2) {
            this.innerEntry = param1Entry;
            this.key = param1Object1;
            this.val = param1Object2;
        }

        public final Object getKey() {
            return this.key;
        }

        public final Object getValue() {
            return this.val;
        }

        public final Object setValue(Object param1Object) {
            return this.innerEntry.setValue(new DoubleWeakHashMap.WVal((DoubleWeakHashMap.WKey) this.innerEntry.getKey(), param1Object, DoubleWeakHashMap.this.valQ));
        }
    }

    class UserKeySet
            implements Set {
        public boolean add(Object param1Object) {
            DoubleWeakHashMap.this.cleanCleared();
            throw new UnsupportedOperationException("You cannot add to a Map's key set.");
        }

        public boolean addAll(Collection param1Collection) {
            DoubleWeakHashMap.this.cleanCleared();
            throw new UnsupportedOperationException("You cannot add to a Map's key set.");
        }

        public void clear() {
            DoubleWeakHashMap.this.clear();
        }

        public boolean contains(Object param1Object) {
            return DoubleWeakHashMap.this.containsKey(param1Object);
        }

        public boolean containsAll(Collection param1Collection) {
            for (Iterator iterator = param1Collection.iterator(); iterator.hasNext(); ) {
                if (!contains(iterator.next()))
                    return false;
            }
            return true;
        }

        public boolean isEmpty() {
            return DoubleWeakHashMap.this.isEmpty();
        }

        public Iterator iterator() {
            DoubleWeakHashMap.this.cleanCleared();
            return (Iterator) new WrapperIterator(DoubleWeakHashMap.this.inner.keySet().iterator(), true) {
                protected Object transformObject(Object param2Object) {
                    T t = ((DoubleWeakHashMap.WKey) param2Object).get();

                    if (t == null) {
                        return WrapperIterator.SKIP_TOKEN;
                    }
                    return t;
                }
            };
        }

        public boolean remove(Object param1Object) {
            return (DoubleWeakHashMap.this.remove(param1Object) != null);
        }

        public boolean removeAll(Collection param1Collection) {
            boolean bool = false;
            for (Iterator iterator = param1Collection.iterator(); iterator.hasNext(); )
                bool |= remove(iterator.next());
            return bool;
        }

        public boolean retainAll(Collection param1Collection) {
            boolean bool = false;
            for (Iterator iterator = iterator(); iterator.hasNext(); ) {

                if (!param1Collection.contains(iterator.next())) {

                    iterator.remove();
                    bool = true;
                }
            }
            return bool;
        }

        public int size() {
            return DoubleWeakHashMap.this.size();
        }

        public Object[] toArray() {
            DoubleWeakHashMap.this.cleanCleared();
            return (new HashSet(this)).toArray();
        }

        public Object[] toArray(Object[] param1ArrayOfObject) {
            DoubleWeakHashMap.this.cleanCleared();
            return (new HashSet(this)).toArray(param1ArrayOfObject);
        }
    }

    class ValuesCollection
            implements Collection {
        public boolean add(Object param1Object) {
            DoubleWeakHashMap.this.cleanCleared();
            throw new UnsupportedOperationException("DoubleWeakHashMap does not support adding to its values Collection.");
        }

        public boolean addAll(Collection param1Collection) {
            DoubleWeakHashMap.this.cleanCleared();
            throw new UnsupportedOperationException("DoubleWeakHashMap does not support adding to its values Collection.");
        }

        public void clear() {
            DoubleWeakHashMap.this.clear();
        }

        public boolean contains(Object param1Object) {
            return DoubleWeakHashMap.this.containsValue(param1Object);
        }

        public boolean containsAll(Collection param1Collection) {
            for (Iterator iterator = param1Collection.iterator(); iterator.hasNext(); ) {
                if (!contains(iterator.next()))
                    return false;
            }
            return true;
        }

        public boolean isEmpty() {
            return DoubleWeakHashMap.this.isEmpty();
        }

        public Iterator iterator() {
            return (Iterator) new WrapperIterator(DoubleWeakHashMap.this.inner.values().iterator(), true) {
                protected Object transformObject(Object param2Object) {
                    T t = ((DoubleWeakHashMap.WVal) param2Object).get();

                    if (t == null) {
                        return WrapperIterator.SKIP_TOKEN;
                    }
                    return t;
                }
            };
        }

        public boolean remove(Object param1Object) {
            DoubleWeakHashMap.this.cleanCleared();
            return removeValue(param1Object);
        }

        public boolean removeAll(Collection param1Collection) {
            DoubleWeakHashMap.this.cleanCleared();
            boolean bool = false;
            for (Iterator iterator = param1Collection.iterator(); iterator.hasNext(); )
                bool |= removeValue(iterator.next());
            return bool;
        }

        public boolean retainAll(Collection param1Collection) {
            DoubleWeakHashMap.this.cleanCleared();
            return retainValues(param1Collection);
        }

        public int size() {
            return DoubleWeakHashMap.this.size();
        }

        public Object[] toArray() {
            DoubleWeakHashMap.this.cleanCleared();
            return (new ArrayList(this)).toArray();
        }

        public Object[] toArray(Object[] param1ArrayOfObject) {
            DoubleWeakHashMap.this.cleanCleared();
            return (new ArrayList(this)).toArray(param1ArrayOfObject);
        }

        private boolean removeValue(Object param1Object) {
            boolean bool = false;
            for (Iterator<DoubleWeakHashMap.WVal> iterator = DoubleWeakHashMap.this.inner.values().iterator(); iterator.hasNext(); ) {

                DoubleWeakHashMap.WVal wVal = iterator.next();
                if (param1Object.equals(wVal.get())) {

                    iterator.remove();
                    bool = true;
                }
            }
            return bool;
        }

        private boolean retainValues(Collection param1Collection) {
            boolean bool = false;
            for (Iterator<DoubleWeakHashMap.WVal> iterator = DoubleWeakHashMap.this.inner.values().iterator(); iterator.hasNext(); ) {

                DoubleWeakHashMap.WVal wVal = iterator.next();
                if (!param1Collection.contains(wVal.get())) {

                    iterator.remove();
                    bool = true;
                }
            }
            return bool;
        }
    }
}

