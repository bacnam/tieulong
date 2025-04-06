/*     */ package business.player.feature.pve;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.InstanceType;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefCrystalPrice;
/*     */ import core.config.refdata.ref.RefInstance;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ import core.database.game.bo.InstanceInfoBO;
/*     */ import java.util.List;
/*     */ 
/*     */ public class InstanceFeature extends Feature {
/*     */   private InstanceInfoBO instanceInfo;
/*     */   
/*     */   public InstanceFeature(Player player) {
/*  27 */     super(player);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  34 */     this.instanceInfo = (InstanceInfoBO)BM.getBM(InstanceInfoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
/*     */   }
/*     */   
/*     */   public InstanceInfoBO getOrCreate() {
/*  38 */     InstanceInfoBO bo = this.instanceInfo;
/*  39 */     if (bo != null) {
/*  40 */       return bo;
/*     */     }
/*  42 */     synchronized (this) {
/*  43 */       bo = this.instanceInfo;
/*  44 */       if (bo != null) {
/*  45 */         return bo;
/*     */       }
/*  47 */       bo = new InstanceInfoBO();
/*  48 */       bo.setPid(this.player.getPid());
/*  49 */       bo.setInstanceMaxLevelAll(0); byte b; int i; InstanceType[] arrayOfInstanceType;
/*  50 */       for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) { InstanceType type = arrayOfInstanceType[b];
/*  51 */         if (type == InstanceType.EquipInstance) {
/*  52 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).EquipInstanceTimes);
/*     */         }
/*  54 */         if (type == InstanceType.MeridianInstance) {
/*  55 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).MeridianInstanceTimes);
/*     */         }
/*  57 */         if (type == InstanceType.GemInstance) {
/*  58 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).GemInstanceTimes);
/*     */         }
/*  60 */         if (type == InstanceType.GuaidInstance)
/*  61 */           bo.setChallengTimes(type.ordinal(), RefDataMgr.getFactor("GuaidInstanceChallengTimes", 3)); 
/*     */         b++; }
/*     */       
/*  64 */       bo.insert();
/*  65 */       this.instanceInfo = bo;
/*     */     } 
/*  67 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRefresh() {
/*     */     try {
/*  79 */       InstanceInfoBO bo = getOrCreate(); byte b; int i; InstanceType[] arrayOfInstanceType;
/*  80 */       for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) { InstanceType type = arrayOfInstanceType[b];
/*  81 */         if (type == InstanceType.EquipInstance) {
/*  82 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).EquipInstanceTimes);
/*     */         }
/*  84 */         if (type == InstanceType.MeridianInstance) {
/*  85 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).MeridianInstanceTimes);
/*     */         }
/*  87 */         if (type == InstanceType.GemInstance)
/*  88 */           bo.setChallengTimes(type.ordinal(), ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).GemInstanceTimes); 
/*     */         b++; }
/*     */       
/*  91 */       bo.saveAll();
/*  92 */     } catch (Exception e) {
/*     */       
/*  94 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int fightInstance(int reqLevel, InstanceType type) throws WSException {
/*  99 */     int level = getOrCreate().getInstanceMaxLevel(type.ordinal());
/* 100 */     if (reqLevel > level + 1) {
/* 101 */       throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
/*     */     }
/* 103 */     if (getOrCreate().getChallengTimes(type.ordinal()) <= 0) {
/* 104 */       throw new WSException(ErrorCode.Instance_NotEounghTimes, "挑战次数不够");
/*     */     }
/* 106 */     if (reqLevel > ((List)RefInstance.instanceMap.get(type)).size()) {
/* 107 */       throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
/*     */     }
/* 109 */     RefInstance ref = ((List<RefInstance>)RefInstance.instanceMap.get(type)).get(reqLevel - 1);
/* 110 */     if (ref != null && ref.Material != 0) {
/* 111 */       PrizeType prizeType = null;
/* 112 */       switch (type) {
/*     */         case null:
/* 114 */           prizeType = PrizeType.EquipInstanceMaterial;
/*     */           break;
/*     */         case MeridianInstance:
/* 117 */           prizeType = PrizeType.MeridianInstanceMaterial;
/*     */           break;
/*     */         case GemInstance:
/* 120 */           prizeType = PrizeType.GemInstanceMaterial;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 126 */       PlayerCurrency playerCurrency = (PlayerCurrency)this.player.getFeature(PlayerCurrency.class);
/* 127 */       if (!playerCurrency.check(prizeType, ref.Material)) {
/* 128 */         throw new WSException(ErrorCode.Instance_NotEounghMaterial, "材料不够");
/*     */       }
/* 130 */       playerCurrency.consume(prizeType, ref.Material, ItemFlow.Instance);
/*     */     } 
/* 132 */     getOrCreate().saveChallengTimes(type.ordinal(), getOrCreate().getChallengTimes(type.ordinal()) - 1);
/*     */     
/* 134 */     return getOrCreate().getChallengTimes(type.ordinal());
/*     */   }
/*     */   
/*     */   public Reward getReward(int reqLevel, boolean sweep, InstanceType type) throws WSException {
/* 138 */     int level = getOrCreate().getInstanceMaxLevel(type.ordinal());
/* 139 */     if (reqLevel > ((List)RefInstance.instanceMap.get(type)).size()) {
/* 140 */       throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
/*     */     }
/*     */     
/* 143 */     if (reqLevel > level + 1) {
/* 144 */       throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
/*     */     }
/*     */     
/* 147 */     if (reqLevel == level + 1) {
/* 148 */       if (sweep) {
/* 149 */         throw new WSException(ErrorCode.Instance_NotPassed, "副本未通关");
/*     */       }
/* 151 */       getOrCreate().saveInstanceMaxLevel(type.ordinal(), level + 1);
/*     */     } 
/* 153 */     RefInstance ref = ((List<RefInstance>)RefInstance.instanceMap.get(type)).get(reqLevel - 1);
/* 154 */     RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId));
/* 155 */     Reward reward = null;
/* 156 */     if (refReward != null) {
/* 157 */       reward = refReward.genReward();
/* 158 */       ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.InstanceWin);
/*     */     } 
/*     */     
/* 161 */     return reward;
/*     */   }
/*     */   public String buyChallengTimes(InstanceType type, int buytimes) throws WSException {
/*     */     int i;
/* 165 */     ConstEnum.DailyRefresh dailyType = null;
/* 166 */     PlayerRecord recorder = (PlayerRecord)this.player.getFeature(PlayerRecord.class);
/* 167 */     PlayerCurrency currency = (PlayerCurrency)this.player.getFeature(PlayerCurrency.class);
/* 168 */     RefCrystalPrice prize = null;
/* 169 */     int count = 0;
/* 170 */     int times = 0;
/*     */     
/* 172 */     switch (type) {
/*     */       case null:
/* 174 */         dailyType = ConstEnum.DailyRefresh.EquipInstanceBuyTimes;
/* 175 */         times = recorder.getValue(dailyType);
/* 176 */         for (i = 0; i < buytimes; i++) {
/* 177 */           prize = RefCrystalPrice.getPrize(times + i);
/* 178 */           count += prize.EquipInstanceAddChallenge;
/*     */         } 
/*     */         break;
/*     */       case GemInstance:
/* 182 */         dailyType = ConstEnum.DailyRefresh.GemInstanceBuyTimes;
/* 183 */         times = recorder.getValue(dailyType);
/* 184 */         for (i = 0; i < buytimes; i++) {
/* 185 */           prize = RefCrystalPrice.getPrize(times + i);
/* 186 */           count += prize.EquipInstanceAddChallenge;
/*     */         } 
/*     */         break;
/*     */       case MeridianInstance:
/* 190 */         dailyType = ConstEnum.DailyRefresh.MeridianInstanceBuyTimes;
/* 191 */         times = recorder.getValue(dailyType);
/* 192 */         for (i = 0; i < buytimes; i++) {
/* 193 */           prize = RefCrystalPrice.getPrize(times + i);
/* 194 */           count += prize.EquipInstanceAddChallenge;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 202 */     if (!currency.check(PrizeType.Crystal, count)) {
/* 203 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", new Object[] { Integer.valueOf(count) });
/*     */     }
/* 205 */     currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);
/*     */     
/* 207 */     recorder.addValue(dailyType, buytimes);
/* 208 */     getOrCreate().saveChallengTimes(type.ordinal(), getOrCreate().getChallengTimes(type.ordinal()) + buytimes);
/* 209 */     return String.valueOf(getOrCreate().getChallengTimes(type.ordinal())) + ";" + recorder.getValue(dailyType);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/pve/InstanceFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */