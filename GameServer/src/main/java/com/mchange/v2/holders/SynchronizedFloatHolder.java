package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SynchronizedFloatHolder
implements ThreadSafeFloatHolder, Serializable
{
transient float value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public synchronized float getValue() {
return this.value;
}
public synchronized void setValue(float paramFloat) {
this.value = paramFloat;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeFloat(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readFloat();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

