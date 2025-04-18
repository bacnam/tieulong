package com.mchange.v2.codegen.intfc;

import com.mchange.v1.lang.ClassUtils;
import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class DelegatorGenerator
{
int class_modifiers = 1025;
int method_modifiers = 1;
int wrapping_ctor_modifiers = 1;
int default_ctor_modifiers = 1;

boolean wrapping_constructor = true;
boolean default_constructor = true;
boolean inner_getter = true;
boolean inner_setter = true;
Class superclass = null;
Class[] extraInterfaces = null;

Method[] reflectiveDelegateMethods = null;
ReflectiveDelegationPolicy reflectiveDelegationPolicy = ReflectiveDelegationPolicy.USE_MAIN_DELEGATE_INTERFACE;

static final Comparator classComp = new Comparator()
{
public int compare(Object param1Object1, Object param1Object2) {
return ((Class)param1Object1).getName().compareTo(((Class)param1Object2).getName());
}
};
public void setGenerateInnerSetter(boolean paramBoolean) {
this.inner_setter = paramBoolean;
}
public boolean isGenerateInnerSetter() {
return this.inner_setter;
}
public void setGenerateInnerGetter(boolean paramBoolean) {
this.inner_getter = paramBoolean;
}
public boolean isGenerateInnerGetter() {
return this.inner_getter;
}
public void setGenerateNoArgConstructor(boolean paramBoolean) {
this.default_constructor = paramBoolean;
}
public boolean isGenerateNoArgConstructor() {
return this.default_constructor;
}
public void setGenerateWrappingConstructor(boolean paramBoolean) {
this.wrapping_constructor = paramBoolean;
}
public boolean isGenerateWrappingConstructor() {
return this.wrapping_constructor;
}
public void setWrappingConstructorModifiers(int paramInt) {
this.wrapping_ctor_modifiers = paramInt;
}
public int getWrappingConstructorModifiers() {
return this.wrapping_ctor_modifiers;
}
public void setNoArgConstructorModifiers(int paramInt) {
this.default_ctor_modifiers = paramInt;
}
public int getNoArgConstructorModifiers() {
return this.default_ctor_modifiers;
}
public void setMethodModifiers(int paramInt) {
this.method_modifiers = paramInt;
}
public int getMethodModifiers() {
return this.method_modifiers;
}
public void setClassModifiers(int paramInt) {
this.class_modifiers = paramInt;
}
public int getClassModifiers() {
return this.class_modifiers;
}
public void setSuperclass(Class paramClass) {
this.superclass = paramClass;
}
public Class getSuperclass() {
return this.superclass;
}
public void setExtraInterfaces(Class[] paramArrayOfClass) {
this.extraInterfaces = paramArrayOfClass;
}
public Class[] getExtraInterfaces() {
return this.extraInterfaces;
}
public Method[] getReflectiveDelegateMethods() {
return this.reflectiveDelegateMethods;
}

public void setReflectiveDelegateMethods(Method[] paramArrayOfMethod) {
this.reflectiveDelegateMethods = paramArrayOfMethod;
}
public ReflectiveDelegationPolicy getReflectiveDelegationPolicy() {
return this.reflectiveDelegationPolicy;
}

public void setReflectiveDelegationPolicy(ReflectiveDelegationPolicy paramReflectiveDelegationPolicy) {
this.reflectiveDelegationPolicy = paramReflectiveDelegationPolicy;
}

public void writeDelegator(Class<?> paramClass, String paramString, Writer paramWriter) throws IOException {
IndentedWriter indentedWriter = CodegenUtils.toIndentedWriter(paramWriter);

String str1 = paramString.substring(0, paramString.lastIndexOf('.'));
String str2 = CodegenUtils.fqcnLastElement(paramString);
String str3 = (this.superclass != null) ? ClassUtils.simpleClassName(this.superclass) : null;
String str4 = ClassUtils.simpleClassName(paramClass);
String[] arrayOfString = null;
if (this.extraInterfaces != null) {

arrayOfString = new String[this.extraInterfaces.length]; byte b1; int j;
for (b1 = 0, j = this.extraInterfaces.length; b1 < j; b1++) {
arrayOfString[b1] = ClassUtils.simpleClassName(this.extraInterfaces[b1]);
}
} 
TreeSet<Class<?>> treeSet = new TreeSet(classComp);

Method[] arrayOfMethod = paramClass.getMethods();

if (!CodegenUtils.inSamePackage(paramClass.getName(), paramString))
treeSet.add(paramClass); 
if (this.superclass != null && !CodegenUtils.inSamePackage(this.superclass.getName(), paramString))
treeSet.add(this.superclass); 
if (this.extraInterfaces != null) {
byte b1; int j;
for (b1 = 0, j = this.extraInterfaces.length; b1 < j; b1++) {

Class<?> clazz = this.extraInterfaces[b1];
if (!CodegenUtils.inSamePackage(clazz.getName(), paramString)) {
treeSet.add(clazz);
}
} 
} 
ensureImports(paramString, treeSet, arrayOfMethod);

if (this.reflectiveDelegateMethods != null) {
ensureImports(paramString, treeSet, this.reflectiveDelegateMethods);
}
if (this.reflectiveDelegationPolicy.delegateClass != null && !CodegenUtils.inSamePackage(this.reflectiveDelegationPolicy.delegateClass.getName(), paramString)) {
treeSet.add(this.reflectiveDelegationPolicy.delegateClass);
}
generateBannerComment(indentedWriter);
indentedWriter.println("package " + str1 + ';');
indentedWriter.println();
for (Iterator<Class<?>> iterator = treeSet.iterator(); iterator.hasNext();)
indentedWriter.println("import " + ((Class)iterator.next()).getName() + ';'); 
generateExtraImports(indentedWriter);
indentedWriter.println();
generateClassJavaDocComment(indentedWriter);
indentedWriter.print(CodegenUtils.getModifierString(this.class_modifiers) + " class " + str2);
if (this.superclass != null)
indentedWriter.print(" extends " + str3); 
indentedWriter.print(" implements " + str4);
if (arrayOfString != null) {
byte b1; int j; for (b1 = 0, j = arrayOfString.length; b1 < j; b1++)
indentedWriter.print(", " + arrayOfString[b1]); 
}  indentedWriter.println();
indentedWriter.println("{");
indentedWriter.upIndent();

indentedWriter.println("protected " + str4 + " inner;");
indentedWriter.println();

if (this.reflectiveDelegateMethods != null)
indentedWriter.println("protected Class __delegateClass = null;"); 
indentedWriter.println();

indentedWriter.println("private void __setInner( " + str4 + " inner )");
indentedWriter.println("{");
indentedWriter.upIndent();
indentedWriter.println("this.inner = inner;");
if (this.reflectiveDelegateMethods != null) {
String str;

if (this.reflectiveDelegationPolicy == ReflectiveDelegationPolicy.USE_MAIN_DELEGATE_INTERFACE) {
str = str4 + ".class";
} else if (this.reflectiveDelegationPolicy == ReflectiveDelegationPolicy.USE_RUNTIME_CLASS) {
str = "inner.getClass()";
} else {
str = ClassUtils.simpleClassName(this.reflectiveDelegationPolicy.delegateClass) + ".class";
} 
indentedWriter.println("this.__delegateClass = inner == null ? null : " + str + ";");
} 
indentedWriter.downIndent();
indentedWriter.println("}");
indentedWriter.println();

if (this.wrapping_constructor) {

indentedWriter.println(CodegenUtils.getModifierString(this.wrapping_ctor_modifiers) + ' ' + str2 + '(' + str4 + " inner)");
indentedWriter.println("{ __setInner( inner ); }");
} 

if (this.default_constructor) {

indentedWriter.println();
indentedWriter.println(CodegenUtils.getModifierString(this.default_ctor_modifiers) + ' ' + str2 + "()");
indentedWriter.println("{}");
} 

if (this.inner_setter) {

indentedWriter.println();
indentedWriter.println(CodegenUtils.getModifierString(this.method_modifiers) + " void setInner( " + str4 + " inner )");
indentedWriter.println("{ __setInner( inner ); }");
} 
if (this.inner_getter) {

indentedWriter.println();
indentedWriter.println(CodegenUtils.getModifierString(this.method_modifiers) + ' ' + str4 + " getInner()");
indentedWriter.println("{ return inner; }");
} 
indentedWriter.println(); byte b; int i;
for (b = 0, i = arrayOfMethod.length; b < i; b++) {

Method method = arrayOfMethod[b];

if (b != 0) indentedWriter.println(); 
indentedWriter.println(CodegenUtils.methodSignature(this.method_modifiers, method, null));
indentedWriter.println("{");
indentedWriter.upIndent();

generatePreDelegateCode(paramClass, paramString, method, indentedWriter);
generateDelegateCode(paramClass, paramString, method, indentedWriter);
generatePostDelegateCode(paramClass, paramString, method, indentedWriter);

indentedWriter.downIndent();
indentedWriter.println("}");
} 

if (this.reflectiveDelegateMethods != null) {

indentedWriter.println("
for (b = 0, i = this.reflectiveDelegateMethods.length; b < i; b++) {

Method method = this.reflectiveDelegateMethods[b];

if (b != 0) indentedWriter.println(); 
indentedWriter.println(CodegenUtils.methodSignature(this.method_modifiers, method, null));
indentedWriter.println("{");
indentedWriter.upIndent();

generatePreDelegateCode(paramClass, paramString, method, indentedWriter);
generateReflectiveDelegateCode(paramClass, paramString, method, indentedWriter);
generatePostDelegateCode(paramClass, paramString, method, indentedWriter);

indentedWriter.downIndent();
indentedWriter.println("}");
} 
} 

indentedWriter.println();
generateExtraDeclarations(paramClass, paramString, indentedWriter);

indentedWriter.downIndent();
indentedWriter.println("}");
}
private void ensureImports(String paramString, Set<Class<?>> paramSet, Method[] paramArrayOfMethod) {
byte b;
int i;
for (b = 0, i = paramArrayOfMethod.length; b < i; b++) {

Class[] arrayOfClass1 = paramArrayOfMethod[b].getParameterTypes(); int j;
for (byte b1 = 0; b1 < j; b1++) {

if (!CodegenUtils.inSamePackage(arrayOfClass1[b1].getName(), paramString))
paramSet.add(CodegenUtils.unarrayClass(arrayOfClass1[b1])); 
} 
Class[] arrayOfClass2 = paramArrayOfMethod[b].getExceptionTypes(); int k;
for (j = 0, k = arrayOfClass2.length; j < k; j++) {

if (!CodegenUtils.inSamePackage(arrayOfClass2[j].getName(), paramString))
{

paramSet.add(CodegenUtils.unarrayClass(arrayOfClass2[j]));
}
} 
if (!CodegenUtils.inSamePackage(paramArrayOfMethod[b].getReturnType().getName(), paramString)) {
paramSet.add(CodegenUtils.unarrayClass(paramArrayOfMethod[b].getReturnType()));
}
} 
}

protected void generateDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {
Class<?> clazz = paramMethod.getReturnType();

paramIndentedWriter.println(((clazz == void.class) ? "" : "return ") + "inner." + CodegenUtils.methodCall(paramMethod) + ";");
}

protected void generateReflectiveDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {
Class<?> clazz = paramMethod.getReturnType();

String str1 = CodegenUtils.reflectiveMethodParameterTypeArray(paramMethod);
String str2 = CodegenUtils.reflectiveMethodObjectArray(paramMethod);

Class[] arrayOfClass = paramMethod.getExceptionTypes();
HashSet hashSet = new HashSet();
hashSet.addAll(Arrays.asList((Class<?>[][])arrayOfClass));

paramIndentedWriter.println("try");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("Method m = __delegateClass.getMethod(\"" + paramMethod.getName() + "\", " + str1 + ");");
paramIndentedWriter.println(((clazz == void.class) ? "" : ("return (" + ClassUtils.simpleClassName(clazz) + ") ")) + "m.invoke( inner, " + str2 + " );");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
if (!hashSet.contains(IllegalAccessException.class)) {

paramIndentedWriter.println("catch (IllegalAccessException iae)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("throw new RuntimeException(\"A reflectively delegated method '" + paramMethod.getName() + "' cannot access the object to which the call is delegated\", iae);");

paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
} 
paramIndentedWriter.println("catch (InvocationTargetException ite)");
paramIndentedWriter.println("{");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("Throwable cause = ite.getCause();");
paramIndentedWriter.println("if (cause instanceof RuntimeException) throw (RuntimeException) cause;");
paramIndentedWriter.println("if (cause instanceof Error) throw (Error) cause;");
int i = arrayOfClass.length;
if (i > 0)
{
for (byte b = 0; b < i; b++) {

String str = ClassUtils.simpleClassName(arrayOfClass[b]);
paramIndentedWriter.println("if (cause instanceof " + str + ") throw (" + str + ") cause;");
} 
}
paramIndentedWriter.println("throw new RuntimeException(\"Target of reflectively delegated method '" + paramMethod.getName() + "' threw an Exception.\", cause);");
paramIndentedWriter.downIndent();
paramIndentedWriter.println("}");
}

protected void generateBannerComment(IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("paramIndentedWriter.println(" * This class generated by " + getClass().getName());
paramIndentedWriter.println(" * " + new Date());
paramIndentedWriter.println(" * DO NOT HAND EDIT!!!!");
paramIndentedWriter.println(" */");
}

protected void generateClassJavaDocComment(IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.println("paramIndentedWriter.println(" * This class was generated by " + getClass().getName() + ".");
paramIndentedWriter.println(" */");
}

protected void generateExtraImports(IndentedWriter paramIndentedWriter) throws IOException {}

protected void generatePreDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {}

protected void generatePostDelegateCode(Class paramClass, String paramString, Method paramMethod, IndentedWriter paramIndentedWriter) throws IOException {}

protected void generateExtraDeclarations(Class paramClass, String paramString, IndentedWriter paramIndentedWriter) throws IOException {}
}

