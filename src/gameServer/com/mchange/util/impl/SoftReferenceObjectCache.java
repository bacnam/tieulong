/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.ObjectCache;
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public abstract class SoftReferenceObjectCache
/*    */   implements ObjectCache
/*    */ {
/* 46 */   Map store = new HashMap<Object, Object>();
/*    */ 
/*    */   
/*    */   public synchronized Object find(Object paramObject) throws Exception {
/* 50 */     Reference<Object> reference = (Reference)this.store.get(paramObject);
/*    */     Object object;
/* 52 */     if (reference == null || (object = reference.get()) == null || isDirty(paramObject, object)) {
/*    */       
/* 54 */       object = createFromKey(paramObject);
/* 55 */       this.store.put(paramObject, new SoftReference(object));
/*    */     } 
/* 57 */     return object;
/*    */   }
/*    */   
/*    */   protected boolean isDirty(Object paramObject1, Object paramObject2) {
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract Object createFromKey(Object paramObject) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/SoftReferenceObjectCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */