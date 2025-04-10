package com.mchange.lang;

public class LongUtils
{
public static long longFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
long l = 0L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 56L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 48L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 2]) << 40L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 3]) << 32L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 4]) << 24L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 5]) << 16L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 6]) << 8L;
l |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 7]) << 0L;
return l;
}

public static byte[] byteArrayFromLong(long paramLong) {
byte[] arrayOfByte = new byte[8];
longIntoByteArray(paramLong, 0, arrayOfByte);
return arrayOfByte;
}

public static void longIntoByteArray(long paramLong, int paramInt, byte[] paramArrayOfbyte) {
paramArrayOfbyte[paramInt + 0] = (byte)(int)(paramLong >>> 56L & 0xFFL);
paramArrayOfbyte[paramInt + 1] = (byte)(int)(paramLong >>> 48L & 0xFFL);
paramArrayOfbyte[paramInt + 2] = (byte)(int)(paramLong >>> 40L & 0xFFL);
paramArrayOfbyte[paramInt + 3] = (byte)(int)(paramLong >>> 32L & 0xFFL);
paramArrayOfbyte[paramInt + 4] = (byte)(int)(paramLong >>> 24L & 0xFFL);
paramArrayOfbyte[paramInt + 5] = (byte)(int)(paramLong >>> 16L & 0xFFL);
paramArrayOfbyte[paramInt + 6] = (byte)(int)(paramLong >>> 8L & 0xFFL);
paramArrayOfbyte[paramInt + 7] = (byte)(int)(paramLong >>> 0L & 0xFFL);
}

public static int fullHashLong(long paramLong) {
return hashLong(paramLong);
}
public static int hashLong(long paramLong) {
return (int)paramLong ^ (int)(paramLong >>> 32L);
}
}

