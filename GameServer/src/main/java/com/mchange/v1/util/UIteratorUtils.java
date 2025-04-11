package com.mchange.v1.util;

import java.util.Collection;
import java.util.Iterator;

public class UIteratorUtils {
    public static void addToCollection(Collection<Object> paramCollection, UIterator paramUIterator) throws Exception {
        while (paramUIterator.hasNext()) {
            paramCollection.add(paramUIterator.next());
        }
    }

    public static UIterator uiteratorFromIterator(final Iterator ii) {
        return new UIterator() {
            public boolean hasNext() {
                return ii.hasNext();
            }

            public Object next() {
                return ii.next();
            }

            public void remove() {
                ii.remove();
            }

            public void close() {
            }
        };
    }
}

