package com.mchange.v2.lang;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Coerce
{
static final Set CAN_COERCE;

static {
Class[] arrayOfClass = { byte.class, boolean.class, char.class, short.class, int.class, long.class, float.class, double.class, String.class, Byte.class, Boolean.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class };

HashSet<?> hashSet = new HashSet();
hashSet.addAll(Arrays.asList((Class<?>[][])arrayOfClass));
CAN_COERCE = Collections.unmodifiableSet(hashSet);
}

public static boolean canCoerce(Class paramClass) {
return CAN_COERCE.contains(paramClass);
}
public static boolean canCoerce(Object paramObject) {
return canCoerce(paramObject.getClass());
}
public static int toInt(String paramString) {
try {
return Integer.parseInt(paramString);
} catch (NumberFormatException numberFormatException) {
return (int)Double.parseDouble(paramString);
} 
}
public static long toLong(String paramString) {
try {
return Long.parseLong(paramString);
} catch (NumberFormatException numberFormatException) {
return (long)Double.parseDouble(paramString);
} 
}
public static float toFloat(String paramString) {
return Float.parseFloat(paramString);
}
public static double toDouble(String paramString) {
return Double.parseDouble(paramString);
}
public static byte toByte(String paramString) {
return (byte)toInt(paramString);
}
public static short toShort(String paramString) {
return (short)toInt(paramString);
}
public static boolean toBoolean(String paramString) {
return Boolean.valueOf(paramString).booleanValue();
}

public static char toChar(String paramString) {
paramString = paramString.trim();
if (paramString.length() == 1) {
return paramString.charAt(0);
}
return (char)toInt(paramString);
}
public static Object toObject(String paramString, Class<byte> paramClass) {
Class<Byte> clazz1;
Class<Double> clazz;
if (paramClass == byte.class) { clazz1 = Byte.class; }
else { Class<Boolean> clazz2; if (clazz1 == boolean.class) { clazz2 = Boolean.class; }
else { Class<Character> clazz3; if (clazz2 == char.class) { clazz3 = Character.class; }
else { Class<Short> clazz4; if (clazz3 == short.class) { clazz4 = Short.class; }
else { Class<Integer> clazz5; if (clazz4 == int.class) { clazz5 = Integer.class; }
else { Class<Long> clazz6; if (clazz5 == long.class) { clazz6 = Long.class; }
else { Class<Float> clazz7; if (clazz6 == float.class) { clazz7 = Float.class; }
else if (clazz7 == double.class) { clazz = Double.class; }  }  }  }  }  }
}
if (clazz == String.class)
return paramString; 
if (clazz == Byte.class)
return new Byte(toByte(paramString)); 
if (clazz == Boolean.class)
return Boolean.valueOf(paramString); 
if (clazz == Character.class)
return new Character(toChar(paramString)); 
if (clazz == Short.class)
return new Short(toShort(paramString)); 
if (clazz == Integer.class)
return new Integer(paramString); 
if (clazz == Long.class)
return new Long(paramString); 
if (clazz == Float.class)
return new Float(paramString); 
if (clazz == Double.class) {
return new Double(paramString);
}
throw new IllegalArgumentException("Cannot coerce to type: " + clazz.getName());
}
}

