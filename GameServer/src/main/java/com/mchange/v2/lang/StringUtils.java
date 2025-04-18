package com.mchange.v2.lang;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public final class StringUtils
{
static final Pattern COMMA_SEP_TRIM_REGEX;
static final Pattern COMMA_SEP_NO_TRIM_REGEX;

static {
try {
COMMA_SEP_TRIM_REGEX = Pattern.compile("\\s*\\,\\s*");
COMMA_SEP_NO_TRIM_REGEX = Pattern.compile("\\,");
}
catch (PatternSyntaxException patternSyntaxException) {

patternSyntaxException.printStackTrace();
throw new InternalError(patternSyntaxException.toString());
} 
}

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

public static String[] splitCommaSeparated(String paramString, boolean paramBoolean) {
Pattern pattern = paramBoolean ? COMMA_SEP_TRIM_REGEX : COMMA_SEP_NO_TRIM_REGEX;
return pattern.split(paramString);
}
}

