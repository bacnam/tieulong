package com.mchange.v2.codegen.bean;

public interface ResolvedClassInfo extends ClassInfo {
    Class[] getInterfaces();

    Class[] getSuperclass();
}

