package com.mchange.v2.codegen.bean;

import com.mchange.v2.codegen.CodegenUtils;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;

public class InnerBeanPropertyBeanGenerator
extends SimplePropertyBeanGenerator
{
String innerBeanClassName;
int inner_bean_member_modifiers = 4;

int inner_bean_accessor_modifiers = 4;
int inner_bean_replacer_modifiers = 4;

String innerBeanInitializationExpression = null;

public void setInnerBeanClassName(String paramString) {
this.innerBeanClassName = paramString;
}
public String getInnerBeanClassName() {
return this.innerBeanClassName;
}
private String defaultInnerBeanInitializationExpression() {
return "new " + this.innerBeanClassName + "()";
}
private String findInnerBeanClassName() {
return (this.innerBeanClassName == null) ? "InnerBean" : this.innerBeanClassName;
}
private String findInnerBeanInitializationExpression() {
return (this.innerBeanInitializationExpression == null) ? defaultInnerBeanInitializationExpression() : this.innerBeanInitializationExpression;
}

private int findInnerClassModifiers() {
int i = 8;
if (Modifier.isPublic(this.inner_bean_accessor_modifiers) || Modifier.isPublic(this.inner_bean_replacer_modifiers)) {
i |= 0x1;
} else if (Modifier.isProtected(this.inner_bean_accessor_modifiers) || Modifier.isProtected(this.inner_bean_replacer_modifiers)) {
i |= 0x4;
} else if (Modifier.isPrivate(this.inner_bean_accessor_modifiers) && Modifier.isPrivate(this.inner_bean_replacer_modifiers)) {
i |= 0x2;
} 
return i;
}

private void writeSyntheticInnerBeanClass() throws IOException {
int i = this.props.length;
Property[] arrayOfProperty = new Property[i];
for (byte b = 0; b < i; b++) {

arrayOfProperty[b] = new SimplePropertyMask(this.props[b])
{
public int getVariableModifiers() {
return 130;
}
};
} 
WrapperClassInfo wrapperClassInfo = new WrapperClassInfo(this.info)
{
public String getClassName() {
return "InnerBean";
}
public int getModifiers() {
return InnerBeanPropertyBeanGenerator.this.findInnerClassModifiers();
}
};
createInnerGenerator().generate(wrapperClassInfo, arrayOfProperty, (Writer)this.iw);
}

protected PropertyBeanGenerator createInnerGenerator() {
SimplePropertyBeanGenerator simplePropertyBeanGenerator = new SimplePropertyBeanGenerator();
simplePropertyBeanGenerator.setInner(true);
simplePropertyBeanGenerator.addExtension(new SerializableExtension());
CloneableExtension cloneableExtension = new CloneableExtension();
cloneableExtension.setExceptionSwallowing(true);
simplePropertyBeanGenerator.addExtension(cloneableExtension);
return simplePropertyBeanGenerator;
}

protected void writeOtherVariables() throws IOException {
this.iw.println(CodegenUtils.getModifierString(this.inner_bean_member_modifiers) + ' ' + findInnerBeanClassName() + " innerBean = " + findInnerBeanInitializationExpression() + ';');

this.iw.println();
this.iw.println(CodegenUtils.getModifierString(this.inner_bean_accessor_modifiers) + ' ' + findInnerBeanClassName() + " accessInnerBean()");

this.iw.println("{ return innerBean; }");
}

protected void writeOtherFunctions() throws IOException {
this.iw.print(CodegenUtils.getModifierString(this.inner_bean_replacer_modifiers) + ' ' + findInnerBeanClassName() + " replaceInnerBean( " + findInnerBeanClassName() + " innerBean )");

if (constrainedProperties()) {
this.iw.println(" throws PropertyVetoException");
} else {
this.iw.println();
}  this.iw.println("{");
this.iw.upIndent();
this.iw.println("beforeReplaceInnerBean();");
this.iw.println("this.innerBean = innerBean;");
this.iw.println("afterReplaceInnerBean();");
this.iw.downIndent();
this.iw.println("}");
this.iw.println();

boolean bool = Modifier.isAbstract(this.info.getModifiers());
this.iw.print("protected ");
if (bool)
this.iw.print("abstract "); 
this.iw.print("void beforeReplaceInnerBean()");
if (constrainedProperties())
this.iw.print(" throws PropertyVetoException"); 
if (bool) {
this.iw.println(';');
} else {
this.iw.println(" {} 
}  this.iw.println();

this.iw.print("protected ");
if (bool)
this.iw.print("abstract "); 
this.iw.print("void afterReplaceInnerBean()");
if (bool) {
this.iw.println(';');
} else {
this.iw.println(" {} 
}  this.iw.println();

BeangenUtils.writeExplicitDefaultConstructor(1, this.info, this.iw);
this.iw.println();
this.iw.println("public " + this.info.getClassName() + "(" + findInnerBeanClassName() + " innerBean)");
this.iw.println("{ this.innerBean = innerBean; }");
}

protected void writeOtherClasses() throws IOException {
if (this.innerBeanClassName == null) {
writeSyntheticInnerBeanClass();
}
}

protected void writePropertyVariable(Property paramProperty) throws IOException {}

protected void writePropertyGetter(Property paramProperty, Class paramClass) throws IOException {
String str1 = paramProperty.getSimpleTypeName();
String str2 = "boolean".equals(str1) ? "is" : "get";
String str3 = str2 + BeangenUtils.capitalize(paramProperty.getName());
this.iw.print(CodegenUtils.getModifierString(paramProperty.getGetterModifiers()));
this.iw.println(' ' + paramProperty.getSimpleTypeName() + ' ' + str3 + "()");
this.iw.println('{');
this.iw.upIndent();
this.iw.println(str1 + ' ' + paramProperty.getName() + " = innerBean." + str3 + "();");
String str4 = getGetterDefensiveCopyExpression(paramProperty, paramClass);
if (str4 == null) str4 = paramProperty.getName(); 
this.iw.println("return " + str4 + ';');
this.iw.downIndent();
this.iw.println('}');
}

protected void writePropertySetter(Property paramProperty, Class paramClass) throws IOException {
String str1 = paramProperty.getSimpleTypeName();
String str2 = "boolean".equals(str1) ? "is" : "get";

String str3 = getSetterDefensiveCopyExpression(paramProperty, paramClass);
if (str3 == null) str3 = paramProperty.getName(); 
String str4 = "innerBean." + str2 + BeangenUtils.capitalize(paramProperty.getName()) + "()";
String str5 = "innerBean.set" + BeangenUtils.capitalize(paramProperty.getName()) + "( " + str3 + " );";
BeangenUtils.writePropertySetterWithGetExpressionSetStatement(paramProperty, str4, str5, this.iw);
}
}

