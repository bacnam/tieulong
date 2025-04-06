/*     */ package com.mchange.v2.beans.swing;
/*     */ 
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.EventSetDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.beans.PropertyEditor;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.SwingUtilities;
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
/*     */ class PropertyComponentBindingUtility
/*     */ {
/*  52 */   static final Object[] EMPTY_ARGS = new Object[0];
/*     */   
/*     */   HostBindingInterface hbi;
/*     */   
/*     */   Object bean;
/*  57 */   PropertyDescriptor pd = null;
/*  58 */   EventSetDescriptor propChangeEsd = null;
/*  59 */   Method addMethod = null;
/*  60 */   Method removeMethod = null;
/*  61 */   Method propGetter = null;
/*  62 */   Method propSetter = null;
/*  63 */   PropertyEditor propEditor = null;
/*     */   
/*  65 */   Object nullReplacement = null;
/*     */ 
/*     */ 
/*     */   
/*     */   PropertyComponentBindingUtility(final HostBindingInterface hbi, Object paramObject, final String propName, boolean paramBoolean) throws IntrospectionException {
/*  70 */     this.hbi = hbi;
/*  71 */     this.bean = paramObject;
/*     */     
/*  73 */     BeanInfo beanInfo = Introspector.getBeanInfo(paramObject.getClass());
/*     */     
/*  75 */     PropertyDescriptor[] arrayOfPropertyDescriptor = beanInfo.getPropertyDescriptors(); int i;
/*  76 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  78 */       PropertyDescriptor propertyDescriptor = arrayOfPropertyDescriptor[b];
/*  79 */       if (propName.equals(propertyDescriptor.getName())) {
/*     */         
/*  81 */         this.pd = propertyDescriptor;
/*     */         break;
/*     */       } 
/*     */     } 
/*  85 */     if (this.pd == null) {
/*  86 */       throw new IntrospectionException("Cannot find property on bean Object with name '" + propName + "'.");
/*     */     }
/*  88 */     EventSetDescriptor[] arrayOfEventSetDescriptor = beanInfo.getEventSetDescriptors(); int j;
/*  89 */     for (i = 0, j = arrayOfEventSetDescriptor.length; i < j; i++) {
/*     */       
/*  91 */       EventSetDescriptor eventSetDescriptor = arrayOfEventSetDescriptor[i];
/*  92 */       if ("propertyChange".equals(eventSetDescriptor.getName())) {
/*     */         
/*  94 */         this.propChangeEsd = eventSetDescriptor;
/*     */         break;
/*     */       } 
/*     */     } 
/*  98 */     if (this.propChangeEsd == null) {
/*  99 */       throw new IntrospectionException("Cannot find PropertyChangeEvent on bean Object with name '" + propName + "'.");
/*     */     }
/* 101 */     this.propEditor = BeansUtils.findPropertyEditor(this.pd);
/* 102 */     if (paramBoolean && this.propEditor == null) {
/* 103 */       throw new IntrospectionException("Could not find an appropriate PropertyEditor for property: " + propName);
/*     */     }
/*     */ 
/*     */     
/* 107 */     this.propGetter = this.pd.getReadMethod();
/* 108 */     this.propSetter = this.pd.getWriteMethod();
/*     */     
/* 110 */     if (this.propGetter == null || this.propSetter == null) {
/* 111 */       throw new IntrospectionException("The specified property '" + propName + "' must be both readdable and writable, but it is not!");
/*     */     }
/* 113 */     Class<?> clazz = this.pd.getPropertyType();
/* 114 */     if (clazz.isPrimitive()) {
/*     */       
/* 116 */       if (clazz == boolean.class)
/* 117 */         this.nullReplacement = Boolean.FALSE; 
/* 118 */       if (clazz == byte.class) {
/* 119 */         this.nullReplacement = new Byte((byte)0);
/* 120 */       } else if (clazz == char.class) {
/* 121 */         this.nullReplacement = new Character(false);
/* 122 */       } else if (clazz == short.class) {
/* 123 */         this.nullReplacement = new Short((short)0);
/* 124 */       } else if (clazz == int.class) {
/* 125 */         this.nullReplacement = new Integer(0);
/* 126 */       } else if (clazz == long.class) {
/* 127 */         this.nullReplacement = new Long(0L);
/* 128 */       } else if (clazz == float.class) {
/* 129 */         this.nullReplacement = new Float(0.0F);
/* 130 */       } else if (clazz == double.class) {
/* 131 */         this.nullReplacement = new Double(0.0D);
/*     */       } else {
/* 133 */         throw new InternalError("What kind of primitive is " + clazz.getName() + "???");
/*     */       } 
/*     */     } 
/* 136 */     this.addMethod = this.propChangeEsd.getAddListenerMethod();
/* 137 */     this.removeMethod = this.propChangeEsd.getAddListenerMethod();
/*     */     
/* 139 */     PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent param1PropertyChangeEvent)
/*     */         {
/* 143 */           String str = param1PropertyChangeEvent.getPropertyName();
/* 144 */           if (str.equals(propName)) {
/* 145 */             hbi.syncToValue(PropertyComponentBindingUtility.this.propEditor, param1PropertyChangeEvent.getNewValue());
/*     */           }
/*     */         }
/*     */       };
/*     */     try {
/* 150 */       this.addMethod.invoke(paramObject, new Object[] { propertyChangeListener });
/* 151 */     } catch (Exception exception) {
/*     */       
/* 153 */       exception.printStackTrace();
/* 154 */       throw new IntrospectionException("The introspected PropertyChangeEvent adding method failed with an Exception.");
/*     */     } 
/*     */     
/* 157 */     hbi.addUserModificationListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   public void userModification() {
/* 162 */     Object object = null;
/*     */     try {
/* 164 */       object = this.propGetter.invoke(this.bean, EMPTY_ARGS);
/* 165 */     } catch (Exception exception) {
/* 166 */       exception.printStackTrace();
/*     */     } 
/*     */     
/*     */     try {
/* 170 */       Object object1 = this.hbi.fetchUserModification(this.propEditor, object);
/* 171 */       if (object1 == null)
/* 172 */         object1 = this.nullReplacement; 
/* 173 */       this.propSetter.invoke(this.bean, new Object[] { object1 });
/*     */     }
/* 175 */     catch (Exception exception) {
/*     */       
/* 177 */       if (!(exception instanceof java.beans.PropertyVetoException))
/* 178 */         exception.printStackTrace(); 
/* 179 */       syncComponentToValue(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resync() {
/* 184 */     syncComponentToValue(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void syncComponentToValue(final boolean alert_error) {
/*     */     try {
/* 190 */       final Object reversionValue = this.propGetter.invoke(this.bean, EMPTY_ARGS);
/* 191 */       Runnable runnable = new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 195 */             if (alert_error)
/* 196 */               PropertyComponentBindingUtility.this.hbi.alertErroneousInput(); 
/* 197 */             PropertyComponentBindingUtility.this.hbi.syncToValue(PropertyComponentBindingUtility.this.propEditor, reversionValue);
/*     */           }
/*     */         };
/* 200 */       SwingUtilities.invokeLater(runnable);
/*     */     }
/* 202 */     catch (Exception exception) {
/*     */       
/* 204 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/beans/swing/PropertyComponentBindingUtility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */