/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.player.Player;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.GuildBossBO;
/*    */ import core.database.game.bo.GuildBossChallengeBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class GuildBossCheck
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class GuildBoss
/*    */   {
/*    */     List<GuildBossBO> bosslist;
/*    */     GuildBossChallengeBO challengBO;
/*    */     NumberRange openTime;
/*    */     int nextTime;
/*    */     int maxLevel;
/*    */     int openTimes;
/*    */     
/*    */     private GuildBoss(List<GuildBossBO> bosslist, GuildBossChallengeBO challengBO, NumberRange openTime, int nextTime, int maxLevel, int openTimes) {
/* 32 */       this.bosslist = bosslist;
/* 33 */       this.challengBO = challengBO;
/* 34 */       this.openTime = openTime;
/* 35 */       this.nextTime = nextTime;
/* 36 */       this.maxLevel = maxLevel;
/* 37 */       this.openTimes = openTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 44 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 45 */     Guild guild = guildMember.getGuild();
/* 46 */     if (guild == null) {
/* 47 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 49 */     List<GuildBossBO> bosslist = guild.getbosslist();
/* 50 */     GuildBossChallengeBO challengBO = guildMember.getOrCreateChalleng();
/* 51 */     NumberRange openTime = GuildConfig.getGuildBossOpenTime();
/* 52 */     int nexttime = Math.max(guildMember.nextRefreshTime() - CommTime.nowSecond(), 0);
/* 53 */     int maxLevel = guild.getBo().getGuildbossLevel();
/*    */     
/* 55 */     request.response(new GuildBoss(bosslist, challengBO, openTime, nexttime, maxLevel, guild.getBo().getGuildbossOpenNum(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */