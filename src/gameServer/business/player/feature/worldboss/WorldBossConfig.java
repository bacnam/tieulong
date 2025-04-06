/*    */ package business.player.feature.worldboss;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.BattleStatus;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldBossConfig
/*    */ {
/*    */   public static int getFightCD() {
/* 17 */     int fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30);
/* 18 */     return (fightCD < 0) ? 0 : fightCD;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getReliveTaskWaitCD() {
/* 38 */     return RefDataMgr.getFactor("WorldBossReviveCD", 30) * 1000;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static BattleStatus getTodayFightStatus() {
/* 56 */     int todaySecond = CommTime.getTodaySecond();
/* 57 */     int beginTime = RefDataMgr.getFactor("WorldBossBeginFightTime");
/* 58 */     int endTime = RefDataMgr.getFactor("WorldBossEndFightTime");
/* 59 */     if (todaySecond < beginTime)
/* 60 */       return BattleStatus.NotBeginning; 
/* 61 */     if (todaySecond >= endTime) {
/* 62 */       return BattleStatus.BattleEnded;
/*    */     }
/* 64 */     return BattleStatus.Battling;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/worldboss/WorldBossConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */