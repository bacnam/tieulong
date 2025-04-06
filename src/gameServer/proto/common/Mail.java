/*    */ package proto.common;
/*    */ 
/*    */ public class Mail {
/*    */   public static class SendMail {
/*    */     public long scid;
/*    */     public int mailID;
/*    */     public String uniformIDList;
/*    */     public String uniformCountList;
/*    */     public String params;
/*    */     
/*    */     public SendMail(long scid, int mailID, String uniformIDList, String uniformCountList, String params) {
/* 12 */       this.scid = scid;
/* 13 */       this.mailID = mailID;
/* 14 */       this.uniformIDList = uniformIDList;
/* 15 */       this.uniformCountList = uniformCountList;
/* 16 */       this.params = params;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/proto/common/Mail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */