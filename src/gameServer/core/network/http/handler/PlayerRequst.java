/*     */ package core.network.http.handler;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerBase;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.annotation.RequestMapping;
/*     */ import com.zhonglian.server.http.server.HttpRequest;
/*     */ import com.zhonglian.server.http.server.HttpResponse;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class PlayerRequst
/*     */ {
/*  21 */   public Gson gson = new Gson();
/*     */   
/*     */   @RequestMapping(uri = "/game/player/info")
/*     */   public void playerList(HttpRequest request, HttpResponse response) throws Exception {
/*  25 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/*  27 */     String openid = HttpUtils.getString(postParam, "open_id");
/*     */     
/*  29 */     int serverid = HttpUtils.getInt(postParam, "server_id");
/*     */     
/*  31 */     List<Player> player = PlayerMgr.getInstance().searchPlayer(openid, serverid);
/*  32 */     if (player == null) {
/*  33 */       response.error(30103, "找不到该玩家:%s", new Object[] { openid });
/*     */       return;
/*     */     } 
/*  36 */     JsonArray roles = new JsonArray();
/*  37 */     for (Player p : player) {
/*     */       
/*  39 */       JsonObject roleObj = new JsonObject();
/*  40 */       roleObj.addProperty("server", p.getSid());
/*  41 */       roleObj.addProperty("id", p.getPid());
/*  42 */       roleObj.addProperty("name", p.getName());
/*  43 */       roleObj.addProperty("level", p.getLv());
/*  44 */       roleObj.addProperty("vip", p.getVipLevel());
/*  45 */       roleObj.addProperty("crystal", p.getPlayerBO().getCrystal());
/*  46 */       roles.add((JsonElement)roleObj);
/*     */     } 
/*     */     
/*  49 */     JsonObject rtn = new JsonObject();
/*  50 */     rtn.addProperty("state", Integer.valueOf(1000));
/*  51 */     rtn.add("roleinfo", (JsonElement)roles);
/*     */     
/*  53 */     response.response(this.gson.toJson((JsonElement)rtn));
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/player/bannedLogin")
/*     */   public void bannedPlayerLogin(HttpRequest request, HttpResponse response) throws Exception {
/*  58 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/*  60 */     List<Long> cidList = HttpUtils.getLongList(postParam, "pid");
/*     */     
/*  62 */     int duration = HttpUtils.getInt(postParam, "duration");
/*     */     
/*  64 */     List<Long> failCids = Lists.newArrayList();
/*     */     
/*  66 */     cidList.forEach(cid -> {
/*     */           Player player = PlayerMgr.getInstance().getPlayer(cid.longValue());
/*     */           
/*     */           if (player != null) {
/*     */             ((PlayerBase)player.getFeature(PlayerBase.class)).bannedLogin(paramInt);
/*     */           } else {
/*     */             paramList.add(cid);
/*     */           } 
/*     */         });
/*  75 */     cidList.removeAll(failCids);
/*     */     
/*  77 */     JsonObject rtn = new JsonObject();
/*  78 */     rtn.addProperty("state", Integer.valueOf(1000));
/*  79 */     rtn.addProperty("msg", "成功禁登:" + cidList.toString() + ",找不到玩家:" + failCids.toString());
/*     */     
/*  81 */     response.response(this.gson.toJson((JsonElement)rtn));
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/player/bannedChat")
/*     */   public void bannedPlayerChat(HttpRequest request, HttpResponse response) throws Exception {
/*  86 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/*  88 */     List<Long> cidList = HttpUtils.getLongList(postParam, "pid");
/*     */     
/*  90 */     int duration = HttpUtils.getInt(postParam, "duration");
/*  91 */     if (duration > 0) {
/*  92 */       duration = CommTime.nowSecond() + duration;
/*     */     }
/*     */     
/*  95 */     int expiredTime = duration;
/*     */     
/*  97 */     List<Long> failCids = Lists.newArrayList();
/*     */     
/*  99 */     cidList.forEach(cid -> {
/*     */           Player player = PlayerMgr.getInstance().getPlayer(cid.longValue());
/*     */           
/*     */           if (player != null) {
/*     */             player.getPlayerBO().saveBannedChatExpiredTime(paramInt);
/*     */           } else {
/*     */             paramList.add(cid);
/*     */           } 
/*     */         });
/* 108 */     cidList.removeAll(failCids);
/*     */     
/* 110 */     JsonObject rtn = new JsonObject();
/* 111 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 112 */     rtn.addProperty("msg", "成功禁言:" + cidList.toString() + ",找不到玩家:" + failCids.toString());
/*     */     
/* 114 */     response.response(this.gson.toJson((JsonElement)rtn));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/PlayerRequst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */