/*    */ package ch.qos.logback.core.status;
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
/*    */ public class InfoStatus
/*    */   extends StatusBase
/*    */ {
/*    */   public InfoStatus(String msg, Object origin) {
/* 20 */     super(0, msg, origin);
/*    */   }
/*    */   
/*    */   public InfoStatus(String msg, Object origin, Throwable t) {
/* 24 */     super(0, msg, origin, t);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/status/InfoStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */