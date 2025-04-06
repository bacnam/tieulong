/*    */ package com.mchange.v2.ser;
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
/*    */ public final class IndirectPolicy
/*    */ {
/* 40 */   public static final IndirectPolicy DEFINITELY_INDIRECT = new IndirectPolicy("DEFINITELY_INDIRECT");
/* 41 */   public static final IndirectPolicy INDIRECT_ON_EXCEPTION = new IndirectPolicy("INDIRECT_ON_EXCEPTION");
/* 42 */   public static final IndirectPolicy DEFINITELY_DIRECT = new IndirectPolicy("DEFINITELY_DIRECT");
/*    */   
/*    */   String name;
/*    */   
/*    */   private IndirectPolicy(String paramString) {
/* 47 */     this.name = paramString;
/*    */   }
/*    */   public String toString() {
/* 50 */     return "[IndirectPolicy: " + this.name + ']';
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/ser/IndirectPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */