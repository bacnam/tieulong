package com.mchange.util.impl;

import com.mchange.util.IntEnumeration;
import com.mchange.util.IntObjectMap;

import java.util.NoSuchElementException;

public class IntObjectHash
        implements IntObjectMap {
    IOHRecord[] records;
    int init_capacity;
    float load_factor;
    int threshold;
    int size;

    public IntObjectHash(int paramInt, float paramFloat) {
        this.init_capacity = paramInt;
        this.load_factor = paramFloat;
        clear();
    }

    public IntObjectHash() {
        this(101, 0.75F);
    }

    public synchronized Object get(int paramInt) {
        int i = getIndex(paramInt);
        Object object = null;
        if (this.records[i] != null)
            object = this.records[i].get(paramInt);
        return object;
    }

    public synchronized void put(int paramInt, Object paramObject) {
        if (paramObject == null)
            throw new NullPointerException("Null values not permitted.");
        int i = getIndex(paramInt);
        if (this.records[i] == null)
            this.records[i] = new IOHRecord(i);
        boolean bool = this.records[i].add(paramInt, paramObject, true);
        if (!bool) this.size++;
        if (this.size > this.threshold) rehash();

    }

    public synchronized boolean putNoReplace(int paramInt, Object paramObject) {
        if (paramObject == null)
            throw new NullPointerException("Null values not permitted.");
        int i = getIndex(paramInt);
        if (this.records[i] == null)
            this.records[i] = new IOHRecord(i);
        boolean bool = this.records[i].add(paramInt, paramObject, false);
        if (bool) {
            return false;
        }

        this.size++;
        if (this.size > this.threshold) rehash();
        return true;
    }

    public int getSize() {
        return this.size;
    }

    public synchronized boolean containsInt(int paramInt) {
        int i = getIndex(paramInt);
        return (this.records[i] != null && this.records[i].findInt(paramInt) != null);
    }

    private int getIndex(int paramInt) {
        return Math.abs(paramInt % this.records.length);
    }

    public synchronized Object remove(int paramInt) {
        IOHRecord iOHRecord = this.records[getIndex(paramInt)];
        Object object = (iOHRecord == null) ? null : iOHRecord.remove(paramInt);
        if (object != null) this.size--;
        return object;
    }

    public synchronized void clear() {
        this.records = new IOHRecord[this.init_capacity];
        this.threshold = (int) (this.load_factor * this.init_capacity);
        this.size = 0;
    }

    public synchronized IntEnumeration ints() {
        return new IntEnumerationHelperBase() {
            int index;

            IOHRecElem finger;

            public boolean hasMoreInts() {
                return (this.index < IntObjectHash.this.records.length);
            }

            public int nextInt() {
                try {
                    int i = this.finger.num;
                    findNext();
                    return i;
                } catch (NullPointerException nullPointerException) {
                    throw new NoSuchElementException();
                }
            }

            private void findNext() {
                if (this.finger.next != null) {
                    this.finger = this.finger.next;
                } else {
                    nextIndex();
                }

            }

            private void nextIndex() {
                try {
                    int i = IntObjectHash.this.records.length;
                    do {
                        this.index++;
                    } while (IntObjectHash.this.records[this.index] == null && this.index <= i);
                    this.finger = (IntObjectHash.this.records[this.index]).next;
                } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {

                    this.finger = null;
                }
            }
        };
    }

    protected void rehash() {
        IOHRecord[] arrayOfIOHRecord = new IOHRecord[this.records.length * 2];
        for (byte b = 0; b < this.records.length; b++) {

            if (this.records[b] != null) {

                arrayOfIOHRecord[b] = this.records[b];
                arrayOfIOHRecord[b * 2] = this.records[b].split(arrayOfIOHRecord.length);
            }
        }
        this.records = arrayOfIOHRecord;
        this.threshold = (int) (this.load_factor * this.records.length);
    }
}

