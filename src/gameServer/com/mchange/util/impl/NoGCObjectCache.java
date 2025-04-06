/*    */ package com.mchange.util.impl;
/*    */ 
/*    */ import com.mchange.util.ObjectCache;
/*    */ import java.util.Hashtable;
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
/*    */ public abstract class NoGCObjectCache
/*    */   implements ObjectCache
/*    */ {
/* 43 */   Hashtable store = new Hashtable<Object, Object>();
/*    */ 
/*    */   
/*    */   public Object find(Object paramObject) throws Exception {
/* 47 */     Object object = this.store.get(paramObject);
/* 48 */     if (object == null || isDirty(paramObject, object)) {
/*    */       
/* 50 */       object = createFromKey(paramObject);
/* 51 */       this.store.put(paramObject, object);
/*    */     } 
/* 53 */     return object;
/*    */   }
/*    */   
/*    */   protected boolean isDirty(Object paramObject1, Object paramObject2) {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   protected abstract Object createFromKey(Object paramObject) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/NoGCObjectCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */