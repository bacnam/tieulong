/*     */ package com.mchange.v2.lang;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Coerce
/*     */ {
/*     */   static final Set CAN_COERCE;
/*     */   
/*     */   static {
/*  46 */     Class[] arrayOfClass = { byte.class, boolean.class, char.class, short.class, int.class, long.class, float.class, double.class, String.class, Byte.class, Boolean.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  66 */     HashSet<?> hashSet = new HashSet();
/*  67 */     hashSet.addAll(Arrays.asList((Class<?>[][])arrayOfClass));
/*  68 */     CAN_COERCE = Collections.unmodifiableSet(hashSet);
/*     */   }
/*     */   
/*     */   public static boolean canCoerce(Class paramClass) {
/*  72 */     return CAN_COERCE.contains(paramClass);
/*     */   }
/*     */   public static boolean canCoerce(Object paramObject) {
/*  75 */     return canCoerce(paramObject.getClass());
/*     */   }
/*     */   public static int toInt(String paramString) {
/*     */     try {
/*  79 */       return Integer.parseInt(paramString);
/*  80 */     } catch (NumberFormatException numberFormatException) {
/*  81 */       return (int)Double.parseDouble(paramString);
/*     */     } 
/*     */   }
/*     */   public static long toLong(String paramString) {
/*     */     try {
/*  86 */       return Long.parseLong(paramString);
/*  87 */     } catch (NumberFormatException numberFormatException) {
/*  88 */       return (long)Double.parseDouble(paramString);
/*     */     } 
/*     */   }
/*     */   public static float toFloat(String paramString) {
/*  92 */     return Float.parseFloat(paramString);
/*     */   }
/*     */   public static double toDouble(String paramString) {
/*  95 */     return Double.parseDouble(paramString);
/*     */   }
/*     */   public static byte toByte(String paramString) {
/*  98 */     return (byte)toInt(paramString);
/*     */   }
/*     */   public static short toShort(String paramString) {
/* 101 */     return (short)toInt(paramString);
/*     */   }
/*     */   public static boolean toBoolean(String paramString) {
/* 104 */     return Boolean.valueOf(paramString).booleanValue();
/*     */   }
/*     */   
/*     */   public static char toChar(String paramString) {
/* 108 */     paramString = paramString.trim();
/* 109 */     if (paramString.length() == 1) {
/* 110 */       return paramString.charAt(0);
/*     */     }
/* 112 */     return (char)toInt(paramString);
/*     */   }
/*     */   public static Object toObject(String paramString, Class<byte> paramClass) {
/*     */     Class<Byte> clazz1;
/*     */     Class<Double> clazz;
/* 117 */     if (paramClass == byte.class) { clazz1 = Byte.class; }
/* 118 */     else { Class<Boolean> clazz2; if (clazz1 == boolean.class) { clazz2 = Boolean.class; }
/* 119 */       else { Class<Character> clazz3; if (clazz2 == char.class) { clazz3 = Character.class; }
/* 120 */         else { Class<Short> clazz4; if (clazz3 == short.class) { clazz4 = Short.class; }
/* 121 */           else { Class<Integer> clazz5; if (clazz4 == int.class) { clazz5 = Integer.class; }
/* 122 */             else { Class<Long> clazz6; if (clazz5 == long.class) { clazz6 = Long.class; }
/* 123 */               else { Class<Float> clazz7; if (clazz6 == float.class) { clazz7 = Float.class; }
/* 124 */                 else if (clazz7 == double.class) { clazz = Double.class; }  }  }  }  }  }
/*     */        }
/* 126 */      if (clazz == String.class)
/* 127 */       return paramString; 
/* 128 */     if (clazz == Byte.class)
/* 129 */       return new Byte(toByte(paramString)); 
/* 130 */     if (clazz == Boolean.class)
/* 131 */       return Boolean.valueOf(paramString); 
/* 132 */     if (clazz == Character.class)
/* 133 */       return new Character(toChar(paramString)); 
/* 134 */     if (clazz == Short.class)
/* 135 */       return new Short(toShort(paramString)); 
/* 136 */     if (clazz == Integer.class)
/* 137 */       return new Integer(paramString); 
/* 138 */     if (clazz == Long.class)
/* 139 */       return new Long(paramString); 
/* 140 */     if (clazz == Float.class)
/* 141 */       return new Float(paramString); 
/* 142 */     if (clazz == Double.class) {
/* 143 */       return new Double(paramString);
/*     */     }
/* 145 */     throw new IllegalArgumentException("Cannot coerce to type: " + clazz.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/Coerce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */