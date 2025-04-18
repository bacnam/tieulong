package com.mchange.v2.codegen.bean;

public class SimpleProperty
implements Property
{
int variable_modifiers;
String name;
String simpleTypeName;
String defensiveCopyExpression;
String defaultValueExpression;
int getter_modifiers;
int setter_modifiers;
boolean is_read_only;
boolean is_bound;
boolean is_constrained;

public int getVariableModifiers() {
return this.variable_modifiers;
} public String getName() { return this.name; }
public String getSimpleTypeName() { return this.simpleTypeName; }
public String getDefensiveCopyExpression() { return this.defensiveCopyExpression; }
public String getDefaultValueExpression() { return this.defaultValueExpression; }
public int getGetterModifiers() { return this.getter_modifiers; }
public int getSetterModifiers() { return this.setter_modifiers; }
public boolean isReadOnly() { return this.is_read_only; }
public boolean isBound() { return this.is_bound; } public boolean isConstrained() {
return this.is_constrained;
}

public SimpleProperty(int paramInt1, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
this.variable_modifiers = paramInt1;
this.name = paramString1;
this.simpleTypeName = paramString2;
this.defensiveCopyExpression = paramString3;
this.defaultValueExpression = paramString4;
this.getter_modifiers = paramInt2;
this.setter_modifiers = paramInt3;
this.is_read_only = paramBoolean1;
this.is_bound = paramBoolean2;
this.is_constrained = paramBoolean3;
}

public SimpleProperty(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
this(2, paramString1, paramString2, paramString3, paramString4, 1, 1, paramBoolean1, paramBoolean2, paramBoolean3);
}
}

