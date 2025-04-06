/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SignInOpenServer
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 18 */     business.global.activity.detail.SignInOpenServer openServer = (business.global.activity.detail.SignInOpenServer)ActivityMgr.getActivity(business.global.activity.detail.SignInOpenServer.class);
/* 19 */     if (openServer.getStatus() == ActivityStatus.Close) {
/* 20 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { openServer.getType() });
/*    */     }
/* 22 */     request.response(openServer.doSignIn(player));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/SignInOpenServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */