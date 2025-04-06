/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.common.utils.StringUtils;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class MarrySignInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     Player.Summary lover;
/*    */     int loverSign;
/*    */     Player.Summary my;
/*    */     int mySign;
/*    */     boolean isSign;
/*    */     List<Integer> alreadyPick;
/*    */     
/*    */     public Response(Player.Summary lover, int loverSign, Player.Summary my, int mySign, boolean isSign, List<Integer> alreadyPick) {
/* 29 */       this.lover = lover;
/* 30 */       this.loverSign = loverSign;
/* 31 */       this.my = my;
/* 32 */       this.mySign = mySign;
/* 33 */       this.isSign = isSign;
/* 34 */       this.alreadyPick = alreadyPick;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 42 */     if (!feature.isMarried()) {
/* 43 */       throw new WSException(ErrorCode.Marry_NotYet, "尚未结婚");
/*    */     }
/* 45 */     Player.Summary lover = ((PlayerBase)feature.getLover().getFeature(PlayerBase.class)).summary();
/* 46 */     int loverSign = feature.getLoverFeature().getSignin();
/* 47 */     Player.Summary my = ((PlayerBase)player.getFeature(PlayerBase.class)).summary();
/* 48 */     int mySign = feature.getSignin();
/* 49 */     boolean isSign = feature.bo.getIsSign();
/* 50 */     List<Integer> alreadyPick = StringUtils.string2Integer(feature.bo.getSignReward());
/* 51 */     request.response(new Response(lover, loverSign, my, mySign, isSign, alreadyPick));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/MarrySignInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */