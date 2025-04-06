/*     */ package com.mchange.v2.beans;
/*     */ 
/*     */ import com.mchange.v2.lang.Coerce;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.beans.PropertyEditorManager;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
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
/*     */ public final class BeansUtils
/*     */ {
/*  47 */   static final MLogger logger = MLog.getLogger(BeansUtils.class);
/*     */   
/*  49 */   static final Object[] EMPTY_ARGS = new Object[0];
/*     */ 
/*     */   
/*     */   public static PropertyEditor findPropertyEditor(PropertyDescriptor paramPropertyDescriptor) {
/*  53 */     PropertyEditor propertyEditor = null;
/*  54 */     Class<?> clazz = null;
/*     */     
/*     */     try {
/*  57 */       clazz = paramPropertyDescriptor.getPropertyEditorClass();
/*  58 */       if (clazz != null) {
/*  59 */         propertyEditor = (PropertyEditor)clazz.newInstance();
/*     */       }
/*  61 */     } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  66 */       if (logger.isLoggable(MLevel.WARNING)) {
/*  67 */         logger.log(MLevel.WARNING, "Bad property editor class " + clazz.getName() + " registered for property " + paramPropertyDescriptor.getName(), exception);
/*     */       }
/*     */     } 
/*  70 */     if (propertyEditor == null)
/*  71 */       propertyEditor = PropertyEditorManager.findEditor(paramPropertyDescriptor.getPropertyType()); 
/*  72 */     return propertyEditor;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equalsByAccessibleProperties(Object paramObject1, Object paramObject2) throws IntrospectionException {
/*  77 */     return equalsByAccessibleProperties(paramObject1, paramObject2, Collections.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equalsByAccessibleProperties(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
/*  82 */     HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
/*  83 */     HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
/*  84 */     extractAccessiblePropertiesToMap(hashMap1, paramObject1, paramCollection);
/*  85 */     extractAccessiblePropertiesToMap(hashMap2, paramObject2, paramCollection);
/*     */ 
/*     */     
/*  88 */     return hashMap1.equals(hashMap2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsByAccessiblePropertiesVerbose(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
/*  94 */     HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
/*  95 */     HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
/*  96 */     extractAccessiblePropertiesToMap(hashMap1, paramObject1, paramCollection);
/*  97 */     extractAccessiblePropertiesToMap(hashMap2, paramObject2, paramCollection);
/*     */     
/*  99 */     boolean bool = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     if (hashMap1.size() != hashMap2.size()) {
/*     */       
/* 107 */       System.err.println("Unequal sizes --> Map0: " + hashMap1.size() + "; m1: " + hashMap2.size());
/* 108 */       Set set1 = hashMap1.keySet();
/* 109 */       set1.removeAll(hashMap2.keySet());
/*     */       
/* 111 */       Set set2 = hashMap2.keySet();
/* 112 */       set2.removeAll(hashMap1.keySet());
/*     */       
/* 114 */       if (set1.size() > 0) {
/*     */         
/* 116 */         System.err.println("Map0 extras:");
/* 117 */         for (Iterator<E> iterator = set1.iterator(); iterator.hasNext();)
/* 118 */           System.err.println('\t' + iterator.next().toString()); 
/*     */       } 
/* 120 */       if (set2.size() > 0) {
/*     */         
/* 122 */         System.err.println("Map1 extras:");
/* 123 */         for (Iterator<E> iterator = set2.iterator(); iterator.hasNext();)
/* 124 */           System.err.println('\t' + iterator.next().toString()); 
/*     */       } 
/* 126 */       bool = false;
/*     */     } 
/* 128 */     for (String str : hashMap1.keySet()) {
/*     */ 
/*     */       
/* 131 */       Object object1 = hashMap1.get(str);
/* 132 */       Object object2 = hashMap2.get(str);
/* 133 */       if ((object1 == null && object2 != null) || (object1 != null && !object1.equals(object2))) {
/*     */         
/* 135 */         System.err.println('\t' + str + ": " + object1 + " != " + object2);
/* 136 */         bool = false;
/*     */       } 
/*     */     } 
/*     */     
/* 140 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void overwriteAccessibleProperties(Object paramObject1, Object paramObject2) throws IntrospectionException {
/* 145 */     overwriteAccessibleProperties(paramObject1, paramObject2, Collections.EMPTY_SET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void overwriteAccessibleProperties(Object paramObject1, Object paramObject2, Collection paramCollection) throws IntrospectionException {
/*     */     try {
/* 152 */       BeanInfo beanInfo = Introspector.getBeanInfo(paramObject1.getClass(), Object.class);
/* 153 */       PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
/* 154 */       for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */         
/* 156 */         PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 157 */         if (!paramCollection.contains(propertyDescriptor.getName())) {
/*     */ 
/*     */           
/* 160 */           Method method1 = propertyDescriptor.getReadMethod();
/* 161 */           Method method2 = propertyDescriptor.getWriteMethod();
/*     */           
/* 163 */           if (method1 == null || method2 == null) {
/*     */             
/* 165 */             if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 173 */               if (logger.isLoggable(MLevel.WARNING)) {
/* 174 */                 logger.warning("BeansUtils.overwriteAccessibleProperties() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
/*     */               }
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 182 */             if (logger.isLoggable(MLevel.INFO)) {
/* 183 */               logger.info("Property inaccessible for overwriting: " + propertyDescriptor.getName());
/*     */             }
/*     */           } else {
/*     */             
/* 187 */             Object object = method1.invoke(paramObject1, EMPTY_ARGS);
/* 188 */             method2.invoke(paramObject2, new Object[] { object });
/*     */           } 
/*     */         } 
/*     */       } 
/* 192 */     } catch (IntrospectionException introspectionException) {
/* 193 */       throw introspectionException;
/* 194 */     } catch (Exception exception) {
/*     */ 
/*     */       
/* 197 */       if (logger.isLoggable(MLevel.FINE)) {
/* 198 */         logger.log(MLevel.FINE, "Converting exception to throwable IntrospectionException");
/*     */       }
/* 200 */       throw new IntrospectionException(exception.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean) throws IntrospectionException {
/* 206 */     overwriteAccessiblePropertiesFromMap(paramMap, paramObject, paramBoolean, Collections.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean, Collection paramCollection) throws IntrospectionException {
/* 211 */     overwriteAccessiblePropertiesFromMap(paramMap, paramObject, paramBoolean, paramCollection, false, MLevel.WARNING, MLevel.WARNING, true);
/*     */   }
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
/*     */   public static void overwriteAccessiblePropertiesFromMap(Map paramMap, Object paramObject, boolean paramBoolean1, Collection paramCollection, boolean paramBoolean2, MLevel paramMLevel1, MLevel paramMLevel2, boolean paramBoolean3) throws IntrospectionException {
/* 231 */     if (paramMLevel1 == null)
/* 232 */       paramMLevel1 = MLevel.WARNING; 
/* 233 */     if (paramMLevel2 == null) {
/* 234 */       paramMLevel2 = MLevel.WARNING;
/*     */     }
/* 236 */     Set set = paramMap.keySet();
/*     */     
/* 238 */     String str = null;
/* 239 */     BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass(), Object.class);
/* 240 */     PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b;
/*     */     int i;
/* 242 */     for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */       
/* 244 */       PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 245 */       str = propertyDescriptor.getName();
/*     */       
/* 247 */       if (!set.contains(str)) {
/*     */         continue;
/*     */       }
/* 250 */       if (paramCollection != null && paramCollection.contains(str)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 258 */       Object object = paramMap.get(str);
/* 259 */       if (object == null)
/*     */       {
/* 261 */         if (paramBoolean1) {
/*     */           continue;
/*     */         }
/*     */       }
/* 265 */       Method method = propertyDescriptor.getWriteMethod();
/* 266 */       boolean bool = false;
/*     */       
/* 268 */       Class<?> clazz = propertyDescriptor.getPropertyType();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 273 */       if (method == null) {
/*     */         
/* 275 */         if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
/*     */         {
/* 277 */           if (logger.isLoggable(MLevel.FINER)) {
/* 278 */             logger.finer("BeansUtils.overwriteAccessiblePropertiesFromMap() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 286 */         if (logger.isLoggable(paramMLevel1))
/*     */         {
/* 288 */           String str1 = "Property inaccessible for overwriting: " + str;
/* 289 */           logger.log(paramMLevel1, str1);
/* 290 */           if (paramBoolean3)
/*     */           {
/* 292 */             bool = true;
/* 293 */             throw new IntrospectionException(str1);
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 300 */       else if (paramBoolean2 && object != null && object.getClass() == String.class && (clazz = propertyDescriptor.getPropertyType()) != String.class && Coerce.canCoerce(clazz)) {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 309 */           Object object1 = Coerce.toObject((String)object, clazz);
/*     */           
/* 311 */           method.invoke(paramObject, new Object[] { object1 });
/*     */         }
/* 313 */         catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */ 
/*     */           
/* 317 */           String str1 = "Failed to coerce property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";
/*     */ 
/*     */           
/* 320 */           if (logger.isLoggable(paramMLevel2))
/* 321 */             logger.log(paramMLevel2, str1, illegalArgumentException); 
/* 322 */           if (paramBoolean3)
/*     */           {
/* 324 */             bool = true;
/* 325 */             throw new IntrospectionException(str1);
/*     */           }
/*     */         
/* 328 */         } catch (Exception exception) {
/*     */           
/* 330 */           String str1 = "Failed to set property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";
/*     */ 
/*     */           
/* 333 */           if (logger.isLoggable(paramMLevel1))
/* 334 */             logger.log(paramMLevel1, str1, exception); 
/* 335 */           if (paramBoolean3) {
/*     */             
/* 337 */             bool = true;
/* 338 */             throw new IntrospectionException(str1);
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 347 */           method.invoke(paramObject, new Object[] { object });
/*     */         }
/* 349 */         catch (Exception exception) {
/*     */           
/* 351 */           String str1 = "Failed to set property: " + str + " [propVal: " + object + "; propType: " + clazz + "]";
/*     */ 
/*     */           
/* 354 */           if (logger.isLoggable(paramMLevel1))
/* 355 */             logger.log(paramMLevel1, str1, exception); 
/* 356 */           if (paramBoolean3) {
/*     */             
/* 358 */             bool = true;
/* 359 */             throw new IntrospectionException(str1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       continue;
/*     */     } 
/*     */   }
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
/*     */   public static void appendPropNamesAndValues(StringBuffer paramStringBuffer, Object paramObject, Collection paramCollection) throws IntrospectionException {
/* 387 */     TreeMap<String, Object> treeMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
/* 388 */     extractAccessiblePropertiesToMap(treeMap, paramObject, paramCollection);
/* 389 */     boolean bool = true;
/* 390 */     for (String str : treeMap.keySet()) {
/*     */ 
/*     */       
/* 393 */       Object object = treeMap.get(str);
/* 394 */       if (bool) {
/* 395 */         bool = false;
/*     */       } else {
/* 397 */         paramStringBuffer.append(", ");
/* 398 */       }  paramStringBuffer.append(str);
/* 399 */       paramStringBuffer.append(" -> ");
/* 400 */       paramStringBuffer.append(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void extractAccessiblePropertiesToMap(Map paramMap, Object paramObject) throws IntrospectionException {
/* 406 */     extractAccessiblePropertiesToMap(paramMap, paramObject, Collections.EMPTY_SET);
/*     */   }
/*     */   
/*     */   public static void extractAccessiblePropertiesToMap(Map<String, Object> paramMap, Object paramObject, Collection paramCollection) throws IntrospectionException {
/* 410 */     String str = null;
/*     */     
/*     */     try {
/* 413 */       BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass(), Object.class);
/* 414 */       PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
/* 415 */       for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */         
/* 417 */         PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 418 */         str = propertyDescriptor.getName();
/* 419 */         if (!paramCollection.contains(str)) {
/*     */ 
/*     */           
/* 422 */           Method method = propertyDescriptor.getReadMethod();
/* 423 */           Object object = method.invoke(paramObject, EMPTY_ARGS);
/* 424 */           paramMap.put(str, object);
/*     */         } 
/*     */       } 
/* 427 */     } catch (IntrospectionException introspectionException) {
/*     */ 
/*     */ 
/*     */       
/* 431 */       if (logger.isLoggable(MLevel.WARNING))
/* 432 */         logger.warning("Problem occurred while overwriting property: " + str); 
/* 433 */       if (logger.isLoggable(MLevel.FINE)) {
/* 434 */         logger.logp(MLevel.FINE, BeansUtils.class.getName(), "extractAccessiblePropertiesToMap( Map fillMe, Object bean, Collection ignoreProps )", ((str != null) ? ("Problem occurred while overwriting property: " + str) : "") + " throwing...", introspectionException);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 439 */       throw introspectionException;
/*     */     }
/* 441 */     catch (Exception exception) {
/*     */ 
/*     */       
/* 444 */       if (logger.isLoggable(MLevel.FINE)) {
/* 445 */         logger.logp(MLevel.FINE, BeansUtils.class.getName(), "extractAccessiblePropertiesToMap( Map fillMe, Object bean, Collection ignoreProps )", "Caught unexpected Exception; Converting to IntrospectionException.", exception);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 450 */       throw new IntrospectionException(exception.toString() + ((str == null) ? "" : (" [" + str + ']')));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void overwriteProperty(String paramString, Object paramObject1, Method paramMethod, Object paramObject2) throws Exception {
/* 457 */     if (paramMethod.getDeclaringClass().isAssignableFrom(paramObject2.getClass())) {
/* 458 */       paramMethod.invoke(paramObject2, new Object[] { paramObject1 });
/*     */     } else {
/*     */       
/* 461 */       BeanInfo beanInfo = Introspector.getBeanInfo(paramObject2.getClass(), Object.class);
/* 462 */       PropertyDescriptor propertyDescriptor = null;
/*     */       
/* 464 */       PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
/* 465 */       for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/* 466 */         if (paramString.equals(arrayOfPropertyDescriptor[b].getName())) {
/*     */           
/* 468 */           propertyDescriptor = arrayOfPropertyDescriptor[b];
/*     */           break;
/*     */         } 
/*     */       } 
/* 472 */       Method method = propertyDescriptor.getWriteMethod();
/* 473 */       method.invoke(paramObject2, new Object[] { paramObject1 });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void overwriteSpecificAccessibleProperties(Object paramObject1, Object paramObject2, Collection<?> paramCollection) throws IntrospectionException {
/*     */     try {
/* 483 */       HashSet hashSet = new HashSet(paramCollection);
/*     */       
/* 485 */       BeanInfo beanInfo = Introspector.getBeanInfo(paramObject1.getClass(), Object.class);
/* 486 */       PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b; int i;
/* 487 */       for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */         
/* 489 */         PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 490 */         String str = propertyDescriptor.getName();
/* 491 */         if (hashSet.remove(str)) {
/*     */ 
/*     */           
/* 494 */           Method method1 = propertyDescriptor.getReadMethod();
/* 495 */           Method method2 = propertyDescriptor.getWriteMethod();
/*     */           
/* 497 */           if (method1 == null || method2 == null) {
/*     */             
/* 499 */             if (propertyDescriptor instanceof java.beans.IndexedPropertyDescriptor)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 507 */               if (logger.isLoggable(MLevel.WARNING)) {
/* 508 */                 logger.warning("BeansUtils.overwriteAccessibleProperties() does not support indexed properties that do not provide single-valued array getters and setters! [The indexed methods provide no means of modifying the size of the array in the destination bean if it does not match the source.]");
/*     */               }
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 515 */             if (logger.isLoggable(MLevel.INFO)) {
/* 516 */               logger.info("Property inaccessible for overwriting: " + propertyDescriptor.getName());
/*     */             }
/*     */           } else {
/*     */             
/* 520 */             Object object = method1.invoke(paramObject1, EMPTY_ARGS);
/* 521 */             overwriteProperty(str, object, method2, paramObject2);
/*     */           } 
/*     */         } 
/*     */       } 
/* 525 */       if (logger.isLoggable(MLevel.WARNING))
/*     */       {
/* 527 */         for (Iterator<String> iterator = hashSet.iterator(); iterator.hasNext();) {
/* 528 */           logger.warning("failed to find expected property: " + iterator.next());
/*     */         }
/*     */       }
/*     */     }
/* 532 */     catch (IntrospectionException introspectionException) {
/* 533 */       throw introspectionException;
/* 534 */     } catch (Exception exception) {
/*     */ 
/*     */       
/* 537 */       if (logger.isLoggable(MLevel.FINE)) {
/* 538 */         logger.logp(MLevel.FINE, BeansUtils.class.getName(), "overwriteSpecificAccessibleProperties( Object sourceBean, Object destBean, Collection props )", "Caught unexpected Exception; Converting to IntrospectionException.", exception);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 543 */       throw new IntrospectionException(exception.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void debugShowPropertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 549 */     System.err.println("PropertyChangeEvent: [ propertyName -> " + paramPropertyChangeEvent.getPropertyName() + ", oldValue -> " + paramPropertyChangeEvent.getOldValue() + ", newValue -> " + paramPropertyChangeEvent.getNewValue() + " ]");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/BeansUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */