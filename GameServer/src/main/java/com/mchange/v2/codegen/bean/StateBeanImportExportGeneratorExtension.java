package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class StateBeanImportExportGeneratorExtension
implements GeneratorExtension
{
int ctor_modifiers = 1;

public Collection extraGeneralImports() {
return Arrays.asList(new String[] { "com.mchange.v2.bean" });
}
public Collection extraSpecificImports() {
return Collections.EMPTY_SET;
}
public Collection extraInterfaceNames() {
return Arrays.asList(new String[] { "StateBeanExporter" });
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
String str = paramClassInfo.getClassName();

int i = paramArrayOfProperty.length;
Property[] arrayOfProperty = new Property[i]; byte b;
for (b = 0; b < i; b++) {
arrayOfProperty[b] = new SimplePropertyMask(paramArrayOfProperty[b]);
}
paramIndentedWriter.println("protected class MyStateBean implements StateBean");
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
paramIndentedWriter.println();
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();
paramIndentedWriter.println("public StateBean exportStateBean()");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("MyStateBean out = createEmptyStateBean();");
for (b = 0; b < i; b++) {

String str1 = BeangenUtils.capitalize(paramArrayOfProperty[b].getName());
paramIndentedWriter.println("out.set" + str1 + "( this." + ((paramArrayOfClass[b] == boolean.class) ? "is" : "get") + str1 + "() );");
} 
paramIndentedWriter.println("return out;");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();

paramIndentedWriter.println("public void importStateBean( StateBean bean )");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("MyStateBean msb = (MyStateBean) bean;");
for (b = 0; b < i; b++) {

String str1 = BeangenUtils.capitalize(paramArrayOfProperty[b].getName());
paramIndentedWriter.println("this.set" + str1 + "( msb." + ((paramArrayOfClass[b] == boolean.class) ? "is" : "get") + str1 + "() );");
} 
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();

paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
paramIndentedWriter.println(" " + str + "( StateBean bean )");
paramIndentedWriter.println("{ importStateBean( bean ); }");

paramIndentedWriter.println("protected MyStateBean createEmptyStateBean() throws StateBeanException");
paramIndentedWriter.println("{ return new MyStateBean(); }");
}
}

