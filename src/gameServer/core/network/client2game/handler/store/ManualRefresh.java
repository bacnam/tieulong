/*    */ package core.network.client2game.handler.store;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.store.PlayerStore;
/*    */ import business.player.feature.store.StoreFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ManualRefresh
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     StoreType storeType;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     PlayerStore store = ((StoreFeature)player.getFeature(StoreFeature.class)).getOrCreate(req.storeType);
/* 24 */     store.manualRefresh();
/* 25 */     player.pushProto("store.RefreshInfo", store.refreshInfo());
/* 26 */     player.pushProto("store.GoodsList", store.getGoodsList());
/* 27 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/store/ManualRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */