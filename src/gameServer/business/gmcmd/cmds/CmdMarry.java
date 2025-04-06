/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ 
/*    */ @Commander(name = "marry", comment = "婚恋相关命令")
/*    */ public class CmdMarry
/*    */ {
/*    */   @Command(comment = "刷新婚恋")
/*    */   public String signrefresh(Player player) {
/* 13 */     ((MarryFeature)player.getFeature(MarryFeature.class)).dailyRefresh();
/* 14 */     return "ok";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdMarry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */