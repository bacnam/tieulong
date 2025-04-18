package com.mchange.v1.jvm;

public final class InternalNameUtils
{
public static String dottifySlashesAndDollarSigns(String paramString) {
return _dottifySlashesAndDollarSigns(paramString).toString();
}
public static String decodeType(String paramString) throws TypeFormatException {
return _decodeType(paramString).toString();
}

public static String decodeTypeList(String paramString) throws TypeFormatException {
StringBuffer stringBuffer = new StringBuffer(64);
_decodeTypeList(paramString, 0, stringBuffer);
return stringBuffer.toString();
}

public static boolean isPrimitive(char paramChar) {
return (paramChar == 'Z' || paramChar == 'B' || paramChar == 'C' || paramChar == 'S' || paramChar == 'I' || paramChar == 'J' || paramChar == 'F' || paramChar == 'D' || paramChar == 'V');
}

private static void _decodeTypeList(String paramString, int paramInt, StringBuffer paramStringBuffer) throws TypeFormatException {
if (paramStringBuffer.length() != 0) {
paramStringBuffer.append(' ');
}
char c = paramString.charAt(paramInt);
if (isPrimitive(c)) {

paramStringBuffer.append(_decodeType(paramString.substring(paramInt, paramInt + 1)));
paramInt++;
} else {
int i;

if (c == '[') {

int j = paramInt + 1;
while (paramString.charAt(j) == '[')
j++; 
if (paramString.charAt(j) == 'L') {

j++;
while (paramString.charAt(j) != ';')
j++; 
} 
i = j;
}
else {

i = paramString.indexOf(';', paramInt);
if (i < 0) {
throw new TypeFormatException(paramString.substring(paramInt) + " is neither a primitive nor semicolon terminated!");
}
} 
paramStringBuffer.append(_decodeType(paramString.substring(paramInt, paramInt = i + 1)));
} 
if (paramInt < paramString.length()) {

paramStringBuffer.append(',');
_decodeTypeList(paramString, paramInt, paramStringBuffer);
} 
}

private static StringBuffer _decodeType(String paramString) throws TypeFormatException {
StringBuffer stringBuffer;
byte b1 = 0;

char c = paramString.charAt(0);

switch (c) {

case 'Z':
stringBuffer = new StringBuffer("boolean");
break;
case 'B':
stringBuffer = new StringBuffer("byte");
break;
case 'C':
stringBuffer = new StringBuffer("char");
break;
case 'S':
stringBuffer = new StringBuffer("short");
break;
case 'I':
stringBuffer = new StringBuffer("int");
break;
case 'J':
stringBuffer = new StringBuffer("long");
break;
case 'F':
stringBuffer = new StringBuffer("float");
break;
case 'D':
stringBuffer = new StringBuffer("double");
break;
case 'V':
stringBuffer = new StringBuffer("void");
break;
case '[':
b1++;
stringBuffer = _decodeType(paramString.substring(1));
break;
case 'L':
stringBuffer = _decodeSimpleClassType(paramString);
break;
default:
throw new TypeFormatException(paramString + " is not a valid inernal type name.");
} 
for (byte b2 = 0; b2 < b1; b2++)
stringBuffer.append("[]"); 
return stringBuffer;
}

private static StringBuffer _decodeSimpleClassType(String paramString) throws TypeFormatException {
int i = paramString.length();
if (paramString.charAt(0) != 'L' || paramString.charAt(i - 1) != ';') {
throw new TypeFormatException(paramString + " is not a valid representation of a simple class type.");
}
return _dottifySlashesAndDollarSigns(paramString.substring(1, i - 1));
}

private static StringBuffer _dottifySlashesAndDollarSigns(String paramString) {
StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
for (b = 0, i = stringBuffer.length(); b < i; b++) {

char c = stringBuffer.charAt(b);
if (c == '/' || c == '$')
stringBuffer.setCharAt(b, '.'); 
} 
return stringBuffer;
}

public static void main(String[] paramArrayOfString) {
try {
System.out.println(decodeTypeList(paramArrayOfString[0]));
}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

