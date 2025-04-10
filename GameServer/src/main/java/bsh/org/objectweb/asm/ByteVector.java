package bsh.org.objectweb.asm;

final class ByteVector
{
byte[] data;
int length;

public ByteVector() {
this.data = new byte[64];
}

public ByteVector(int initialSize) {
this.data = new byte[initialSize];
}

public ByteVector put1(int b) {
int length = this.length;
if (length + 1 > this.data.length) {
enlarge(1);
}
this.data[length++] = (byte)b;
this.length = length;
return this;
}

public ByteVector put11(int b1, int b2) {
int length = this.length;
if (length + 2 > this.data.length) {
enlarge(2);
}
byte[] data = this.data;
data[length++] = (byte)b1;
data[length++] = (byte)b2;
this.length = length;
return this;
}

public ByteVector put2(int s) {
int length = this.length;
if (length + 2 > this.data.length) {
enlarge(2);
}
byte[] data = this.data;
data[length++] = (byte)(s >>> 8);
data[length++] = (byte)s;
this.length = length;
return this;
}

public ByteVector put12(int b, int s) {
int length = this.length;
if (length + 3 > this.data.length) {
enlarge(3);
}
byte[] data = this.data;
data[length++] = (byte)b;
data[length++] = (byte)(s >>> 8);
data[length++] = (byte)s;
this.length = length;
return this;
}

public ByteVector put4(int i) {
int length = this.length;
if (length + 4 > this.data.length) {
enlarge(4);
}
byte[] data = this.data;
data[length++] = (byte)(i >>> 24);
data[length++] = (byte)(i >>> 16);
data[length++] = (byte)(i >>> 8);
data[length++] = (byte)i;
this.length = length;
return this;
}

public ByteVector put8(long l) {
int length = this.length;
if (length + 8 > this.data.length) {
enlarge(8);
}
byte[] data = this.data;
int i = (int)(l >>> 32L);
data[length++] = (byte)(i >>> 24);
data[length++] = (byte)(i >>> 16);
data[length++] = (byte)(i >>> 8);
data[length++] = (byte)i;
i = (int)l;
data[length++] = (byte)(i >>> 24);
data[length++] = (byte)(i >>> 16);
data[length++] = (byte)(i >>> 8);
data[length++] = (byte)i;
this.length = length;
return this;
}

public ByteVector putUTF(String s) {
int charLength = s.length();
int byteLength = 0;
for (int i = 0; i < charLength; i++) {
char c = s.charAt(i);
if (c >= '\001' && c <= '') {
byteLength++;
} else if (c > '߿') {
byteLength += 3;
} else {
byteLength += 2;
} 
} 
if (byteLength > 65535) {
throw new IllegalArgumentException();
}
int length = this.length;
if (length + 2 + byteLength > this.data.length) {
enlarge(2 + byteLength);
}
byte[] data = this.data;
data[length++] = (byte)(byteLength >>> 8);
data[length++] = (byte)byteLength;
for (int j = 0; j < charLength; j++) {
char c = s.charAt(j);
if (c >= '\001' && c <= '') {
data[length++] = (byte)c;
} else if (c > '߿') {
data[length++] = (byte)(0xE0 | c >> 12 & 0xF);
data[length++] = (byte)(0x80 | c >> 6 & 0x3F);
data[length++] = (byte)(0x80 | c & 0x3F);
} else {
data[length++] = (byte)(0xC0 | c >> 6 & 0x1F);
data[length++] = (byte)(0x80 | c & 0x3F);
} 
} 
this.length = length;
return this;
}

public ByteVector putByteArray(byte[] b, int off, int len) {
if (this.length + len > this.data.length) {
enlarge(len);
}
if (b != null) {
System.arraycopy(b, off, this.data, this.length, len);
}
this.length += len;
return this;
}

private void enlarge(int size) {
byte[] newData = new byte[Math.max(2 * this.data.length, this.length + size)];
System.arraycopy(this.data, 0, newData, 0, this.length);
this.data = newData;
}
}

