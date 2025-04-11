package com.mchange.v2.codegen.bean;

class SimplePropertyMask
        implements Property {
    Property p;

    SimplePropertyMask(Property paramProperty) {
        this.p = paramProperty;
    }

    public int getVariableModifiers() {
        return 2;
    }

    public String getName() {
        return this.p.getName();
    }

    public String getSimpleTypeName() {
        return this.p.getSimpleTypeName();
    }

    public String getDefensiveCopyExpression() {
        return null;
    }

    public String getDefaultValueExpression() {
        return this.p.getDefaultValueExpression();
    }

    public int getGetterModifiers() {
        return 1;
    }

    public int getSetterModifiers() {
        return 1;
    }

    public boolean isReadOnly() {
        return false;
    }

    public boolean isBound() {
        return false;
    }

    public boolean isConstrained() {
        return false;
    }
}

