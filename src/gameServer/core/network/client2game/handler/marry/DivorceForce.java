/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerItem;
/*    */ import business.player.feature.marry.MarryConfig;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class DivorceForce
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 30 */     Player toPlayer = PlayerMgr.getInstance().getPlayer(req.pid);
/* 31 */     MarryFeature to_feature = (MarryFeature)toPlayer.getFeature(MarryFeature.class);
/*    */     
/* 33 */     if (!to_feature.isMarried()) {
/* 34 */       throw new WSException(ErrorCode.Marry_DivorceAlready, "玩家已离婚");
/*    */     }
/*    */     
/* 37 */     if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(MarryConfig.getMarryDivorceApplyCostId(), MarryConfig.getMarryDivorceApplyCostCount(), 
/* 38 */         ItemFlow.MarryDivorce)) {
/* 39 */       throw new WSException(ErrorCode.NotEnough_Currency, "缺少离婚许可");
/*    */     }
/*    */     
/* 42 */     to_feature.divorce();
/* 43 */     toPlayer.pushProto("beDivorce", to_feature.getLoveInfo());
/* 44 */     MailCenter.getInstance().sendMail(toPlayer.getPid(), RefDataMgr.getFactor("ForceDivorceMail", 1700001), new String[0]);
/*    */     
/* 46 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 47 */     feature.divorce();
/* 48 */     request.response(feature.getLoveInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/DivorceForce.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */