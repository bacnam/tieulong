package com.mchange.v1.lang.holders;

public class SynchronizedFloatHolder
implements ThreadSafeFloatHolder
{
float value;

public synchronized float getValue() {
return this.value;
}
public synchronized void setValue(float paramFloat) {
this.value = paramFloat;
}
}

