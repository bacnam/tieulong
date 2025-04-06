/*    */ package core.network.client2game.handler.pvc;
/*    */ 
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.worldboss.WorldBossFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class WorldBossAuto
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     WorldBossMgr.getInstance().applyAuto(player);
/* 17 */     request.response(Boolean.valueOf(((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossAuto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */