package com.mchange.v1.lang.holders;

public class SynchronizedCharHolder
implements ThreadSafeCharHolder
{
char value;

public synchronized char getValue() {
return this.value;
}
public synchronized void setValue(char paramChar) {
this.value = paramChar;
}
}

