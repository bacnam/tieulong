package com.mchange.v2.cmdline;

public class UnexpectedSwitchException
extends BadCommandLineException
{
String sw;

UnexpectedSwitchException(String paramString1, String paramString2) {
super(paramString1);
this.sw = paramString2;
}

public String getUnexpectedSwitch() {
return this.sw;
}
}

