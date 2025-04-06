/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.MarryApplyBO;
/*    */ import core.database.game.bo.MarryDivorceApplyBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class CheckApply
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 23 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 24 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/*    */     
/* 26 */     MarryDivorceApplyBO dbo = feature.getDivorceApply(req.pid);
/* 27 */     MarryApplyBO bo = feature.getApply(req.pid);
/* 28 */     if (dbo != null) {
/* 29 */       feature.checkApply(dbo);
/*    */     }
/*    */     
/* 32 */     if (bo != null) {
/* 33 */       feature.checkApply(bo);
/*    */     }
/*    */     
/* 36 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/CheckApply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */