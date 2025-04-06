/*    */ package core.network.client2game.handler.guild;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.common.enums.SacrificeType;
/*    */ import com.zhonglian.server.common.utils.CommMath;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.config.refdata.ref.RefGuildSacrifice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GuildSacrificeCrystal
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Sacrifice
/*    */   {
/*    */     boolean iscritical;
/*    */     int donate;
/*    */     int exp;
/*    */     int totalexp;
/*    */     int sacrificeTimes;
/*    */     
/*    */     private Sacrifice(boolean iscritical, int donate, int exp, int totalexp, int sacrificeTimes) {
/* 35 */       this.iscritical = iscritical;
/* 36 */       this.donate = donate;
/* 37 */       this.exp = exp;
/* 38 */       this.totalexp = totalexp;
/* 39 */       this.sacrificeTimes = sacrificeTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 46 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 47 */     Guild guild = guildMember.getGuild();
/*    */     
/* 49 */     if (guild == null) {
/* 50 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 53 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 54 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 56 */     int times = recorder.getValue(ConstEnum.DailyRefresh.GuildSacrifice);
/* 57 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
/* 58 */     if (!currency.check(PrizeType.Crystal, prize.SacrificeCost)) {
/* 59 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次祭天需要钻石%s", new Object[] { Integer.valueOf(times), Integer.valueOf(prize.ArenaResetRefreshCD) });
/*    */     }
/* 61 */     currency.consume(PrizeType.Crystal, prize.SacrificeCost, ItemFlow.Guild_GuildSacrifice);
/*    */ 
/*    */     
/* 64 */     RefGuildSacrifice ref = (RefGuildSacrifice)RefDataMgr.get(RefGuildSacrifice.class, SacrificeType.Crystal);
/*    */     
/* 66 */     boolean critical = (CommMath.randomInt(10000) < ref.Critical);
/* 67 */     int crivalue = critical ? ref.CriticalValue : 10000;
/*    */     
/* 69 */     int exp = prize.SacrificeExp * crivalue / 10000;
/* 70 */     int donate = prize.SacrificeDonate * crivalue / 10000;
/*    */     
/* 72 */     int gain = guild.gainExp(exp);
/* 73 */     guild.incSacrificeCount(player.getPid());
/* 74 */     recorder.addValue(ConstEnum.DailyRefresh.GuildSacrifice);
/* 75 */     guildMember.gainDonate(donate, ItemFlow.Guild_GuildSacrifice);
/* 76 */     guildMember.gainSacriDonate(donate);
/*    */     
/* 78 */     Sacrifice sacrifice = new Sacrifice(critical, donate, gain, guild.bo.getExp(), recorder.getValue(ConstEnum.DailyRefresh.GuildSacrifice), null);
/* 79 */     guild.broadcast("sacrifice", sacrifice, player.getPid());
/* 80 */     request.response(sacrifice);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guild/GuildSacrificeCrystal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */