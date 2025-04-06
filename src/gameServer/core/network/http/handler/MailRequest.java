/*     */ package core.network.http.handler;
/*     */ 
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.gmmail.TimerMailMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.http.annotation.RequestMapping;
/*     */ import com.zhonglian.server.http.server.HttpRequest;
/*     */ import com.zhonglian.server.http.server.HttpResponse;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MailRequest
/*     */ {
/*     */   @RequestMapping(uri = "/game/mail/send")
/*     */   public void send(HttpRequest request, HttpResponse response) throws Exception {
/*  21 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*  22 */     int globalmail = HttpUtils.getInt(postParam, "globalmail");
/*  23 */     if (globalmail != 0 && globalmail != 1) {
/*  24 */       response.error(30103, "非法的操作类型:%s", new Object[] { Integer.valueOf(globalmail) });
/*     */       return;
/*     */     } 
/*  27 */     if (globalmail == 0) {
/*  28 */       sendToPointPlayer(postParam);
/*  29 */     } else if (globalmail == 1) {
/*  30 */       sendToAllPlayer(postParam);
/*     */     } 
/*     */     
/*  33 */     JsonObject rtn = new JsonObject();
/*  34 */     rtn.addProperty("state", Integer.valueOf(1000));
/*  35 */     rtn.addProperty("msg", "success");
/*  36 */     response.response(rtn.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendToPointPlayer(JsonObject postParam) throws Exception {
/*  47 */     List<Long> cids = HttpUtils.getLongList(postParam, "pid");
/*     */     
/*  49 */     String sendName = HttpUtils.getString(postParam, "name");
/*     */     
/*  51 */     String title = HttpUtils.getString(postParam, "title");
/*     */     
/*  53 */     String content = HttpUtils.getString(postParam, "content");
/*     */     
/*  55 */     Reward reward = new Reward(HttpUtils.getJsonArray(postParam, "itemlist"));
/*     */     
/*  57 */     for (Iterator<Long> iterator = cids.iterator(); iterator.hasNext(); ) { long cid = ((Long)iterator.next()).longValue();
/*  58 */       Player player = PlayerMgr.getInstance().getPlayer(cid);
/*  59 */       if (player != null) {
/*  60 */         MailCenter.getInstance().sendMail(cid, sendName, title, content, reward.uniformItemIds(), reward.uniformItemCounts());
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendToAllPlayer(JsonObject postParam) throws Exception {
/*  73 */     String sendName = HttpUtils.getString(postParam, "name");
/*     */     
/*  75 */     String title = HttpUtils.getString(postParam, "title");
/*     */     
/*  77 */     String content = HttpUtils.getString(postParam, "content");
/*     */     
/*  79 */     Reward reward = new Reward(HttpUtils.getJsonArray(postParam, "itemlist"));
/*     */     
/*  81 */     MailCenter.getInstance().sendGlobalMail(sendName, title, content, 0, 0, reward.uniformItemIds(), reward.uniformItemCounts());
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(uri = "/game/mail/timerMailList")
/*     */   public void timerMailList(HttpRequest request, HttpResponse response) throws Exception {
/*  87 */     HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/*  89 */     JsonObject rtn = new JsonObject();
/*  90 */     rtn.addProperty("state", Integer.valueOf(1000));
/*  91 */     rtn.add("mails", (JsonElement)TimerMailMgr.getInstance().timerMailList());
/*  92 */     response.response(rtn.toString());
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/mail/timersend")
/*     */   public void timerSend(HttpRequest request, HttpResponse response) throws Exception {
/*  97 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*  98 */     int beginTime = HttpUtils.getTime(postParam, "begintime");
/*  99 */     if (beginTime == 0) {
/* 100 */       response.error(30103, "非法的开始时间:%s", new Object[] { Integer.valueOf(beginTime) });
/*     */       return;
/*     */     } 
/* 103 */     int cycles = HttpUtils.getInt(postParam, "cycles");
/* 104 */     if (cycles < 1) {
/* 105 */       response.error(30103, "非法的循环次数:%s", new Object[] { Integer.valueOf(cycles) });
/*     */       return;
/*     */     } 
/* 108 */     TimerMailMgr.getInstance().addTimerMail(postParam);
/*     */ 
/*     */     
/* 111 */     JsonObject rtn = new JsonObject();
/* 112 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 113 */     rtn.addProperty("msg", "success");
/* 114 */     response.response(rtn.toString());
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/mail/timerdel")
/*     */   public void timerDel(HttpRequest request, HttpResponse response) throws Exception {
/* 119 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/* 120 */     long mailId = HttpUtils.getLong(postParam, "mailid");
/* 121 */     if (mailId < 0L) {
/* 122 */       response.error(30103, "非法的邮件Id:%s", new Object[] { Long.valueOf(mailId) });
/*     */       return;
/*     */     } 
/* 125 */     TimerMailMgr.getInstance().delTimerMail(mailId);
/*     */     
/* 127 */     JsonObject rtn = new JsonObject();
/* 128 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 129 */     rtn.addProperty("msg", "success");
/* 130 */     response.response(rtn.toString());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/MailRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */