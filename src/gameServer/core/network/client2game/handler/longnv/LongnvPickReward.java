/*    */ package core.network.client2game.handler.longnv;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class LongnvPickReward
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     Reward reward;
/*    */     int pickTimes;
/*    */     
/*    */     private Response(Reward reward, int pickTimes) {
/* 23 */       this.reward = reward;
/* 24 */       this.pickTimes = pickTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 32 */     Guild guild = guildMember.getGuild();
/*    */     
/* 34 */     if (guild == null) {
/* 35 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 37 */     Reward reward = guild.getOrCreateLongnv().pickReward(player);
/*    */     
/* 39 */     request.response(new Response(reward, guildMember.bo.getLongnvPickReward(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/longnv/LongnvPickReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */