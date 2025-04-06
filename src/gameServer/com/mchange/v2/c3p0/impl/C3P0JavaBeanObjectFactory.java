/*    */ package com.mchange.v2.c3p0.impl;
/*    */ 
/*    */ import com.mchange.v2.c3p0.C3P0Registry;
/*    */ import com.mchange.v2.naming.JavaBeanObjectFactory;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C3P0JavaBeanObjectFactory
/*    */   extends JavaBeanObjectFactory
/*    */ {
/* 46 */   private static final Class[] CTOR_ARG_TYPES = new Class[] { boolean.class };
/* 47 */   private static final Object[] CTOR_ARGS = new Object[] { Boolean.FALSE };
/*    */ 
/*    */   
/*    */   protected Object createBlankInstance(Class<?> beanClass) throws Exception {
/* 51 */     if (IdentityTokenized.class.isAssignableFrom(beanClass)) {
/*    */       
/* 53 */       Constructor<?> ctor = beanClass.getConstructor(CTOR_ARG_TYPES);
/* 54 */       return ctor.newInstance(CTOR_ARGS);
/*    */     } 
/*    */     
/* 57 */     return super.createBlankInstance(beanClass);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object findBean(Class beanClass, Map propertyMap, Set refProps) throws Exception {
/* 62 */     Object out = super.findBean(beanClass, propertyMap, refProps);
/* 63 */     if (out instanceof IdentityTokenized) {
/* 64 */       out = C3P0Registry.reregister((IdentityTokenized)out);
/*    */     }
/*    */     
/* 67 */     return out;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/C3P0JavaBeanObjectFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */