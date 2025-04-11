package com.mchange.v1.identicator;

import com.mchange.v1.util.WrapperIterator;

import java.lang.ref.ReferenceQueue;
import java.util.*;

public final class IdWeakHashMap
        extends IdMap
        implements Map {
    ReferenceQueue rq;

    public IdWeakHashMap(Identicator paramIdenticator) {
        super(new HashMap<Object, Object>(), paramIdenticator);
        this.rq = new ReferenceQueue();
    }

    public int size() {
        cleanCleared();
        return super.size();
    }

    public boolean isEmpty() {
        try {
            return super.isEmpty();
        } finally {
            cleanCleared();
        }
    }

    public boolean containsKey(Object paramObject) {
        try {
            return super.containsKey(paramObject);
        } finally {
            cleanCleared();
        }
    }

    public boolean containsValue(Object paramObject) {
        try {
            return super.containsValue(paramObject);
        } finally {
            cleanCleared();
        }
    }

    public Object get(Object paramObject) {
        try {
            return super.get(paramObject);
        } finally {
            cleanCleared();
        }
    }

    public Object put(Object paramObject1, Object paramObject2) {
        try {
            return super.put(paramObject1, paramObject2);
        } finally {
            cleanCleared();
        }
    }

    public Object remove(Object paramObject) {
        try {
            return super.remove(paramObject);
        } finally {
            cleanCleared();
        }
    }

    public void putAll(Map<? extends K, ? extends V> paramMap) {
        try {
            super.putAll(paramMap);
        } finally {
            cleanCleared();
        }
    }

    public void clear() {
        try {
            super.clear();
        } finally {
            cleanCleared();
        }
    }

    public Set keySet() {
        try {
            return super.keySet();
        } finally {
            cleanCleared();
        }
    }

    public Collection values() {
        try {
            return super.values();
        } finally {
            cleanCleared();
        }
    }

    public Set entrySet() {
        try {
            return new WeakUserEntrySet();
        } finally {
            cleanCleared();
        }
    }

    public boolean equals(Object paramObject) {
        try {
            return super.equals(paramObject);
        } finally {
            cleanCleared();
        }
    }

    public int hashCode() {
        try {
            return super.hashCode();
        } finally {
            cleanCleared();
        }
    }

    protected IdHashKey createIdKey(Object paramObject) {
        return new WeakIdHashKey(paramObject, this.id, this.rq);
    }

    private void cleanCleared() {
        WeakIdHashKey.Ref ref;
        while ((ref = (WeakIdHashKey.Ref) this.rq.poll()) != null)
            removeIdHashKey(ref.getKey());
    }

    private final class WeakUserEntrySet
            extends AbstractSet {
        Set innerEntries = IdWeakHashMap.this.internalEntrySet();

        private WeakUserEntrySet() {
        }

        public Iterator iterator() {
            try {
                return (Iterator) new WrapperIterator(this.innerEntries.iterator(), true) {
                    protected Object transformObject(Object param2Object) {
                        Map.Entry entry = (Map.Entry) param2Object;
                        final Object userKey = ((IdHashKey) entry.getKey()).getKeyObj();
                        if (object == null) {
                            return WrapperIterator.SKIP_TOKEN;
                        }
                        return new IdMap.UserEntry(entry) {
                            Object preventRefClear = userKey;
                        };
                    }
                };
            } finally {
                IdWeakHashMap.this.cleanCleared();
            }
        }

        public int size() {
            IdWeakHashMap.this.cleanCleared();
            return this.innerEntries.size();
        }

        public boolean contains(Object param1Object) {
            try {
                if (param1Object instanceof Map.Entry) {

                    Map.Entry entry = (Map.Entry) param1Object;
                    return this.innerEntries.contains(IdWeakHashMap.this.createIdEntry(entry));
                }

                return false;
            } finally {

                IdWeakHashMap.this.cleanCleared();
            }
        }

        public boolean remove(Object param1Object) {
            try {
                if (param1Object instanceof Map.Entry) {

                    Map.Entry entry = (Map.Entry) param1Object;
                    return this.innerEntries.remove(IdWeakHashMap.this.createIdEntry(entry));
                }

                return false;
            } finally {

                IdWeakHashMap.this.cleanCleared();
            }
        }

        public void clear() {
            try {
                IdWeakHashMap.this.inner.clear();
            } finally {
                IdWeakHashMap.this.cleanCleared();
            }
        }
    }
}

