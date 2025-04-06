/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.worldboss.WorldBossFeature;
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import core.database.game.bo.WorldBossBO;
/*    */ 
/*    */ 
/*    */ @Commander(name = "worldboss", comment = "世界BOSS相关命令")
/*    */ public class CmdWorldBoss
/*    */ {
/*    */   @Command(comment = "刷新玩家挑战信息")
/*    */   public String refreshplayer(Player player) {
/* 19 */     ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).dailyRefresh();
/* 20 */     return String.format("刷新玩家参战信息成功", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "刷新世界BOSS")
/*    */   public String refresh(Player player, int bossId) {
/* 25 */     WorldBossMgr.getInstance().dailyRefreshWorldBoss(bossId);
/* 26 */     return String.format("刷新世界BOSS" + bossId + "成功", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "清空世界BOSS排行榜")
/*    */   public String clearrank(Player player, int bossId) {
/* 31 */     RankType type = null;
/* 32 */     switch (bossId) {
/*    */       case 1:
/* 34 */         type = RankType.WorldBoss1;
/*    */         break;
/*    */       case 2:
/* 37 */         type = RankType.WorldBoss2;
/*    */         break;
/*    */       case 3:
/* 40 */         type = RankType.WorldBoss3;
/*    */         break;
/*    */       case 4:
/* 43 */         type = RankType.WorldBoss4;
/*    */         break;
/*    */     } 
/*    */ 
/*    */     
/* 48 */     RankManager.getInstance().clear(type);
/* 49 */     return String.format("清空世界BOSS" + bossId + "排行榜成功", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "设置BOSS最大血量并刷新")
/*    */   public String sethp(Player player, int bossId, int bossMaxHp) {
/* 54 */     WorldBossBO boss = WorldBossMgr.getInstance().getBO(bossId);
/* 55 */     boss.saveBossMaxHp(bossMaxHp);
/* 56 */     WorldBossMgr.getInstance().dailyRefreshWorldBoss(bossId);
/* 57 */     return String.format("设置BOSS" + bossId + "最大血量并刷新", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "删除所有世界BOSS重新读表")
/*    */   public String delall(Player player) {
/* 62 */     BM.getBM(WorldBossBO.class).delAll();
/* 63 */     WorldBossMgr.getInstance().init();
/* 64 */     return String.format("删除所有世界BOSS", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "设置BOSS当前血量")
/*    */   public String nowhp(Player player, int bossId, int Hp) {
/* 69 */     WorldBossBO boss = WorldBossMgr.getInstance().getBO(bossId);
/* 70 */     boss.saveBossHp(Hp);
/* 71 */     return String.format("设置BOSS" + bossId + "当前血量", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "发送世界BOSS奖励")
/*    */   public String mail(Player player, int bossId) {
/* 76 */     WorldBossMgr.getInstance().sendRankReward(bossId, true);
/* 77 */     return String.format("删除所有世界BOSS", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "自动挑战世界BOSS")
/*    */   public String auto(Player player, int bossId) {
/* 82 */     WorldBossMgr.getInstance().autoFight(bossId);
/* 83 */     return String.format("自动挑战世界BOSS", new Object[0]);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdWorldBoss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */