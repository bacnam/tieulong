/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.DrawPrize;
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
/*    */ public class DrawPrizeInfoHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 19 */     DrawPrize accumRecharge = (DrawPrize)ActivityMgr.getActivity(DrawPrize.class);
/* 20 */     if (accumRecharge.getStatus() == ActivityStatus.Close) {
/* 21 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { accumRecharge.getType() });
/*    */     }
/* 23 */     request.response(accumRecharge.accumRechargeProto(player));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/DrawPrizeInfoHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */