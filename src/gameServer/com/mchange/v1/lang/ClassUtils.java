/*     */ package com.mchange.v1.lang;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassUtils
/*     */ {
/*  51 */   static final String[] EMPTY_SA = new String[0];
/*     */   
/*     */   static Map primitivesToClasses;
/*     */ 
/*     */   
/*     */   static {
/*  57 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/*  58 */     hashMap.put("boolean", boolean.class);
/*  59 */     hashMap.put("int", int.class);
/*  60 */     hashMap.put("char", char.class);
/*  61 */     hashMap.put("short", short.class);
/*  62 */     hashMap.put("int", int.class);
/*  63 */     hashMap.put("long", long.class);
/*  64 */     hashMap.put("float", float.class);
/*  65 */     hashMap.put("double", double.class);
/*  66 */     hashMap.put("void", void.class);
/*     */     
/*  68 */     primitivesToClasses = Collections.unmodifiableMap(hashMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set publicSupertypesForMethods(Class paramClass, Method[] paramArrayOfMethod) {
/*  73 */     Set set = allAssignableFrom(paramClass);
/*  74 */     HashSet<Class<?>> hashSet = new HashSet();
/*  75 */     for (Class<?> clazz : (Iterable<Class<?>>)set) {
/*     */ 
/*     */       
/*  78 */       if (isPublic(clazz) && hasAllMethodsAsSupertype(clazz, paramArrayOfMethod))
/*  79 */         hashSet.add(clazz); 
/*     */     } 
/*  81 */     return Collections.unmodifiableSet(hashSet);
/*     */   }
/*     */   
/*     */   public static boolean isPublic(Class paramClass) {
/*  85 */     return ((paramClass.getModifiers() & 0x1) != 0);
/*     */   }
/*     */   public static boolean hasAllMethodsAsSupertype(Class paramClass, Method[] paramArrayOfMethod) {
/*  88 */     return hasAllMethods(paramClass, paramArrayOfMethod, true);
/*     */   }
/*     */   public static boolean hasAllMethodsAsSubtype(Class paramClass, Method[] paramArrayOfMethod) {
/*  91 */     return hasAllMethods(paramClass, paramArrayOfMethod, false);
/*     */   } private static boolean hasAllMethods(Class paramClass, Method[] paramArrayOfMethod, boolean paramBoolean) {
/*     */     byte b;
/*     */     int i;
/*  95 */     for (b = 0, i = paramArrayOfMethod.length; b < i; b++) {
/*  96 */       if (!containsMethod(paramClass, paramArrayOfMethod[b], paramBoolean))
/*  97 */         return false; 
/*  98 */     }  return true;
/*     */   }
/*     */   
/*     */   public static boolean containsMethodAsSupertype(Class paramClass, Method paramMethod) {
/* 102 */     return containsMethod(paramClass, paramMethod, true);
/*     */   }
/*     */   public static boolean containsMethodAsSubtype(Class paramClass, Method paramMethod) {
/* 105 */     return containsMethod(paramClass, paramMethod, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean containsMethod(Class paramClass, Method paramMethod, boolean paramBoolean) {
/*     */     try {
/* 113 */       Method method = paramClass.getMethod(paramMethod.getName(), paramMethod.getParameterTypes());
/* 114 */       Class<?> clazz1 = paramMethod.getReturnType();
/* 115 */       Class<?> clazz2 = method.getReturnType();
/*     */ 
/*     */       
/* 118 */       return (clazz1.equals(clazz2) || (paramBoolean && clazz2.isAssignableFrom(clazz1)) || (!paramBoolean && clazz1.isAssignableFrom(clazz2)));
/*     */ 
/*     */     
/*     */     }
/* 122 */     catch (NoSuchMethodException noSuchMethodException) {
/* 123 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set allAssignableFrom(Class<?> paramClass) {
/* 128 */     HashSet<Class<?>> hashSet = new HashSet();
/*     */ 
/*     */     
/* 131 */     for (Class<?> clazz = paramClass; clazz != null; clazz = clazz.getSuperclass()) {
/* 132 */       hashSet.add(clazz);
/*     */     }
/*     */     
/* 135 */     addSuperInterfacesToSet(paramClass, hashSet);
/* 136 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String simpleClassName(Class<?> paramClass) {
/* 142 */     byte b = 0;
/* 143 */     while (paramClass.isArray()) {
/*     */       
/* 145 */       b++;
/* 146 */       paramClass = paramClass.getComponentType();
/*     */     } 
/* 148 */     String str = simpleClassName(paramClass.getName());
/* 149 */     if (b > 0) {
/*     */       
/* 151 */       StringBuffer stringBuffer = new StringBuffer(16);
/* 152 */       stringBuffer.append(str);
/* 153 */       for (byte b1 = 0; b1 < b; b1++)
/* 154 */         stringBuffer.append("[]"); 
/* 155 */       return stringBuffer.toString();
/*     */     } 
/*     */     
/* 158 */     return str;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String simpleClassName(String paramString) {
/* 163 */     int i = paramString.lastIndexOf('.');
/* 164 */     if (i < 0)
/* 165 */       return paramString; 
/* 166 */     String str = paramString.substring(i + 1);
/* 167 */     if (str.indexOf('$') >= 0) {
/*     */       
/* 169 */       StringBuffer stringBuffer = new StringBuffer(str); byte b; int j;
/* 170 */       for (b = 0, j = stringBuffer.length(); b < j; b++) {
/*     */         
/* 172 */         if (stringBuffer.charAt(b) == '$')
/* 173 */           stringBuffer.setCharAt(b, '.'); 
/*     */       } 
/* 175 */       return stringBuffer.toString();
/*     */     } 
/*     */     
/* 178 */     return str;
/*     */   }
/*     */   
/*     */   public static boolean isPrimitive(String paramString) {
/* 182 */     return (primitivesToClasses.get(paramString) != null);
/*     */   }
/*     */   public static Class classForPrimitive(String paramString) {
/* 185 */     return (Class)primitivesToClasses.get(paramString);
/*     */   }
/*     */   
/*     */   public static Class forName(String paramString) throws ClassNotFoundException {
/* 189 */     Class<?> clazz = classForPrimitive(paramString);
/* 190 */     if (clazz == null)
/* 191 */       clazz = Class.forName(paramString); 
/* 192 */     return clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class forName(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) throws AmbiguousClassNameException, ClassNotFoundException {
/*     */     try {
/* 199 */       return Class.forName(paramString);
/* 200 */     } catch (ClassNotFoundException classNotFoundException) {
/* 201 */       return classForSimpleName(paramString, paramArrayOfString1, paramArrayOfString2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class classForSimpleName(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) throws AmbiguousClassNameException, ClassNotFoundException {
/* 207 */     HashSet<String> hashSet = new HashSet();
/* 208 */     Class<?> clazz = classForPrimitive(paramString);
/*     */     
/* 210 */     if (clazz == null) {
/*     */       
/* 212 */       if (paramArrayOfString1 == null) {
/* 213 */         paramArrayOfString1 = EMPTY_SA;
/*     */       }
/* 215 */       if (paramArrayOfString2 == null)
/* 216 */         paramArrayOfString2 = EMPTY_SA;  byte b;
/*     */       int i;
/* 218 */       for (b = 0, i = paramArrayOfString2.length; b < i; b++) {
/*     */         
/* 220 */         String str = fqcnLastElement(paramArrayOfString2[b]);
/* 221 */         if (!hashSet.add(str)) {
/* 222 */           throw new IllegalArgumentException("Duplicate imported classes: " + str);
/*     */         }
/* 224 */         if (paramString.equals(str))
/*     */         {
/* 226 */           clazz = Class.forName(paramArrayOfString2[b]); } 
/*     */       } 
/* 228 */       if (clazz == null) {
/*     */         try {
/* 230 */           clazz = Class.forName("java.lang." + paramString);
/* 231 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */         
/* 234 */         for (b = 0, i = paramArrayOfString1.length; b < i; b++) {
/*     */ 
/*     */           
/*     */           try {
/* 238 */             String str = paramArrayOfString1[b] + '.' + paramString;
/* 239 */             Class<?> clazz1 = Class.forName(str);
/* 240 */             if (clazz == null) {
/* 241 */               clazz = clazz1;
/*     */             } else {
/* 243 */               throw new AmbiguousClassNameException(paramString, clazz, clazz1);
/*     */             } 
/* 245 */           } catch (ClassNotFoundException classNotFoundException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 250 */     if (clazz == null) {
/* 251 */       throw new ClassNotFoundException("Could not find a class whose unqualified name is \"" + paramString + "\" with the imports supplied. Import packages are " + Arrays.asList(paramArrayOfString1) + "; class imports are " + Arrays.asList(paramArrayOfString2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 256 */     return clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String resolvableTypeName(Class paramClass, String[] paramArrayOfString1, String[] paramArrayOfString2) throws ClassNotFoundException {
/* 262 */     String str = simpleClassName(paramClass);
/*     */     try {
/* 264 */       classForSimpleName(str, paramArrayOfString1, paramArrayOfString2);
/* 265 */     } catch (AmbiguousClassNameException ambiguousClassNameException) {
/* 266 */       return paramClass.getName();
/* 267 */     }  return str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String fqcnLastElement(String paramString) {
/* 272 */     int i = paramString.lastIndexOf('.');
/* 273 */     if (i < 0)
/* 274 */       return paramString; 
/* 275 */     return paramString.substring(i + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void addSuperInterfacesToSet(Class paramClass, Set<Class<?>> paramSet) {
/* 282 */     Class[] arrayOfClass = paramClass.getInterfaces(); byte b; int i;
/* 283 */     for (b = 0, i = arrayOfClass.length; b < i; b++) {
/*     */       
/* 285 */       paramSet.add(arrayOfClass[b]);
/* 286 */       addSuperInterfacesToSet(arrayOfClass[b], paramSet);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/lang/ClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */