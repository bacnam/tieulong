package com.mchange.v1.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class WrapperIterator
implements Iterator
{
protected static final Object SKIP_TOKEN = new Object();

static final boolean DEBUG = true;

Iterator inner;
boolean supports_remove;
Object lastOut = null;
Object nextOut = SKIP_TOKEN;

public WrapperIterator(Iterator paramIterator, boolean paramBoolean) {
this.inner = paramIterator;
this.supports_remove = paramBoolean;
}

public WrapperIterator(Iterator paramIterator) {
this(paramIterator, false);
}

public boolean hasNext() {
findNext();
return (this.nextOut != SKIP_TOKEN);
}

private void findNext() {
if (this.nextOut == SKIP_TOKEN)
{
while (this.inner.hasNext() && this.nextOut == SKIP_TOKEN) {
this.nextOut = transformObject(this.inner.next());
}
}
}

public Object next() {
findNext();
if (this.nextOut != SKIP_TOKEN) {

this.lastOut = this.nextOut;
this.nextOut = SKIP_TOKEN;
} else {

throw new NoSuchElementException();
} 

DebugUtils.myAssert((this.nextOut == SKIP_TOKEN && this.lastOut != SKIP_TOKEN));
return this.lastOut;
}

public void remove() {
if (this.supports_remove) {

if (this.nextOut != SKIP_TOKEN) {
throw new UnsupportedOperationException(getClass().getName() + " cannot support remove after" + " hasNext() has been called!");
}

if (this.lastOut != SKIP_TOKEN) {
this.inner.remove();
} else {
throw new NoSuchElementException();
} 
} else {
throw new UnsupportedOperationException();
} 
}

protected abstract Object transformObject(Object paramObject);
}

