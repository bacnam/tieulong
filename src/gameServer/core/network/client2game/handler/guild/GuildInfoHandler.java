/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Guild;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildInfoHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class GuildBaseInfo
/*    */   {
/*    */     Guild.guildInfo guildInfo;
/*    */     Guild.member myInfo;
/*    */     int joinCD;
/*    */     
/*    */     public GuildBaseInfo(Guild.guildInfo guildInfo1, Guild.member myInfo, int joinCD) {
/* 23 */       this.guildInfo = guildInfo1;
/* 24 */       this.myInfo = myInfo;
/* 25 */       this.joinCD = joinCD;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 32 */     Guild guildL = guildMember.getGuild();
/* 33 */     Guild.guildInfo guildInfo = null;
/* 34 */     Guild.member member = guildMember.memberInfo();
/* 35 */     if (guildL != null) {
/* 36 */       guildInfo = guildL.guildInfo();
/*    */     }
/* 38 */     request.response(new GuildBaseInfo(guildInfo, member, guildMember.getJoinCD()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildInfoHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */