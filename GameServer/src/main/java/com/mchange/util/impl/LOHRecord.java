package com.mchange.util.impl;

class LOHRecord
extends LOHRecElem
{
LongObjectHash parent;
int size = 0;

LOHRecord(long paramLong) {
super(paramLong, null, null);
}

LOHRecElem findLong(long paramLong) {
for (LOHRecord lOHRecord = this; lOHRecord.next != null; lOHRecElem = lOHRecord.next) {
LOHRecElem lOHRecElem; if (lOHRecord.next.num == paramLong) return lOHRecord; 
}  return null;
}

boolean add(long paramLong, Object paramObject, boolean paramBoolean) {
LOHRecElem lOHRecElem = findLong(paramLong);
if (lOHRecElem != null) {

if (paramBoolean)
lOHRecElem.next = new LOHRecElem(paramLong, paramObject, lOHRecElem.next.next); 
return true;
} 

this.next = new LOHRecElem(paramLong, paramObject, this.next);
this.size++;
return false;
}

Object remove(long paramLong) {
LOHRecElem lOHRecElem = findLong(paramLong);
if (lOHRecElem == null) return null;

Object object = lOHRecElem.next.obj;
lOHRecElem.next = lOHRecElem.next.next;
this.size--;
if (this.size == 0)
this.parent.records[(int)this.num] = null; 
return object;
}

Object get(long paramLong) {
LOHRecElem lOHRecElem = findLong(paramLong);
if (lOHRecElem != null)
return lOHRecElem.next.obj; 
return null;
}

LOHRecord split(int paramInt) {
LOHRecord lOHRecord1 = null;
LOHRecord lOHRecord2 = null;
for (LOHRecord lOHRecord3 = this; lOHRecord3.next != null; lOHRecElem = lOHRecord3.next) {
LOHRecElem lOHRecElem;
if (lOHRecord3.next.num % paramInt != this.num) {

if (lOHRecord1 == null) {

lOHRecord1 = new LOHRecord(this.num * 2L);
lOHRecord2 = lOHRecord1;
} 
lOHRecord2.next = lOHRecord3.next;
lOHRecord3.next = lOHRecord3.next.next;
LOHRecElem lOHRecElem1 = lOHRecord2.next;
lOHRecElem1.next = null;
} 
} 
return lOHRecord1;
}
}

