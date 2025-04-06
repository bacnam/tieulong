/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.player.Player;
/*    */ import business.player.feature.pvp.WorshipFeature;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Arena;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class ArenaRankList
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Respose
/*    */   {
/*    */     int worshipTimes;
/*    */     List<Arena.CompetitorInfo> ranklist;
/*    */     
/*    */     private Respose(int worshipTimes, List<Arena.CompetitorInfo> ranklist) {
/* 27 */       this.worshipTimes = worshipTimes;
/* 28 */       this.ranklist = ranklist;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 35 */     List<Competitor> rank = ArenaManager.getInstance().getRankList(ArenaConfig.showSize());
/* 36 */     List<Arena.CompetitorInfo> rtn = new ArrayList<>();
/* 37 */     for (Competitor opp : rank) {
/* 38 */       rtn.add(new Arena.CompetitorInfo(opp));
/*    */     }
/*    */     
/* 41 */     int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(RankType.Arena.ordinal());
/* 42 */     request.response(new Respose(times, rtn, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaRankList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */