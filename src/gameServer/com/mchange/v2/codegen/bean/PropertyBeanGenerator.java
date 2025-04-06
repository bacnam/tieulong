package com.mchange.v2.codegen.bean;

import java.io.IOException;
import java.io.Writer;

public interface PropertyBeanGenerator {
  void generate(ClassInfo paramClassInfo, Property[] paramArrayOfProperty, Writer paramWriter) throws IOException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/PropertyBeanGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */