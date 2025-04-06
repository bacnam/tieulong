package com.mchange.v2.log;

public interface NameTransformer {
  String transformName(String paramString);
  
  String transformName(Class paramClass);
  
  String transformName();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/NameTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */