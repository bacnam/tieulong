package com.mchange.lang;

import java.io.StringWriter;

public final class CharUtils
{
public static int charFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
int i = 0;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 0]) << 8;
i |= ByteUtils.toUnsigned(paramArrayOfbyte[paramInt + 1]) << 0;
return i;
}

public static byte[] byteArrayFromChar(char paramChar) {
byte[] arrayOfByte = new byte[2];
charIntoByteArray(paramChar, 0, arrayOfByte);
return arrayOfByte;
}

public static void charIntoByteArray(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) {
paramArrayOfbyte[paramInt2 + 0] = (byte)(paramInt1 >>> 8 & 0xFF);
paramArrayOfbyte[paramInt2 + 1] = (byte)(paramInt1 >>> 0 & 0xFF);
}

public static String toHexAscii(char paramChar) {
StringWriter stringWriter = new StringWriter(4);
ByteUtils.addHexAscii((byte)(paramChar >>> 8 & 0xFF), stringWriter);
ByteUtils.addHexAscii((byte)(paramChar & 0xFF), stringWriter);
return stringWriter.toString();
}

public static char[] fromHexAscii(String paramString) {
int i = paramString.length();
if (i % 4 != 0) {
throw new NumberFormatException("Hex ascii must be exactly four digits per char.");
}
byte[] arrayOfByte = ByteUtils.fromHexAscii(paramString);
int j = i / 4;
char[] arrayOfChar = new char[j];
for (byte b = 0; i < j; b++)
arrayOfChar[b] = (char)charFromByteArray(arrayOfByte, b * 2); 
return arrayOfChar;
}
}

