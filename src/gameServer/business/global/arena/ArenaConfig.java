/*    */ package business.global.arena;
/*    */ 
/*    */ import core.config.refdata.RefDataMgr;
/*    */ 
/*    */ public class ArenaConfig
/*    */ {
/*    */   public static int fightTime() {
/*  8 */     return RefDataMgr.getFactor("Arena_FightTime", 90);
/*    */   }
/*    */   
/*    */   public static int opponentsSize() {
/* 12 */     return RefDataMgr.getFactor("Arena_OpponentsSize", 4);
/*    */   }
/*    */   
/*    */   public static int opponentsOffset() {
/* 16 */     return RefDataMgr.getFactor("Arena_OpponentsOffset", 5);
/*    */   }
/*    */   
/*    */   public static int showSize() {
/* 20 */     return RefDataMgr.getFactor("Arena_ShowSize", 30);
/*    */   }
/*    */   
/*    */   public static int maxChallengeTimes() {
/* 24 */     return RefDataMgr.getFactor("Arena_MaxChallengeTimes", 10);
/*    */   }
/*    */   
/*    */   public static int fightCD() {
/* 28 */     return RefDataMgr.getFactor("Arena_FightCD", 60);
/*    */   }
/*    */   
/*    */   public static int winnerToken() {
/* 32 */     return RefDataMgr.getFactor("Arena_WinnerToken", 150);
/*    */   }
/*    */   
/*    */   public static int winnerGold() {
/* 36 */     return RefDataMgr.getFactor("Arena_WinnerGold", 3000);
/*    */   }
/*    */   
/*    */   public static int loserToken() {
/* 40 */     return RefDataMgr.getFactor("Arena_LoserToken", 50);
/*    */   }
/*    */   
/*    */   public static int refreshCD() {
/* 44 */     return RefDataMgr.getFactor("Arena_RefreshCD", 30);
/*    */   }
/*    */   
/*    */   public static int resetRefreshCDCost() {
/* 48 */     return RefDataMgr.getFactor("Arena_resetRefreshCD", 10);
/*    */   }
/*    */   
/*    */   public static int resetFightCDCost() {
/* 52 */     return RefDataMgr.getFactor("Arena_resetFightCD", 5);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/arena/ArenaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */