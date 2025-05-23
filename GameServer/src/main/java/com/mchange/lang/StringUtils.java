package com.mchange.lang;

import java.io.UnsupportedEncodingException;

public final class StringUtils
{
public static final String[] EMPTY_STRING_ARRAY = new String[0];

public static String normalString(String paramString) {
return nonEmptyTrimmedOrNull(paramString);
}
public static boolean nonEmptyString(String paramString) {
return (paramString != null && paramString.length() > 0);
}
public static boolean nonWhitespaceString(String paramString) {
return (paramString != null && paramString.trim().length() > 0);
}
public static String nonEmptyOrNull(String paramString) {
return nonEmptyString(paramString) ? paramString : null;
}
public static String nonNullOrBlank(String paramString) {
return (paramString != null) ? paramString : "";
}

public static String nonEmptyTrimmedOrNull(String paramString) {
String str = paramString;
if (str != null) {

str = str.trim();
str = (str.length() > 0) ? str : null;
} 
return str;
}

public static byte[] getUTF8Bytes(String paramString) {
try {
return paramString.getBytes("UTF8");
} catch (UnsupportedEncodingException unsupportedEncodingException) {

unsupportedEncodingException.printStackTrace();
throw new InternalError("UTF8 is an unsupported encoding?!?");
} 
}
}

