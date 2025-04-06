/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.utils.StringUtils;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class MarrySignPick
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int signId;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     List<Integer> alreadyPick;
/*    */     Reward reward;
/*    */     
/*    */     public Response(List<Integer> alreadyPick, Reward reward) {
/* 28 */       this.alreadyPick = alreadyPick;
/* 29 */       this.reward = reward;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 36 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 37 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 38 */     Reward reward = feature.pickSignInReward(req.signId);
/* 39 */     List<Integer> alreadyPick = StringUtils.string2Integer(feature.bo.getSignReward());
/* 40 */     request.response(new Response(alreadyPick, reward));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/MarrySignPick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */