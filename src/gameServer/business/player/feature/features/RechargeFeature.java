/*     */ package business.player.feature.features;
/*     */ 
/*     */ import business.global.recharge.RechargeMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.database.game.bo.PlayerRechargeInfoBO;
/*     */ import core.database.game.bo.PlayerRechargeRecordBO;
/*     */ import core.database.game.bo.RechargeOrderBO;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RechargeFeature
/*     */   extends Feature
/*     */ {
/*     */   public Map<String, PlayerRechargeRecordBO> rechargeRecords;
/*     */   public PlayerRechargeInfoBO rechargeInfo;
/*     */   
/*     */   public RechargeFeature(Player owner) {
/*  30 */     super(owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  41 */     List<PlayerRechargeRecordBO> recordBOs = BM.getBM(PlayerRechargeRecordBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  42 */     this.rechargeRecords = new HashMap<>();
/*  43 */     for (PlayerRechargeRecordBO r : recordBOs) {
/*  44 */       this.rechargeRecords.put(r.getGoodsID(), r);
/*     */     }
/*     */     
/*  47 */     this.rechargeInfo = (PlayerRechargeInfoBO)BM.getBM(PlayerRechargeInfoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */   
/*     */   public PlayerRechargeRecordBO getRecharged(String goodsid) {
/*  51 */     return this.rechargeRecords.get(goodsid);
/*     */   }
/*     */   
/*     */   public Collection<PlayerRechargeRecordBO> getRecharged() {
/*  55 */     return this.rechargeRecords.values();
/*     */   }
/*     */   
/*     */   public boolean isRecharged() {
/*  59 */     for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
/*  60 */       if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/*     */         continue;
/*     */       }
/*  63 */       PlayerRechargeRecordBO record = this.rechargeRecords.get(ref.id);
/*  64 */       if (record != null && record.getResetSign().equals(RechargeMgr.getInstance().getResetSign(ref.id))) {
/*  65 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  70 */     return false;
/*     */   }
/*     */   
/*     */   public void recordRecharge(RechargeOrderBO orderBO, String resetsign) {
/*  74 */     PlayerRechargeRecordBO bo = this.rechargeRecords.get(orderBO.getProductid());
/*  75 */     if (bo != null) {
/*  76 */       bo.setBuyCount(bo.getBuyCount() + orderBO.getQuantity());
/*  77 */       bo.setLastBuyTime(CommTime.nowSecond());
/*  78 */       bo.saveResetSign(resetsign);
/*  79 */       bo.saveAll();
/*     */     } else {
/*  81 */       bo = new PlayerRechargeRecordBO();
/*  82 */       bo.setPid(this.player.getPid());
/*  83 */       bo.setGoodsID(orderBO.getProductid());
/*  84 */       bo.setBuyCount(bo.getBuyCount() + orderBO.getQuantity());
/*  85 */       bo.setLastBuyTime(CommTime.nowSecond());
/*  86 */       bo.setResetSign(resetsign);
/*  87 */       bo.insert();
/*  88 */       this.rechargeRecords.put(orderBO.getProductid(), bo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getRebateRemains(Achievement.AchievementType rebateAchievement) {
/*  93 */     if (this.rechargeInfo == null)
/*  94 */       return 0; 
/*  95 */     if (rebateAchievement == Achievement.AchievementType.MonthCardCrystal)
/*  96 */       return this.rechargeInfo.getMonthCardRemains(); 
/*  97 */     if (rebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/*  98 */       return this.rechargeInfo.getPermantCardRemains();
/*     */     }
/* 100 */     throw new UnsupportedOperationException("暂不支持月卡或至尊卡以外的月卡类型");
/*     */   }
/*     */ 
/*     */   
/*     */   public void desRebateRemains(int day) {
/*     */     try {
/* 106 */       if (this.rechargeInfo == null) {
/*     */         return;
/*     */       }
/* 109 */       this.rechargeInfo.saveMonthCardRemains(Math.max(0, this.rechargeInfo.getMonthCardRemains() - day));
/* 110 */     } catch (Exception e) {
/*     */       
/* 112 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void incRebateRemains(Achievement.AchievementType rebateAchievement, int rebateDays) {
/* 117 */     if (this.rechargeInfo == null) {
/* 118 */       synchronized (this) {
/* 119 */         if (this.rechargeInfo == null) {
/* 120 */           this.rechargeInfo = new PlayerRechargeInfoBO();
/* 121 */           this.rechargeInfo.setPid(this.player.getPid());
/* 122 */           this.rechargeInfo.insert();
/*     */         } 
/*     */       } 
/*     */     }
/* 126 */     if (rebateAchievement == Achievement.AchievementType.MonthCardCrystal) {
/* 127 */       this.rechargeInfo.saveMonthCardRemains(this.rechargeInfo.getMonthCardRemains() + rebateDays);
/*     */     }
/* 129 */     else if (rebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/* 130 */       this.rechargeInfo.savePermantCardRemains(rebateDays);
/*     */     } else {
/* 132 */       throw new UnsupportedOperationException("暂不支持月卡或至尊卡以外的月卡类型");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/RechargeFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */