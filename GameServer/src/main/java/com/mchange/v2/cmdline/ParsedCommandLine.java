package com.mchange.v2.cmdline;

public interface ParsedCommandLine {
  String[] getRawArgs();

  String getSwitchPrefix();

  boolean includesSwitch(String paramString);

  String getSwitchArg(String paramString);

  String[] getUnswitchedArgs();
}

