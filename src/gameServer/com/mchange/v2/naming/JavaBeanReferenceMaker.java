/*     */ package com.mchange.v2.naming;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.lang.Coerce;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.ser.IndirectPolicy;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.naming.BinaryRefAddr;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
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
/*     */ public class JavaBeanReferenceMaker
/*     */   implements ReferenceMaker
/*     */ {
/*  51 */   private static final MLogger logger = MLog.getLogger(JavaBeanReferenceMaker.class);
/*     */   
/*     */   static final String REF_PROPS_KEY = "com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY";
/*     */   
/*  55 */   static final Object[] EMPTY_ARGS = new Object[0];
/*     */   
/*  57 */   static final byte[] NULL_TOKEN_BYTES = new byte[0];
/*     */   
/*  59 */   String factoryClassName = "com.mchange.v2.naming.JavaBeanObjectFactory";
/*  60 */   String defaultFactoryClassLocation = null;
/*     */   
/*  62 */   Set referenceProperties = new HashSet();
/*     */   
/*  64 */   ReferenceIndirector indirector = new ReferenceIndirector();
/*     */   
/*     */   public Hashtable getEnvironmentProperties() {
/*  67 */     return this.indirector.getEnvironmentProperties();
/*     */   }
/*     */   public void setEnvironmentProperties(Hashtable paramHashtable) {
/*  70 */     this.indirector.setEnvironmentProperties(paramHashtable);
/*     */   }
/*     */   public void setFactoryClassName(String paramString) {
/*  73 */     this.factoryClassName = paramString;
/*     */   }
/*     */   public String getFactoryClassName() {
/*  76 */     return this.factoryClassName;
/*     */   }
/*     */   public String getDefaultFactoryClassLocation() {
/*  79 */     return this.defaultFactoryClassLocation;
/*     */   }
/*     */   public void setDefaultFactoryClassLocation(String paramString) {
/*  82 */     this.defaultFactoryClassLocation = paramString;
/*     */   }
/*     */   public void addReferenceProperty(String paramString) {
/*  85 */     this.referenceProperties.add(paramString);
/*     */   }
/*     */   public void removeReferenceProperty(String paramString) {
/*  88 */     this.referenceProperties.remove(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference createReference(Object paramObject) throws NamingException {
/*     */     try {
/*  95 */       BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass());
/*  96 */       PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors();
/*  97 */       ArrayList<BinaryRefAddr> arrayList = new ArrayList();
/*  98 */       String str = this.defaultFactoryClassLocation;
/*     */       
/* 100 */       boolean bool = (this.referenceProperties.size() > 0) ? true : false;
/*     */ 
/*     */       
/* 103 */       if (bool)
/* 104 */         arrayList.add(new BinaryRefAddr("com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY", SerializableUtils.toByteArray(this.referenceProperties)));  byte b;
/*     */       int i;
/* 106 */       for (b = 0, i = arrayOfPropertyDescriptor.length; b < i; b++) {
/*     */         
/* 108 */         PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/* 109 */         String str1 = propertyDescriptor.getName();
/*     */ 
/*     */         
/* 112 */         if (!bool || this.referenceProperties.contains(str1)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 118 */           Class<?> clazz = propertyDescriptor.getPropertyType();
/* 119 */           Method method1 = propertyDescriptor.getReadMethod();
/* 120 */           Method method2 = propertyDescriptor.getWriteMethod();
/* 121 */           if (method1 != null && method2 != null) {
/*     */             
/* 123 */             Object object = method1.invoke(paramObject, EMPTY_ARGS);
/*     */             
/* 125 */             if (str1.equals("factoryClassLocation")) {
/*     */               
/* 127 */               if (String.class != clazz) {
/* 128 */                 throw new NamingException(getClass().getName() + " requires a factoryClassLocation property to be a string, " + clazz.getName() + " is not valid.");
/*     */               }
/* 130 */               str = (String)object;
/*     */             } 
/*     */             
/* 133 */             if (object == null) {
/*     */               
/* 135 */               BinaryRefAddr binaryRefAddr = new BinaryRefAddr(str1, NULL_TOKEN_BYTES);
/* 136 */               arrayList.add(binaryRefAddr);
/*     */             }
/* 138 */             else if (Coerce.canCoerce(clazz)) {
/*     */               
/* 140 */               StringRefAddr stringRefAddr = new StringRefAddr(str1, String.valueOf(object));
/* 141 */               arrayList.add(stringRefAddr);
/*     */             } else {
/*     */               BinaryRefAddr binaryRefAddr;
/*     */               
/* 145 */               StringRefAddr stringRefAddr = null;
/* 146 */               PropertyEditor propertyEditor = BeansUtils.findPropertyEditor(propertyDescriptor);
/* 147 */               if (propertyEditor != null) {
/*     */                 
/* 149 */                 propertyEditor.setValue(object);
/* 150 */                 String str2 = propertyEditor.getAsText();
/* 151 */                 if (str2 != null)
/* 152 */                   stringRefAddr = new StringRefAddr(str1, str2); 
/*     */               } 
/* 154 */               if (stringRefAddr == null) {
/* 155 */                 binaryRefAddr = new BinaryRefAddr(str1, SerializableUtils.toByteArray(object, this.indirector, IndirectPolicy.INDIRECT_ON_EXCEPTION));
/*     */               }
/*     */               
/* 158 */               arrayList.add(binaryRefAddr);
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 166 */           else if (logger.isLoggable(MLevel.WARNING)) {
/* 167 */             logger.warning(getClass().getName() + ": Skipping " + str1 + " because it is " + ((method2 == null) ? "read-only." : "write-only."));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 172 */       Reference reference = new Reference(paramObject.getClass().getName(), this.factoryClassName, str);
/* 173 */       for (Iterator<BinaryRefAddr> iterator = arrayList.iterator(); iterator.hasNext();)
/* 174 */         reference.add(iterator.next()); 
/* 175 */       return reference;
/*     */     }
/* 177 */     catch (Exception exception) {
/*     */ 
/*     */       
/* 180 */       if (logger.isLoggable(MLevel.FINE)) {
/* 181 */         logger.log(MLevel.FINE, "Exception trying to create Reference.", exception);
/*     */       }
/* 183 */       throw new NamingException("Could not create reference from bean: " + exception.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/naming/JavaBeanReferenceMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */