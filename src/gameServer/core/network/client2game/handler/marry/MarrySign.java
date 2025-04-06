/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class MarrySign
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int mySign;
/*    */     boolean isSign;
/*    */     
/*    */     public Response(int mySign, boolean isSign) {
/* 20 */       this.mySign = mySign;
/* 21 */       this.isSign = isSign;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 29 */     feature.signIn();
/* 30 */     request.response(new Response(feature.getSignin(), feature.bo.getIsSign()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/MarrySign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */