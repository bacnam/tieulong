package com.mchange.v1.util;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class SetUtils
{
public static Set oneElementUnmodifiableSet(final Object elem) {
return new AbstractSet()
{
public Iterator iterator() {
return IteratorUtils.oneElementUnmodifiableIterator(elem);
} public int size() {
return 1;
}
public boolean isEmpty() {
return false;
}
public boolean contains(Object param1Object) {
return (param1Object == elem);
}
};
}

public static Set setFromArray(Object[] paramArrayOfObject) {
HashSet<Object> hashSet = new HashSet(); byte b; int i;
for (b = 0, i = paramArrayOfObject.length; b < i; b++)
hashSet.add(paramArrayOfObject[b]); 
return hashSet;
}

public static boolean equivalentDisregardingSort(Set<?> paramSet1, Set<?> paramSet2) {
return (paramSet1.containsAll(paramSet2) && paramSet2.containsAll(paramSet1));
}

public static int hashContentsDisregardingSort(Set paramSet) {
int i = 0;
for (Object object : paramSet) {

if (object != null) i ^= object.hashCode(); 
} 
return i;
}
}

