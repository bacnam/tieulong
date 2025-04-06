/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.player.LingBaoFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class LingBaoInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int level;
/*    */     int exp;
/*    */     
/*    */     private Response(int level, int exp) {
/* 20 */       this.level = level;
/* 21 */       this.exp = exp;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     LingBaoFeature feature = (LingBaoFeature)player.getFeature(LingBaoFeature.class);
/* 29 */     request.response(new Response(feature.getLevel(), feature.getExp(), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/LingBaoInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */