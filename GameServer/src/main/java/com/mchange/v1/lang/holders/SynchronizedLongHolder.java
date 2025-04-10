package com.mchange.v1.lang.holders;

public class SynchronizedLongHolder
implements ThreadSafeLongHolder
{
long value;

public synchronized long getValue() {
return this.value;
}
public synchronized void setValue(long paramLong) {
this.value = paramLong;
}
public SynchronizedLongHolder(long paramLong) {
this.value = paramLong;
}
public SynchronizedLongHolder() {
this(0L);
}
}

