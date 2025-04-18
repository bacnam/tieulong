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

