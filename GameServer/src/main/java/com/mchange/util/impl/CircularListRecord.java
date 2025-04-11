package com.mchange.util.impl;

class CircularListRecord {
    Object object;
    CircularListRecord next;
    CircularListRecord prev;

    CircularListRecord(Object paramObject, CircularListRecord paramCircularListRecord1, CircularListRecord paramCircularListRecord2) {
        this.object = paramObject;
        this.prev = paramCircularListRecord1;
        this.next = paramCircularListRecord2;
    }

    CircularListRecord(Object paramObject) {
        this.object = paramObject;
        this.prev = this;
        this.next = this;
    }
}

