/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefCrystalPrice;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DroiyanSearch
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 25 */     DroiyanFeature droiyan = (DroiyanFeature)player.getFeature(DroiyanFeature.class);
/* 26 */     if (droiyan.isFullDroiyan()) {
/* 27 */       throw new WSException(ErrorCode.Droiyan_FullDroiyan, "玩家[%s]的检索列表已满，无法继续检索", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/*    */     
/* 30 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 31 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/*    */     
/* 33 */     int times = recorder.getValue(ConstEnum.DailyRefresh.DroiyanSearch);
/* 34 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
/* 35 */     if (!currency.check(PrizeType.Crystal, prize.DroiyanSearch)) {
/* 36 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次检索需要钻石%s", new Object[] { Integer.valueOf(times), Integer.valueOf(prize.DroiyanSearch) });
/*    */     }
/* 38 */     currency.consume(PrizeType.Crystal, prize.DroiyanSearch, ItemFlow.DroiyanSearch);
/* 39 */     recorder.addValue(ConstEnum.DailyRefresh.DroiyanSearch);
/*    */     
/* 41 */     long pid = droiyan.search();
/*    */     
/* 43 */     Player tar = PlayerMgr.getInstance().getPlayer(pid);
/* 44 */     Player.Summary summary = ((PlayerBase)tar.getFeature(PlayerBase.class)).summary();
/* 45 */     if (((DroiyanFeature)tar.getFeature(DroiyanFeature.class)).haveTreature()) {
/* 46 */       summary.name = "神秘玩家";
/*    */     }
/* 48 */     request.response(summary);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanSearch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */