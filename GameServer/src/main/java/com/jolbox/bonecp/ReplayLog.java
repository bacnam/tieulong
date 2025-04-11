package com.jolbox.bonecp;

import java.lang.reflect.Method;

public class ReplayLog {
    private Object target;
    private Method method;
    private Object[] args;

    public ReplayLog(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getTarget() {
        return this.target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String toString() {
        return ((this.target == null) ? "" : this.target.getClass().getName()) + "." + ((this.method == null) ? "" : this.method.getName()) + " with args " + ((this.args == null) ? "null" : (String) this.args);
    }
}

