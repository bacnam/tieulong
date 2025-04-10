package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class SimpleStateBeanImportExportGeneratorExtension
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

static class SimplePropertyMask implements Property {
Property p;

SimplePropertyMask(Property param1Property) {
this.p = param1Property;
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
return null;
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

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
int i = paramArrayOfProperty.length;
Property[] arrayOfProperty = new Property[i]; byte b;
for (b = 0; b < i; b++) {
arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
}
paramIndentedWriter.println("protected static class SimpleStateBean implements ExportedState");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

for (b = 0; b < i; b++) {

arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
BeangenUtils.writePropertyMember(arrayOfProperty[b], paramIndentedWriter);
paramIndentedWriter.println();
BeangenUtils.writePropertyGetter(arrayOfProperty[b], paramIndentedWriter);
paramIndentedWriter.println();
BeangenUtils.writePropertySetter(arrayOfProperty[b], paramIndentedWriter);
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
}

