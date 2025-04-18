package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VolatileByteHolder
implements ThreadSafeByteHolder, Serializable
{
volatile transient byte value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public byte getValue() {
return this.value;
}
public void setValue(byte paramByte) {
this.value = paramByte;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeByte(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readByte();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

