package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class CompleteConstructorGeneratorExtension
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
paramIndentedWriter.print(paramClassInfo.getClassName() + "( ");
BeangenUtils.writeArgList(paramArrayOfProperty, true, paramIndentedWriter);
paramIndentedWriter.println(" )");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent(); byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

paramIndentedWriter.print("this." + paramArrayOfProperty[b].getName() + " = ");
String str = paramArrayOfProperty[b].getDefensiveCopyExpression();
if (str == null)
str = paramArrayOfProperty[b].getName(); 
paramIndentedWriter.println(str + ';');
} 

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}
}

