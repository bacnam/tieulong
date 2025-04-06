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
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FirstReward
/*     */   extends Activity
/*     */ {
/*     */   public List<DayReward> firstReward;
/*     */   
/*     */   public FirstReward(ActivityBO data) {
/*  30 */     super(data);
/*     */ 
/*     */     
/*  33 */     this.firstReward = new ArrayList<>();
/*     */   }
/*     */   
/*     */   private static class DayReward {
/*     */     int id;
/*     */     int value;
/*     */     int power;
/*     */     Reward reward;
/*     */     
/*     */     private DayReward(JsonObject json) throws WSException {
/*  43 */       this.id = json.get("aid").getAsInt();
/*  44 */       this.value = json.get("value").getAsInt();
/*  45 */       this.power = json.get("power").getAsInt();
/*  46 */       this.reward = new Reward(json.get("awards").getAsJsonArray());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  54 */     for (JsonElement element : json.get("rewards").getAsJsonArray()) {
/*  55 */       JsonObject obj = element.getAsJsonObject();
/*  56 */       DayReward builder = new DayReward(obj, null);
/*  57 */       this.firstReward.add(builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  63 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  68 */     return ActivityType.FirstReward;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needNotify(Player player) {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAllRecharge() {
/*  80 */     clearActRecord();
/*  81 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  82 */       player.pushProto("clearFirstReward", this.firstReward);
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
/*  94 */     ActivityRecordBO bo = getRecord(player);
/*  95 */     if (bo == null) {
/*  96 */       return true;
/*     */     }
/*  98 */     return (bo.getExtInt(0) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public fristRewardProto fristRechargeProto(Player player) {
/* 108 */     fristRewardProto proto = new fristRewardProto(null);
/* 109 */     RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/* 110 */     proto.isFirstRecharge = (isFirstRecharge(player) && !rechargeFeature.isRecharged());
/* 111 */     proto.firstReward = this.firstReward;
/* 112 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 113 */     int times = bo.getExtInt(2);
/* 114 */     proto.isOver = (times >= this.firstReward.size());
/*     */     
/* 116 */     return proto;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class fristRewardProto
/*     */   {
/*     */     boolean isFirstRecharge;
/*     */     
/*     */     List<FirstReward.DayReward> firstReward;
/*     */     boolean isOver;
/*     */     
/*     */     private fristRewardProto() {}
/*     */   }
/*     */   
/*     */   public void setFirstReward(Player player) {
/* 131 */     synchronized (this) {
/* 132 */       boolean isFirstRecharge = isFirstRecharge(player);
/* 133 */       if (!isFirstRecharge) {
/*     */         return;
/*     */       }
/* 136 */       ActivityRecordBO bo = getOrCreateRecord(player);
/* 137 */       bo.setExtInt(0, 1);
/* 138 */       bo.setExtInt(1, CommTime.nowSecond());
/* 139 */       bo.saveAll();
/*     */ 
/*     */       
/* 142 */       player.pushProto("firstThreeReward", Boolean.valueOf(isFirstRecharge(player)));
/*     */     } 
/*     */     
/* 145 */     sendFirstReward(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendFirstReward(Player player) {
/*     */     try {
/* 153 */       synchronized (this) {
/* 154 */         boolean isFirstRecharge = isFirstRecharge(player);
/* 155 */         if (isFirstRecharge) {
/*     */           return;
/*     */         }
/* 158 */         ActivityRecordBO bo = getOrCreateRecord(player);
/* 159 */         int times = bo.getExtInt(2);
/*     */         
/* 161 */         if (times > this.firstReward.size() - 1) {
/*     */           return;
/*     */         }
/* 164 */         Reward reward = getReward(times + 1);
/* 165 */         if (reward == null)
/*     */           return; 
/* 167 */         bo.saveExtInt(2, times + 1);
/*     */         
/* 169 */         MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward.uniformItemIds(), 
/* 170 */             reward.uniformItemCounts());
/*     */       } 
/* 172 */     } catch (Exception e) {
/*     */       
/* 174 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Reward getReward(int times) {
/* 179 */     for (DayReward day : this.firstReward) {
/* 180 */       if (day.id == times) {
/* 181 */         return day.reward;
/*     */       }
/*     */     } 
/* 184 */     return null;
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/FirstReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */