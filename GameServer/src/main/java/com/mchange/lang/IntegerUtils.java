package com.mchange.lang;

public final class IntegerUtils
{
public static final long UNSIGNED_MAX_VALUE = -1L;

public static int parseInt(String paramString, int paramInt) {
if (paramString == null) {
return paramInt;
}
try {
return Integer.parseInt(paramString);
} catch (NumberFormatException numberFormatException) {
return paramInt;
} 
}

public static int parseInt(String paramString, int paramInt1, int paramInt2) {
if (paramString == null) {
return paramInt2;
}
try {
return Integer.parseInt(paramString, paramInt1);
} catch (NumberFormatException numberFormatException) {
return paramInt2;
} 
}

public static int intFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
int i = 0;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 24;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 16;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 2]) << 8;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 3]) << 0;
return i;
}

public static byte[] byteArrayFromInt(int paramInt) {
byte[] arrayOfByte = new byte[4];
intIntoByteArray(paramInt, 0, arrayOfByte);
return arrayOfByte;
}

public static void intIntoByteArray(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
paramArrayOfbyte[paramInt2 + 0] = (byte)(paramInt1 >>> 24 & 0xFF);
paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 16 & 0xFF);
paramArrayOfbyte[paramInt2 + 2] = (byte)(paramInt1 >>> 8 & 0xFF);
paramArrayOfbyte[paramInt2 + 3] = (byte)(paramInt1 >>> 0 & 0xFF);
}

public static long toUnsigned(int paramInt) {
return (paramInt < 0) ? (0L + paramInt) : paramInt;
}
}

