/*    */ package com.zhonglian.server.common.data.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ public class RefGeneral
/*    */   extends RefBase {
/*    */   @RefField(iskey = true)
/*    */   public String id;
/*    */   public String Value;
/*    */   public String Desction;
/*    */   
/*    */   public boolean Assert() {
/* 14 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 19 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/ref/RefGeneral.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */