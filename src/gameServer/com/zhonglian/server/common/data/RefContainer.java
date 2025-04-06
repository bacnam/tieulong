/*    */ package com.zhonglian.server.common.data;
/*    */ 
/*    */ import com.zhonglian.server.common.data.ref.RefBase;
/*    */ import java.util.Random;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefContainer<T extends RefBase>
/*    */   extends TreeMap<Object, T>
/*    */ {
/*    */   private static final long serialVersionUID = 5170618524149079288L;
/*    */   
/*    */   public T random() {
/* 21 */     if (size() <= 0) {
/* 22 */       return null;
/*    */     }
/* 24 */     int rand = 0;
/* 25 */     if (size() > 1) {
/* 26 */       rand = (new Random()).nextInt(size());
/*    */     }
/* 28 */     return (T)values().toArray()[rand];
/*    */   }
/*    */   
/*    */   public T last() {
/* 32 */     if (size() <= 0) {
/* 33 */       return null;
/*    */     }
/* 35 */     Object k = keySet().toArray()[size() - 1];
/* 36 */     return get(k);
/*    */   }
/*    */   
/*    */   public T first() {
/* 40 */     if (size() <= 0) {
/* 41 */       return null;
/*    */     }
/* 43 */     Object k = keySet().toArray()[0];
/* 44 */     return get(k);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/RefContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */