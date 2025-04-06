/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import business.player.feature.pvp.WorshipFeature;
/*    */ import com.zhonglian.server.common.enums.GuildJob;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GuildRank
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int worshipTimes;
/*    */     int rank;
/*    */     String name;
/*    */     List<GuildRank.GuildRankInfo> rankList;
/*    */     
/*    */     public Response(int worshipTimes, int rank, String name, List<Record> records) {
/* 31 */       this.worshipTimes = worshipTimes;
/* 32 */       this.rank = rank;
/* 33 */       this.name = name;
/* 34 */       this.rankList = new ArrayList<>();
/* 35 */       for (int i = 0; i < records.size(); i++) {
/* 36 */         Record r = records.get(i);
/* 37 */         if (r != null)
/*    */         {
/*    */           
/* 40 */           this.rankList.add(new GuildRank.GuildRankInfo(r)); } 
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public static class GuildRankInfo {
/*    */     int rank;
/*    */     long value;
/*    */     String guildName;
/*    */     long presidentId;
/*    */     String presidentName;
/*    */     int presidentLv;
/*    */     int presidentVip;
/*    */     
/*    */     public GuildRankInfo(Record record) {
/* 55 */       Guild guild = GuildMgr.getInstance().getGuild(record.getPid());
/* 56 */       this.rank = record.getRank();
/* 57 */       this.value = record.getValue();
/* 58 */       this.guildName = guild.getName();
/* 59 */       Player player = PlayerMgr.getInstance().getPlayer(((Long)guild.getMember(GuildJob.President).get(0)).longValue());
/* 60 */       this.presidentId = player.getPid();
/* 61 */       this.presidentName = player.getName();
/* 62 */       this.presidentLv = player.getLv();
/* 63 */       this.presidentVip = player.getVipLevel();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 69 */     List<Record> records = RankManager.getInstance().getRankList(RankType.Guild, 10);
/* 70 */     int rank = 0;
/* 71 */     Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
/* 72 */     String name = "";
/* 73 */     if (guild != null) {
/* 74 */       name = guild.getName();
/* 75 */       rank = RankManager.getInstance().getRank(RankType.Guild, guild.getGuildId());
/*    */     } 
/* 77 */     int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(RankType.Guild.ordinal());
/* 78 */     request.response(new Response(times, rank, name, records));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */