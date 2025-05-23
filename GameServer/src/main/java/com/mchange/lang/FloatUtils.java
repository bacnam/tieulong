package com.mchange.lang;

public final class FloatUtils
{
static final boolean DEBUG = true;
private static FParser fParser;

static {
try {
fParser = new J12FParser();
fParser.parseFloat("0.1");
}
catch (NoSuchMethodError noSuchMethodError) {

System.err.println("com.mchange.lang.FloatUtils: reconfiguring for Java 1.1 environment");
fParser = new J11FParser();
} 
}

public static byte[] byteArrayFromFloat(float paramFloat) {
int i = Float.floatToIntBits(paramFloat);
return IntegerUtils.byteArrayFromInt(i);
}

public static float floatFromByteArray(byte[] paramArrayOfbyte, int paramInt) {
int i = IntegerUtils.intFromByteArray(paramArrayOfbyte, paramInt);
return Float.intBitsToFloat(i);
}

public static float parseFloat(String paramString, float paramFloat) {
if (paramString == null) {
return paramFloat;
}
try {
return fParser.parseFloat(paramString);
} catch (NumberFormatException numberFormatException) {
return paramFloat;
} 
}

public static float parseFloat(String paramString) throws NumberFormatException {
return fParser.parseFloat(paramString);
}

public static String floatToString(float paramFloat, int paramInt) {
boolean bool = (paramFloat < 0.0F) ? true : false;
paramFloat = bool ? -paramFloat : paramFloat;

long l = Math.round(paramFloat * Math.pow(10.0D, -paramInt));

String str = String.valueOf(l);
if (l == 0L) {
return str;
}
int i = str.length();
int j = i + paramInt;

StringBuffer stringBuffer = new StringBuffer(32);
if (bool) stringBuffer.append('-');

if (j <= 0) {

stringBuffer.append("0.");
for (byte b = 0; b < -j; b++)
stringBuffer.append('0'); 
stringBuffer.append(str);

}
else {

stringBuffer.append(str.substring(0, Math.min(j, i)));
if (j < i) {

stringBuffer.append('.');
stringBuffer.append(str.substring(j));
}
else if (j > i) {
byte b; int k;
for (b = 0, k = j - i; b < k; b++)
stringBuffer.append('0'); 
} 
} 
return stringBuffer.toString();
}

static interface FParser
{
float parseFloat(String param1String) throws NumberFormatException;
}

static class J12FParser
implements FParser {
public float parseFloat(String param1String) throws NumberFormatException {
return Float.parseFloat(param1String);
}
}

static class J11FParser implements FParser {
public float parseFloat(String param1String) throws NumberFormatException {
return (new Float(param1String)).floatValue();
}
}
}

