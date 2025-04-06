/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.config.refdata.ref.RefGuildLevel;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildDealApply
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */     boolean isAccept;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 30 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 31 */     Guild guild = guildMember.getGuild();
/* 32 */     if (guild == null) {
/* 33 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 35 */     RefGuildJobInfo job = guildMember.getJobRef();
/* 36 */     if (!job.DealRequest) {
/* 37 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有处理申请[DealRequest]的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
/*    */     }
/* 39 */     if (guild.getApply(req.pid) == null) {
/* 40 */       throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]没有申请加入[%s]公会，或者已经加入其他公会", new Object[] { Long.valueOf(req.pid), guild.getName() });
/*    */     }
/* 42 */     if (req.isAccept) {
/* 43 */       RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
/* 44 */       if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
/* 45 */         throw new WSException(ErrorCode.Guild_FullMember, "[%s]帮会成员已满", new Object[] { guild.getName() });
/*    */       }
/* 47 */       Player newMember = PlayerMgr.getInstance().getPlayer(req.pid);
/* 48 */       GuildMemberFeature newFeature = guild.getMember(req.pid);
/* 49 */       if (newFeature != null) {
/* 50 */         throw new WSException(ErrorCode.Guild_Already, "玩家已在公会");
/*    */       }
/*    */       
/* 53 */       guild.takeinMember(newMember);
/*    */     } else {
/* 55 */       GuildMgr.getInstance().removeApply(req.pid, guild.getGuildId());
/*    */     } 
/* 57 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildDealApply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */