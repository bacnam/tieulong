/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.SignInSeven;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class SignInSevenInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     SignInSeven openServer = (SignInSeven)ActivityMgr.getActivity(SignInSeven.class);
/* 17 */     request.response(openServer.dailySignProto(player));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/SignInSevenInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */