package com.mchange.v1.lang.holders;

public class VolatileCharHolder
implements ThreadSafeCharHolder
{
volatile char value;

public char getValue() {
return this.value;
}
public void setValue(char paramChar) {
this.value = paramChar;
}
}

