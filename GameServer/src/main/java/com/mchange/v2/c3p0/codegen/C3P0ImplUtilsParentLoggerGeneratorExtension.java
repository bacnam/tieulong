package com.mchange.v2.c3p0.codegen;

import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.codegen.bean.ClassInfo;
import com.mchange.v2.codegen.bean.GeneratorExtension;
import com.mchange.v2.codegen.bean.Property;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class C3P0ImplUtilsParentLoggerGeneratorExtension
implements GeneratorExtension
{
public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}
public Collection extraSpecificImports() {
return Arrays.asList(new String[] { "java.util.logging.Logger", "com.mchange.v2.c3p0.impl.C3P0ImplUtils", "java.sql.SQLFeatureNotSupportedException" });
}
public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
iw.println("
iw.println("public Logger getParentLogger() throws SQLFeatureNotSupportedException");
iw.println("{ return C3P0ImplUtils.PARENT_LOGGER;}");
}
}

