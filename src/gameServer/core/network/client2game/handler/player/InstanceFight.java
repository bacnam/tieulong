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
/*    */ public class InstanceFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     InstanceType type;
/*    */     int level;
/*    */   }
/*    */   
/*    */   static class instanceFightInfo {
/*    */     InstanceType type;
/*    */     int challengTimes;
/*    */     
/*    */     public instanceFightInfo(InstanceType type, int challengTimes) {
/* 25 */       this.type = type;
/* 26 */       this.challengTimes = challengTimes;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 32 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 33 */     int challengTimes = 0;
/* 34 */     InstanceFeature feature = (InstanceFeature)player.getFeature(InstanceFeature.class);
/* 35 */     challengTimes = feature.getOrCreate().getChallengTimes(req.type.ordinal());
/* 36 */     request.response(new instanceFightInfo(req.type, challengTimes));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/InstanceFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */