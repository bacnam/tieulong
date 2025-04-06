/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.DailyRecharge;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DailyRechargeReceiveHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     int awardId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 23 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 24 */     DailyRecharge dailyRecharge = (DailyRecharge)ActivityMgr.getActivity(DailyRecharge.class);
/* 25 */     if (dailyRecharge.getStatus() == ActivityStatus.Close) {
/* 26 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { dailyRecharge.getType() });
/*    */     }
/* 28 */     int awardId = req.awardId;
/* 29 */     if (awardId < 0) {
/* 30 */       throw new WSException(ErrorCode.InvalidParam, "非法的参数awardId=%s", new Object[] { Integer.valueOf(awardId) });
/*    */     }
/* 32 */     request.response(dailyRecharge.doReceivePrize(player, awardId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/DailyRechargeReceiveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */