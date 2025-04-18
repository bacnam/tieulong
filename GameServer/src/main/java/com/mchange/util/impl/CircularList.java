package com.mchange.util.impl;

import java.util.Enumeration;

public class CircularList
implements Cloneable
{
CircularListRecord firstRecord = null;
int size = 0;

private void addElement(Object paramObject, boolean paramBoolean) {
if (this.firstRecord == null) {
this.firstRecord = new CircularListRecord(paramObject);
} else {

CircularListRecord circularListRecord = new CircularListRecord(paramObject, this.firstRecord.prev, this.firstRecord);
this.firstRecord.prev.next = circularListRecord;
this.firstRecord.prev = circularListRecord;
if (paramBoolean) this.firstRecord = circularListRecord; 
} 
this.size++;
}

private void removeElement(boolean paramBoolean) {
if (this.size == 1) {
this.firstRecord = null;
} else {

if (paramBoolean) this.firstRecord = this.firstRecord.next; 
zap(this.firstRecord.prev);
} 
this.size--;
}

private void zap(CircularListRecord paramCircularListRecord) {
paramCircularListRecord.next.prev = paramCircularListRecord.prev;
paramCircularListRecord.prev.next = paramCircularListRecord.next;
}

public void appendElement(Object paramObject) {
addElement(paramObject, false);
}
public void addElementToFront(Object paramObject) {
addElement(paramObject, true);
}
public void removeFirstElement() {
removeElement(true);
}
public void removeLastElement() {
removeElement(false);
}

public void removeFromFront(int paramInt) {
if (paramInt > this.size)
throw new IndexOutOfBoundsException(paramInt + ">" + this.size); 
for (byte b = 0; b < paramInt; ) { removeElement(true); b++; }

}

public void removeFromBack(int paramInt) {
if (paramInt > this.size)
throw new IndexOutOfBoundsException(paramInt + ">" + this.size); 
for (byte b = 0; b < paramInt; ) { removeElement(false); b++; }

}

public void removeAllElements() {
this.size = 0;
this.firstRecord = null;
}

public Object getElementFromFront(int paramInt) {
if (paramInt >= this.size) {
throw new IndexOutOfBoundsException(paramInt + ">=" + this.size);
}

CircularListRecord circularListRecord = this.firstRecord;
for (byte b = 0; b < paramInt; b++)
circularListRecord = circularListRecord.next; 
return circularListRecord.object;
}

public Object getElementFromBack(int paramInt) {
if (paramInt >= this.size) {
throw new IndexOutOfBoundsException(paramInt + ">=" + this.size);
}

CircularListRecord circularListRecord = this.firstRecord.prev;
for (byte b = 0; b < paramInt; b++)
circularListRecord = circularListRecord.prev; 
return circularListRecord.object;
}

public Object getFirstElement() {
try {
return this.firstRecord.object;
} catch (NullPointerException nullPointerException) {
throw new IndexOutOfBoundsException("CircularList is empty.");
} 
}
public Object getLastElement() {
try {
return this.firstRecord.prev.object;
} catch (NullPointerException nullPointerException) {
throw new IndexOutOfBoundsException("CircularList is empty.");
} 
}

public Enumeration elements(boolean paramBoolean1, boolean paramBoolean2) {
return new CircularListEnumeration(this, paramBoolean1, paramBoolean2);
}
public Enumeration elements(boolean paramBoolean) {
return elements(paramBoolean, true);
}
public Enumeration elements() {
return elements(true, true);
}
public int size() {
return this.size;
}

public Object clone() {
CircularList circularList = new CircularList();
int i = size();
for (byte b = 0; b < i; b++)
circularList.appendElement(getElementFromFront(b)); 
return circularList;
}

public static void main(String[] paramArrayOfString) {
CircularList circularList = new CircularList();
circularList.appendElement("Hello");
circularList.appendElement("There");
circularList.appendElement("Joe.");
for (Enumeration<String> enumeration = circularList.elements(); enumeration.hasMoreElements();)
System.out.println("x " + enumeration.nextElement()); 
}
}

