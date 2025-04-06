/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.ClientSession;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerInfo
/*    */   extends BaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/* 17 */     ClientSession session = (ClientSession)request.getSession();
/* 18 */     Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
/* 19 */     if (player != null) {
/* 20 */       request.response(((PlayerBase)player.getFeature(PlayerBase.class)).fullInfo());
/*    */       return;
/*    */     } 
/* 23 */     player = session.getPlayer();
/* 24 */     if (player != null) {
/* 25 */       request.response(((PlayerBase)player.getFeature(PlayerBase.class)).fullInfo());
/*    */       return;
/*    */     } 
/* 28 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/PlayerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */