package com.mchange.v1.lang.holders;

public class SynchronizedBooleanHolder
implements ThreadSafeBooleanHolder
{
boolean value;

public synchronized boolean getValue() {
return this.value;
}
public synchronized void setValue(boolean paramBoolean) {
this.value = paramBoolean;
}
}

