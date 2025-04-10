package com.mchange.v1.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IteratorUtils
{
public static final Iterator EMPTY_ITERATOR = new Iterator()
{
public boolean hasNext() {
return false;
}
public Object next() {
throw new NoSuchElementException();
}
public void remove() {
throw new IllegalStateException();
}
};

public static Iterator oneElementUnmodifiableIterator(final Object elem) {
return new Iterator() {
boolean shot = false;

public boolean hasNext() {
return !this.shot;
}

public Object next() {
if (this.shot) {
throw new NoSuchElementException();
}

this.shot = true;
return elem;
}

public void remove() {
throw new UnsupportedOperationException("remove() not supported.");
}
};
}

public static boolean equivalent(Iterator<Object> paramIterator1, Iterator<Object> paramIterator2) {
while (true) {
boolean bool1 = paramIterator1.hasNext();
boolean bool2 = paramIterator2.hasNext();
if (bool1 ^ bool2)
return false; 
if (bool1) {

Object object1 = paramIterator1.next();
Object object2 = paramIterator2.next();
if (object1 == object2)
continue; 
if (object1 == null)
return false; 
if (!object1.equals(object2))
return false;  continue;
}  break;
}  return true;
}

public static ArrayList toArrayList(Iterator paramIterator, int paramInt) {
ArrayList arrayList = new ArrayList(paramInt);
while (paramIterator.hasNext())
arrayList.add(paramIterator.next()); 
return arrayList;
}

public static void fillArray(Iterator paramIterator, Object[] paramArrayOfObject, boolean paramBoolean) {
byte b = 0;
int i = paramArrayOfObject.length;
while (b < i && paramIterator.hasNext())
paramArrayOfObject[b++] = paramIterator.next(); 
if (paramBoolean && b < i)
paramArrayOfObject[b] = null; 
}

public static void fillArray(Iterator paramIterator, Object[] paramArrayOfObject) {
fillArray(paramIterator, paramArrayOfObject, false);
}

public static Object[] toArray(Iterator paramIterator, int paramInt, Class<?> paramClass, boolean paramBoolean) {
Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
fillArray(paramIterator, arrayOfObject, paramBoolean);
return arrayOfObject;
}

public static Object[] toArray(Iterator paramIterator, int paramInt, Class paramClass) {
return toArray(paramIterator, paramInt, paramClass, false);
}

public static Object[] toArray(Iterator paramIterator, int paramInt, Object[] paramArrayOfObject) {
if (paramArrayOfObject.length >= paramInt) {

fillArray(paramIterator, paramArrayOfObject, true);
return paramArrayOfObject;
} 

Class<?> clazz = paramArrayOfObject.getClass().getComponentType();
return toArray(paramIterator, paramInt, clazz);
}
}

