/*     */ package business.player.feature.features;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import core.database.game.bo.PlayerRecordBO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerRecord
/*     */   extends Feature
/*     */ {
/*     */   private PlayerRecordBO bo;
/*     */   
/*     */   public PlayerRecord(Player owner) {
/*  19 */     super(owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  26 */     this.bo = (PlayerRecordBO)BM.getBM(PlayerRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerRecordBO getOrCreate() {
/*  35 */     PlayerRecordBO bo = this.bo;
/*  36 */     if (bo != null) {
/*  37 */       return bo;
/*     */     }
/*  39 */     synchronized (this) {
/*  40 */       bo = new PlayerRecordBO();
/*  41 */       bo.setPid(this.player.getPid());
/*  42 */       bo.insert();
/*  43 */       this.bo = bo;
/*     */     } 
/*  45 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getValue(ConstEnum.DailyRefresh index) {
/*  55 */     if (index == ConstEnum.DailyRefresh.None) {
/*  56 */       return -1;
/*     */     }
/*  58 */     PlayerRecordBO dr = getOrCreate();
/*  59 */     return dr.getValue(index.ordinal());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addValue(ConstEnum.DailyRefresh index) {
/*  68 */     addValue(index, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addValue(ConstEnum.DailyRefresh index, int value) {
/*  78 */     if (index == ConstEnum.DailyRefresh.None) {
/*     */       return;
/*     */     }
/*  81 */     setValue(index, getValue(index) + value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setValue(ConstEnum.DailyRefresh index, int value) {
/*  91 */     if (value < 0) {
/*     */       return;
/*     */     }
/*  94 */     PlayerRecordBO record = getOrCreate();
/*     */     
/*  96 */     record.saveValue(index.ordinal(), value);
/*     */     
/*  98 */     notifyValue(record, index.ordinal(), value);
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
/*     */   public synchronized void clearValue8() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clearValue0() {
/*     */     try {
/* 122 */       PlayerRecordBO record = getOrCreate(); byte b; int i; ConstEnum.DailyRefresh[] arrayOfDailyRefresh;
/* 123 */       for (i = (arrayOfDailyRefresh = ConstEnum.DailyRefresh.values()).length, b = 0; b < i; ) { ConstEnum.DailyRefresh dr = arrayOfDailyRefresh[b];
/* 124 */         switch (dr) {
/*     */           case PackageBuyTimes:
/*     */             break;
/*     */           default:
/* 128 */             record.setValue(dr.ordinal(), 0); break;
/*     */         } 
/*     */         b++; }
/*     */       
/* 132 */       record.saveAll();
/* 133 */     } catch (Exception e) {
/*     */       
/* 135 */       e.printStackTrace();
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
/*     */   public void notifyValue(PlayerRecordBO dr, int index, int value) {
/* 149 */     this.player.pushProto("refreshDailyValue", String.valueOf(index) + ";" + value);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/PlayerRecord.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */