/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildChangeNotice
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     String notice;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 25 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 26 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 27 */     Guild guild = guildMember.getGuild();
/* 28 */     if (guild == null) {
/* 29 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 32 */     RefGuildJobInfo job = guildMember.getJobRef();
/* 33 */     if (!job.ChangeNotice) {
/* 34 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有改名[ChangeNotice]的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
/*    */     }
/* 36 */     GuildMgr.CheckNoteValid(req.notice, player.getPid());
/*    */     
/* 38 */     guild.getBo().saveNotice(req.notice);
/*    */     
/* 40 */     request.response(guild.guildInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildChangeNotice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */