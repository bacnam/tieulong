/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.database.game.bo.PlayerRechargeRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NewFirstReward
/*     */   extends Activity
/*     */ {
/*     */   public List<DayReward> firstReward;
/*     */   
/*     */   public NewFirstReward(ActivityBO data) {
/*  34 */     super(data);
/*     */ 
/*     */     
/*  37 */     this.firstReward = new ArrayList<>();
/*     */   }
/*     */   
/*     */   private static class DayReward {
/*     */     int id;
/*     */     int value;
/*     */     int power;
/*     */     Reward reward;
/*     */     
/*     */     private DayReward(JsonObject json) throws WSException {
/*  47 */       this.id = json.get("aid").getAsInt();
/*  48 */       this.value = json.get("value").getAsInt();
/*  49 */       this.power = json.get("power").getAsInt();
/*  50 */       this.reward = new Reward(json.get("awards").getAsJsonArray());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  58 */     for (JsonElement element : json.get("rewards").getAsJsonArray()) {
/*  59 */       JsonObject obj = element.getAsJsonObject();
/*  60 */       DayReward builder = new DayReward(obj, null);
/*  61 */       this.firstReward.add(builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  67 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  72 */     return ActivityType.NewFirstReward;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needNotify(Player player) {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAllRecharge() {
/*  84 */     clearActRecord();
/*  85 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  86 */       player.pushProto("clearFirstReward", this.firstReward);
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
/*     */   public boolean isFirstRecharge(Player player) {
/*  98 */     ActivityRecordBO bo = getRecord(player);
/*  99 */     if (bo == null) {
/* 100 */       return true;
/*     */     }
/* 102 */     return (bo.getExtInt(0) == 0);
/*     */   }
/*     */   
/*     */   public void dailyRefesh(Player player, int days) {
/* 106 */     if (isFirstRecharge(player)) {
/*     */       return;
/*     */     }
/* 109 */     ActivityRecordBO bo = getRecord(player);
/*     */     
/* 111 */     if (bo.getExtInt(2) > this.firstReward.size() - 1) {
/*     */       return;
/*     */     }
/*     */     
/* 115 */     if (bo.getExtInt(2) == this.firstReward.size() - 1) {
/* 116 */       bo.saveExtInt(2, bo.getExtInt(2) + days);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 121 */     resetRecharge(player);
/*     */     
/* 123 */     bo.saveExtInt(3, 0);
/* 124 */     bo.saveExtInt(2, bo.getExtInt(2) + days);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetRecharge(Player player) {
/* 129 */     for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
/* 130 */       if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/*     */         continue;
/*     */       }
/* 133 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/*     */       
/* 135 */       PlayerRechargeRecordBO record = rechargeFeature.getRecharged(ref.id);
/* 136 */       if (record != null) {
/* 137 */         record.saveResetSign(String.valueOf(CommTime.nowSecond()) + "+" + player.getPid());
/*     */       }
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
/*     */   public fristRewardProto fristRechargeProto(Player player) {
/* 150 */     fristRewardProto proto = new fristRewardProto(null);
/* 151 */     proto.isFirstRecharge = isFirstRecharge(player);
/* 152 */     proto.firstReward = this.firstReward;
/* 153 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 154 */     int times = bo.getExtInt(2);
/* 155 */     proto.times = times;
/* 156 */     proto.isOver = (times > this.firstReward.size() - 1);
/* 157 */     proto.todayRecharge = bo.getExtInt(3);
/*     */     
/* 159 */     return proto;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class fristRewardProto
/*     */   {
/*     */     boolean isFirstRecharge;
/*     */     
/*     */     List<NewFirstReward.DayReward> firstReward;
/*     */     int times;
/*     */     boolean isOver;
/*     */     int todayRecharge;
/*     */     
/*     */     private fristRewardProto() {}
/*     */   }
/*     */   
/*     */   public void setFirstReward(Player player) {
/* 176 */     boolean isFirstRecharge = isFirstRecharge(player);
/* 177 */     if (isFirstRecharge) {
/* 178 */       ActivityRecordBO bo = getOrCreateRecord(player);
/* 179 */       bo.setExtInt(0, 1);
/* 180 */       bo.setExtInt(1, CommTime.nowSecond());
/* 181 */       bo.saveAll();
/*     */       
/* 183 */       resetRecharge(player);
/*     */       
/* 185 */       player.pushProto("firstThreeReward", Boolean.valueOf(isFirstRecharge(player)));
/*     */     } 
/* 187 */     sendFirstReward(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendFirstReward(Player player) {
/*     */     try {
/* 195 */       synchronized (this) {
/* 196 */         boolean isFirstRecharge = isFirstRecharge(player);
/* 197 */         if (isFirstRecharge) {
/*     */           return;
/*     */         }
/* 200 */         ActivityRecordBO bo = getOrCreateRecord(player);
/* 201 */         if (bo.getExtInt(3) != 0) {
/*     */           return;
/*     */         }
/* 204 */         int times = bo.getExtInt(2);
/* 205 */         Reward reward = getReward(times + 1);
/* 206 */         if (reward == null)
/*     */           return; 
/* 208 */         bo.setExtInt(3, 1);
/*     */         
/* 210 */         MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward.uniformItemIds(), 
/* 211 */             reward.uniformItemCounts());
/*     */       } 
/* 213 */     } catch (Exception e) {
/*     */       
/* 215 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Reward getReward(int times) {
/* 220 */     for (DayReward day : this.firstReward) {
/* 221 */       if (day.id == times) {
/* 222 */         return day.reward;
/*     */       }
/*     */     } 
/* 225 */     return null;
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/NewFirstReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */