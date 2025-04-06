/*    */ package com.zhonglian.server.common.data.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
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
/*    */ public abstract class RefBase
/*    */ {
/*    */   public abstract boolean Assert();
/*    */   
/*    */   public abstract boolean AssertAll(RefContainer<?> paramRefContainer);
/*    */   
/*    */   public String toString() {
/* 37 */     return super.toString();
/*    */   }
/*    */   
/*    */   public String[] getOptionFields() {
/* 41 */     return new String[0]; } public boolean isFieldRequired(String filed) {
/*    */     byte b;
/*    */     int i;
/*    */     String[] arrayOfString;
/* 45 */     for (i = (arrayOfString = getOptionFields()).length, b = 0; b < i; ) { String f = arrayOfString[b];
/* 46 */       if (f.equals(filed))
/* 47 */         return false; 
/*    */       b++; }
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/ref/RefBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */