package com.mchange.util.impl;

import java.util.Enumeration;
import java.util.NoSuchElementException;

class CircularListEnumeration
implements Enumeration
{
boolean forward;
boolean terminated;
boolean done;
CircularListRecord startRecord;
CircularListRecord lastRecord;

CircularListEnumeration(CircularList paramCircularList, boolean paramBoolean1, boolean paramBoolean2) {
if (paramCircularList.firstRecord == null) {
this.done = true;
} else {

this.done = false;
this.forward = paramBoolean1;
this.terminated = paramBoolean2;
this.startRecord = paramBoolean1 ? paramCircularList.firstRecord : paramCircularList.firstRecord.prev;
this.lastRecord = paramBoolean1 ? this.startRecord.prev : this.startRecord;
} 
}

public boolean hasMoreElements() {
return !this.done;
}

public Object nextElement() {
if (this.done) {
throw new NoSuchElementException();
}

this.lastRecord = this.forward ? this.lastRecord.next : this.lastRecord.prev;
if (this.terminated && this.lastRecord == (this.forward ? this.startRecord.prev : this.startRecord))
this.done = true; 
return this.lastRecord.object;
}
}

