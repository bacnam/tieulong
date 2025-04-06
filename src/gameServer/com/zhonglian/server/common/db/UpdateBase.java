/*    */ package com.zhonglian.server.common.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.version.IUpdateDBVersion;
/*    */ 
/*    */ public class UpdateBase
/*    */   implements IUpdateDBVersion
/*    */ {
/*    */   public String getRequestVersion() {
/*  9 */     String name = getClass().getSimpleName();
/* 10 */     String version = name.split("_To_")[0].replace("Update_", "");
/* 11 */     return version.replace("_", ".");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getTargetVersion() {
/* 16 */     String name = getClass().getSimpleName();
/* 17 */     String version = name.split("_To_")[1];
/* 18 */     return version.replace("_", ".");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean run() {
/* 23 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/UpdateBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */