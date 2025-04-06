/*     */ package business.global.activity;
/*     */ 
/*     */ import business.player.Player;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefActivity;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import proto.gameworld.ActivityInfo;
/*     */ 
/*     */ public abstract class Activity {
/*     */   public ActivityBO bo;
/*     */   public RefActivity ref;
/*     */   
/*     */   public abstract void load(JsonObject paramJsonObject) throws WSException;
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  30 */     return null;
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
/*     */   
/*  45 */   public ActivityType type = null; public Map<Long, ActivityRecordBO> playerActRecords;
/*     */   
/*     */   public abstract void onOpen();
/*     */   
/*     */   public Activity(ActivityBO bo) {
/*  50 */     this.bo = bo;
/*  51 */     if (bo != null)
/*  52 */       this.ref = (RefActivity)RefDataMgr.get(RefActivity.class, ActivityType.valueOf(bo.getActivity())); 
/*  53 */     this.playerActRecords = new HashMap<>();
/*     */   } public abstract void onEnd();
/*     */   public abstract void onClosed();
/*     */   public void load() throws Exception {
/*  57 */     if (!this.bo.getIsActive()) {
/*     */       return;
/*     */     }
/*  60 */     JsonObject jsonObject = (new JsonParser()).parse(this.bo.getJson()).getAsJsonObject();
/*  61 */     load(jsonObject);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String checkAndSubscribeItem(JsonArray itemArray) throws RequestException {
/*  72 */     String result = "";
/*  73 */     for (JsonElement itemElement : itemArray) {
/*  74 */       JsonObject itemObj = itemElement.getAsJsonObject();
/*  75 */       int uniformId = itemObj.get("uniformId").getAsInt();
/*  76 */       RefUniformItem refUniformItem = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId));
/*  77 */       if (refUniformItem == null) {
/*  78 */         throw new RequestException(4000001, String.format("UniformId=%s的物品不存在", new Object[] { Integer.valueOf(uniformId) }), new Object[0]);
/*     */       }
/*  80 */       int count = itemObj.get("count").getAsInt();
/*  81 */       if (count <= 0) {
/*  82 */         throw new RequestException(4000001, String.format("UniformId=%s的物品数量小于0", new Object[] { Integer.valueOf(uniformId) }), new Object[0]);
/*     */       }
/*  84 */       result = String.valueOf(result) + "名称:" + refUniformItem.Name + "," + "数量:" + count + ";";
/*     */     } 
/*  86 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearActRecord() {
/*  93 */     BM.getBM(ActivityRecordBO.class).delAll("aid", Long.valueOf(this.bo.getId()));
/*  94 */     this.playerActRecords.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getId() {
/* 103 */     return this.bo.getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActNo() {
/* 112 */     return this.ref.ActNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityStatus getStatus() {
/* 121 */     if (!this.bo.getIsActive()) {
/* 122 */       return ActivityStatus.Inactive;
/*     */     }
/* 124 */     int now = CommTime.nowSecond();
/* 125 */     if (now < getBeginTime()) {
/* 126 */       return ActivityStatus.Close;
/*     */     }
/* 128 */     if (getCloseTime() != 0 && now >= getCloseTime()) {
/* 129 */       return ActivityStatus.Close;
/*     */     }
/* 131 */     if (getEndTime() != 0 && now >= getEndTime()) {
/* 132 */       return ActivityStatus.End;
/*     */     }
/* 134 */     return ActivityStatus.Open;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 143 */     ActivityStatus status = getStatus();
/* 144 */     if (status == ActivityStatus.Open) {
/* 145 */       return true;
/*     */     }
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 156 */     ActivityStatus status = getStatus();
/* 157 */     if (status == ActivityStatus.Inactive || status == ActivityStatus.Close) {
/* 158 */       return true;
/*     */     }
/* 160 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRecvReward() {
/* 169 */     ActivityStatus status = getStatus();
/* 170 */     if (status == ActivityStatus.Open || status == ActivityStatus.End) {
/* 171 */       return true;
/*     */     }
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract ActivityType getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean needNotify(Player player) {
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBeginTime() {
/* 200 */     return this.bo.getBeginTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndTime() {
/* 209 */     return this.bo.getEndTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCloseTime() {
/* 218 */     return this.bo.getCloseTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityInfo activitySummary(Player player) {
/* 227 */     ActivityInfo builder = new ActivityInfo();
/* 228 */     builder.setType(getType());
/* 229 */     builder.setStatus(getStatus());
/* 230 */     builder.setBeginTime(getBeginTime());
/* 231 */     builder.setEndTime(getEndTime());
/* 232 */     builder.setCloseTime(getCloseTime());
/* 233 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO getRecord(Player player) {
/* 243 */     return this.playerActRecords.get(Long.valueOf(player.getPid()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ActivityRecordBO getOrCreateRecord(Player player) {
/* 253 */     ActivityRecordBO bo = this.playerActRecords.get(Long.valueOf(player.getPid()));
/* 254 */     if (bo == null) {
/* 255 */       synchronized (this) {
/* 256 */         bo = this.playerActRecords.get(Long.valueOf(player.getPid()));
/* 257 */         if (bo != null) {
/* 258 */           return bo;
/*     */         }
/* 260 */         this.playerActRecords.put(Long.valueOf(player.getPid()), bo = createPlayerActRecord(player));
/*     */       } 
/*     */     }
/* 263 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 273 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 274 */     bo.setPid(player.getPid());
/* 275 */     bo.setAid(this.bo.getId());
/* 276 */     bo.setActivity(getType().toString());
/* 277 */     bo.insert();
/* 278 */     return bo;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/Activity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */