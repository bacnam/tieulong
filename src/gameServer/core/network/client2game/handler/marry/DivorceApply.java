/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.MarryDivorceApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DivorceApply
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 23 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 24 */     MarryDivorceApplyBO bo = feature.marryDivorceApply(req.pid);
/* 25 */     request.response(bo);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/DivorceApply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */