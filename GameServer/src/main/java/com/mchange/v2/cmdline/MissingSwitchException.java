package com.mchange.v2.cmdline;

public class MissingSwitchException
extends BadCommandLineException
{
String sw;

MissingSwitchException(String paramString1, String paramString2) {
super(paramString1);
this.sw = paramString2;
}

public String getMissingSwitch() {
return this.sw;
}
}

