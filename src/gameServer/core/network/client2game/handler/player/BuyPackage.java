/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class BuyPackage
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int buyTimes;
/*    */     int extpackage;
/*    */     
/*    */     public Response(int buyTimes, int extpackage) {
/* 27 */       this.buyTimes = buyTimes;
/* 28 */       this.extpackage = extpackage;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 36 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 37 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 39 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.PackageBuyTimes);
/* 40 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
/*    */     
/* 42 */     if (prize == null) {
/* 43 */       throw new WSException(ErrorCode.NotEnough_Crystal, "背包已购买最大");
/*    */     }
/* 45 */     if (!currency.check(PrizeType.Crystal, prize.PackageBuyTimes)) {
/* 46 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次购买背包需要钻石%s", new Object[] { Integer.valueOf(curTimes), Integer.valueOf(prize.PackageBuyTimes) });
/*    */     }
/* 48 */     currency.consume(PrizeType.Crystal, prize.PackageBuyTimes, ItemFlow.BuyPackage);
/* 49 */     player.getPlayerBO().saveExtPackage(player.getPlayerBO().getExtPackage() + RefDataMgr.getFactor("BuyPackageSize", 50));
/* 50 */     recorder.addValue(ConstEnum.DailyRefresh.PackageBuyTimes);
/* 51 */     request.response(new Response(recorder.getValue(ConstEnum.DailyRefresh.PackageBuyTimes), player.getPlayerBO().getExtPackage()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/BuyPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */