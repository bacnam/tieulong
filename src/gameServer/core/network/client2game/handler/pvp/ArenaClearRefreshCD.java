/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
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
/*    */ public class ArenaClearRefreshCD
/*    */   extends PlayerHandler
/*    */ {
/* 21 */   static ArenaManager manager = null;
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 25 */     if (manager == null) {
/* 26 */       manager = ArenaManager.getInstance();
/*    */     }
/*    */     
/* 29 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 30 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 32 */     int times = recorder.getValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD);
/* 33 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
/* 34 */     if (!currency.check(PrizeType.Crystal, prize.ArenaResetRefreshCD)) {
/* 35 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次清除需要钻石%s", new Object[] { Integer.valueOf(times), Integer.valueOf(prize.ArenaResetRefreshCD) });
/*    */     }
/* 37 */     currency.consume(PrizeType.Crystal, prize.ArenaResetFightCD, ItemFlow.ArenaClearFightCD);
/* 38 */     recorder.addValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD);
/*    */     
/* 40 */     Competitor competitor = manager.getOrCreate(player.getPid());
/* 41 */     competitor.setRefreshCD(0);
/* 42 */     request.response(Integer.valueOf(recorder.getValue(ConstEnum.DailyRefresh.ArenaResetRefreshCD)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaClearRefreshCD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */