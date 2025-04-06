/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildRecord;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.features.RechargeFeature;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.GuildRankType;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.PlayerBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class GuildBossRank
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int rank;
/*    */     long damage;
/*    */     int attacktimes;
/*    */     List<GuildBossRank.RankInfo> rankList;
/*    */     
/*    */     public Response(int rank, long damage, int attacktimes, List<GuildRecord> records) {
/* 34 */       this.rank = rank;
/* 35 */       this.damage = damage;
/* 36 */       this.attacktimes = attacktimes;
/* 37 */       this.rankList = new ArrayList<>();
/* 38 */       for (int i = 0; i < records.size(); i++) {
/* 39 */         GuildRecord r = records.get(i);
/* 40 */         if (r != null)
/*    */         {
/*    */           
/* 43 */           this.rankList.add(new GuildBossRank.RankInfo(r)); } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static class RankInfo extends Player.Summary {
/*    */     static PlayerMgr playerMgr;
/*    */     int rank;
/*    */     long value;
/*    */     int attacktimes;
/*    */     
/*    */     public RankInfo(GuildRecord record) {
/* 55 */       if (playerMgr == null) {
/* 56 */         playerMgr = PlayerMgr.getInstance();
/*    */       }
/*    */       
/* 59 */       Player player = playerMgr.getPlayer(record.getPid());
/* 60 */       PlayerBO bo = player.getPlayerBO();
/* 61 */       this.pid = bo.getId();
/* 62 */       this.name = bo.getName();
/* 63 */       this.lv = bo.getLv();
/* 64 */       this.icon = bo.getIcon();
/* 65 */       this.vipLv = bo.getVipLevel();
/* 66 */       this.power = ((CharFeature)player.getFeature(CharFeature.class)).getPower();
/* 67 */       this.rank = record.getRank();
/* 68 */       this.value = record.getValue();
/* 69 */       this.attacktimes = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getOrCreateChalleng().getAttackTimes();
/*    */       
/* 71 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/* 72 */       int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
/* 73 */       this.MonthCard = (monthNum > 0);
/* 74 */       int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
/* 75 */       this.YearCard = (yearNum == -1);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 81 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 82 */     Guild guild = guildMember.getGuild();
/* 83 */     if (guild == null) {
/* 84 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 86 */     List<GuildRecord> records = guild.getRankList(GuildRankType.GuildBoss, 30);
/* 87 */     int rank = guild.getRank(GuildRankType.GuildBoss, player.getPid());
/* 88 */     long damage = guild.getValue(GuildRankType.GuildBoss, player.getPid());
/* 89 */     int times = guildMember.getOrCreateChalleng().getAttackTimes();
/* 90 */     request.response(new Response(rank, damage, times, records));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */