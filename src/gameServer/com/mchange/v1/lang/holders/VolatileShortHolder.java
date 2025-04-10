package com.mchange.v1.lang.holders;

public class VolatileShortHolder
implements ThreadSafeShortHolder
{
volatile short value;

public short getValue() {
return this.value;
}
public void setValue(short paramShort) {
this.value = paramShort;
}
}

