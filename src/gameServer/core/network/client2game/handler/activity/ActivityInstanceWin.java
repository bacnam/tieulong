/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.ActivityInstance;
/*    */ import business.player.Player;
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ActivityInstanceWin
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int leftTimes;
/*    */     Reward rewardItem;
/*    */     
/*    */     private Response(int leftTimes, Reward rewardItem) {
/* 21 */       this.leftTimes = leftTimes;
/* 22 */       this.rewardItem = rewardItem;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 30 */     ActivityInstance instance = (ActivityInstance)ActivityMgr.getActivity(ActivityInstance.class);
/* 31 */     Reward reward = instance.instanceWin(player);
/* 32 */     request.response(new Response(instance.getLeftTimes(player), reward, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/ActivityInstanceWin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */