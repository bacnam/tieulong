package com.mchange.util.impl;

class IOHRecord
extends IOHRecElem
{
IntObjectHash parent;
int size = 0;

IOHRecord(int paramInt) {
super(paramInt, null, null);
}

IOHRecElem findInt(int paramInt) {
for (IOHRecord iOHRecord = this; iOHRecord.next != null; iOHRecElem = iOHRecord.next) {
IOHRecElem iOHRecElem; if (iOHRecord.next.num == paramInt) return iOHRecord; 
}  return null;
}

boolean add(int paramInt, Object paramObject, boolean paramBoolean) {
IOHRecElem iOHRecElem = findInt(paramInt);
if (iOHRecElem != null) {

if (paramBoolean)
iOHRecElem.next = new IOHRecElem(paramInt, paramObject, iOHRecElem.next.next); 
return true;
} 

this.next = new IOHRecElem(paramInt, paramObject, this.next);
this.size++;
return false;
}

Object remove(int paramInt) {
IOHRecElem iOHRecElem = findInt(paramInt);
if (iOHRecElem == null) return null;

Object object = iOHRecElem.next.obj;
iOHRecElem.next = iOHRecElem.next.next;
this.size--;
if (this.size == 0)
this.parent.records[this.num] = null; 
return object;
}

Object get(int paramInt) {
IOHRecElem iOHRecElem = findInt(paramInt);
if (iOHRecElem != null)
return iOHRecElem.next.obj; 
return null;
}

IOHRecord split(int paramInt) {
IOHRecord iOHRecord1 = null;
IOHRecord iOHRecord2 = null;
for (IOHRecord iOHRecord3 = this; iOHRecord3.next != null; iOHRecElem = iOHRecord3.next) {
IOHRecElem iOHRecElem;
if (Math.abs(iOHRecord3.next.num % paramInt) != this.num) {

if (iOHRecord1 == null) {

iOHRecord1 = new IOHRecord(this.num * 2);
iOHRecord2 = iOHRecord1;
} 
iOHRecord2.next = iOHRecord3.next;
iOHRecord3.next = iOHRecord3.next.next;
IOHRecElem iOHRecElem1 = iOHRecord2.next;
iOHRecElem1.next = null;
} 
} 
return iOHRecord1;
}
}

