package com.mchange.v2.codegen.bean;

import java.io.IOException;
import java.io.Writer;

public interface PropertyBeanGenerator {
    void generate(ClassInfo paramClassInfo, Property[] paramArrayOfProperty, Writer paramWriter) throws IOException;
}

