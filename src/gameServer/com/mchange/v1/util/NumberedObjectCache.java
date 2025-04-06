/*    */ package com.mchange.v1.util;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ public abstract class NumberedObjectCache
/*    */ {
/* 42 */   ArrayList al = new ArrayList();
/*    */ 
/*    */   
/*    */   public Object getObject(int paramInt) throws Exception {
/* 46 */     Object object = null;
/* 47 */     int i = paramInt + 1;
/* 48 */     if (i > this.al.size()) {
/*    */       
/* 50 */       this.al.ensureCapacity(i * 2);
/* 51 */       for (int j = this.al.size(), k = i * 2; j < k; j++)
/* 52 */         this.al.add(null); 
/* 53 */       object = addToCache(paramInt);
/*    */     }
/*    */     else {
/*    */       
/* 57 */       object = this.al.get(paramInt);
/* 58 */       if (object == null)
/* 59 */         object = addToCache(paramInt); 
/*    */     } 
/* 61 */     return object;
/*    */   }
/*    */ 
/*    */   
/*    */   private Object addToCache(int paramInt) throws Exception {
/* 66 */     Object object = findObject(paramInt);
/* 67 */     this.al.set(paramInt, object);
/* 68 */     return object;
/*    */   }
/*    */   
/*    */   protected abstract Object findObject(int paramInt) throws Exception;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/NumberedObjectCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */