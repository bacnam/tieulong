package com.zhonglian.server.common.utils.secure;

public class CSTeaImpl
{
private int DELTA = -1640531527;

public byte[] encrypt(byte[] content, int offset, int[] key, int times) {
int[] tempInt = byteToInt(content, offset);
int y = tempInt[0], z = tempInt[1], sum = 0;
int a = key[0], b = key[1], c = key[2], d = key[3];

for (int i = 0; i < times; i++) {
sum += this.DELTA;
y += (z << 4) + a ^ z + sum ^ (z >> 5) + b;
z += (y << 4) + c ^ y + sum ^ (y >> 5) + d;
} 
tempInt[0] = y;
tempInt[1] = z;
return intToByte(tempInt, 0);
}

public byte[] decrypt(byte[] encryptContent, int offset, int[] key, int times) {
int[] tempInt = byteToInt(encryptContent, offset);
int y = tempInt[0], z = tempInt[1], sum = 0;
int a = key[0], b = key[1], c = key[2], d = key[3];
if (times == 32) {
sum = -957401312;
} else if (times == 16) {
sum = -478700656;
} else {
sum = this.DELTA * times;
} 

for (int i = 0; i < times; i++) {
z -= (y << 4) + c ^ y + sum ^ (y >> 5) + d;
y -= (z << 4) + a ^ z + sum ^ (z >> 5) + b;
sum -= this.DELTA;
} 
tempInt[0] = y;
tempInt[1] = z;

return intToByte(tempInt, 0);
}

private int[] byteToInt(byte[] content, int offset) {
int[] result = new int[content.length >> 2];

for (int i = 0, j = offset; j < content.length; i++, j += 4) {
result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16 | content[j] << 24;
}

return result;
}

private byte[] intToByte(int[] content, int offset) {
byte[] result = new byte[content.length << 2];

for (int i = 0, j = offset; j < result.length; i++, j += 4) {
result[j + 3] = (byte)(content[i] & 0xFF);
result[j + 2] = (byte)(content[i] >> 8 & 0xFF);
result[j + 1] = (byte)(content[i] >> 16 & 0xFF);
result[j] = (byte)(content[i] >> 24 & 0xFF);
} 

return result;
}

private static int transform(byte temp) {
int tempInt = temp;
if (tempInt < 0) {
tempInt += 256;
}
return tempInt;
}
}

