/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccumConsumeInfo
/*    */ {
/*    */   private int awardId;
/*    */   private int status;
/*    */   private int consume;
/*    */   private Reward prize;
/*    */   
/*    */   public int getAwardId() {
/* 16 */     return this.awardId;
/*    */   }
/*    */   
/*    */   public void setAwardId(int awardId) {
/* 20 */     this.awardId = awardId;
/*    */   }
/*    */   
/*    */   public int getStatus() {
/* 24 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setStatus(int status) {
/* 28 */     this.status = status;
/*    */   }
/*    */   
/*    */   public int getConsume() {
/* 32 */     return this.consume;
/*    */   }
/*    */   
/*    */   public void setConsume(int consume) {
/* 36 */     this.consume = consume;
/*    */   }
/*    */   
/*    */   public Reward getPrize() {
/* 40 */     return this.prize;
/*    */   }
/*    */   
/*    */   public void setPrize(Reward prize) {
/* 44 */     this.prize = prize;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/AccumConsumeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */