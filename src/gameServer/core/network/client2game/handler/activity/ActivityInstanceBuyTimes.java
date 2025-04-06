/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.ActivityInstance;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ActivityInstanceBuyTimes
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int leftTimes;
/*    */     int leftBuyTimes;
/*    */     
/*    */     private Response(int leftTimes, int leftBuyTimes) {
/* 20 */       this.leftTimes = leftTimes;
/* 21 */       this.leftBuyTimes = leftBuyTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 29 */     ActivityInstance instance = (ActivityInstance)ActivityMgr.getActivity(ActivityInstance.class);
/*    */     
/* 31 */     instance.buyTimes(player);
/* 32 */     request.response(new Response(instance.getLeftTimes(player), instance.getBuyTimes() - instance.getBuyTimes(player), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/ActivityInstanceBuyTimes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */