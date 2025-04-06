/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.DrawPrize;
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerCurrency;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DrawPrizeHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int times;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 30 */     DrawPrize accumRecharge = (DrawPrize)ActivityMgr.getActivity(DrawPrize.class);
/* 31 */     if (accumRecharge.getStatus() == ActivityStatus.Close) {
/* 32 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { accumRecharge.getType() });
/*    */     }
/*    */     
/* 35 */     int cost = 0;
/* 36 */     if (req.times == 1) {
/* 37 */       cost = accumRecharge.price;
/* 38 */     } else if (req.times == 10) {
/* 39 */       cost = accumRecharge.tenPrice;
/*    */     } 
/* 41 */     PlayerCurrency playerCurrency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 42 */     if (!playerCurrency.check(PrizeType.Crystal, cost)) {
/* 43 */       throw new WSException(ErrorCode.NotEnough_Money, "玩家元宝:%s<所需元宝:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cost) });
/*    */     }
/* 45 */     playerCurrency.consume(PrizeType.Crystal, cost, ItemFlow.DrawPrize);
/* 46 */     Reward reward = accumRecharge.find(player, req.times);
/* 47 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.FindTreasure);
/*    */     
/* 49 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/DrawPrizeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */