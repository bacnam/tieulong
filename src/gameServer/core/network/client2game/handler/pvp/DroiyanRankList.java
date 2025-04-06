/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
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
/*    */ public class DroiyanRankList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 21 */     List<Competitor> rank = ArenaManager.getInstance().getRankList(ArenaConfig.showSize());
/* 22 */     List<Arena.CompetitorInfo> rtn = new ArrayList<>();
/* 23 */     for (Competitor opp : rank) {
/* 24 */       rtn.add(new Arena.CompetitorInfo(opp));
/*    */     }
/* 26 */     request.response(rtn);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanRankList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */