package com.mchange.v2.codegen;

import com.mchange.v1.lang.ClassUtils;
import java.io.File;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class CodegenUtils
{
public static String getModifierString(int paramInt) {
StringBuffer stringBuffer = new StringBuffer(32);
if (Modifier.isPublic(paramInt))
stringBuffer.append("public "); 
if (Modifier.isProtected(paramInt))
stringBuffer.append("protected "); 
if (Modifier.isPrivate(paramInt))
stringBuffer.append("private "); 
if (Modifier.isAbstract(paramInt))
stringBuffer.append("abstract "); 
if (Modifier.isStatic(paramInt))
stringBuffer.append("static "); 
if (Modifier.isFinal(paramInt))
stringBuffer.append("final "); 
if (Modifier.isSynchronized(paramInt))
stringBuffer.append("synchronized "); 
if (Modifier.isTransient(paramInt))
stringBuffer.append("transient "); 
if (Modifier.isVolatile(paramInt))
stringBuffer.append("volatile "); 
if (Modifier.isStrict(paramInt))
stringBuffer.append("strictfp "); 
if (Modifier.isNative(paramInt))
stringBuffer.append("native "); 
if (Modifier.isInterface(paramInt))
stringBuffer.append("interface "); 
return stringBuffer.toString().trim();
}

public static Class unarrayClass(Class<?> paramClass) {
Class<?> clazz = paramClass;
while (clazz.isArray())
clazz = clazz.getComponentType(); 
return clazz;
}

public static boolean inSamePackage(String paramString1, String paramString2) {
int i = paramString1.lastIndexOf('.');
int j = paramString2.lastIndexOf('.');

if (i < 0 || j < 0)
return true; 
if (paramString1.substring(0, i).equals(paramString1.substring(0, i))) {

if (paramString2.indexOf('.') >= 0) {
return false;
}
return true;
} 

return false;
}

public static String fqcnLastElement(String paramString) {
return ClassUtils.fqcnLastElement(paramString);
}
public static String methodSignature(Method paramMethod) {
return methodSignature(paramMethod, null);
}
public static String methodSignature(Method paramMethod, String[] paramArrayOfString) {
return methodSignature(1, paramMethod, paramArrayOfString);
}

public static String methodSignature(int paramInt, Method paramMethod, String[] paramArrayOfString) {
StringBuffer stringBuffer = new StringBuffer(256);
stringBuffer.append(getModifierString(paramInt));
stringBuffer.append(' ');
stringBuffer.append(ClassUtils.simpleClassName(paramMethod.getReturnType()));
stringBuffer.append(' ');
stringBuffer.append(paramMethod.getName());
stringBuffer.append('(');
Class[] arrayOfClass1 = paramMethod.getParameterTypes(); int i;
for (byte b = 0; b < i; b++) {

if (b != 0)
stringBuffer.append(", "); 
stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass1[b]));
stringBuffer.append(' ');
stringBuffer.append((paramArrayOfString == null) ? String.valueOf((char)(97 + b)) : paramArrayOfString[b]);
} 
stringBuffer.append(')');
Class[] arrayOfClass2 = paramMethod.getExceptionTypes();
if (arrayOfClass2.length > 0) {

stringBuffer.append(" throws "); int j;
for (i = 0, j = arrayOfClass2.length; i < j; i++) {

if (i != 0)
stringBuffer.append(", "); 
stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass2[i]));
} 
} 
return stringBuffer.toString();
}

public static String methodCall(Method paramMethod) {
return methodCall(paramMethod, null);
}

public static String methodCall(Method paramMethod, String[] paramArrayOfString) {
StringBuffer stringBuffer = new StringBuffer(256);
stringBuffer.append(paramMethod.getName());
stringBuffer.append('(');
Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
for (b = 0, i = arrayOfClass.length; b < i; b++) {

if (b != 0)
stringBuffer.append(", "); 
stringBuffer.append((paramArrayOfString == null) ? generatedArgumentName(b) : paramArrayOfString[b]);
} 
stringBuffer.append(')');
return stringBuffer.toString();
}

public static String reflectiveMethodObjectArray(Method paramMethod) {
return reflectiveMethodObjectArray(paramMethod, null);
}

public static String reflectiveMethodObjectArray(Method paramMethod, String[] paramArrayOfString) {
StringBuffer stringBuffer = new StringBuffer(256);
stringBuffer.append("new Object[] ");
stringBuffer.append('{');
Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
for (b = 0, i = arrayOfClass.length; b < i; b++) {

if (b != 0)
stringBuffer.append(", "); 
stringBuffer.append((paramArrayOfString == null) ? generatedArgumentName(b) : paramArrayOfString[b]);
} 
stringBuffer.append('}');
return stringBuffer.toString();
}

public static String reflectiveMethodParameterTypeArray(Method paramMethod) {
StringBuffer stringBuffer = new StringBuffer(256);
stringBuffer.append("new Class[] ");
stringBuffer.append('{');
Class[] arrayOfClass = paramMethod.getParameterTypes(); byte b; int i;
for (b = 0, i = arrayOfClass.length; b < i; b++) {

if (b != 0)
stringBuffer.append(", "); 
stringBuffer.append(ClassUtils.simpleClassName(arrayOfClass[b]));
stringBuffer.append(".class");
} 
stringBuffer.append('}');
return stringBuffer.toString();
}

public static String generatedArgumentName(int paramInt) {
return String.valueOf((char)(97 + paramInt));
}
public static String simpleClassName(Class paramClass) {
return ClassUtils.simpleClassName(paramClass);
}
public static IndentedWriter toIndentedWriter(Writer paramWriter) {
return (paramWriter instanceof IndentedWriter) ? (IndentedWriter)paramWriter : new IndentedWriter(paramWriter);
}

public static String packageNameToFileSystemDirPath(String paramString) {
StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
for (b = 0, i = stringBuffer.length(); b < i; b++) {
if (stringBuffer.charAt(b) == '.')
stringBuffer.setCharAt(b, File.separatorChar); 
}  stringBuffer.append(File.separatorChar);
return stringBuffer.toString();
}
}

