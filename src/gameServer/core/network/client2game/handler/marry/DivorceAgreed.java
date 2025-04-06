/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DivorceAgreed
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 21 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 22 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 23 */     feature.agreedDivorce(req.pid);
/* 24 */     request.response(feature.getLoveInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/DivorceAgreed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */