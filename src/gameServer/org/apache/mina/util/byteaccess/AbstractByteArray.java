package org.apache.mina.util.byteaccess;

abstract class AbstractByteArray
implements ByteArray
{
public final int length() {
return last() - first();
}

public final boolean equals(Object other) {
if (other == this) {
return true;
}

if (!(other instanceof ByteArray)) {
return false;
}
ByteArray otherByteArray = (ByteArray)other;

if (first() != otherByteArray.first() || last() != otherByteArray.last() || !order().equals(otherByteArray.order()))
{
return false;
}

ByteArray.Cursor cursor = cursor();
ByteArray.Cursor otherCursor = otherByteArray.cursor();
for (int remaining = cursor.getRemaining(); remaining > 0; ) {

if (remaining >= 4) {
int i = cursor.getInt();
int otherI = otherCursor.getInt();
if (i != otherI)
return false; 
continue;
} 
byte b = cursor.get();
byte otherB = otherCursor.get();
if (b != otherB) {
return false;
}
} 

return true;
}
}

