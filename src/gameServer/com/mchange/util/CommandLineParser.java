package com.mchange.util;

public interface CommandLineParser {
  boolean checkSwitch(String paramString);
  
  String findSwitchArg(String paramString);
  
  boolean checkArgv();
  
  int findLastSwitched();
  
  String[] findUnswitchedArgs();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/CommandLineParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */