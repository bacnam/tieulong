/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.Lottery;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class LotteryLoadInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     ConstEnum.LotteryType type;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     Lottery Lottery = (Lottery)ActivityMgr.getActivity(Lottery.class);
/* 24 */     request.response(Lottery.loadLotteryInfo(player, req.type));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/LotteryLoadInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */