/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.global.recharge.RechargeMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.MailFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class MailList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     request.response(((MailFeature)player.getFeature(MailFeature.class)).cmd_getMailList());
/* 17 */     RechargeMgr.getInstance().trySendCachedOrder(player.getPid());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/MailList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */