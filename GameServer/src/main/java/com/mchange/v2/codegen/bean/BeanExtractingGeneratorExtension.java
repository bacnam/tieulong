package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class BeanExtractingGeneratorExtension
implements GeneratorExtension
{
int ctor_modifiers = 1;
int method_modifiers = 2;

public void setConstructorModifiers(int paramInt) {
this.ctor_modifiers = paramInt;
}
public int getConstructorModifiers() {
return this.ctor_modifiers;
}
public void setExtractMethodModifiers(int paramInt) {
this.method_modifiers = paramInt;
}
public int getExtractMethodModifiers() {
return this.method_modifiers;
}
public Collection extraGeneralImports() {
return Collections.EMPTY_SET;
}

public Collection extraSpecificImports() {
HashSet<String> hashSet = new HashSet();
hashSet.add("java.beans.BeanInfo");
hashSet.add("java.beans.PropertyDescriptor");
hashSet.add("java.beans.Introspector");
hashSet.add("java.beans.IntrospectionException");
hashSet.add("java.lang.reflect.InvocationTargetException");
return hashSet;
}

public Collection extraInterfaceNames() {
return Collections.EMPTY_SET;
}

public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("private static Class[] NOARGS = new Class[0];");
paramIndentedWriter.println();
paramIndentedWriter.print(CodegenUtils.getModifierString(this.method_modifiers));
paramIndentedWriter.print(" void extractPropertiesFromBean( Object bean ) throws InvocationTargetException, IllegalAccessException, IntrospectionException");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();

paramIndentedWriter.println("BeanInfo bi = Introspector.getBeanInfo( bean.getClass() );");
paramIndentedWriter.println("PropertyDescriptor[] pds = bi.getPropertyDescriptors();");
paramIndentedWriter.println("for (int i = 0, len = pds.length; i < len; ++i)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent(); byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

paramIndentedWriter.println("if (\"" + paramArrayOfProperty[b].getName() + "\".equals( pds[i].getName() ) )");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("this." + paramArrayOfProperty[b].getName() + " = " + extractorExpr(paramArrayOfProperty[b], paramArrayOfClass[b]) + ';');
paramIndentedWriter.downIndent();
} 
paramIndentedWriter.println("}");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
paramIndentedWriter.println();
paramIndentedWriter.print(CodegenUtils.getModifierString(this.ctor_modifiers));
paramIndentedWriter.println(' ' + paramClassInfo.getClassName() + "( Object bean ) throws InvocationTargetException, IllegalAccessException, IntrospectionException");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("extractPropertiesFromBean( bean );");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}

private String extractorExpr(Property paramProperty, Class<char> paramClass) {
if (paramClass.isPrimitive()) {

String str1 = BeangenUtils.capitalize(paramProperty.getSimpleTypeName());
String str2 = paramProperty.getSimpleTypeName() + "Value()";

if (paramClass == char.class) {
str1 = "Character";
} else if (paramClass == int.class) {
str1 = "Integer";
} 
return "((" + str1 + ") pds[i].getReadMethod().invoke( bean, NOARGS ))." + str2;
} 

return "(" + paramProperty.getSimpleTypeName() + ") pds[i].getReadMethod().invoke( bean, NOARGS )";
}
}

