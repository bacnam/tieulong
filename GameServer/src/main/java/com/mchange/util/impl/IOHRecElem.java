package com.mchange.util.impl;

class IOHRecElem {
    int num;
    Object obj;
    IOHRecElem next;

    IOHRecElem(int paramInt, Object paramObject, IOHRecElem paramIOHRecElem) {
        this.num = paramInt;
        this.obj = paramObject;
        this.next = paramIOHRecElem;
    }
}

