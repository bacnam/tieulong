/*    */ package core.network.proto;
/*    */ 
/*    */ 
/*    */ public class MarryApplyInfo
/*    */ {
/*    */   Player.Summary summary;
/*    */   int leftTime;
/*    */   
/*    */   public Player.Summary getSummary() {
/* 10 */     return this.summary;
/*    */   }
/*    */   
/*    */   public void setSummary(Player.Summary summary) {
/* 14 */     this.summary = summary;
/*    */   }
/*    */   
/*    */   public int getLeftTime() {
/* 18 */     return this.leftTime;
/*    */   }
/*    */   
/*    */   public void setLeftTime(int leftTime) {
/* 22 */     this.leftTime = leftTime;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/MarryApplyInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */