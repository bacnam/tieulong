/*    */ package core.server;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerIDUtils
/*    */ {
/*    */   public static String getSidsBySid(int sid) {
/* 16 */     return String.valueOf(sid);
/*    */   }
/*    */   
/*    */   public static int getSidBySids(String sids) {
/*    */     try {
/* 21 */       return Integer.valueOf(sids).intValue();
/* 22 */     } catch (Exception e) {
/* 23 */       CommLog.error("无法将[{}]转化为相关的数字型id", sids);
/*    */       
/* 25 */       return 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/ServerIDUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */