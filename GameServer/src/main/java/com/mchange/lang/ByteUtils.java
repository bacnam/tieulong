package com.mchange.lang;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public final class ByteUtils
{
public static final short UNSIGNED_MAX_VALUE = 255;

public static short toUnsigned(byte paramByte) {
return (short)((paramByte < 0) ? (256 + paramByte) : paramByte);
}

public static String toHexAscii(byte paramByte) {
StringWriter stringWriter = new StringWriter(2);
addHexAscii(paramByte, stringWriter);
return stringWriter.toString();
}

public static String toHexAscii(byte[] paramArrayOfbyte) {
int i = paramArrayOfbyte.length;
StringWriter stringWriter = new StringWriter(i * 2);
for (byte b = 0; b < i; b++)
addHexAscii(paramArrayOfbyte[b], stringWriter); 
return stringWriter.toString();
}

public static byte[] fromHexAscii(String paramString) throws NumberFormatException {
try {
int i = paramString.length();
if (i % 2 != 0) {
throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
}
int j = i / 2;
byte[] arrayOfByte = new byte[j];
byte b = 0;
StringReader stringReader = new StringReader(paramString);
while (b < j) {

int k = 16 * fromHexDigit(stringReader.read()) + fromHexDigit(stringReader.read());
arrayOfByte[b++] = (byte)k;
} 
return arrayOfByte;
}
catch (IOException iOException) {
throw new InternalError("IOException reading from StringReader?!?!");
} 
}

static void addHexAscii(byte paramByte, StringWriter paramStringWriter) {
short s = toUnsigned(paramByte);
int i = s / 16;
int j = s % 16;
paramStringWriter.write(toHexDigit(i));
paramStringWriter.write(toHexDigit(j));
}

private static int fromHexDigit(int paramInt) throws NumberFormatException {
if (paramInt >= 48 && paramInt < 58)
return paramInt - 48; 
if (paramInt >= 65 && paramInt < 71)
return paramInt - 55; 
if (paramInt >= 97 && paramInt < 103) {
return paramInt - 87;
}
throw new NumberFormatException((39 + paramInt) + "' is not a valid hexadecimal digit.");
}

private static char toHexDigit(int paramInt) {
char c;
if (paramInt <= 9) { c = (char)(paramInt + 48); }
else { c = (char)(paramInt + 55); }

return c;
}
}

