/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.GuildWarFightProtol;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildWarFightInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int centerId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     GuildWarFightProtol info = GuildWarMgr.getInstance().getFightInfo(req.centerId, player);
/* 24 */     request.response(info);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarFightInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */