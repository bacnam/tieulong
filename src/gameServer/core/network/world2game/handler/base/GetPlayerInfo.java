/*    */ package core.network.world2game.handler.base;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.Config;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.world2game.handler.WBaseHandler;
/*    */ import proto.gamezone.Player;
/*    */ 
/*    */ public class GetPlayerInfo
/*    */   extends WBaseHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(WebSocketRequest request, String message) throws WSException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 26 */     Player.PlayerInfo info = new Player.PlayerInfo();
/* 27 */     Player player = PlayerMgr.getInstance().getPlayer(req.pid);
/* 28 */     if (player != null) {
/* 29 */       info.pid = player.getPid();
/* 30 */       info.icon = player.getPlayerBO().getIcon();
/* 31 */       info.name = player.getName();
/* 32 */       info.lv = player.getLv();
/* 33 */       info.vipLv = player.getVipLevel();
/* 34 */       Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 35 */       if (guild != null) {
/* 36 */         info.guildID = guild.getGuildId();
/* 37 */         info.guildName = guild.getName();
/*    */       } 
/* 39 */       info.serverId = Config.ServerID();
/* 40 */       info.maxPower = ((CharFeature)player.getFeature(CharFeature.class)).getPower();
/*    */     } 
/* 42 */     request.response(info);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/world2game/handler/base/GetPlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */