package com.mchange.v2.codegen.bean;

public abstract class WrapperClassInfo
implements ClassInfo
{
ClassInfo inner;

public WrapperClassInfo(ClassInfo paramClassInfo) {
this.inner = paramClassInfo;
}
public String getPackageName() { return this.inner.getPackageName(); }
public int getModifiers() { return this.inner.getModifiers(); }
public String getClassName() { return this.inner.getClassName(); }
public String getSuperclassName() { return this.inner.getSuperclassName(); }
public String[] getInterfaceNames() { return this.inner.getInterfaceNames(); }
public String[] getGeneralImports() { return this.inner.getGeneralImports(); } public String[] getSpecificImports() {
return this.inner.getSpecificImports();
}
}

