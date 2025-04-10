package ch.qos.logback.core.joran.util;

import ch.qos.logback.core.spi.ContextAware;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class StringToObjectConverter
{
private static final Class<?>[] STING_CLASS_PARAMETER = new Class[] { String.class };

public static boolean canBeBuiltFromSimpleString(Class<?> parameterClass) {
Package p = parameterClass.getPackage();
if (parameterClass.isPrimitive())
return true; 
if (p != null && "java.lang".equals(p.getName()))
return true; 
if (followsTheValueOfConvention(parameterClass))
return true; 
if (parameterClass.isEnum())
return true; 
if (isOfTypeCharset(parameterClass)) {
return true;
}
return false;
}

public static Object convertArg(ContextAware ca, String val, Class<?> type) {
if (val == null) {
return null;
}
String v = val.trim();
if (String.class.isAssignableFrom(type))
return v; 
if (int.class.isAssignableFrom(type))
return new Integer(v); 
if (long.class.isAssignableFrom(type))
return new Long(v); 
if (float.class.isAssignableFrom(type))
return new Float(v); 
if (double.class.isAssignableFrom(type))
return new Double(v); 
if (boolean.class.isAssignableFrom(type)) {
if ("true".equalsIgnoreCase(v))
return Boolean.TRUE; 
if ("false".equalsIgnoreCase(v))
return Boolean.FALSE; 
} else {
if (type.isEnum())
return convertToEnum(ca, v, (Class)type); 
if (followsTheValueOfConvention(type))
return convertByValueOfMethod(ca, type, v); 
if (isOfTypeCharset(type)) {
return convertToCharset(ca, val);
}
} 
return null;
}

private static boolean isOfTypeCharset(Class<?> type) {
return Charset.class.isAssignableFrom(type);
}

private static Charset convertToCharset(ContextAware ca, String val) {
try {
return Charset.forName(val);
} catch (UnsupportedCharsetException e) {
ca.addError("Failed to get charset [" + val + "]", e);
return null;
} 
}

private static boolean followsTheValueOfConvention(Class<?> parameterClass) {
try {
Method valueOfMethod = parameterClass.getMethod("valueOf", STING_CLASS_PARAMETER);

int mod = valueOfMethod.getModifiers();
if (Modifier.isStatic(mod)) {
return true;
}
} catch (SecurityException e) {

} catch (NoSuchMethodException e) {}

return false;
}

private static Object convertByValueOfMethod(ContextAware ca, Class<?> type, String val) {
try {
Method valueOfMethod = type.getMethod("valueOf", STING_CLASS_PARAMETER);

return valueOfMethod.invoke(null, new Object[] { val });
} catch (Exception e) {
ca.addError("Failed to invoke valueOf{} method in class [" + type.getName() + "] with value [" + val + "]");

return null;
} 
}

private static Object convertToEnum(ContextAware ca, String val, Class<? extends Enum> enumType) {
return Enum.valueOf(enumType, val);
}

boolean isBuildableFromSimpleString() {
return false;
}
}

