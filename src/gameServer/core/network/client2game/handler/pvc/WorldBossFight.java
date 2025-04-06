/*    */ package core.network.client2game.handler.pvc;
/*    */ 
/*    */ import business.global.worldboss.WorldBossMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.worldboss.WorldBossFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.WorldBossBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WorldBossFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     int bossId;
/*    */     int damage;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 23 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 24 */     if (req.damage < 0)
/* 25 */       req.damage = 0; 
/* 26 */     ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).hurtWorldBoss(req.bossId, req.damage);
/* 27 */     WorldBossBO boss = WorldBossMgr.getInstance().getBO(req.bossId);
/* 28 */     request.response(boss);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */