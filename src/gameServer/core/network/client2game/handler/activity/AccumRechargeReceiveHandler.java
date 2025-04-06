/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.AccumRecharge;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class AccumRechargeReceiveHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     int awardId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 25 */     AccumRecharge accumRecharge = (AccumRecharge)ActivityMgr.getActivity(AccumRecharge.class);
/* 26 */     if (accumRecharge.getStatus() == ActivityStatus.Close) {
/* 27 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { accumRecharge.getType() });
/*    */     }
/* 29 */     int awardId = req.awardId;
/* 30 */     if (awardId < 0) {
/* 31 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数awardId=%s", new Object[] { Integer.valueOf(awardId) });
/*    */     }
/* 33 */     request.response(accumRecharge.doReceivePrize(player, awardId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/AccumRechargeReceiveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */