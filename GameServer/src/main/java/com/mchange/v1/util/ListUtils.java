package com.mchange.v1.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;

public final class ListUtils {
    public static List oneElementUnmodifiableList(final Object elem) {
        return new AbstractList() {
            public Iterator iterator() {
                return IteratorUtils.oneElementUnmodifiableIterator(elem);
            }

            public int size() {
                return 1;
            }

            public boolean isEmpty() {
                return false;
            }

            public boolean contains(Object param1Object) {
                return (param1Object == elem);
            }

            public Object get(int param1Int) {
                if (param1Int != 0) {
                    throw new IndexOutOfBoundsException("One element list has no element index " + param1Int);
                }

                return elem;
            }
        };
    }

    public static boolean equivalent(List paramList1, List paramList2) {
        if (paramList1.size() != paramList2.size()) {
            return false;
        }

        Iterator iterator1 = paramList1.iterator();
        Iterator iterator2 = paramList2.iterator();
        return IteratorUtils.equivalent(iterator1, iterator2);
    }

    public static int hashContents(List paramList) {
        int i = 0;
        byte b = 0;
        for (Object object : paramList) {

            if (object != null) i ^= object.hashCode() ^ b;
            b++;
        }
        return i;
    }
}

