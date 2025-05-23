package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SynchronizedDoubleHolder
implements ThreadSafeDoubleHolder, Serializable
{
transient double value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public synchronized double getValue() {
return this.value;
}
public synchronized void setValue(double paramDouble) {
this.value = paramDouble;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeDouble(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readDouble();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

