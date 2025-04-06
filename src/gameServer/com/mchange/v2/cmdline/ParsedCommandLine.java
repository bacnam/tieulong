package com.mchange.v2.cmdline;

public interface ParsedCommandLine {
  String[] getRawArgs();
  
  String getSwitchPrefix();
  
  boolean includesSwitch(String paramString);
  
  String getSwitchArg(String paramString);
  
  String[] getUnswitchedArgs();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cmdline/ParsedCommandLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */