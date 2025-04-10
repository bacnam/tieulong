package com.mchange.v1.util;

import java.util.StringTokenizer;

public final class StringTokenizerUtils
{
public static String[] tokenizeToArray(String paramString1, String paramString2, boolean paramBoolean) {
StringTokenizer stringTokenizer = new StringTokenizer(paramString1, paramString2, paramBoolean);
String[] arrayOfString = new String[stringTokenizer.countTokens()];
for (byte b = 0; stringTokenizer.hasMoreTokens(); b++)
arrayOfString[b] = stringTokenizer.nextToken(); 
return arrayOfString;
}

public static String[] tokenizeToArray(String paramString1, String paramString2) {
return tokenizeToArray(paramString1, paramString2, false);
}
public static String[] tokenizeToArray(String paramString) {
return tokenizeToArray(paramString, " \t\r\n");
}
}

