package com.mchange.v2.codegen.bean;

public class SimpleClassInfo
implements ClassInfo
{
String packageName;
int modifiers;
String className;
String superclassName;
String[] interfaceNames;
String[] generalImports;
String[] specificImports;

public String getPackageName() {
return this.packageName;
} public int getModifiers() { return this.modifiers; }
public String getClassName() { return this.className; }
public String getSuperclassName() { return this.superclassName; }
public String[] getInterfaceNames() { return this.interfaceNames; }
public String[] getGeneralImports() { return this.generalImports; } public String[] getSpecificImports() {
return this.specificImports;
}

public SimpleClassInfo(String paramString1, int paramInt, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3) {
this.packageName = paramString1;
this.modifiers = paramInt;
this.className = paramString2;
this.superclassName = paramString3;
this.interfaceNames = paramArrayOfString1;
this.generalImports = paramArrayOfString2;
this.specificImports = paramArrayOfString3;
}
}

