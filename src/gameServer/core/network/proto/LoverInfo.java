/*    */ package core.network.proto;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class LoverInfo
/*    */ {
/*    */   Player.Summary husband;
/*    */   Player.Summary wife;
/*    */   int level;
/*    */   int exp;
/*    */   int flowerTimes;
/*    */   List<Integer> alreadyPick;
/*    */   
/*    */   public List<Integer> getAlreadyPick() {
/* 16 */     return this.alreadyPick;
/*    */   }
/*    */   
/*    */   public void setAlreadyPick(List<Integer> alreadyPick) {
/* 20 */     this.alreadyPick = alreadyPick;
/*    */   }
/*    */   
/*    */   public int getFlowerTimes() {
/* 24 */     return this.flowerTimes;
/*    */   }
/*    */   
/*    */   public void setFlowerTimes(int flowerTimes) {
/* 28 */     this.flowerTimes = flowerTimes;
/*    */   }
/*    */   
/*    */   public Player.Summary getHusband() {
/* 32 */     return this.husband;
/*    */   }
/*    */   
/*    */   public void setHusband(Player.Summary husband) {
/* 36 */     this.husband = husband;
/*    */   }
/*    */   
/*    */   public Player.Summary getWife() {
/* 40 */     return this.wife;
/*    */   }
/*    */   
/*    */   public void setWife(Player.Summary wife) {
/* 44 */     this.wife = wife;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 48 */     return this.level;
/*    */   }
/*    */   
/*    */   public void setLevel(int level) {
/* 52 */     this.level = level;
/*    */   }
/*    */   
/*    */   public int getExp() {
/* 56 */     return this.exp;
/*    */   }
/*    */   
/*    */   public void setExp(int exp) {
/* 60 */     this.exp = exp;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/LoverInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */