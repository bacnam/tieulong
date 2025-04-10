package com.mchange.v2.cmdline;

public class UnexpectedSwitchArgumentException
extends BadCommandLineException
{
String sw;
String arg;

UnexpectedSwitchArgumentException(String paramString1, String paramString2, String paramString3) {
super(paramString1);
this.sw = paramString2;
this.arg = paramString3;
}

public String getSwitch() {
return this.sw;
}
public String getUnexpectedArgument() {
return this.arg;
}
}

