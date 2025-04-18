package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SynchronizedLongHolder
implements ThreadSafeLongHolder, Serializable
{
transient long value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

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

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeLong(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readLong();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

