/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.chat.ChatMgr;
/*    */ import business.global.guild.Guild;
/*    */ import business.global.notice.NoticeMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.ChatType;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGuildBoss;
/*    */ import core.config.refdata.ref.RefGuildJobInfo;
/*    */ import core.database.game.bo.GuildBossBO;
/*    */ import core.database.game.bo.GuildBossChallengeBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GuildBossOpen
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   private static class GuildBoss
/*    */   {
/*    */     List<GuildBossBO> bosslist;
/*    */     GuildBossChallengeBO challengBO;
/*    */     NumberRange openTime;
/*    */     int nexttime;
/*    */     
/*    */     private GuildBoss(List<GuildBossBO> bosslist, GuildBossChallengeBO challengBO, NumberRange openTime, int nexttime) {
/* 42 */       this.bosslist = bosslist;
/* 43 */       this.challengBO = challengBO;
/* 44 */       this.openTime = openTime;
/* 45 */       this.nexttime = nexttime;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 52 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 53 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 54 */     Guild guild = guildMember.getGuild();
/*    */     
/* 56 */     RefGuildJobInfo job = guildMember.getJobRef();
/* 57 */     if (!job.OpenInstance) {
/* 58 */       throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有开启帮派BOSS的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
/*    */     }
/*    */     
/* 61 */     if (guild == null) {
/* 62 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 64 */     GuildBossBO boss = guild.getboss(req.bossId);
/* 65 */     if (boss != null) {
/* 66 */       throw new WSException(ErrorCode.GuildBoss_AlreadyOpen, "帮派boss已开启");
/*    */     }
/* 68 */     if (!guild.isInOpenHour()) {
/* 69 */       throw new WSException(ErrorCode.GuildBoss_NotOpen, "不在时间段内");
/*    */     }
/* 71 */     RefGuildBoss ref = (RefGuildBoss)RefDataMgr.get(RefGuildBoss.class, Integer.valueOf(req.bossId));
/* 72 */     if (guild.getLevel() < ref.NeedGuildLevel) {
/* 73 */       throw new WSException(ErrorCode.GuildBoss_NotenoughLv, "帮派等级不够");
/*    */     }
/* 75 */     if (guild.getBo().getGuildbossLevel() < ref.id - 1) {
/* 76 */       throw new WSException(ErrorCode.GuildBoss_Lock, "帮派副本为解锁");
/*    */     }
/*    */     
/* 79 */     if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(ref.UniformId, ref.UniformCount, ItemFlow.Guild_OpenBoss)) {
/* 80 */       throw new WSException(ErrorCode.GuildBoss_NotEnoughCoin, "帮派副本资源不足");
/*    */     }
/*    */     
/* 83 */     guild.openBoss(ref);
/* 84 */     NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.OpenGuildBoss, new String[] { guild.getName(), player.getName() });
/* 85 */     ChatMgr.getInstance().addChat(player, "帮派副本已开启，请各位成员赶紧前往战斗", ChatType.CHATTYPE_GUILD, 0L);
/* 86 */     request.response(guild.getboss(req.bossId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossOpen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */