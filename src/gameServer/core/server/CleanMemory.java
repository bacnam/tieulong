/*    */ package core.server;
/*    */ 
/*    */ import BaseServer._ACleanMemory;
/*    */ import business.player.PlayerMgr;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ 
/*    */ public class CleanMemory
/*    */   extends _ACleanMemory
/*    */ {
/* 10 */   private static CleanMemory instance = new CleanMemory();
/*    */   
/*    */   public static CleanMemory GetInstance() {
/* 13 */     return instance;
/*    */   }
/*    */   
/* 16 */   private int featureGCTime = 86400;
/*    */   
/*    */   public int getFeatureGCTime() {
/* 19 */     return this.featureGCTime;
/*    */   }
/*    */   
/*    */   public void setFeatureGCTime(int featureGCTime) {
/* 23 */     this.featureGCTime = featureGCTime;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/* 29 */     CommTime.RecentSec = CommTime.nowSecond();
/*    */ 
/*    */     
/* 32 */     PlayerMgr.getInstance().releasPlayer(this.featureGCTime);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/CleanMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */