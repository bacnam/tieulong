package com.mchange.v1.lang.holders;

public class VolatileIntHolder
implements ThreadSafeIntHolder
{
volatile int value;

public int getValue() {
return this.value;
}
public void setValue(int paramInt) {
this.value = paramInt;
}
}

