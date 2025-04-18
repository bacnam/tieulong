package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;

public interface GeneratorExtension {
  Collection extraGeneralImports();

  Collection extraSpecificImports();

  Collection extraInterfaceNames();

  void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException;
}

