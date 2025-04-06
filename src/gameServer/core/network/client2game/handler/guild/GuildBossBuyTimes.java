/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildBossBuyTimes
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int times;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     int leftTimes;
/*    */     int leftBuyTimes;
/*    */     
/*    */     private Response(int leftTimes, int leftBuyTimes) {
/* 32 */       this.leftTimes = leftTimes;
/* 33 */       this.leftBuyTimes = leftBuyTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 40 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 41 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 42 */     Guild guild = guildMember.getGuild();
/* 43 */     if (guild == null) {
/* 44 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 46 */     if (guildMember.getOrCreateChalleng().getChallengeBuyTimes() + req.times > GuildConfig.getGuildBossMaxBuyTime(player)) {
/* 47 */       throw new WSException(ErrorCode.GuildBoss_NotChallengeBuyTimes, "玩家购买次数不足");
/*    */     }
/*    */     
/* 50 */     int finalcount = 0;
/* 51 */     int times = guildMember.getOrCreateChalleng().getChallengeBuyTimes();
/* 52 */     for (int i = 0; i < req.times; i++) {
/* 53 */       RefCrystalPrice prize = RefCrystalPrice.getPrize(times + i);
/* 54 */       finalcount += prize.GuildBossBuyChallenge;
/*    */     } 
/*    */     
/* 57 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 58 */     if (!playerCurrency.checkAndConsume(PrizeType.Crystal, finalcount, ItemFlow.Guild_BuyTimes)) {
/* 59 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝不足");
/*    */     }
/* 61 */     guildMember.getOrCreateChalleng().saveChallengeMaxTimes(guildMember.getOrCreateChalleng().getChallengeMaxTimes() + req.times);
/* 62 */     guildMember.getOrCreateChalleng().saveChallengeBuyTimes(guildMember.getOrCreateChalleng().getChallengeBuyTimes() + req.times);
/* 63 */     request.response(new Response(guildMember.getOrCreateChalleng().getChallengeMaxTimes(), guildMember.getOrCreateChalleng().getChallengeBuyTimes(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildBossBuyTimes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */