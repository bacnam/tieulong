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
/*    */ public class LingBaoUp
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int level;
/*    */     int exp;
/*    */     int gain;
/*    */     
/*    */     private Response(int level, int exp, int gain) {
/* 21 */       this.level = level;
/* 22 */       this.exp = exp;
/* 23 */       this.gain = gain;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 30 */     LingBaoFeature feature = (LingBaoFeature)player.getFeature(LingBaoFeature.class);
/* 31 */     int exp = feature.levelUp();
/* 32 */     request.response(new Response(feature.getLevel(), feature.getExp(), exp, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/LingBaoUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */