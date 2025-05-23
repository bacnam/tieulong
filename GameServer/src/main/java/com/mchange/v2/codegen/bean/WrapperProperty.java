package com.mchange.v2.codegen.bean;

public abstract class WrapperProperty
implements Property
{
Property p;

public WrapperProperty(Property paramProperty) {
this.p = paramProperty;
}
protected Property getInner() {
return this.p;
}
public int getVariableModifiers() {
return this.p.getVariableModifiers();
}
public String getName() {
return this.p.getName();
}
public String getSimpleTypeName() {
return this.p.getSimpleTypeName();
}
public String getDefensiveCopyExpression() {
return this.p.getDefensiveCopyExpression();
}
public String getDefaultValueExpression() {
return this.p.getDefaultValueExpression();
}
public int getGetterModifiers() {
return this.p.getGetterModifiers();
}
public int getSetterModifiers() {
return this.p.getSetterModifiers();
}
public boolean isReadOnly() {
return this.p.isReadOnly();
}
public boolean isBound() {
return this.p.isBound();
}
public boolean isConstrained() {
return this.p.isConstrained();
}
}

