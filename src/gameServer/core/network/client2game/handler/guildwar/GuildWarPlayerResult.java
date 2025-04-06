/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildWarPlayerResult
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long sid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 21 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 22 */     request.response((GuildWarMgr.getInstance()).fightBattle.get(Long.valueOf(req.sid)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarPlayerResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */