/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.MarryApplyInfo;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class MarryTo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     long pid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 25 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 26 */     feature.marryApply(req.pid);
/* 27 */     MarryApplyInfo info = new MarryApplyInfo();
/* 28 */     info.setSummary(((PlayerBase)PlayerMgr.getInstance().getPlayer(feature.sendApplys.getPid()).getFeature(PlayerBase.class)).summary());
/* 29 */     info.setLeftTime(feature.getLeftTime(feature.sendApplys));
/* 30 */     request.response(info);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/MarryTo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */