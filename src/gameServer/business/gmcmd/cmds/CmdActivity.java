/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.AccumRechargeDay;
/*    */ import business.global.activity.detail.RankWing;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ 
/*    */ @Commander(name = "activity", comment = "商城相关命令")
/*    */ public class CmdActivity
/*    */ {
/*    */   @Command(comment = "羽翼榜活动结束")
/*    */   public String closewingrank(Player player) {
/* 15 */     RankWing wingRank = (RankWing)ActivityMgr.getActivity(RankWing.class);
/* 16 */     wingRank.onClosed();
/* 17 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "连续充值")
/*    */   public String accumday(Player player) {
/* 22 */     ((AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class)).dailyRefresh(player);
/* 23 */     return "ok";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */