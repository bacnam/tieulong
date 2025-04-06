package com.mchange.v2.codegen.bean;

public interface ClassInfo {
  String getPackageName();
  
  int getModifiers();
  
  String getClassName();
  
  String getSuperclassName();
  
  String[] getInterfaceNames();
  
  String[] getGeneralImports();
  
  String[] getSpecificImports();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/ClassInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */