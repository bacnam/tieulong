package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VolatileBooleanHolder
implements ThreadSafeBooleanHolder, Serializable
{
volatile transient boolean value;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public boolean getValue() {
return this.value;
}
public void setValue(boolean paramBoolean) {
this.value = paramBoolean;
}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeBoolean(this.value);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readBoolean();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

