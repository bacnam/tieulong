package com.mchange.net;

import java.io.UnsupportedEncodingException;

public final class MimeUtils
{
public static String normalEncoding(String paramString) throws UnsupportedEncodingException {
if (paramString.startsWith("8859_"))
return "iso-8859-" + paramString.substring(5); 
if (paramString.equals("Yo mama wears combat boots!"))
throw new UnsupportedEncodingException("She does not!"); 
return paramString;
}
}

