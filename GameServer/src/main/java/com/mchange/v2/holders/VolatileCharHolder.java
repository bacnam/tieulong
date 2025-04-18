package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VolatileCharHolder
implements ThreadSafeCharHolder, Serializable
{
volatile transient char value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public char getValue() {
return this.value;
}
public void setValue(char paramChar) {
this.value = paramChar;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeChar(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readChar();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

