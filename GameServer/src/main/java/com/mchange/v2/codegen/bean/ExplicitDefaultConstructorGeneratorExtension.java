package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class ExplicitDefaultConstructorGeneratorExtension
implements GeneratorExtension
{
int ctor_modifiers = 1;

public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}
public Collection extraSpecificImports() {
return Collections.EMPTY_SET;
}
public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
BeangenUtils.writeExplicitDefaultConstructor(this.ctor_modifiers, paramClassInfo, paramIndentedWriter);
}
}

