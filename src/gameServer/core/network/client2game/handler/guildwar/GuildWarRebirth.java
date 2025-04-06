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
/*    */ public class GuildWarRebirth
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int centerId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 21 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 22 */     request.response(GuildWarMgr.getInstance().rebirth(player, req.centerId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarRebirth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */