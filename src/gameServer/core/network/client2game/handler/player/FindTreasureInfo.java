/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.treasure.FindTreasureFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class FindTreasureInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int leftTimes;
/*    */     int leftTentimes;
/*    */     
/*    */     private Response(int leftTimes, int leftTentimes) {
/* 20 */       this.leftTimes = leftTimes;
/* 21 */       this.leftTentimes = leftTentimes;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     FindTreasureFeature feature = (FindTreasureFeature)player.getFeature(FindTreasureFeature.class);
/* 29 */     request.response(new Response(feature.getLeftTimes(ConstEnum.FindTreasureType.single), feature.getLeftTimes(ConstEnum.FindTreasureType.Ten), null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/FindTreasureInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */