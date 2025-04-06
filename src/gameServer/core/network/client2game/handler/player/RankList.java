/*     */ package core.network.client2game.handler.player;
/*     */ 
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.rank.Record;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.feature.pvp.WorshipFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*     */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*     */ import core.database.game.bo.PlayerBO;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.network.game2world.WorldConnector;
/*     */ import core.network.proto.Player;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import proto.gameworld.WorldRankInfo;
/*     */ 
/*     */ public class RankList
/*     */   extends PlayerHandler
/*     */ {
/*     */   private static class Request
/*     */   {
/*     */     RankType rank;
/*     */   }
/*     */   
/*     */   private static class Response
/*     */   {
/*     */     int worshipTimes;
/*     */     int rank;
/*     */     long value;
/*     */     List<RankList.RankInfo> rankList;
/*     */     
/*     */     public Response(int worshipTimes, int rank, long value, List<Record> records) {
/*  42 */       this.worshipTimes = worshipTimes;
/*  43 */       this.rank = rank;
/*  44 */       this.value = value;
/*  45 */       this.rankList = new ArrayList<>();
/*  46 */       for (int i = 0; i < records.size(); i++) {
/*  47 */         Record r = records.get(i);
/*  48 */         if (r != null)
/*     */         {
/*     */           
/*  51 */           this.rankList.add(new RankList.RankInfo(r)); } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RankInfo extends Player.Summary {
/*     */     static PlayerMgr playerMgr;
/*     */     int rank;
/*     */     long value;
/*     */     
/*     */     public RankInfo(Record record) {
/*  62 */       if (playerMgr == null) {
/*  63 */         playerMgr = PlayerMgr.getInstance();
/*     */       }
/*     */       
/*  66 */       Player player = playerMgr.getPlayer(record.getPid());
/*  67 */       PlayerBO bo = player.getPlayerBO();
/*  68 */       this.pid = bo.getId();
/*  69 */       this.name = bo.getName();
/*  70 */       this.lv = bo.getLv();
/*  71 */       this.icon = bo.getIcon();
/*  72 */       this.vipLv = bo.getVipLevel();
/*  73 */       this.power = ((CharFeature)player.getFeature(CharFeature.class)).getPower();
/*  74 */       this.rank = record.getRank();
/*  75 */       this.value = record.getValue();
/*     */ 
/*     */       
/*  78 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/*  79 */       int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
/*  80 */       this.MonthCard = (monthNum > 0);
/*  81 */       int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
/*  82 */       this.YearCard = (yearNum == -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class WorldRequest
/*     */   {
/*     */     RankType rank;
/*     */     
/*     */     long pid;
/*     */ 
/*     */     
/*     */     private WorldRequest() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
/*  99 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*     */     
/* 101 */     if (WorldConnector.getInstance().isConnected()) {
/* 102 */       RankType rankType = null;
/* 103 */       switch (req.rank) {
/*     */         case WorldRecharge:
/* 105 */           rankType = req.rank;
/*     */           break;
/*     */         case WorldConsume:
/* 108 */           rankType = req.rank;
/*     */           break;
/*     */         case WorldTreasure:
/* 111 */           rankType = req.rank;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 116 */       if (rankType != null) {
/* 117 */         WorldRequest worldRequest = new WorldRequest(null);
/* 118 */         worldRequest.rank = rankType;
/* 119 */         worldRequest.pid = player.getPid();
/* 120 */         WorldConnector.request("activity.ranklist", worldRequest, new ResponseHandler()
/*     */             {
/*     */               public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException
/*     */               {
/* 124 */                 WorldRankInfo req = (WorldRankInfo)(new Gson()).fromJson(body, WorldRankInfo.class);
/* 125 */                 request.response(req);
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {}
/*     */             });
/*     */         return;
/*     */       } 
/*     */     } 
/* 139 */     List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
/* 140 */     int rank = RankManager.getInstance().getRank(req.rank, player.getPid());
/* 141 */     long value = RankManager.getInstance().getValue(req.rank, player.getPid());
/* 142 */     int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(req.rank.ordinal());
/* 143 */     request.response(new Response(times, rank, value, records));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/RankList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */