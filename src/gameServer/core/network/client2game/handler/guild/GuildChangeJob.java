/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.chat.ChatMgr;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ChatType;
/*    */ import com.zhonglian.server.common.enums.GuildJob;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildChangeJob
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */     GuildJob job;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 32 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 33 */     Guild guild = guildMember.getGuild();
/* 34 */     RefGuildJobInfo job = guildMember.getJobRef();
/* 35 */     if (guild == null) {
/* 36 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 39 */     if (!job.ManageGuild) {
/* 40 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有任命职位的权限", new Object[] { Long.valueOf(player.getPid()), guildMember.getJob() });
/*    */     }
/* 42 */     if (req.pid == player.getPid()) {
/* 43 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]无法调整自己在公会[%s]中的职务", new Object[] { Long.valueOf(player.getPid()), guild.getName() });
/*    */     }
/*    */     
/* 46 */     GuildMemberFeature target = guild.getMember(req.pid);
/*    */     
/* 48 */     if (target == null) {
/* 49 */       throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]并不是公会[%s]的成员，无法任职", new Object[] { Long.valueOf(req.pid), guild.getName() });
/*    */     }
/* 51 */     String oldjob = target.getJobName();
/*    */     
/* 53 */     RefGuildJobInfo jobref = (RefGuildJobInfo)RefDataMgr.get(RefGuildJobInfo.class, req.job);
/* 54 */     if (guild.getLevel() < jobref.UnlockLevel) {
/* 55 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]公会[%s]未解锁[%s]职位", new Object[] { Long.valueOf(player.getPid()), guild.getName(), req.job });
/*    */     }
/*    */     
/* 58 */     int jobcount = guild.getMemberCount(req.job);
/* 59 */     if (jobcount >= jobref.JobAmount) {
/* 60 */       throw new WSException(ErrorCode.Guild_FullMember, "公会[%s]的职务[%s]已经[%s]人满员", new Object[] { guild.getName(), req.job, Integer.valueOf(jobcount) });
/*    */     }
/* 62 */     guild.getMember(target.getJob()).remove(Long.valueOf(target.getPid()));
/* 63 */     target.setJob(req.job);
/* 64 */     guild.broadcastMember(new long[] { req.pid });
/* 65 */     if (req.job == GuildJob.VicePresident) {
/* 66 */       String notice = GuildConfig.VicePresidentNotice();
/* 67 */       notice = notice.replace("{0}", target.getPlayer().getName());
/* 68 */       ChatMgr.getInstance().addChat(player, notice, ChatType.CHATTYPE_GUILD, 0L);
/*    */     } 
/* 70 */     MailCenter.getInstance().sendMail(req.pid, GuildConfig.ChangeJobGuildMailID(), new String[] { oldjob, target.getJobName() });
/* 71 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildChangeJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */