/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FirstRecharge
/*     */   extends Activity
/*     */ {
/*     */   public Reward firstReward;
/*     */   
/*     */   public FirstRecharge(ActivityBO data) {
/*  26 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  33 */     this.firstReward = new Reward(json.get("awards").getAsJsonArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  38 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  43 */     return ActivityType.FirstRecharge;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needNotify(Player player) {
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearAllRecharge() {
/*  55 */     clearActRecord();
/*  56 */     for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  57 */       player.pushProto("clearFirstReward", this.firstReward);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFirstRecharge(Player player) {
/*  68 */     ActivityRecordBO bo = getRecord(player);
/*  69 */     if (bo == null) {
/*  70 */       return true;
/*     */     }
/*  72 */     return (bo.getExtInt(0) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fristRechargeProto(Player player) {
/*  82 */     return isFirstRecharge(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendFirstRechargeReward(Player player) {
/*  89 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     synchronized (this) {
/*  94 */       boolean isFirstRecharge = isFirstRecharge(player);
/*  95 */       if (!isFirstRecharge) {
/*     */         return;
/*     */       }
/*  98 */       ActivityRecordBO bo = getOrCreateRecord(player);
/*  99 */       bo.setExtInt(0, 1);
/* 100 */       bo.setExtInt(1, CommTime.nowSecond());
/* 101 */       bo.saveAll();
/*     */       
/* 103 */       MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, this.firstReward.uniformItemIds(), 
/* 104 */           this.firstReward.uniformItemCounts());
/*     */       
/* 106 */       player.pushProto("firstReward", this.firstReward);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/FirstRecharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */