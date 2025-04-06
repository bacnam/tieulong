/*    */ package core.network.client2game.handler.pvc;
/*    */ 
/*    */ import business.global.rank.RankManager;
/*    */ import business.global.rank.Record;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WorldBossRank
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request {
/*    */     int bossId;
/*    */   }
/*    */   
/*    */   public static class RankInfo {
/*    */     Player.Summary players;
/*    */     int rank;
/*    */     long damage;
/*    */     
/*    */     public RankInfo(Player.Summary players, int rank, long damage) {
/* 31 */       this.players = players;
/* 32 */       this.rank = rank;
/* 33 */       this.damage = damage;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 40 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 41 */     RankType type = null;
/* 42 */     switch (req.bossId) {
/*    */       case 1:
/* 44 */         type = RankType.WorldBoss1;
/*    */         break;
/*    */       case 2:
/* 47 */         type = RankType.WorldBoss2;
/*    */         break;
/*    */       case 3:
/* 50 */         type = RankType.WorldBoss3;
/*    */         break;
/*    */       case 4:
/* 53 */         type = RankType.WorldBoss4;
/*    */         break;
/*    */     } 
/*    */     
/* 57 */     List<Record> listRecord = RankManager.getInstance().getRankList(type, 100);
/* 58 */     List<RankInfo> rankinfo = new ArrayList<>();
/* 59 */     if (listRecord.size() > 1) {
/* 60 */       for (int i = 1; i < listRecord.size(); i++) {
/* 61 */         Record record = listRecord.get(i);
/* 62 */         Player tmpPlayer = PlayerMgr.getInstance().getPlayer(record.getPid());
/* 63 */         Player.Summary playersum = ((PlayerBase)tmpPlayer.getFeature(PlayerBase.class)).summary();
/* 64 */         rankinfo.add(new RankInfo(playersum, record.getRank(), record.getValue()));
/*    */       } 
/*    */     }
/*    */     
/* 68 */     request.response(rankinfo);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossRank.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */