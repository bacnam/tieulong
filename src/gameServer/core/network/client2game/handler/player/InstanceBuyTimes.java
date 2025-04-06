/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.pve.InstanceFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.InstanceType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class InstanceBuyTimes
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     InstanceType type;
/*    */     int times;
/*    */   }
/*    */   
/*    */   static class instanceFightInfo {
/*    */     InstanceType type;
/*    */     int challengTimes;
/*    */     int buyTimes;
/*    */     
/*    */     public instanceFightInfo(InstanceType type, int challengTimes, int buyTimes) {
/* 26 */       this.type = type;
/* 27 */       this.challengTimes = challengTimes;
/* 28 */       this.buyTimes = buyTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 34 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 35 */     if (req.times == 0) {
/* 36 */       req.times = 1;
/*    */     }
/*    */     
/* 39 */     InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
/* 40 */     String challengAndTimes = feature.buyChallengTimes(req.type, req.times);
/* 41 */     String[] info = challengAndTimes.split(";");
/*    */     
/* 43 */     request.response(new instanceFightInfo(req.type, Integer.valueOf(info[0]).intValue(), Integer.valueOf(info[1]).intValue()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/InstanceBuyTimes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */