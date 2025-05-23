package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SynchronizedIntHolder
implements ThreadSafeIntHolder, Serializable
{
transient int value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public SynchronizedIntHolder(int paramInt) {
this.value = paramInt;
}
public SynchronizedIntHolder() {
this(0);
}
public synchronized int getValue() {
return this.value;
}
public synchronized void setValue(int paramInt) {
this.value = paramInt;
}
public synchronized void increment() {
this.value++;
}
public synchronized void decrement() {
this.value--;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeInt(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readInt();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

