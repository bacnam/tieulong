package com.mchange.v1.lang;

public final class BooleanUtils
{
public static boolean parseBoolean(String paramString) throws IllegalArgumentException {
if (paramString.equals("true"))
return true; 
if (paramString.equals("false")) {
return false;
}
throw new IllegalArgumentException("\"str\" is neither \"true\" nor \"false\".");
}
}

