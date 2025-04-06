package com.mchange.v2.codegen.bean;

public interface Property {
  int getVariableModifiers();
  
  String getName();
  
  String getSimpleTypeName();
  
  String getDefensiveCopyExpression();
  
  String getDefaultValueExpression();
  
  int getGetterModifiers();
  
  int getSetterModifiers();
  
  boolean isReadOnly();
  
  boolean isBound();
  
  boolean isConstrained();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */