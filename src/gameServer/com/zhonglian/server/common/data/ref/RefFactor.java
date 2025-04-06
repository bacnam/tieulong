/*    */ package com.zhonglian.server.common.data.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefFactor
/*    */   extends RefBase
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public String id;
/*    */   public double Value;
/*    */   public String Remarks;
/*    */   
/*    */   public boolean Assert() {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 23 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/ref/RefFactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */