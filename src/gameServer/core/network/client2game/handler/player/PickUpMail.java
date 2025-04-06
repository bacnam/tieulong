/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.MailFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PickUpMail
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request {
/*    */     long sid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 19 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 20 */     long mailId = req.sid;
/* 21 */     ((MailFeature)player.getFeature(MailFeature.class)).pickUpMail(mailId, request);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/PickUpMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */