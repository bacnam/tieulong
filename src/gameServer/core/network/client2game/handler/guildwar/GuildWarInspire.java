/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildWarInspire
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 25 */     Guild guild = guildMember.getGuild();
/* 26 */     if (guild == null) {
/* 27 */       throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 30 */     PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 31 */     int times = record.getValue(ConstEnum.DailyRefresh.GuildwarInspire);
/* 32 */     RefCrystalPrice.getPrize(times);
/*    */     
/* 34 */     int crystalCost = (RefCrystalPrice.getPrize(times)).GuildwarInspire;
/*    */ 
/*    */     
/* 37 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 38 */     if (!playerCurrency.check(PrizeType.Crystal, crystalCost)) {
/* 39 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝:%s<鼓励需要元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(crystalCost) });
/*    */     }
/*    */ 
/*    */     
/* 43 */     playerCurrency.consume(PrizeType.Crystal, crystalCost, ItemFlow.WorldBoss_Inspiring);
/* 44 */     record.addValue(ConstEnum.DailyRefresh.GuildwarInspire);
/* 45 */     guildMember.bo.saveGuildwarInspire(guildMember.bo.getGuildwarInspire() + 1);
/* 46 */     request.response(Integer.valueOf(guildMember.bo.getGuildwarInspire()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarInspire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */