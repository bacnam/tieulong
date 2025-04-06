/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.pve.InstanceFeature;
/*    */ 
/*    */ @Commander(name = "instance", comment = "副本相关命令")
/*    */ public class CmdInstance
/*    */ {
/*    */   @Command(comment = "刷新所有副本挑战信息")
/*    */   public String refresh(Player player) {
/* 13 */     ((InstanceFeature)player.getFeature(InstanceFeature.class)).dailyRefresh();
/* 14 */     return String.format("刷新玩家副本挑战信息成功", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "设置副本挑战次数")
/*    */   public String set(Player player, int times) {
/* 19 */     ((InstanceFeature)player.getFeature(InstanceFeature.class)).getOrCreate().saveChallengTimesAll(times);
/* 20 */     return String.format("ok", new Object[0]);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */