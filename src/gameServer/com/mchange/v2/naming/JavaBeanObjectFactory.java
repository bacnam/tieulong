/*     */ package com.mchange.v2.naming;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.lang.Coerce;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.naming.BinaryRefAddr;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.ObjectFactory;
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
/*     */ public class JavaBeanObjectFactory
/*     */   implements ObjectFactory
/*     */ {
/*  50 */   private static final MLogger logger = MLog.getLogger(JavaBeanObjectFactory.class);
/*     */   
/*  52 */   static final Object NULL_TOKEN = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable) throws Exception {
/*  57 */     if (paramObject instanceof Reference) {
/*     */       
/*  59 */       Reference reference = (Reference)paramObject;
/*  60 */       HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/*  61 */       for (Enumeration<RefAddr> enumeration = reference.getAll(); enumeration.hasMoreElements(); ) {
/*     */         
/*  63 */         RefAddr refAddr = enumeration.nextElement();
/*  64 */         hashMap.put(refAddr.getType(), refAddr);
/*     */       } 
/*  66 */       Class<?> clazz = Class.forName(reference.getClassName());
/*  67 */       Set set = null;
/*  68 */       BinaryRefAddr binaryRefAddr = (BinaryRefAddr)hashMap.remove("com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY");
/*  69 */       if (binaryRefAddr != null)
/*  70 */         set = (Set)SerializableUtils.fromByteArray((byte[])binaryRefAddr.getContent()); 
/*  71 */       Map map = createPropertyMap(clazz, hashMap);
/*  72 */       return findBean(clazz, map, set);
/*     */     } 
/*     */     
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map createPropertyMap(Class<?> paramClass, Map paramMap) throws Exception {
/*  80 */     BeanInfo beanInfo = Introspector.getBeanInfo(paramClass);
/*  81 */     PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors();
/*     */     
/*  83 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); byte b; int i;
/*  84 */     for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */       
/*  86 */       PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/*  87 */       String str = propertyDescriptor.getName();
/*  88 */       Class<?> clazz = propertyDescriptor.getPropertyType();
/*  89 */       Object object = paramMap.remove(str);
/*  90 */       if (object != null)
/*     */       {
/*  92 */         if (object instanceof StringRefAddr) {
/*     */           
/*  94 */           String str1 = (String)((StringRefAddr)object).getContent();
/*  95 */           if (Coerce.canCoerce(clazz)) {
/*  96 */             hashMap.put(str, Coerce.toObject(str1, clazz));
/*     */           } else {
/*     */             
/*  99 */             PropertyEditor propertyEditor = BeansUtils.findPropertyEditor(propertyDescriptor);
/* 100 */             propertyEditor.setAsText(str1);
/* 101 */             hashMap.put(str, propertyEditor.getValue());
/*     */           }
/*     */         
/* 104 */         } else if (object instanceof BinaryRefAddr) {
/*     */           
/* 106 */           byte[] arrayOfByte = (byte[])((BinaryRefAddr)object).getContent();
/* 107 */           if (arrayOfByte.length == 0) {
/* 108 */             hashMap.put(str, NULL_TOKEN);
/*     */           } else {
/* 110 */             hashMap.put(str, SerializableUtils.fromByteArray(arrayOfByte));
/*     */           }
/*     */         
/*     */         }
/* 114 */         else if (logger.isLoggable(MLevel.WARNING)) {
/* 115 */           logger.warning(getClass().getName() + " -- unknown RefAddr subclass: " + object.getClass().getName());
/*     */         } 
/*     */       }
/*     */     } 
/* 119 */     for (String str : paramMap.keySet()) {
/*     */ 
/*     */       
/* 122 */       if (logger.isLoggable(MLevel.WARNING))
/* 123 */         logger.warning(getClass().getName() + " -- RefAddr for unknown property: " + str); 
/*     */     } 
/* 125 */     return hashMap;
/*     */   }
/*     */   
/*     */   protected Object createBlankInstance(Class paramClass) throws Exception {
/* 129 */     return paramClass.newInstance();
/*     */   }
/*     */   
/*     */   protected Object findBean(Class paramClass, Map paramMap, Set paramSet) throws Exception {
/* 133 */     Object object = createBlankInstance(paramClass);
/* 134 */     BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
/* 135 */     PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); byte b;
/*     */     int i;
/* 137 */     for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */       
/* 139 */       PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 140 */       String str = propertyDescriptor.getName();
/* 141 */       Object object1 = paramMap.get(str);
/* 142 */       Method method = propertyDescriptor.getWriteMethod();
/* 143 */       if (object1 != null) {
/*     */         
/* 145 */         if (method != null) {
/* 146 */           method.invoke(object, new Object[] { (object1 == NULL_TOKEN) ? null : object1 });
/*     */ 
/*     */         
/*     */         }
/* 150 */         else if (logger.isLoggable(MLevel.WARNING)) {
/* 151 */           logger.warning(getClass().getName() + ": Could not restore read-only property '" + str + "'.");
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 156 */       else if (method != null) {
/*     */         
/* 158 */         if (paramSet == null || paramSet.contains(str))
/*     */         {
/*     */ 
/*     */           
/* 162 */           if (logger.isLoggable(MLevel.WARNING)) {
/* 163 */             logger.warning(getClass().getName() + " -- Expected writable property ''" + str + "'' left at default value");
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     return object;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/naming/JavaBeanObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */