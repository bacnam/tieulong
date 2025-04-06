/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.GuildwarResultBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.GuildWarFightProtol;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class GuildWarGuildResult
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long sid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 25 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 26 */     List<GuildWarFightProtol.ResultRecord> result = new ArrayList<>();
/* 27 */     if ((GuildWarMgr.getInstance()).guildAllResulet.get(Long.valueOf(req.sid)) != null) {
/* 28 */       for (GuildwarResultBO bo : (GuildWarMgr.getInstance()).guildAllResulet.get(Long.valueOf(req.sid))) {
/* 29 */         result.add(new GuildWarFightProtol.ResultRecord(bo));
/*    */       }
/*    */     }
/* 32 */     request.response(result);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarGuildResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */