package com.mchange.v2.cmdline;

public final class CommandLineUtils
{
public static ParsedCommandLine parse(String[] paramArrayOfString1, String paramString, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4) throws BadCommandLineException {
return new ParsedCommandLineImpl(paramArrayOfString1, paramString, paramArrayOfString2, paramArrayOfString3, paramArrayOfString4);
}
}

