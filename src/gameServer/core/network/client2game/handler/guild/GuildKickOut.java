/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
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
/*    */ public class GuildKickOut
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 27 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 28 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 29 */     Guild guild = guildMember.getGuild();
/* 30 */     if (guild == null) {
/* 31 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 34 */     RefGuildJobInfo job = guildMember.getJobRef();
/*    */     
/* 36 */     if (!job.Kickout) {
/* 37 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有踢人的权限", new Object[] { Long.valueOf(player.getPid()), job });
/*    */     }
/*    */     
/* 40 */     GuildMemberFeature target = guild.getMember(req.pid);
/* 41 */     if (target == null) {
/* 42 */       throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]并不是公会[%s]的成员，无法踢人", new Object[] { Long.valueOf(req.pid), guild.getName() });
/*    */     }
/* 44 */     target.leave();
/* 45 */     target.getBo().saveLastLeaveTime(guildMember.getBo().getLastLeaveTime() - GuildConfig.JoinCD());
/* 46 */     guild.broadcast("guildkickout", Long.valueOf(req.pid));
/* 47 */     Player targetplayer = PlayerMgr.getInstance().getPlayer(req.pid);
/* 48 */     targetplayer.pushProto("bekickout", "");
/* 49 */     MailCenter.getInstance().sendMail(targetplayer.getPid(), GuildConfig.KickoutGuildMailID(), new String[] { guild.getName() });
/* 50 */     request.response(Long.valueOf(req.pid));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildKickOut.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */