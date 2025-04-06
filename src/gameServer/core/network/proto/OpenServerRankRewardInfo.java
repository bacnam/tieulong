/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ 
/*    */ public class OpenServerRankRewardInfo {
/*    */   int count;
/*    */   boolean isPicked;
/*    */   Reward reward;
/*    */   
/*    */   public OpenServerRankRewardInfo(int count, boolean isPicked) {
/* 11 */     this.count = count;
/* 12 */     this.isPicked = isPicked;
/*    */   }
/*    */   
/*    */   public OpenServerRankRewardInfo(int count, boolean isPicked, Reward reward) {
/* 16 */     this.count = count;
/* 17 */     this.isPicked = isPicked;
/* 18 */     this.reward = reward;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/OpenServerRankRewardInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */