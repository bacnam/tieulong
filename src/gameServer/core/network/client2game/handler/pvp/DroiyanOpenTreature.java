/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DroiyanOpenTreature
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Request
/*    */   {
/*    */     long sid;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     int openTimes;
/*    */     Reward reward;
/*    */     
/*    */     private Response(int openTimes, Reward reward) {
/* 28 */       this.openTimes = openTimes;
/* 29 */       this.reward = reward;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 35 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 36 */     Reward reward = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).openTreature(req.sid);
/* 37 */     int times = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.OpenTreasure);
/* 38 */     request.response(new Response(times, reward, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanOpenTreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */