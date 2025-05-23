package com.mchange.util.impl;

import com.mchange.util.LongObjectMap;

public class LongObjectHash
implements LongObjectMap
{
LOHRecord[] records;
float load_factor;
long threshold;
long size;

public LongObjectHash(int paramInt, float paramFloat) {
this.records = new LOHRecord[paramInt];
this.load_factor = paramFloat;
this.threshold = (long)(paramFloat * paramInt);
}

public LongObjectHash() {
this(101, 0.75F);
}

public synchronized Object get(long paramLong) {
int i = (int)(paramLong % this.records.length);
Object object = null;
if (this.records[i] != null)
object = this.records[i].get(paramLong); 
return object;
}

public synchronized void put(long paramLong, Object paramObject) {
int i = (int)(paramLong % this.records.length);
if (this.records[i] == null)
this.records[i] = new LOHRecord(i); 
boolean bool = this.records[i].add(paramLong, paramObject, true);
if (!bool) this.size++; 
if (this.size > this.threshold) rehash();

}

public synchronized boolean putNoReplace(long paramLong, Object paramObject) {
int i = (int)(paramLong % this.records.length);
if (this.records[i] == null)
this.records[i] = new LOHRecord(i); 
boolean bool = this.records[i].add(paramLong, paramObject, false);
if (bool) {
return false;
}

this.size++;
if (this.size > this.threshold) rehash(); 
return true;
}

public long getSize() {
return this.size;
}

public synchronized boolean containsLong(long paramLong) {
int i = (int)(paramLong % this.records.length);
return (this.records[i] != null && this.records[i].findLong(paramLong) != null);
}

public synchronized Object remove(long paramLong) {
LOHRecord lOHRecord = this.records[(int)(paramLong % this.records.length)];
Object object = (lOHRecord == null) ? null : lOHRecord.remove(paramLong);
if (object != null) this.size--; 
return object;
}

protected void rehash() {
if (this.records.length * 2L > 2147483647L) {
throw new Error("Implementation of LongObjectHash allows a capacity of only 2147483647");
}
LOHRecord[] arrayOfLOHRecord = new LOHRecord[this.records.length * 2];
for (byte b = 0; b < this.records.length; b++) {

if (this.records[b] != null) {

arrayOfLOHRecord[b] = this.records[b];
arrayOfLOHRecord[b * 2] = this.records[b].split(arrayOfLOHRecord.length);
} 
} 
this.records = arrayOfLOHRecord;
this.threshold = (long)(this.load_factor * this.records.length);
}
}

