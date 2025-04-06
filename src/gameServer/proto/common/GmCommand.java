/*    */ package proto.common;
/*    */ 
/*    */ public class GmCommand {
/*    */   public static class G_GmCommand {
/*    */     public long cid;
/*    */     public String cmd;
/*    */     
/*    */     public G_GmCommand(long cid, String cmd) {
/*  9 */       this.cid = cid;
/* 10 */       this.cmd = cmd;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Z_GmCommand {
/*    */     public String rslt;
/*    */     
/*    */     public Z_GmCommand(String rslt) {
/* 18 */       this.rslt = rslt;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/common/GmCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */