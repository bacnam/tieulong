/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ 
/*    */ public class DailyRechargeInfo {
/*    */   private int awardId;
/*    */   private int maxTimes;
/*    */   private int receivedTimes;
/*    */   private int status;
/*    */   private int recharge;
/*    */   private Reward prize;
/*    */   private int leftTimes;
/*    */   
/*    */   public int getLeftTimes() {
/* 15 */     return this.leftTimes;
/*    */   }
/*    */   
/*    */   public void setLeftTimes(int leftTimes) {
/* 19 */     this.leftTimes = leftTimes;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getAwardId() {
/* 27 */     return this.awardId;
/*    */   }
/*    */   
/*    */   public void setAwardId(int awardId) {
/* 31 */     this.awardId = awardId;
/*    */   }
/*    */   
/*    */   public int getMaxTimes() {
/* 35 */     return this.maxTimes;
/*    */   }
/*    */   
/*    */   public void setMaxTimes(int maxTimes) {
/* 39 */     this.maxTimes = maxTimes;
/*    */   }
/*    */   
/*    */   public int getReceivedTimes() {
/* 43 */     return this.receivedTimes;
/*    */   }
/*    */   
/*    */   public void setReceivedTimes(int receivedTimes) {
/* 47 */     this.receivedTimes = receivedTimes;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 51 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(int status) {
/* 55 */     this.status = status;
/*    */   }
/*    */   
/*    */   public int getRecharge() {
/* 59 */     return this.recharge;
/*    */   }
/*    */   
/*    */   public void setRecharge(int recharge) {
/* 63 */     this.recharge = recharge;
/*    */   }
/*    */   
/*    */   public Reward getPrize() {
/* 67 */     return this.prize;
/*    */   }
/*    */   
/*    */   public void setPrize(Reward prize) {
/* 71 */     this.prize = prize;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/DailyRechargeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */