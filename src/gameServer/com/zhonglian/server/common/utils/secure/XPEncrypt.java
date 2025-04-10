package com.zhonglian.server.common.utils.secure;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

public class XPEncrypt
{
public static final int ALIGN = 8;
public static final int MAGIC = 1164408404;
private byte[] eData;
private ByteBuffer eBuffer;
private int eSize;
private int key;

public XPEncrypt(int key) {
this.key = key;
}

public ByteBuffer encrypt(ByteBuffer data) {
return encrypt(data.array());
}

public ByteBuffer encrypt(byte[] data) {
if (data == null) {
return null;
}

this.eData = data;
paddingAndMakeBuffer();
return doEncrypt();
}

public ByteBuffer decrypt(byte[] data) {
ByteBuffer dst = ByteBuffer.allocate(data.length);
ByteBuffer src = ByteBuffer.wrap(data);
XPEncryptGroup group = new XPEncryptGroup(this.key);

if (data.length % 8 != 0) {
return null;
}

while (src.hasRemaining()) {
dst.putLong(group.decode(src.getLong()));
}

int dataSize = dst.getInt(data.length - 12);

int sum = 0;
dst.rewind();
while (dst.remaining() > 8) {
sum += dst.getInt();
}

if (dst.getInt(data.length - 8) != 1164408404 || dst.getInt(data.length - 4) != sum)
{

return null;
}

dst.limit(dataSize);
dst.rewind();
return dst;
}

public ByteArrayInputStream decrypt(ByteArrayInputStream input) throws Exception {
int length = input.available();

if (length % 8 != 0) {
throw new Exception("消息字节流错误，非刚好8位字节流");
}

ByteBuffer dst = ByteBuffer.allocate(length);
XPEncryptGroup group = new XPEncryptGroup(this.key);

byte[] ibuff = new byte[4];
while (input.available() > 0) {
input.read(ibuff);
int hv = toInt(ibuff);

input.read(ibuff);
int lv = toInt(ibuff);
dst.putLong(group.decode(hv, lv));
} 

int dataSize = dst.getInt(length - 12);

int sum = 0;
dst.rewind();
while (dst.remaining() > 8) {
sum += dst.getInt();
}

if (dst.getInt(length - 8) != 1164408404 || dst.getInt(length - 4) != sum) {
throw new Exception("消息字节流错误，check sum错误");
}

dst.limit(dataSize);
dst.rewind();
return new ByteArrayInputStream(dst.array(), 0, dataSize);
}

private ByteBuffer doEncrypt() {
ByteBuffer ret = ByteBuffer.allocate(this.eSize);
XPEncryptGroup group = new XPEncryptGroup(this.key);
this.eBuffer.rewind();
while (this.eBuffer.hasRemaining()) {
ret.putLong(group.encode(this.eBuffer.getLong()));
}
ret.rewind();
return ret;
}

private void paddingAndMakeBuffer() {
int dataSize = this.eData.length;
int append = 0;

append = 11 - ((dataSize & 0x7) + 3 & 0x7);
int zeros = append - 4;

this.eSize = dataSize + append + 8;
this.eBuffer = ByteBuffer.allocate(this.eSize);
this.eBuffer.put(this.eData);

for (int i = 0; i < zeros; i++) {
this.eBuffer.put((byte)0);
}

this.eBuffer.putInt(dataSize);

this.eBuffer.rewind();
int checksum = 0;
while (this.eBuffer.remaining() > 8) {
checksum += this.eBuffer.getInt();
}
this.eBuffer.putInt(this.eSize - 8, 1164408404);
this.eBuffer.putInt(this.eSize - 4, checksum);
}

private int toInt(byte[] src) {
int value = src[3] & 0xFF | (
src[2] & 0xFF) << 8 | (
src[1] & 0xFF) << 16 | (
src[0] & 0xFF) << 24;
return value;
}
}

