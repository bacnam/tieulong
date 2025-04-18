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

