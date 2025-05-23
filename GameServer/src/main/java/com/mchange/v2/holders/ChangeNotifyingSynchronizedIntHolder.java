package com.mchange.v2.holders;

import com.mchange.v2.ser.UnsupportedVersionException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class ChangeNotifyingSynchronizedIntHolder
implements ThreadSafeIntHolder, Serializable
{
transient int value;
transient boolean notify_all;
static final long serialVersionUID = 1L;
private static final short VERSION = 1;

public ChangeNotifyingSynchronizedIntHolder(int paramInt, boolean paramBoolean) {
this.value = paramInt;
this.notify_all = paramBoolean;
}

public ChangeNotifyingSynchronizedIntHolder() {
this(0, true);
}
public synchronized int getValue() {
return this.value;
}

public synchronized void setValue(int paramInt) {
if (paramInt != this.value) {

this.value = paramInt;
doNotify();
} 
}

public synchronized void increment() {
this.value++;
doNotify();
}

public synchronized void decrement() {
this.value--;
doNotify();
}

private void doNotify() {
if (this.notify_all) { notifyAll(); }
else { notify(); }

}

private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
paramObjectOutputStream.writeShort(1);
paramObjectOutputStream.writeInt(this.value);
paramObjectOutputStream.writeBoolean(this.notify_all);
}

private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
short s = paramObjectInputStream.readShort();
switch (s) {

case 1:
this.value = paramObjectInputStream.readInt();
this.notify_all = paramObjectInputStream.readBoolean();
return;
} 
throw new UnsupportedVersionException(this, s);
}
}

