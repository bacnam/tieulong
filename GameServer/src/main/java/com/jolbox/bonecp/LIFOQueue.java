package com.jolbox.bonecp;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import jsr166y.TransferQueue;

public class LIFOQueue<E>
extends LinkedBlockingDeque<E>
implements TransferQueue<E>
{
private static final long serialVersionUID = -3503791017846313243L;

public LIFOQueue(int capacity) {
super(capacity);
}

public LIFOQueue() {}

public boolean tryTransfer(E e) {
return offerFirst(e);
}

public void transfer(E e) throws InterruptedException {
putFirst(e);
}

public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
return offerFirst(e, timeout, unit);
}

public boolean hasWaitingConsumer() {
return false;
}

public int getWaitingConsumerCount() {
return 0;
}

public boolean offer(E e) {
return offerFirst(e);
}
}

