/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.DrawPrize;
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
/*    */ public class DrawPrizePick
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     int awardId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 25 */     DrawPrize accumRecharge = (DrawPrize)ActivityMgr.getActivity(DrawPrize.class);
/* 26 */     if (accumRecharge.getStatus() == ActivityStatus.Close) {
/* 27 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { accumRecharge.getType() });
/*    */     }
/*    */     
/* 30 */     request.response(accumRecharge.doReceivePrize(player, req.awardId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/DrawPrizePick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */