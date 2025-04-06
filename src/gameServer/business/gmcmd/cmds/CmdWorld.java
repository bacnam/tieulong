/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.global.chat.ChatMgr;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import core.network.game2world.WorldConnector;
/*    */ 
/*    */ @Commander(name = "world", comment = "跨服相关命令")
/*    */ public class CmdWorld
/*    */ {
/*    */   @Command(comment = "测试跨服请求")
/*    */   public String test(Player player) {
/* 14 */     WorldConnector.request("wforward.client.Test", "", null);
/*    */     
/* 16 */     return "ok";
/*    */   }
/*    */   
/*    */   @Command(comment = "测试聊天")
/*    */   public String chat(Player player) {
/* 21 */     ChatMgr.getInstance().init();
/* 22 */     return "ok";
/*    */   }
/*    */ 
/*    */   
/*    */   @Command(comment = "测试聊天2")
/*    */   public String chat2(Player player) {
/* 28 */     return "ok";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdWorld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */