package com.mchange.v2.c3p0.codegen;

import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.codegen.bean.ClassInfo;
import com.mchange.v2.codegen.bean.GeneratorExtension;
import com.mchange.v2.codegen.bean.Property;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class UnsupportedParentLoggerGeneratorExtension
implements GeneratorExtension
{
public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}
public Collection extraSpecificImports() {
return Arrays.asList(new String[] { "java.util.logging.Logger", "java.sql.SQLFeatureNotSupportedException" });
}
public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
iw.println("
iw.println("public Logger getParentLogger() throws SQLFeatureNotSupportedException");
iw.println("{ throw new SQLFeatureNotSupportedException(\"javax.sql.DataSource.getParentLogger() is not currently supported by \" + this.getClass().getName());}");
}
}

