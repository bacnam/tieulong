/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.global.recharge.RechargeMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.record.VipRecord;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class PlayerVipChange
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     request.response(((VipRecord)player.getFeature(VipRecord.class)).getFetchGiftStatusList());
/* 17 */     RechargeMgr.getInstance().trySendCachedOrder(player.getPid());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/PlayerVipChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */