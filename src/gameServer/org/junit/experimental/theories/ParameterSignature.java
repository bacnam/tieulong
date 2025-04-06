/*     */ package org.junit.experimental.theories;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ParameterSignature
/*     */ {
/*  15 */   private static final Map<Class<?>, Class<?>> CONVERTABLE_TYPES_MAP = buildConvertableTypesMap(); private final Class<?> type;
/*     */   
/*     */   private static Map<Class<?>, Class<?>> buildConvertableTypesMap() {
/*  18 */     Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
/*     */     
/*  20 */     putSymmetrically(map, boolean.class, Boolean.class);
/*  21 */     putSymmetrically(map, byte.class, Byte.class);
/*  22 */     putSymmetrically(map, short.class, Short.class);
/*  23 */     putSymmetrically(map, char.class, Character.class);
/*  24 */     putSymmetrically(map, int.class, Integer.class);
/*  25 */     putSymmetrically(map, long.class, Long.class);
/*  26 */     putSymmetrically(map, float.class, Float.class);
/*  27 */     putSymmetrically(map, double.class, Double.class);
/*     */     
/*  29 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */   private final Annotation[] annotations;
/*     */   private static <T> void putSymmetrically(Map<T, T> map, T a, T b) {
/*  33 */     map.put(a, b);
/*  34 */     map.put(b, a);
/*     */   }
/*     */   
/*     */   public static ArrayList<ParameterSignature> signatures(Method method) {
/*  38 */     return signatures(method.getParameterTypes(), method.getParameterAnnotations());
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<ParameterSignature> signatures(Constructor<?> constructor) {
/*  43 */     return signatures(constructor.getParameterTypes(), constructor.getParameterAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ArrayList<ParameterSignature> signatures(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
/*  49 */     ArrayList<ParameterSignature> sigs = new ArrayList<ParameterSignature>();
/*  50 */     for (int i = 0; i < parameterTypes.length; i++) {
/*  51 */       sigs.add(new ParameterSignature(parameterTypes[i], parameterAnnotations[i]));
/*     */     }
/*     */     
/*  54 */     return sigs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParameterSignature(Class<?> type, Annotation[] annotations) {
/*  62 */     this.type = type;
/*  63 */     this.annotations = annotations;
/*     */   }
/*     */   
/*     */   public boolean canAcceptValue(Object candidate) {
/*  67 */     return (candidate == null) ? (!this.type.isPrimitive()) : canAcceptType(candidate.getClass());
/*     */   }
/*     */   
/*     */   public boolean canAcceptType(Class<?> candidate) {
/*  71 */     return (this.type.isAssignableFrom(candidate) || isAssignableViaTypeConversion(this.type, candidate));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPotentiallyAcceptType(Class<?> candidate) {
/*  76 */     return (candidate.isAssignableFrom(this.type) || isAssignableViaTypeConversion(candidate, this.type) || canAcceptType(candidate));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isAssignableViaTypeConversion(Class<?> targetType, Class<?> candidate) {
/*  82 */     if (CONVERTABLE_TYPES_MAP.containsKey(candidate)) {
/*  83 */       Class<?> wrapperClass = CONVERTABLE_TYPES_MAP.get(candidate);
/*  84 */       return targetType.isAssignableFrom(wrapperClass);
/*     */     } 
/*  86 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/*  91 */     return this.type;
/*     */   }
/*     */   
/*     */   public List<Annotation> getAnnotations() {
/*  95 */     return Arrays.asList(this.annotations);
/*     */   }
/*     */   
/*     */   public boolean hasAnnotation(Class<? extends Annotation> type) {
/*  99 */     return (getAnnotation(type) != null);
/*     */   }
/*     */   
/*     */   public <T extends Annotation> T findDeepAnnotation(Class<T> annotationType) {
/* 103 */     Annotation[] annotations2 = this.annotations;
/* 104 */     return findDeepAnnotation(annotations2, annotationType, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T extends Annotation> T findDeepAnnotation(Annotation[] annotations, Class<T> annotationType, int depth) {
/* 109 */     if (depth == 0) {
/* 110 */       return null;
/*     */     }
/* 112 */     for (Annotation each : annotations) {
/* 113 */       if (annotationType.isInstance(each)) {
/* 114 */         return annotationType.cast(each);
/*     */       }
/* 116 */       Annotation candidate = findDeepAnnotation(each.annotationType().getAnnotations(), annotationType, depth - 1);
/*     */       
/* 118 */       if (candidate != null) {
/* 119 */         return annotationType.cast(candidate);
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
/* 127 */     for (Annotation each : getAnnotations()) {
/* 128 */       if (annotationType.isInstance(each)) {
/* 129 */         return annotationType.cast(each);
/*     */       }
/*     */     } 
/* 132 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/junit/experimental/theories/ParameterSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */