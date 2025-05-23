package com.mchange.v1.util;

import java.util.NoSuchElementException;

public abstract class WrapperUIterator
implements UIterator
{
protected static final Object SKIP_TOKEN = new Object();

static final boolean DEBUG = true;

UIterator inner;
boolean supports_remove;
Object lastOut = null;
Object nextOut = SKIP_TOKEN;

public WrapperUIterator(UIterator paramUIterator, boolean paramBoolean) {
this.inner = paramUIterator;
this.supports_remove = paramBoolean;
}

public WrapperUIterator(UIterator paramUIterator) {
this(paramUIterator, false);
}

public boolean hasNext() throws Exception {
findNext();
return (this.nextOut != SKIP_TOKEN);
}

private void findNext() throws Exception {
if (this.nextOut == SKIP_TOKEN)
{
while (this.inner.hasNext() && this.nextOut == SKIP_TOKEN) {
this.nextOut = transformObject(this.inner.next());
}
}
}

public Object next() throws NoSuchElementException, Exception {
findNext();
if (this.nextOut != SKIP_TOKEN) {

this.lastOut = this.nextOut;
this.nextOut = SKIP_TOKEN;
} else {

throw new NoSuchElementException();
} 

DebugUtils.myAssert((this.nextOut == SKIP_TOKEN && this.lastOut != SKIP_TOKEN));

assert this.nextOut == SKIP_TOKEN && this.lastOut != SKIP_TOKEN;

return this.lastOut;
}

public void remove() throws Exception {
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
public void close() throws Exception {
this.inner.close();
}

protected abstract Object transformObject(Object paramObject) throws Exception;
}

