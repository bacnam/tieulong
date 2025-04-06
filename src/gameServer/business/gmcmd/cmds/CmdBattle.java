/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import business.global.battle.SimulatBattle;
/*    */ import business.global.battle.detail.WorldbossBattle;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWorldBoss;
/*    */ 
/*    */ @Commander(name = "battle", comment = "模拟战斗")
/*    */ public class CmdBattle
/*    */ {
/*    */   @Command(comment = "模拟战斗[玩家id]")
/*    */   public String fight(Player player, long t) {
/* 18 */     Player target = PlayerMgr.getInstance().getPlayer(t);
/* 19 */     if (target == null) {
/* 20 */       return String.format("玩家不存在", new Object[0]);
/*    */     }
/* 22 */     SimulatBattle battle = new SimulatBattle(player, target);
/* 23 */     battle.fight();
/*    */     
/* 25 */     return String.format("模拟结束", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "模拟战斗[怪物id]")
/*    */   public String worldboss(Player player, int bossId) {
/* 30 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 31 */     WorldbossBattle battle = new WorldbossBattle(player, ref.BossId);
/* 32 */     battle.init(bossId);
/* 33 */     battle.fight();
/* 34 */     CommLog.error((new StringBuilder(String.valueOf(battle.getDamage()))).toString());
/* 35 */     return String.format("模拟结束", new Object[0]);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdBattle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */