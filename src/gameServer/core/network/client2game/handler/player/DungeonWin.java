/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.pve.DungeonFeature;
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DungeonWin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 19 */     DungeonFeature dungeon = (DungeonFeature)player.getFeature(DungeonFeature.class);
/* 20 */     if (dungeon.isInWinCD()) {
/* 21 */       request.error(ErrorCode.Dungeon_WinCD, "副本挑战CD中", new Object[0]);
/* 22 */       if (dungeon.isCheater()) {
/* 23 */         ((PlayerBase)player.getFeature(PlayerBase.class)).kickout();
/*    */       }
/*    */     } else {
/* 26 */       Reward reward = dungeon.win();
/* 27 */       request.response(reward);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/DungeonWin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */