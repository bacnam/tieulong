package com.mchange.util.impl;

import com.mchange.util.IntChecklist;
import com.mchange.util.IntEnumeration;

import java.util.NoSuchElementException;

public class LinkedListIntChecklistImpl
        implements IntChecklist {
    private final LLICIRecord headRecord = new LLICIRecord();
    private int num_checked = 0;

    public void check(int paramInt) {
        LLICIRecord lLICIRecord = findPrevious(paramInt);
        if (lLICIRecord.next == null || lLICIRecord.next.contained != paramInt) {

            LLICIRecord lLICIRecord1 = new LLICIRecord();
            lLICIRecord1.next = lLICIRecord.next;
            lLICIRecord1.contained = paramInt;
            lLICIRecord.next = lLICIRecord1;
            this.num_checked++;
        }
    }

    public void uncheck(int paramInt) {
        LLICIRecord lLICIRecord = findPrevious(paramInt);
        if (lLICIRecord.next != null && lLICIRecord.next.contained == paramInt) {

            lLICIRecord.next = lLICIRecord.next.next;
            this.num_checked--;
        }
    }

    public boolean isChecked(int paramInt) {
        LLICIRecord lLICIRecord = findPrevious(paramInt);
        return (lLICIRecord.next != null && lLICIRecord.next.contained == paramInt);
    }

    public void clear() {
        this.headRecord.next = null;
        this.num_checked = 0;
    }

    public int countChecked() {
        return this.num_checked;
    }

    public int[] getChecked() {
        LLICIRecord lLICIRecord = this.headRecord;
        int[] arrayOfInt = new int[this.num_checked];
        byte b = 0;
        while (lLICIRecord.next != null) {

            arrayOfInt[b++] = lLICIRecord.next.contained;
            lLICIRecord = lLICIRecord.next;
        }
        return arrayOfInt;
    }

    public IntEnumeration checked() {
        return new IntEnumerationHelperBase() {
            LLICIRecord finger = LinkedListIntChecklistImpl.this.headRecord;

            public int nextInt() {
                try {
                    this.finger = this.finger.next;
                    return this.finger.contained;
                } catch (NullPointerException nullPointerException) {
                    throw new NoSuchElementException();
                }
            }

            public boolean hasMoreInts() {
                return (this.finger.next != null);
            }
        };
    }

    private LLICIRecord findPrevious(int paramInt) {
        LLICIRecord lLICIRecord = this.headRecord;
        while (lLICIRecord.next != null && lLICIRecord.next.contained < paramInt)
            lLICIRecord = lLICIRecord.next;
        return lLICIRecord;
    }
}

