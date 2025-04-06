/*     */ package core.network.client2game.handler.marry;
/*     */ 
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.rank.Record;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.marry.MarryFeature;
/*     */ import business.player.feature.pvp.WorshipFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.network.proto.LoverInfo;
/*     */ import core.network.proto.Player;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class RankList
/*     */   extends PlayerHandler
/*     */ {
/*     */   private static class Request
/*     */   {
/*     */     RankType rank;
/*     */   }
/*     */   
/*     */   private static class Response {
/*     */     int worshipTimes;
/*     */     int rank;
/*     */     long value;
/*     */     List<RankList.RankInfo> rankList;
/*     */     
/*     */     public Response(int worshipTimes, int rank, long value, List<Record> records) {
/*  36 */       this.worshipTimes = worshipTimes;
/*  37 */       this.rank = rank;
/*  38 */       this.value = value;
/*  39 */       this.rankList = new ArrayList<>();
/*  40 */       List<Record> remove = new ArrayList<>();
/*  41 */       for (int i = 0; i < records.size(); i++) {
/*  42 */         Record r = records.get(i);
/*  43 */         if (r != null) {
/*     */ 
/*     */           
/*  46 */           Player player = PlayerMgr.getInstance().getPlayer(r.getPid());
/*  47 */           MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
/*  48 */           LoverInfo info = marry.getLoveInfo();
/*  49 */           if (info == null)
/*  50 */           { remove.add(r); }
/*     */           
/*     */           else
/*     */           
/*  54 */           { this.rankList.add(new RankList.RankInfo(r)); } 
/*     */         } 
/*  56 */       }  for (Record r : remove) {
/*  57 */         Player player = PlayerMgr.getInstance().getPlayer(r.getPid());
/*  58 */         RankManager.getInstance().clearPlayerData(player, RankType.Lovers);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RankInfo
/*     */   {
/*     */     int rank;
/*     */     long value;
/*     */     Player.Summary husband;
/*     */     Player.Summary wife;
/*     */     
/*     */     public RankInfo(Record record) {
/*  71 */       Player player = PlayerMgr.getInstance().getPlayer(record.getPid());
/*  72 */       MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
/*  73 */       LoverInfo info = marry.getLoveInfo();
/*  74 */       this.rank = record.getRank();
/*  75 */       this.value = record.getValue();
/*  76 */       this.husband = info.getHusband();
/*  77 */       this.wife = info.getWife();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*  83 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  84 */     List<Record> records = RankManager.getInstance().getRankList(req.rank, 30);
/*  85 */     int rank = 0;
/*  86 */     long value = 0L;
/*  87 */     MarryFeature marry = (MarryFeature)player.getFeature(MarryFeature.class);
/*  88 */     Player man = null;
/*  89 */     if (marry.bo.getMarried() != 0) {
/*  90 */       Player lover = PlayerMgr.getInstance().getPlayer(marry.bo.getLoverPid());
/*  91 */       if (marry.bo.getSex() == ConstEnum.SexType.Man.ordinal()) {
/*  92 */         man = player;
/*     */       } else {
/*  94 */         man = lover;
/*     */       } 
/*     */     } 
/*  97 */     if (man != null) {
/*  98 */       rank = RankManager.getInstance().getRank(req.rank, man.getPid());
/*  99 */       value = RankManager.getInstance().getValue(req.rank, man.getPid());
/*     */     } 
/* 101 */     int times = ((WorshipFeature)player.getFeature(WorshipFeature.class)).getTimes(req.rank.ordinal());
/* 102 */     request.response(new Response(times, rank, value, records));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/RankList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */