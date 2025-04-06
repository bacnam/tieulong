/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.enums.GuildJob;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildLeave
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 20 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 21 */     Guild guild = guildMember.getGuild();
/* 22 */     if (guild == null) {
/* 23 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 25 */     if (guildMember.getJob() == GuildJob.President && guild.getMemberCnt() >= 2) {
/* 26 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "帮会[%s]还有2个人以上，玩家[%s]作为帮主无法退出", new Object[] { guild.getName(), Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 29 */     guildMember.leave();
/* 30 */     if (guild.getMemberCnt() == 0) {
/* 31 */       GuildMgr.getInstance().deleteGuild(guild.getGuildId());
/*    */     } else {
/* 33 */       guild.broadcast("memberleave", Long.valueOf(player.getPid()));
/*    */     } 
/*    */     
/* 36 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildLeave.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */