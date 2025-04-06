/*     */ package business.global.notice;
/*     */ 
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.global.guild.Guild;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.http.client.HttpUtil;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefLanguage;
/*     */ import core.network.proto.NoticeInfo;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoticeMgr
/*     */ {
/*  28 */   private static NoticeMgr instance = new NoticeMgr();
/*     */   
/*     */   public static NoticeMgr getInstance() {
/*  31 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public LinkedList<Notice> NoticeList = Lists.newLinkedList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  46 */     SyncTaskManager.schedule(1000, () -> {
/*     */           checkAndSend();
/*     */           return true;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void downMarqueeFromPHP() throws Exception {
/*  59 */     JsonObject param = new JsonObject();
/*  60 */     param.addProperty("gameid", Integer.valueOf(Config.GameID()));
/*  61 */     param.addProperty("platform", Config.getPlatform());
/*  62 */     param.addProperty("server_id", Integer.valueOf(Config.ServerID()));
/*     */     
/*  64 */     String webBody = HttpUtil.sendHttpPost2Web(15000, 15000, Config.PhpMargueeNoticeAddr(), param.toString(), "");
/*     */     
/*  66 */     webBody = HttpUtil.decodeUnicode(webBody);
/*     */     
/*  68 */     JsonArray array = (new JsonParser()).parse(webBody).getAsJsonArray();
/*     */     
/*  70 */     LinkedList<Notice> noticeList = Lists.newLinkedList();
/*  71 */     for (JsonElement element : array) {
/*  72 */       JsonObject obj = element.getAsJsonObject();
/*     */       
/*  74 */       Notice notice = new Notice();
/*  75 */       notice.setId(HttpUtils.getLong(obj, "id"));
/*  76 */       notice.setContent(HttpUtils.getString(obj, "noticeList"));
/*  77 */       notice.setBeginTime(HttpUtils.getTime(obj, "beginTime"));
/*  78 */       notice.setEndTime(HttpUtils.getTime(obj, "endTime"));
/*  79 */       notice.setInterval(HttpUtils.getInt(obj, "interval") * 60);
/*  80 */       notice.setClientType(HttpUtils.getInt(obj, "clientType"));
/*     */       
/*  82 */       noticeList.add(notice);
/*     */     } 
/*  84 */     synchronized (this) {
/*  85 */       this.NoticeList.clear();
/*  86 */       this.NoticeList = noticeList;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String gmAddMarqueeNotice(long id, String content, int beginTime, int endTime, int interval, int clientType) {
/* 102 */     Notice bo = new Notice();
/* 103 */     bo.setId(id);
/* 104 */     bo.setContent(content);
/* 105 */     bo.setBeginTime(beginTime);
/* 106 */     bo.setEndTime(endTime);
/* 107 */     bo.setInterval(interval);
/* 108 */     bo.setClientType(clientType);
/* 109 */     synchronized (this) {
/* 110 */       this.NoticeList.add(bo);
/*     */     } 
/* 112 */     return "添加成功";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMarquee(ConstEnum.UniverseMessageType messageType, String... params) {
/* 124 */     if (messageType == null) {
/* 125 */       messageType = ConstEnum.UniverseMessageType.None;
/*     */     }
/* 127 */     List<String> values = Lists.newArrayList(); byte b; int j; String[] arrayOfString;
/* 128 */     for (j = (arrayOfString = params).length, b = 0; b < j; ) { String s = arrayOfString[b];
/* 129 */       values.add(s);
/*     */       b++; }
/*     */     
/* 132 */     RefLanguage ref = (RefLanguage)RefDataMgr.get(RefLanguage.class, messageType.toString());
/* 133 */     String content = ref.CN;
/*     */     
/* 135 */     for (int i = 0; i < values.size(); i++) {
/* 136 */       content = content.replace("{" + i + "}", values.get(i));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     NoticeInfo info = new NoticeInfo(messageType, values);
/* 145 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/* 146 */       player.pushProto("marquee", info);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMarquee(ConstEnum.UniverseMessageType messageType, Guild guild, String... params) {
/* 159 */     if (messageType == null) {
/* 160 */       messageType = ConstEnum.UniverseMessageType.None;
/*     */     }
/* 162 */     List<String> values = Lists.newArrayList(); byte b; int j; String[] arrayOfString;
/* 163 */     for (j = (arrayOfString = params).length, b = 0; b < j; ) { String s = arrayOfString[b];
/* 164 */       values.add(s);
/*     */       b++; }
/*     */     
/* 167 */     RefLanguage ref = (RefLanguage)RefDataMgr.get(RefLanguage.class, messageType.toString());
/* 168 */     String content = ref.CN;
/*     */     
/* 170 */     for (int i = 0; i < values.size(); i++) {
/* 171 */       content = content.replace("{" + i + "}", values.get(i));
/*     */     }
/* 173 */     NoticeInfo info = new NoticeInfo(messageType, values);
/* 174 */     guild.broadcast("marquee", info);
/*     */   }
/*     */   
/*     */   public void checkAndSend() {
/* 178 */     int nowSecond = CommTime.nowSecond();
/*     */     
/* 180 */     List<Notice> noticeList = null;
/* 181 */     synchronized (this) {
/* 182 */       List<Notice> removeList = Lists.newArrayList();
/* 183 */       for (Notice notice : this.NoticeList) {
/* 184 */         if (notice.getEndTime() < nowSecond) {
/* 185 */           removeList.add(notice);
/*     */         }
/*     */       } 
/* 188 */       this.NoticeList.removeAll(removeList);
/* 189 */       noticeList = this.NoticeList;
/*     */     } 
/* 191 */     if (noticeList == null || noticeList.size() == 0) {
/*     */       return;
/*     */     }
/* 194 */     for (Notice bo : noticeList) {
/* 195 */       int beginTime = bo.getBeginTime();
/* 196 */       if (nowSecond < beginTime) {
/*     */         continue;
/*     */       }
/* 199 */       boolean send = false;
/* 200 */       if (bo.getInterval() > 0) {
/* 201 */         if (bo.getLastSendTime() + bo.getInterval() < nowSecond) {
/* 202 */           bo.setLastSendTime(nowSecond);
/* 203 */           send = true;
/*     */         } 
/* 205 */       } else if (bo.getInterval() == 0) {
/* 206 */         bo.setLastSendTime(nowSecond);
/* 207 */         send = true;
/*     */       } 
/* 209 */       if (send)
/* 210 */         sendMarquee(ConstEnum.UniverseMessageType.CommonAnounce, new String[] { bo.getContent() }); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/notice/NoticeMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */