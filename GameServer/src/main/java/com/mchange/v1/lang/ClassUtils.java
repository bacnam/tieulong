package com.mchange.v1.lang;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class ClassUtils
{
static final String[] EMPTY_SA = new String[0];

static Map primitivesToClasses;

static {
HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
hashMap.put("boolean", boolean.class);
hashMap.put("int", int.class);
hashMap.put("char", char.class);
hashMap.put("short", short.class);
hashMap.put("int", int.class);
hashMap.put("long", long.class);
hashMap.put("float", float.class);
hashMap.put("double", double.class);
hashMap.put("void", void.class);

primitivesToClasses = Collections.unmodifiableMap(hashMap);
}

public static Set publicSupertypesForMethods(Class paramClass, Method[] paramArrayOfMethod) {
Set set = allAssignableFrom(paramClass);
HashSet<Class<?>> hashSet = new HashSet();
for (Class<?> clazz : (Iterable<Class<?>>)set) {

if (isPublic(clazz) && hasAllMethodsAsSupertype(clazz, paramArrayOfMethod))
hashSet.add(clazz); 
} 
return Collections.unmodifiableSet(hashSet);
}

public static boolean isPublic(Class paramClass) {
return ((paramClass.getModifiers() & 0x1) != 0);
}
public static boolean hasAllMethodsAsSupertype(Class paramClass, Method[] paramArrayOfMethod) {
return hasAllMethods(paramClass, paramArrayOfMethod, true);
}
public static boolean hasAllMethodsAsSubtype(Class paramClass, Method[] paramArrayOfMethod) {
return hasAllMethods(paramClass, paramArrayOfMethod, false);
} private static boolean hasAllMethods(Class paramClass, Method[] paramArrayOfMethod, boolean paramBoolean) {
byte b;
int i;
for (b = 0, i = paramArrayOfMethod.length; b < i; b++) {
if (!containsMethod(paramClass, paramArrayOfMethod[b], paramBoolean))
return false; 
}  return true;
}

public static boolean containsMethodAsSupertype(Class paramClass, Method paramMethod) {
return containsMethod(paramClass, paramMethod, true);
}
public static boolean containsMethodAsSubtype(Class paramClass, Method paramMethod) {
return containsMethod(paramClass, paramMethod, false);
}

private static boolean containsMethod(Class paramClass, Method paramMethod, boolean paramBoolean) {
try {
Method method = paramClass.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
Class<?> clazz1 = paramMethod.getReturnType();
Class<?> clazz2 = method.getReturnType();

return (clazz1.equals(clazz2) || (paramBoolean && clazz2.isAssignableFrom(clazz1)) || (!paramBoolean && clazz1.isAssignableFrom(clazz2)));

}
catch (NoSuchMethodException noSuchMethodException) {
return false;
} 
}

public static Set allAssignableFrom(Class<?> paramClass) {
HashSet<Class<?>> hashSet = new HashSet();

for (Class<?> clazz = paramClass; clazz != null; clazz = clazz.getSuperclass()) {
hashSet.add(clazz);
}

addSuperInterfacesToSet(paramClass, hashSet);
return hashSet;
}

public static String simpleClassName(Class<?> paramClass) {
byte b = 0;
while (paramClass.isArray()) {

b++;
paramClass = paramClass.getComponentType();
} 
String str = simpleClassName(paramClass.getName());
if (b > 0) {

StringBuffer stringBuffer = new StringBuffer(16);
stringBuffer.append(str);
for (byte b1 = 0; b1 < b; b1++)
stringBuffer.append("[]"); 
return stringBuffer.toString();
} 

return str;
}

private static String simpleClassName(String paramString) {
int i = paramString.lastIndexOf('.');
if (i < 0)
return paramString; 
String str = paramString.substring(i + 1);
if (str.indexOf('$') >= 0) {

StringBuffer stringBuffer = new StringBuffer(str); byte b; int j;
for (b = 0, j = stringBuffer.length(); b < j; b++) {

if (stringBuffer.charAt(b) == '$')
stringBuffer.setCharAt(b, '.'); 
} 
return stringBuffer.toString();
} 

return str;
}

public static boolean isPrimitive(String paramString) {
return (primitivesToClasses.get(paramString) != null);
}
public static Class classForPrimitive(String paramString) {
return (Class)primitivesToClasses.get(paramString);
}

public static Class forName(String paramString) throws ClassNotFoundException {
Class<?> clazz = classForPrimitive(paramString);
if (clazz == null)
clazz = Class.forName(paramString); 
return clazz;
}

public static Class forName(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) throws AmbiguousClassNameException, ClassNotFoundException {
try {
return Class.forName(paramString);
} catch (ClassNotFoundException classNotFoundException) {
return classForSimpleName(paramString, paramArrayOfString1, paramArrayOfString2);
} 
}

public static Class classForSimpleName(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) throws AmbiguousClassNameException, ClassNotFoundException {
HashSet<String> hashSet = new HashSet();
Class<?> clazz = classForPrimitive(paramString);

if (clazz == null) {

if (paramArrayOfString1 == null) {
paramArrayOfString1 = EMPTY_SA;
}
if (paramArrayOfString2 == null)
paramArrayOfString2 = EMPTY_SA;  byte b;
int i;
for (b = 0, i = paramArrayOfString2.length; b < i; b++) {

String str = fqcnLastElement(paramArrayOfString2[b]);
if (!hashSet.add(str)) {
throw new IllegalArgumentException("Duplicate imported classes: " + str);
}
if (paramString.equals(str))
{
clazz = Class.forName(paramArrayOfString2[b]); } 
} 
if (clazz == null) {
try {
clazz = Class.forName("java.lang." + paramString);
} catch (ClassNotFoundException classNotFoundException) {}

for (b = 0, i = paramArrayOfString1.length; b < i; b++) {

try {
String str = paramArrayOfString1[b] + '.' + paramString;
Class<?> clazz1 = Class.forName(str);
if (clazz == null) {
clazz = clazz1;
} else {
throw new AmbiguousClassNameException(paramString, clazz, clazz1);
} 
} catch (ClassNotFoundException classNotFoundException) {}
} 
} 
} 

if (clazz == null) {
throw new ClassNotFoundException("Could not find a class whose unqualified name is \"" + paramString + "\" with the imports supplied. Import packages are " + Arrays.asList(paramArrayOfString1) + "; class imports are " + Arrays.asList(paramArrayOfString2));
}

return clazz;
}

public static String resolvableTypeName(Class paramClass, String[] paramArrayOfString1, String[] paramArrayOfString2) throws ClassNotFoundException {
String str = simpleClassName(paramClass);
try {
classForSimpleName(str, paramArrayOfString1, paramArrayOfString2);
} catch (AmbiguousClassNameException ambiguousClassNameException) {
return paramClass.getName();
}  return str;
}

public static String fqcnLastElement(String paramString) {
int i = paramString.lastIndexOf('.');
if (i < 0)
return paramString; 
return paramString.substring(i + 1);
}

private static void addSuperInterfacesToSet(Class paramClass, Set<Class<?>> paramSet) {
Class[] arrayOfClass = paramClass.getInterfaces(); byte b; int i;
for (b = 0, i = arrayOfClass.length; b < i; b++) {

paramSet.add(arrayOfClass[b]);
addSuperInterfacesToSet(arrayOfClass[b], paramSet);
} 
}
}

