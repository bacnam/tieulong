/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildBossLeave
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     long damage;
/*    */     Reward reward;
/*    */     
/*    */     private Response(long damage, Reward reward) {
/* 28 */       this.damage = damage;
/* 29 */       this.reward = reward;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 36 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 37 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 38 */     Guild guild = guildMember.getGuild();
/* 39 */     if (guild == null) {
/* 40 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 42 */     Reward reward = guildMember.LeaveGuildBoss(req.bossId);
/* 43 */     request.response(new Response(guildMember.getOnceDamage(), reward, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossLeave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */