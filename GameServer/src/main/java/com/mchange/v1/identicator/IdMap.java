package com.mchange.v1.identicator;

import com.mchange.v1.util.AbstractMapEntry;
import com.mchange.v1.util.SimpleMapEntry;
import com.mchange.v1.util.WrapperIterator;

import java.util.*;

abstract class IdMap
        extends AbstractMap
        implements Map {
    Map inner;
    Identicator id;

    protected IdMap(Map paramMap, Identicator paramIdenticator) {
        this.inner = paramMap;
        this.id = paramIdenticator;
    }

    public Object put(Object paramObject1, Object paramObject2) {
        return this.inner.put(createIdKey(paramObject1), paramObject2);
    }

    public boolean containsKey(Object paramObject) {
        return this.inner.containsKey(createIdKey(paramObject));
    }

    public Object get(Object paramObject) {
        return this.inner.get(createIdKey(paramObject));
    }

    public Object remove(Object paramObject) {
        return this.inner.remove(createIdKey(paramObject));
    }

    protected Object removeIdHashKey(IdHashKey paramIdHashKey) {
        return this.inner.remove(paramIdHashKey);
    }

    public Set entrySet() {
        return new UserEntrySet();
    }

    protected final Set internalEntrySet() {
        return this.inner.entrySet();
    }

    protected abstract IdHashKey createIdKey(Object paramObject);

    protected final Map.Entry createIdEntry(Object paramObject1, Object paramObject2) {
        return (Map.Entry) new SimpleMapEntry(createIdKey(paramObject1), paramObject2);
    }

    protected final Map.Entry createIdEntry(Map.Entry paramEntry) {
        return createIdEntry(paramEntry.getKey(), paramEntry.getValue());
    }

    protected static class UserEntry extends AbstractMapEntry {
        private Map.Entry innerEntry;

        UserEntry(Map.Entry param1Entry) {
            this.innerEntry = param1Entry;
        }

        public final Object getKey() {
            return ((IdHashKey) this.innerEntry.getKey()).getKeyObj();
        }

        public final Object getValue() {
            return this.innerEntry.getValue();
        }

        public final Object setValue(Object param1Object) {
            return this.innerEntry.setValue(param1Object);
        }
    }

    private final class UserEntrySet extends AbstractSet {
        Set innerEntries = IdMap.this.inner.entrySet();

        private UserEntrySet() {
        }

        public Iterator iterator() {
            return (Iterator) new WrapperIterator(this.innerEntries.iterator(), true) {
                protected Object transformObject(Object param2Object) {
                    return new IdMap.UserEntry((Map.Entry) param2Object);
                }
            };
        }

        public int size() {
            return this.innerEntries.size();
        }

        public boolean contains(Object param1Object) {
            if (param1Object instanceof Map.Entry) {

                Map.Entry entry = (Map.Entry) param1Object;
                return this.innerEntries.contains(IdMap.this.createIdEntry(entry));
            }

            return false;
        }

        public boolean remove(Object param1Object) {
            if (param1Object instanceof Map.Entry) {

                Map.Entry entry = (Map.Entry) param1Object;
                return this.innerEntries.remove(IdMap.this.createIdEntry(entry));
            }

            return false;
        }

        public void clear() {
            IdMap.this.inner.clear();
        }
    }
}

