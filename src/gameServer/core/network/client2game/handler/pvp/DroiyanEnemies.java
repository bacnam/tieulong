/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class DroiyanEnemies
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static PlayerMgr manager;
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     if (manager == null) {
/* 23 */       manager = PlayerMgr.getInstance();
/*    */     }
/* 25 */     List<Long> list = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getEnemies();
/* 26 */     List<Player.Summary> rtn = new ArrayList<>();
/* 27 */     for (Long bo : list) {
/* 28 */       rtn.add(((PlayerBase)manager.getPlayer(bo.longValue()).getFeature(PlayerBase.class)).summary());
/*    */     }
/* 30 */     request.response(rtn);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanEnemies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */