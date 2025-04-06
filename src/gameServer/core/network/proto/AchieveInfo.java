/*    */ package core.network.proto;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AchieveInfo
/*    */ {
/*    */   int achieveId;
/*    */   int completeCount;
/*    */   int achieveCount;
/*    */   long argument1;
/*    */   long argument2;
/*    */   long argument3;
/*    */   List<Integer> gainPrizeList;
/*    */   
/*    */   public int getAchieveId() {
/* 18 */     return this.achieveId;
/*    */   }
/*    */   
/*    */   public void setAchieveId(int achieveId) {
/* 22 */     this.achieveId = achieveId;
/*    */   }
/*    */   
/*    */   public int getCompleteCount() {
/* 26 */     return this.completeCount;
/*    */   }
/*    */   
/*    */   public void setCompleteCount(int completeCount) {
/* 30 */     this.completeCount = completeCount;
/*    */   }
/*    */   
/*    */   public int getAchieveCount() {
/* 34 */     return this.achieveCount;
/*    */   }
/*    */   
/*    */   public void setAchieveCount(int achieveCount) {
/* 38 */     this.achieveCount = achieveCount;
/*    */   }
/*    */   
/*    */   public List<Integer> getGainPrizeList() {
/* 42 */     return this.gainPrizeList;
/*    */   }
/*    */   
/*    */   public void setGainPrizeList(List<Integer> gainPrizeList) {
/* 46 */     this.gainPrizeList = gainPrizeList;
/*    */   }
/*    */   
/*    */   public long getArgument1() {
/* 50 */     return this.argument1;
/*    */   }
/*    */   
/*    */   public void setArgument1(long argument1) {
/* 54 */     this.argument1 = argument1;
/*    */   }
/*    */   
/*    */   public long getArgument2() {
/* 58 */     return this.argument2;
/*    */   }
/*    */   
/*    */   public void setArgument2(long argument2) {
/* 62 */     this.argument2 = argument2;
/*    */   }
/*    */   
/*    */   public long getArgument3() {
/* 66 */     return this.argument3;
/*    */   }
/*    */   
/*    */   public void setArgument3(long argument3) {
/* 70 */     this.argument3 = argument3;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/AchieveInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */