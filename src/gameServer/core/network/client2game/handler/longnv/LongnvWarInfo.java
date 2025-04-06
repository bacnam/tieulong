/*    */ package core.network.client2game.handler.longnv;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongnvWarInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 18 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 19 */     Guild guild = guildMember.getGuild();
/*    */     
/* 21 */     if (guild == null) {
/* 22 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 25 */     request.response(guild.getOrCreateLongnv().getWarInfo(player));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/longnv/LongnvWarInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */