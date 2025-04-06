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
/*    */ public class LotteryAttend
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     ConstEnum.LotteryType type;
/*    */     int times;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 23 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 24 */     Lottery Lottery = (Lottery)ActivityMgr.getActivity(Lottery.class);
/* 25 */     request.response(Lottery.attend(player, req.type, req.times));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/LotteryAttend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */