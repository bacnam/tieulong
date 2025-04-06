/*    */ package core.network.client2game.handler.store;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.store.PlayerStore;
/*    */ import business.player.feature.store.StoreFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class BuyGoods
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     StoreType storeType;
/*    */     long sid;
/*    */     int buyTimes;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 29 */     if (req.sid <= 0L) {
/* 30 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数:sId=%s", new Object[] { Long.valueOf(req.sid) });
/*    */     }
/* 32 */     if (req.buyTimes <= 0 || req.buyTimes > RefDataMgr.getFactor("BuyItem_MaxBuyTimes", 10000)) {
/* 33 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数:buyTimes=%s", new Object[] { Integer.valueOf(req.buyTimes) });
/*    */     }
/*    */     
/* 36 */     PlayerStore store = ((StoreFeature)player.getFeature(StoreFeature.class)).getOrCreate(req.storeType);
/*    */     
/* 38 */     Reward reward = store.doBuyGoods(req.sid, req.buyTimes);
/* 39 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/store/BuyGoods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */