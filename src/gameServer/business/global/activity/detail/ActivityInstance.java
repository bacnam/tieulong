/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.server.OpenSeverTime;
/*     */ 
/*     */ 
/*     */ public class ActivityInstance
/*     */   extends Activity
/*     */ {
/*     */   private Integer challengTimes;
/*     */   private Integer costId;
/*     */   private Integer costCount;
/*     */   private Integer gainId;
/*     */   private Integer gainCount;
/*     */   
/*     */   public ActivityInstance(ActivityBO data) {
/*  31 */     super(data);
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
/*  42 */     this.begin = 0;
/*  43 */     this.end = 0;
/*     */   }
/*     */   private Integer buyTimes; private RefInstanceInfo instanceref; private int begin; private int end; private int close;
/*     */   
/*     */   public static class RefInstanceInfo { String MapId;
/*     */     int Level;
/*     */     String Name;
/*     */     String BossName;
/*     */     String Icon;
/*     */     int Power;
/*     */     int BeginDialog;
/*     */     int EndDialog;
/*     */     String Wave1;
/*     */     String Wave2;
/*     */     String Wave3;
/*     */     String Wave4;
/*     */     String Wave5;
/*     */     
/*     */     private RefInstanceInfo(JsonObject json) {
/*  62 */       this.MapId = json.get("MapId").getAsString();
/*  63 */       this.Level = json.get("Level").getAsInt();
/*  64 */       this.Name = json.get("Name").getAsString();
/*  65 */       this.BossName = json.get("BossName").getAsString();
/*  66 */       this.Icon = json.get("Icon").getAsString();
/*  67 */       this.Power = json.get("Power").getAsInt();
/*  68 */       this.BeginDialog = json.get("BeginDialog").getAsInt();
/*  69 */       this.EndDialog = json.get("EndDialog").getAsInt();
/*  70 */       this.Wave1 = json.get("Wave1").getAsString();
/*  71 */       this.Wave2 = json.get("Wave2").getAsString();
/*  72 */       this.Wave3 = json.get("Wave3").getAsString();
/*  73 */       this.Wave4 = json.get("Wave4").getAsString();
/*  74 */       this.Wave5 = json.get("Wave5").getAsString();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  82 */     this.begin = json.get("begin").getAsInt();
/*  83 */     this.end = json.get("end").getAsInt();
/*  84 */     this.close = json.get("close").getAsInt();
/*  85 */     this.challengTimes = Integer.valueOf(json.get("challengTimes").getAsInt());
/*  86 */     this.costId = Integer.valueOf(json.get("costId").getAsInt());
/*  87 */     this.costCount = Integer.valueOf(json.get("costCount").getAsInt());
/*  88 */     this.gainId = Integer.valueOf(json.get("gainId").getAsInt());
/*  89 */     this.gainCount = Integer.valueOf(json.get("gainCount").getAsInt());
/*  90 */     this.buyTimes = Integer.valueOf(json.get("buyTimes").getAsInt());
/*  91 */     this.instanceref = new RefInstanceInfo(json.get("Ref").getAsJsonObject(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeginTime() {
/*  96 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndTime() {
/* 101 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.end;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCloseTime() {
/* 106 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.close;
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/* 111 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handDailyRefresh(Player player) {
/*     */     try {
/* 119 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/* 122 */       ActivityRecordBO bo = getRecord(player);
/* 123 */       if (bo == null) {
/*     */         return;
/*     */       }
/* 126 */       bo.setExtInt(0, 0);
/* 127 */       bo.setExtInt(1, 0);
/* 128 */       bo.saveAll();
/* 129 */       player.pushProto("activityInstanceRefresh", protoInfo(player));
/* 130 */     } catch (Exception e) {
/*     */       
/* 132 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class Response
/*     */   {
/*     */     int leftTimes;
/*     */     int leftBuyTimes;
/*     */     Reward costItem;
/*     */     Reward rewardItem;
/*     */     ActivityInstance.RefInstanceInfo ref;
/*     */     
/*     */     private Response(int leftTimes, int leftBuyTimes, Reward costItem, Reward rewardItem, ActivityInstance.RefInstanceInfo ref) {
/* 145 */       this.leftTimes = leftTimes;
/* 146 */       this.leftBuyTimes = leftBuyTimes;
/* 147 */       this.costItem = costItem;
/* 148 */       this.rewardItem = rewardItem;
/* 149 */       this.ref = ref;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Response protoInfo(Player player) {
/* 156 */     return new Response(getLeftTimes(player), getBuyTimes() - getBuyTimes(player), getCost(), getReward(), getRef(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 166 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 167 */     bo.setPid(player.getPid());
/* 168 */     bo.setAid(this.bo.getId());
/* 169 */     bo.setActivity(getType().toString());
/* 170 */     bo.setExtInt(0, 0);
/* 171 */     bo.setExtInt(1, 0);
/* 172 */     bo.insert();
/* 173 */     return bo;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 178 */     return ActivityType.ActivityInstance;
/*     */   }
/*     */   
/*     */   public int getLeftTimes(Player player) {
/* 182 */     return this.challengTimes.intValue() - getOrCreateRecord(player).getExtInt(0);
/*     */   }
/*     */   
/*     */   public int getBuyTimes(Player player) {
/* 186 */     return getOrCreateRecord(player).getExtInt(1);
/*     */   }
/*     */   
/*     */   public Reward getCost() {
/* 190 */     return new Reward(this.costId.intValue(), this.costCount.intValue());
/*     */   }
/*     */   
/*     */   public Reward getReward() {
/* 194 */     Reward reward = new Reward(this.gainId.intValue(), this.gainCount.intValue());
/* 195 */     return reward;
/*     */   }
/*     */   
/*     */   public RefInstanceInfo getRef() {
/* 199 */     return this.instanceref;
/*     */   }
/*     */   
/*     */   public int getBuyTimes() {
/* 203 */     return this.buyTimes.intValue();
/*     */   }
/*     */   
/*     */   public Reward instanceWin(Player player) {
/* 207 */     if (getStatus() != ActivityStatus.Open) {
/* 208 */       return null;
/*     */     }
/* 210 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 211 */     if (bo.getExtInt(0) >= this.challengTimes.intValue()) {
/* 212 */       return null;
/*     */     }
/* 214 */     if (!((PlayerItem)player.getFeature(PlayerItem.class)).checkAndConsume(getCost(), ItemFlow.ActivityInstance)) {
/* 215 */       return null;
/*     */     }
/* 217 */     Reward reward = getReward();
/* 218 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.ActivityInstance);
/* 219 */     bo.setExtInt(0, bo.getExtInt(0) + 1);
/* 220 */     bo.saveAll();
/*     */     
/* 222 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void buyTimes(Player player) {
/* 228 */     if (getBuyTimes(player) >= getBuyTimes()) {
/*     */       return;
/*     */     }
/*     */     
/* 232 */     RefCrystalPrice prize = RefCrystalPrice.getPrize(getBuyTimes(player));
/* 233 */     int count = prize.ActivityInstanceAddChallenge;
/* 234 */     PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
/* 235 */     if (!currency.check(PrizeType.Crystal, count)) {
/*     */       return;
/*     */     }
/* 238 */     currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);
/* 239 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 240 */     bo.saveExtInt(0, bo.getExtInt(0) - 1);
/* 241 */     bo.saveExtInt(1, bo.getExtInt(1) + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 247 */     clearActRecord();
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/ActivityInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */