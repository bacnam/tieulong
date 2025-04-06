/*    */ package core.network.client2game.handler.title;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.player.NewTitleFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class UpTitle
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     int titleId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 20 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 21 */     request.response(((NewTitleFeature)player.getFeature(NewTitleFeature.class)).upgradeTitle(req.titleId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/title/UpTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */