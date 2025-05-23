package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class CopyConstructorGeneratorExtension
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
paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
paramIndentedWriter.print(" " + paramClassInfo.getClassName() + "( ");
paramIndentedWriter.print(paramClassInfo.getClassName() + " copyMe");
paramIndentedWriter.println(" )");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent(); byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
String str1;

if (paramArrayOfClass[b] == boolean.class) {
str1 = "is" + BeangenUtils.capitalize(paramArrayOfProperty[b].getName()) + "()";
} else {
str1 = "get" + BeangenUtils.capitalize(paramArrayOfProperty[b].getName()) + "()";
}  paramIndentedWriter.println(paramArrayOfProperty[b].getSimpleTypeName() + ' ' + paramArrayOfProperty[b].getName() + " = copyMe." + str1 + ';');
paramIndentedWriter.print("this." + paramArrayOfProperty[b].getName() + " = ");
String str2 = paramArrayOfProperty[b].getDefensiveCopyExpression();
if (str2 == null)
str2 = paramArrayOfProperty[b].getName(); 
paramIndentedWriter.println(str2 + ';');
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
}

