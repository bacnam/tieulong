package bsh.collection;

import bsh.BshIterator;
import bsh.CollectionManager;

import java.util.Map;

public class CollectionManagerImpl
        extends CollectionManager {
    public BshIterator getBshIterator(Object obj) throws IllegalArgumentException {
        if (obj instanceof java.util.Collection || obj instanceof java.util.Iterator) {
            return new CollectionIterator(obj);
        }
        return (BshIterator) new CollectionManager.BasicBshIterator(obj);
    }

    public boolean isMap(Object obj) {
        if (obj instanceof Map) {
            return true;
        }
        return super.isMap(obj);
    }

    public Object getFromMap(Object map, Object key) {
        return ((Map) map).get(key);
    }

    public Object putInMap(Object map, Object key, Object value) {
        return ((Map<Object, Object>) map).put(key, value);
    }
}

