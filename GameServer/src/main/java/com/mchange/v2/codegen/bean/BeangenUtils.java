package com.mchange.v2.codegen.bean;

import com.mchange.v1.lang.ClassUtils;
import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import java.io.IOException;
import java.util.Comparator;

public final class BeangenUtils
{
public static final Comparator PROPERTY_COMPARATOR = new Comparator()
{
public int compare(Object param1Object1, Object param1Object2)
{
Property property1 = (Property)param1Object1;
Property property2 = (Property)param1Object2;

return String.CASE_INSENSITIVE_ORDER.compare(property1.getName(), property2.getName());
}
};

public static String capitalize(String paramString) {
char c = paramString.charAt(0);
return Character.toUpperCase(c) + paramString.substring(1);
}

public static void writeExplicitDefaultConstructor(int paramInt, ClassInfo paramClassInfo, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.print(CodegenUtils.getModifierString(paramInt));
paramIndentedWriter.println(' ' + paramClassInfo.getClassName() + "()");
paramIndentedWriter.println("{}");
}

public static void writeArgList(Property[] paramArrayOfProperty, boolean paramBoolean, IndentedWriter paramIndentedWriter) throws IOException {
byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {

if (b != 0)
paramIndentedWriter.print(", "); 
if (paramBoolean)
paramIndentedWriter.print(paramArrayOfProperty[b].getSimpleTypeName() + ' '); 
paramIndentedWriter.print(paramArrayOfProperty[b].getName());
} 
}

public static void writePropertyMember(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
writePropertyVariable(paramProperty, paramIndentedWriter);
}
public static void writePropertyVariable(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
writePropertyVariable(paramProperty, paramProperty.getDefaultValueExpression(), paramIndentedWriter);
}

public static void writePropertyMember(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
writePropertyVariable(paramProperty, paramString, paramIndentedWriter);
}

public static void writePropertyVariable(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getVariableModifiers()));
paramIndentedWriter.print(' ' + paramProperty.getSimpleTypeName() + ' ' + paramProperty.getName());
String str = paramString;
if (str != null)
paramIndentedWriter.print(" = " + str); 
paramIndentedWriter.println(';');
}

public static void writePropertyGetter(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
writePropertyGetter(paramProperty, paramProperty.getDefensiveCopyExpression(), paramIndentedWriter);
}

public static void writePropertyGetter(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
String str1 = "boolean".equals(paramProperty.getSimpleTypeName()) ? "is" : "get";
paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getGetterModifiers()));
paramIndentedWriter.println(' ' + paramProperty.getSimpleTypeName() + ' ' + str1 + capitalize(paramProperty.getName()) + "()");
String str2 = paramString;
if (str2 == null) str2 = paramProperty.getName(); 
paramIndentedWriter.println("{ return " + str2 + "; }");
}

public static void writePropertySetter(Property paramProperty, IndentedWriter paramIndentedWriter) throws IOException {
writePropertySetter(paramProperty, paramProperty.getDefensiveCopyExpression(), paramIndentedWriter);
}

public static void writePropertySetter(Property paramProperty, String paramString, IndentedWriter paramIndentedWriter) throws IOException {
String str1 = paramString;
if (str1 == null) str1 = paramProperty.getName(); 
String str2 = "this." + paramProperty.getName();
String str3 = "this." + paramProperty.getName() + " = " + str1 + ';';
writePropertySetterWithGetExpressionSetStatement(paramProperty, str2, str3, paramIndentedWriter);
}

public static void writePropertySetterWithGetExpressionSetStatement(Property paramProperty, String paramString1, String paramString2, IndentedWriter paramIndentedWriter) throws IOException {
paramIndentedWriter.print(CodegenUtils.getModifierString(paramProperty.getSetterModifiers()));
paramIndentedWriter.print(" void set" + capitalize(paramProperty.getName()) + "( " + paramProperty.getSimpleTypeName() + ' ' + paramProperty.getName() + " )");
if (paramProperty.isConstrained()) {
paramIndentedWriter.println(" throws PropertyVetoException");
} else {
paramIndentedWriter.println();
}  paramIndentedWriter.println('{');
paramIndentedWriter.upIndent();

if (changeMarked(paramProperty)) {
String str3;
paramIndentedWriter.println(paramProperty.getSimpleTypeName() + " oldVal = " + paramString1 + ';');

String str1 = "oldVal";
String str2 = paramProperty.getName();

String str4 = paramProperty.getSimpleTypeName();
if (ClassUtils.isPrimitive(str4)) {

Class<byte> clazz = ClassUtils.classForPrimitive(str4);

if (clazz == byte.class) {

str1 = "new Byte( " + str1 + " )";
str2 = "new Byte( " + str2 + " )";
}
else if (clazz == char.class) {

str1 = "new Character( " + str1 + " )";
str2 = "new Character( " + str2 + " )";
}
else if (clazz == short.class) {

str1 = "new Short( " + str1 + " )";
str2 = "new Short( " + str2 + " )";
}
else if (clazz == float.class) {

str1 = "new Float( " + str1 + " )";
str2 = "new Float( " + str2 + " )";
}
else if (clazz == double.class) {

str1 = "new Double( " + str1 + " )";
str2 = "new Double( " + str2 + " )";
} 

str3 = "oldVal != " + paramProperty.getName();
} else {

str3 = "! eqOrBothNull( oldVal, " + paramProperty.getName() + " )";
} 
if (paramProperty.isConstrained()) {

paramIndentedWriter.println("if ( " + str3 + " )");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("vcs.fireVetoableChange( \"" + paramProperty.getName() + "\", " + str1 + ", " + str2 + " );");
paramIndentedWriter.downIndent();
} 

paramIndentedWriter.println(paramString2);

if (paramProperty.isBound()) {

paramIndentedWriter.println("if ( " + str3 + " )");
paramIndentedWriter.upIndent();
paramIndentedWriter.println("pcs.firePropertyChange( \"" + paramProperty.getName() + "\", " + str1 + ", " + str2 + " );");
paramIndentedWriter.downIndent();
} 
} else {

paramIndentedWriter.println(paramString2);
} 
paramIndentedWriter.downIndent();
paramIndentedWriter.println('}');
}
public static boolean hasBoundProperties(Property[] paramArrayOfProperty) {
byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
if (paramArrayOfProperty[b].isBound()) return true; 
}  return false;
}
public static boolean hasConstrainedProperties(Property[] paramArrayOfProperty) {
byte b;
int i;
for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
if (paramArrayOfProperty[b].isConstrained()) return true; 
}  return false;
}

private static boolean changeMarked(Property paramProperty) {
return (paramProperty.isBound() || paramProperty.isConstrained());
}
}

