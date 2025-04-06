/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildLevel;
/*    */ import core.database.game.bo.GuildApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Guild;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class GuildJoin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long sid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 32 */     int openlevel = GuildConfig.UnlockLevel();
/* 33 */     if (player.getLv() < openlevel) {
/* 34 */       throw new WSException(ErrorCode.NotEnough_TeamLevel, "[%s]玩家等级不足[%s]，无法加入公会", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(openlevel) });
/*    */     }
/*    */     
/* 37 */     GuildMgr guildMgrL = GuildMgr.getInstance();
/* 38 */     Guild guild = guildMgrL.getGuild(req.sid);
/* 39 */     if (guild == null) {
/* 40 */       throw new WSException(ErrorCode.NotFound_Guild, "未查找到[%s]帮会的信息", new Object[] { Long.valueOf(req.sid) });
/*    */     }
/*    */     
/* 43 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/*    */     
/* 45 */     if (guildMember.getJoinCD() > 0) {
/* 46 */       throw new WSException(ErrorCode.Guild_InJoinCD, "[%s]玩家已经在帮会在加入CD中，不能加入帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 49 */     synchronized (this) {
/* 50 */       RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
/* 51 */       if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
/* 52 */         throw new WSException(ErrorCode.Guild_FullMember, "[%s]帮会成员已满", new Object[] { Long.valueOf(req.sid) });
/*    */       }
/* 54 */       List<GuildApplyBO> applyBOs = guildMgrL.getApplyByCid(player.getPid());
/* 55 */       for (GuildApplyBO applyBO : applyBOs) {
/* 56 */         if (applyBO.getGuildId() == req.sid) {
/* 57 */           throw new WSException(ErrorCode.InvalidParam, "[%s]玩家已经申请过帮会[%s]，不能重复申请", new Object[] { Long.valueOf(player.getPid()), guild.getName() });
/*    */         }
/*    */       } 
/* 60 */       Guild oldGuild = guildMember.getGuild();
/* 61 */       if (oldGuild != null) {
/* 62 */         throw new WSException(ErrorCode.Guild_AlreadyInGuild, "[%s]玩家已经在帮会[%s]，不能再加其他帮会", new Object[] { Long.valueOf(player.getPid()), oldGuild.getName() });
/*    */       }
/*    */       
/* 65 */       if (applyBOs.size() >= RefDataMgr.getFactor("Guild_MaxApplyCount", 3)) {
/* 66 */         throw new WSException(ErrorCode.Guild_InJoinCD, "[%s]玩家申请[%s条]已经到达上限，不能再申请帮会", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(RefDataMgr.getFactor("Guild_MaxApplyCount", 3)) });
/*    */       }
/* 68 */       GuildMgr.getInstance().apply(player.getPid(), guild.getGuildId());
/*    */       
/* 70 */       Guild.GuildApply proto = new Guild.GuildApply();
/* 71 */       proto.applyer = ((PlayerBase)player.getFeature(PlayerBase.class)).summary();
/*    */       
/* 73 */       guild.broadcast("newApply", proto, player.getPid());
/*    */     } 
/*    */ 
/*    */     
/* 77 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildJoin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */