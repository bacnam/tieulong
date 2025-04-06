/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class SignInSeven
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     int day;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     business.global.activity.detail.SignInSeven openServer = (business.global.activity.detail.SignInSeven)ActivityMgr.getActivity(business.global.activity.detail.SignInSeven.class);
/* 24 */     if (openServer.getStatus() == ActivityStatus.Close) {
/* 25 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { openServer.getType() });
/*    */     }
/* 27 */     request.response(openServer.doSignIn(player, req.day));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/SignInSeven.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */