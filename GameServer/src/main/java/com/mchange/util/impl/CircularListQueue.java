package com.mchange.util.impl;

import com.mchange.util.Queue;

public class CircularListQueue
implements Queue, Cloneable
{
CircularList list;

public int size() {
return this.list.size();
} public boolean hasMoreElements() { return (this.list.size() > 0); }
public void enqueue(Object paramObject) { this.list.appendElement(paramObject); } public Object peek() {
return this.list.getFirstElement();
}
public Object dequeue() {
Object object = this.list.getFirstElement();
this.list.removeFirstElement();
return object;
}

public Object clone() {
return new CircularListQueue((CircularList)this.list.clone());
}
public CircularListQueue() {
this.list = new CircularList();
}
private CircularListQueue(CircularList paramCircularList) {
this.list = paramCircularList;
}
}

