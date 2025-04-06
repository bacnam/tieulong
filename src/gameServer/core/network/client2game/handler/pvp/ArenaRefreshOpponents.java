/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Arena;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArenaRefreshOpponents
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     Competitor competitor = ArenaManager.getInstance().getOrCreate(player.getPid());
/*    */     
/* 24 */     if (competitor.getRefreshCD() > 0) {
/* 25 */       throw new WSException(ErrorCode.Arena_RefreshInCD, "玩家[%s]的冷却中,还有[%s]秒", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(competitor.getRefreshCD()) });
/*    */     }
/* 27 */     competitor.setRefreshCD(ArenaConfig.refreshCD());
/* 28 */     competitor.resetOpponents();
/*    */     
/* 30 */     List<Arena.CompetitorInfo> opponents = new ArrayList<>();
/* 31 */     for (Competitor opp : competitor.getOpponents()) {
/* 32 */       opponents.add(new Arena.CompetitorInfo(opp));
/*    */     }
/* 34 */     request.response(opponents);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaRefreshOpponents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */