/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.GuildWarCenterInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class loadGuildWarCenterInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Respones
/*    */   {
/*    */     int applyCenter;
/*    */     List<GuildWarCenterInfo> centerInfo;
/*    */     
/*    */     private Respones(int applyCenter, List<GuildWarCenterInfo> centerInfo) {
/* 24 */       this.applyCenter = applyCenter;
/* 25 */       this.centerInfo = centerInfo;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 32 */     Guild guild = guildMember.getGuild();
/* 33 */     int centerId = 0;
/* 34 */     if (guild != null && guild.guildwarCenter != null) {
/* 35 */       centerId = guild.guildwarCenter.getCenterId();
/*    */     }
/*    */     
/* 38 */     request.response(new Respones(centerId, GuildWarMgr.getInstance().getCenterInfo(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/loadGuildWarCenterInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */