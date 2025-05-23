package com.mchange.v1.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class JoinedIterator
implements Iterator
{
Iterator[] its;
Iterator removeIterator = null;
boolean permit_removes;
int cur = 0;

public JoinedIterator(Iterator[] paramArrayOfIterator, boolean paramBoolean) {
this.its = paramArrayOfIterator;
this.permit_removes = paramBoolean;
}

public boolean hasNext() {
if (this.cur == this.its.length)
return false; 
if (this.its[this.cur].hasNext()) {
return true;
}

this.cur++;
return hasNext();
}

public Object next() {
if (!hasNext()) {
throw new NoSuchElementException();
}
this.removeIterator = this.its[this.cur];
return this.removeIterator.next();
}

public void remove() {
if (this.permit_removes) {

if (this.removeIterator != null) {

this.removeIterator.remove();
this.removeIterator = null;
} else {

throw new IllegalStateException("next() not called, or element already removed.");
} 
} else {
throw new UnsupportedOperationException();
} 
}
}

