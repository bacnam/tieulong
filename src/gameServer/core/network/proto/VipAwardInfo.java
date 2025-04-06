/*    */ package core.network.proto;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ 
/*    */ public class VipAwardInfo {
/*    */   int awardId;
/*    */   int MaxTimes;
/*    */   int buyTimes;
/*    */   String icon;
/*    */   int vip;
/*    */   int price;
/*    */   int discount;
/*    */   Reward reward;
/*    */   String timeslist;
/*    */   
/*    */   public String getTimeslist() {
/* 17 */     return this.timeslist;
/*    */   }
/*    */   
/*    */   public void setTimeslist(String timeslist) {
/* 21 */     this.timeslist = timeslist;
/*    */   }
/*    */   
/*    */   public Reward getReward() {
/* 25 */     return this.reward;
/*    */   }
/*    */   
/*    */   public void setReward(Reward reward) {
/* 29 */     this.reward = reward;
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 33 */     return this.icon;
/*    */   }
/*    */   
/*    */   public void setIcon(String icon) {
/* 37 */     this.icon = icon;
/*    */   }
/*    */   
/*    */   public int getVip() {
/* 41 */     return this.vip;
/*    */   }
/*    */   
/*    */   public void setVip(int vip) {
/* 45 */     this.vip = vip;
/*    */   }
/*    */   
/*    */   public int getPrice() {
/* 49 */     return this.price;
/*    */   }
/*    */   
/*    */   public void setPrice(int price) {
/* 53 */     this.price = price;
/*    */   }
/*    */   
/*    */   public int getDiscount() {
/* 57 */     return this.discount;
/*    */   }
/*    */   
/*    */   public void setDiscount(int discount) {
/* 61 */     this.discount = discount;
/*    */   }
/*    */   
/*    */   public int getAwardId() {
/* 65 */     return this.awardId;
/*    */   }
/*    */   
/*    */   public void setAwardId(int awardId) {
/* 69 */     this.awardId = awardId;
/*    */   }
/*    */   
/*    */   public int getMaxTimes() {
/* 73 */     return this.MaxTimes;
/*    */   }
/*    */   
/*    */   public void setMaxTimes(int maxTimes) {
/* 77 */     this.MaxTimes = maxTimes;
/*    */   }
/*    */   
/*    */   public int getBuyTimes() {
/* 81 */     return this.buyTimes;
/*    */   }
/*    */   
/*    */   public void setBuyTimes(int buyTimes) {
/* 85 */     this.buyTimes = buyTimes;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/VipAwardInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */