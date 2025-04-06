/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ 
/*    */ 
/*    */ @Commander(name = "arena", comment = "竞技场")
/*    */ public class CmdArena
/*    */ {
/*    */   @Command(comment = "设置挑战次数")
/*    */   public String times(Player player, int times) {
/* 18 */     ((PlayerRecord)player.getFeature(PlayerRecord.class)).setValue(ConstEnum.DailyRefresh.ArenaChallenge, ArenaConfig.maxChallengeTimes() - times);
/* 19 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "设置挑战CD")
/*    */   public String fightCD(Player player, int cd) {
/* 24 */     ArenaManager.getInstance().getOrCreate(player.getPid()).setFightCD(cd);
/* 25 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "设置刷新CD")
/*    */   public String refreshCD(Player player, int cd) {
/* 30 */     ArenaManager.getInstance().getOrCreate(player.getPid()).setRefreshCD(cd);
/* 31 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "设置排名")
/*    */   public String rank(Player player, int rank) {
/* 36 */     Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
/* 37 */     ArenaManager.getInstance().swithRank(competitor.getRank(), rank);
/* 38 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "设置排名")
/*    */   public String settle(Player player) {
/* 43 */     ArenaManager.getInstance().settle();
/* 44 */     return "ok";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdArena.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */