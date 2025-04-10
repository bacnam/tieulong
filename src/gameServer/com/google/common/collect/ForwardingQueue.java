package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue<E>
extends ForwardingCollection<E>
implements Queue<E>
{
public boolean offer(E o) {
return delegate().offer(o);
}

public E poll() {
return delegate().poll();
}

public E remove() {
return delegate().remove();
}

public E peek() {
return delegate().peek();
}

public E element() {
return delegate().element();
}

@Beta
protected boolean standardOffer(E e) {
try {
return add(e);
} catch (IllegalStateException caught) {
return false;
} 
}

@Beta
protected E standardPeek() {
try {
return element();
} catch (NoSuchElementException caught) {
return null;
} 
}

@Beta
protected E standardPoll() {
try {
return remove();
} catch (NoSuchElementException caught) {
return null;
} 
}

protected abstract Queue<E> delegate();
}

