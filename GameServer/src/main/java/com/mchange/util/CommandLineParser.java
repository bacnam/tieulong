package com.mchange.util;

public interface CommandLineParser {
  boolean checkSwitch(String paramString);

  String findSwitchArg(String paramString);

  boolean checkArgv();

  int findLastSwitched();

  String[] findUnswitchedArgs();
}

