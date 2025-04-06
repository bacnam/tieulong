/*     */ package business.global.gmmail;
/*     */ 
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.player.item.Reward;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import core.database.game.bo.FixedTimeMailBO;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TimerMailMgr
/*     */ {
/*  25 */   private static TimerMailMgr instance = new TimerMailMgr();
/*     */   
/*     */   public static TimerMailMgr getInstance() {
/*  28 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*  32 */   public List<FixedTimeMailBO> timerMailList = Lists.newArrayList();
/*     */   
/*     */   public void init() {
/*  35 */     this.timerMailList = BM.getBM(FixedTimeMailBO.class).findAll();
/*     */     
/*  37 */     SyncTaskManager.schedule(1000, () -> {
/*     */           check2sendMail();
/*     */           return true;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonArray timerMailList() {
/*  47 */     JsonArray mailArray = new JsonArray();
/*  48 */     for (FixedTimeMailBO bo : this.timerMailList) {
/*  49 */       if (bo.getBeginTime() + bo.getCyclesNum() * 86400 < CommTime.nowSecond()) {
/*     */         continue;
/*     */       }
/*  52 */       JsonObject mailObj = new JsonObject();
/*  53 */       mailObj.addProperty("mailid", Long.valueOf(bo.getMailId()));
/*  54 */       mailObj.addProperty("name", bo.getSenderName());
/*  55 */       mailObj.addProperty("title", bo.getTitle());
/*  56 */       mailObj.addProperty("content", bo.getContent());
/*  57 */       JsonArray itemArray = new JsonArray();
/*  58 */       if (bo.getUniformIDList() != null && bo.getUniformIDList().length() > 0) {
/*  59 */         List<Integer> unifromIds = StringUtils.string2Integer(bo.getUniformIDList());
/*  60 */         List<Integer> itemCounts = StringUtils.string2Integer(bo.getUniformCountList());
/*  61 */         for (int index = 0; index < unifromIds.size(); index++) {
/*  62 */           JsonObject itemObj = new JsonObject();
/*  63 */           itemObj.addProperty("uniformId", unifromIds.get(index));
/*  64 */           itemObj.addProperty("count", itemCounts.get(index));
/*  65 */           itemArray.add((JsonElement)itemObj);
/*     */         } 
/*     */       } 
/*  68 */       mailObj.add("item", (JsonElement)itemArray);
/*  69 */       mailObj.addProperty("begintime", CommTime.getTimeString(bo.getBeginTime()));
/*  70 */       mailObj.addProperty("cycles", Integer.valueOf(bo.getCyclesNum()));
/*  71 */       mailArray.add((JsonElement)mailObj);
/*     */     } 
/*  73 */     return mailArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long addTimerMail(JsonObject json) throws Exception {
/*  83 */     Reward reward = new Reward(HttpUtils.getJsonArray(json, "itemlist"));
/*     */     
/*  85 */     int cycles = HttpUtils.getInt(json, "cycles");
/*  86 */     if (cycles < 1) {
/*  87 */       cycles = 1;
/*     */     }
/*  89 */     FixedTimeMailBO bo = new FixedTimeMailBO();
/*  90 */     bo.setMailId(HttpUtils.getLong(json, "mailid"));
/*  91 */     bo.setSenderName(HttpUtils.getString(json, "name"));
/*  92 */     bo.setTitle(HttpUtils.getString(json, "title"));
/*  93 */     bo.setContent(HttpUtils.getString(json, "content"));
/*  94 */     bo.setUniformIDList(reward.uniformItemIds());
/*  95 */     bo.setUniformCountList(reward.uniformItemCounts());
/*  96 */     bo.setBeginTime(HttpUtils.getTime(json, "begintime"));
/*  97 */     bo.setCyclesNum(cycles);
/*  98 */     bo.insert();
/*  99 */     synchronized (this) {
/* 100 */       this.timerMailList.add(bo);
/*     */     } 
/* 102 */     return bo.getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delTimerMail(long mailId) {
/* 112 */     FixedTimeMailBO find = null;
/* 113 */     for (FixedTimeMailBO bo : this.timerMailList) {
/* 114 */       if (bo.getMailId() == mailId) {
/* 115 */         find = bo;
/*     */         break;
/*     */       } 
/*     */     } 
/* 119 */     if (find == null) {
/*     */       return;
/*     */     }
/* 122 */     synchronized (this) {
/* 123 */       this.timerMailList.remove(find);
/* 124 */       find.del();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void check2sendMail() {
/* 132 */     List<FixedTimeMailBO> list = null;
/* 133 */     synchronized (this) {
/* 134 */       list = Lists.newArrayList(this.timerMailList);
/*     */     } 
/* 136 */     int nowSecond = CommTime.nowSecond();
/*     */     
/* 138 */     for (FixedTimeMailBO bo : list) {
/* 139 */       int beginTime = bo.getBeginTime();
/*     */       
/* 141 */       if (beginTime > nowSecond) {
/*     */         continue;
/*     */       }
/* 144 */       int hasSendNum = bo.getHasSendNum();
/*     */       
/* 146 */       if (hasSendNum >= bo.getCyclesNum()) {
/*     */         continue;
/*     */       }
/* 149 */       if (beginTime + hasSendNum * 86400 > nowSecond) {
/*     */         continue;
/*     */       }
/*     */       
/* 153 */       bo.saveHasSendNum(hasSendNum + 1);
/*     */       
/* 155 */       MailCenter.getInstance().sendGlobalMail(bo.getSenderName(), bo.getTitle(), bo.getContent(), 0, 0, bo.getUniformIDList(), bo.getUniformCountList());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/gmmail/TimerMailMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */