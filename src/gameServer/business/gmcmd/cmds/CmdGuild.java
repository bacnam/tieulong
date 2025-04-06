/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.global.guild.GuildWarMgr;
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildLevel;
/*    */ import core.config.refdata.ref.RefGuildWarCenter;
/*    */ import core.database.game.bo.GuildwarapplyBO;
/*    */ 
/*    */ 
/*    */ @Commander(name = "guild", comment = "公会相关命令")
/*    */ public class CmdGuild
/*    */ {
/*    */   @Command(comment = "删除公会")
/*    */   public String del(Player player, String name) {
/* 24 */     Guild guild = GuildMgr.getInstance().getGuild(name);
/* 25 */     GuildMgr.getInstance().deleteGuild(guild.getGuildId());
/* 26 */     return String.format("删除公会", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "设置公会等级")
/*    */   public String level(Player player, int level) {
/* 31 */     RefGuildLevel ref = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(level));
/* 32 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 33 */     if (guild == null || ref == null) {
/* 34 */       return "failed";
/*    */     }
/* 36 */     guild.bo.saveLevel(level);
/* 37 */     return String.format("设置公会等级", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "模拟工会战")
/*    */   public String warStart(Player player) {
/* 42 */     GuildWarMgr.getInstance().Start();
/* 43 */     return String.format("模拟工会战", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "模拟工会战")
/*    */   public String warfight(Player player) {
/* 48 */     GuildWarMgr.getInstance().fight();
/* 49 */     return String.format("模拟工会战", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "模拟工会战报名")
/*    */   public String warapply(Player player, int centerId) throws WSException {
/* 54 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 55 */     Guild guild = guildMember.getGuild();
/* 56 */     if (guild == null) {
/* 57 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 60 */     RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
/* 61 */     if (ref == null) {
/* 62 */       throw new WSException(ErrorCode.GuildWar_NotFoundCenter, "据点不存在");
/*    */     }
/*    */     
/* 65 */     if (guild.guildwarCenter != null) {
/* 66 */       throw new WSException(ErrorCode.GuildWar_AlreadyOpen, "帮派已报名");
/*    */     }
/*    */     
/* 69 */     GuildWarMgr.getInstance().applyGuildWar(centerId, guild);
/* 70 */     GuildwarapplyBO bo = new GuildwarapplyBO();
/* 71 */     bo.setGuildId(guild.getGuildId());
/* 72 */     bo.setCenterId(centerId);
/* 73 */     bo.setApplyTime(CommTime.nowSecond());
/* 74 */     bo.insert();
/* 75 */     guild.guildwarCenter = bo;
/* 76 */     return String.format("模拟工会战报名", new Object[0]);
/*    */   }
/*    */   
/*    */   @Command(comment = "模拟龙女")
/*    */   public String longnvfight(Player player) throws WSException {
/* 81 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 82 */     Guild guild = guildMember.getGuild();
/* 83 */     if (guild == null) {
/* 84 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 86 */     guild.getOrCreateLongnv().Start();
/* 87 */     return String.format("模拟工会战", new Object[0]);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdGuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */