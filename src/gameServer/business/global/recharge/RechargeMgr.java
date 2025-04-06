/*     */ package business.global.recharge;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.AccumRecharge;
/*     */ import business.global.activity.detail.AccumRechargeDay;
/*     */ import business.global.activity.detail.DailyRecharge;
/*     */ import business.global.activity.detail.FirstRecharge;
/*     */ import business.global.activity.detail.FortuneWheel;
/*     */ import business.global.activity.detail.NewFirstReward;
/*     */ import business.global.activity.detail.RedPacket;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.AccountFeature;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.features.RechargeFeature;
/*     */ import business.player.feature.player.TitleFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.enums.Title;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.database.game.bo.AccountBO;
/*     */ import core.database.game.bo.PlayerRechargeRecordBO;
/*     */ import core.database.game.bo.RechargeOrderBO;
/*     */ import core.database.game.bo.RechargeResetBO;
/*     */ import core.logger.flow.FlowLogger;
/*     */ import core.logger.flow.TDRechargeLogger;
/*     */ import core.network.proto.Player;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RechargeMgr
/*     */ {
/*     */   public enum OrderStatus
/*     */   {
/*  49 */     Paid,
/*  50 */     Delivered,
/*  51 */     Cancled;
/*     */   }
/*     */   
/*  54 */   private static RechargeMgr instance = new RechargeMgr();
/*     */   
/*     */   public static RechargeMgr getInstance() {
/*  57 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Long, RechargeOrderBO> cachedOrders;
/*     */   public Map<String, RechargeResetBO> resets;
/*     */   
/*     */   public boolean init() {
/*  65 */     List<RechargeOrderBO> paid = BM.getBM(RechargeOrderBO.class).findAll("status", OrderStatus.Paid.toString());
/*  66 */     this.cachedOrders = new HashMap<>();
/*  67 */     for (RechargeOrderBO o : paid) {
/*  68 */       this.cachedOrders.put(Long.valueOf(o.getPid()), o);
/*     */     }
/*  70 */     List<RechargeResetBO> resetbos = BM.getBM(RechargeResetBO.class).findAll();
/*  71 */     this.resets = new HashMap<>();
/*  72 */     for (RechargeResetBO o : resetbos) {
/*  73 */       this.resets.put(o.getGoodsid(), o);
/*     */     }
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public void cacheOrder(RechargeOrderBO orderBO) {
/*  79 */     this.cachedOrders.put(Long.valueOf(orderBO.getPid()), orderBO);
/*     */   }
/*     */   
/*     */   public void trySendCachedOrder(long cid) {
/*  83 */     if (!this.cachedOrders.containsKey(Long.valueOf(cid))) {
/*     */       return;
/*     */     }
/*  86 */     synchronized (this) {
/*  87 */       RechargeOrderBO orderBO = this.cachedOrders.remove(Long.valueOf(cid));
/*  88 */       if (orderBO != null) {
/*  89 */         sendPrize(orderBO);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendPrize(RechargeOrderBO orderBO) {
/*  95 */     synchronized (this) {
/*     */       
/*  97 */       if (orderBO.getStatus().equals(OrderStatus.Delivered)) {
/*     */         return;
/*     */       }
/* 100 */       RefRecharge ref = (RefRecharge)RefDataMgr.get(RefRecharge.class, orderBO.getProductid());
/* 101 */       if (ref == null) {
/* 102 */         CommLog.error("订单[{}]充值失败，对应商品id[{}]无法查到", orderBO.getCporderid(), orderBO.getProductid());
/*     */         return;
/*     */       } 
/* 105 */       Player player = PlayerMgr.getInstance().getPlayer(orderBO.getPid());
/* 106 */       if (player == null) {
/* 107 */         CommLog.error("玩家[{}]不存在或已注销", Long.valueOf(orderBO.getPid()));
/*     */         return;
/*     */       } 
/* 110 */       RechargeFeature rechargeFeature = (RechargeFeature)player.getFeature(RechargeFeature.class);
/*     */       
/* 112 */       PlayerRechargeRecordBO record = rechargeFeature.getRecharged(ref.id);
/*     */       
/* 114 */       int crystal = ref.Crystal;
/*     */ 
/*     */       
/* 117 */       ((NewFirstReward)ActivityMgr.getActivity(NewFirstReward.class)).setFirstReward(player);
/*     */ 
/*     */       
/* 120 */       String resetsign = getResetSign(ref.id);
/* 121 */       boolean first = !(record != null && resetsign.equals(record.getResetSign()));
/*     */       
/* 123 */       if (first && ref.RebateAchievement == Achievement.AchievementType.None) {
/* 124 */         crystal = ref.First;
/*     */       }
/*     */ 
/*     */       
/* 128 */       rechargeFeature.isRecharged();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/* 134 */         player.getPlayerBO().saveExtPackage(player.getPlayerBO().getExtPackage() + ref.AddPackage);
/* 135 */         player.pushProto("extPackage", Integer.valueOf(player.getPlayerBO().getExtPackage()));
/*     */       } 
/* 137 */       if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal) {
/* 138 */         rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
/* 139 */         AchievementFeature.AchievementIns ins = ((AchievementFeature)player.getFeature(AchievementFeature.class)).getOrCreate(ref.RebateAchievement);
/* 140 */         if (ins.bo.getCompleteCount() != 0) {
/* 141 */           ins.bo.saveCompleteCount(ins.bo.getCompleteCount() + ref.RebateDays);
/*     */         }
/* 143 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).checkMonthCard(Achievement.AchievementType.MonthCardCrystal);
/*     */       } 
/* 145 */       if (ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/* 146 */         rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
/* 147 */         ((AchievementFeature)player.getFeature(AchievementFeature.class)).checkMonthCard(Achievement.AchievementType.YearCardCrystal);
/*     */       } 
/* 149 */       orderBO.saveStatus(OrderStatus.Delivered.toString());
/* 150 */       orderBO.saveDeliverTime(CommTime.nowSecond());
/*     */       
/* 152 */       rechargeFeature.recordRecharge(orderBO, resetsign);
/*     */       
/* 154 */       int gained = ((PlayerCurrency)player.getFeature(PlayerCurrency.class)).gain(PrizeType.Crystal, crystal * orderBO.getQuantity(), ItemFlow.Recharge);
/*     */ 
/*     */       
/* 157 */       player.getPlayerBO().saveTotalRecharge(player.getPlayerBO().getTotalRecharge() + ref.Price);
/*     */       
/* 159 */       ((PlayerRecord)player.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.DailyRecharge, ref.Price / 10);
/* 160 */       ((PlayerCurrency)player.getFeature(PlayerCurrency.class)).gain(PrizeType.VipExp, ref.VipExp, ItemFlow.Recharge);
/*     */       
/* 162 */       Player.RechargeNotify notify = new Player.RechargeNotify(new Reward(PrizeType.Crystal, gained), ref.id);
/*     */       
/* 164 */       player.pushProto("RechargeNotifyResult", notify);
/*     */ 
/*     */       
/* 167 */       if (ref.RebateAchievement != Achievement.AchievementType.MonthCardCrystal && ref.RebateAchievement != Achievement.AchievementType.YearCardCrystal)
/*     */       {
/* 169 */         ((FirstRecharge)ActivityMgr.getActivity(FirstRecharge.class)).sendFirstRechargeReward(player);
/*     */       }
/*     */ 
/*     */       
/* 173 */       ((DailyRecharge)ActivityMgr.getActivity(DailyRecharge.class)).handlePlayerChange(player, ref.Price / 10);
/*     */       
/* 175 */       ((AccumRecharge)ActivityMgr.getActivity(AccumRecharge.class)).handlePlayerChange(player, ref.Price / 10);
/*     */       
/* 177 */       ((AccumRechargeDay)ActivityMgr.getActivity(AccumRechargeDay.class)).handleRecharge(player);
/*     */       
/* 179 */       ((TitleFeature)player.getFeature(TitleFeature.class)).updateInc(Title.RechargeTo, Integer.valueOf(ref.Price / 10));
/*     */       
/* 181 */       ((RedPacket)ActivityMgr.getActivity(RedPacket.class)).handle(ref.Price / 10, player);
/*     */       
/* 183 */       ActivityMgr.updateWorldRank(player, (ref.Price / 10), RankType.WorldRecharge);
/*     */       
/* 185 */       ((FortuneWheel)ActivityMgr.getActivity(FortuneWheel.class)).handlePlayerChange(player, ref.Price / 10);
/*     */       
/* 187 */       rechargeLog(player, orderBO);
/* 188 */       TDrechargeLog(player, orderBO, gained);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getResetSign(String id) {
/* 193 */     RechargeResetBO bo = this.resets.get(id);
/* 194 */     if (bo == null) {
/* 195 */       return "";
/*     */     }
/* 197 */     return bo.getResetSign();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset(String goodsid) {
/* 202 */     RechargeResetBO bo = this.resets.get(goodsid);
/* 203 */     if (bo != null) {
/* 204 */       bo.saveResetSign(String.valueOf(CommTime.nowMS()) + "@server" + Config.ServerID());
/*     */     } else {
/* 206 */       bo = new RechargeResetBO();
/* 207 */       bo.setGoodsid(goodsid);
/* 208 */       bo.saveResetSign(String.valueOf(CommTime.nowMS()) + "@server" + Config.ServerID());
/* 209 */       bo.insert();
/* 210 */       this.resets.put(goodsid, bo);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rechargeLog(Player player, RechargeOrderBO bo) {
/* 215 */     AccountBO account = ((AccountFeature)player.getFeature(AccountFeature.class)).getAccount();
/*     */     try {
/* 217 */       RefRecharge ref = (RefRecharge)RefDataMgr.get(RefRecharge.class, bo.getProductid());
/* 218 */       FlowLogger.rechargeLog(
/* 219 */           player.getPid(), 
/* 220 */           player.getOpenId(), 
/* 221 */           player.getVipLevel(), 
/* 222 */           player.getLv(), 
/* 223 */           bo.getAdfromOrderid(), 
/* 224 */           bo.getCporderid(), 
/* 225 */           bo.getProductid(), 
/* 226 */           ref.Price, 
/* 227 */           ref.Crystal, 
/* 228 */           bo.getStatus(), 
/* 229 */           bo.getDeliverTime(), 
/* 230 */           CommTime.getTimeStringSYMD(bo.getDeliverTime()), 
/* 231 */           bo.getOrderTime(), 
/* 232 */           CommTime.getTimeStringSYMD(bo.getOrderTime()), 
/* 233 */           bo.getAdfrom(), 
/* 234 */           bo.getAdfrom2(), 
/* 235 */           account.getRegIp(), 
/* 236 */           account.getRegTime(), 
/* 237 */           CommTime.getTimeStringSYMD(account.getRegTime()), 
/* 238 */           account.getAdid());
/*     */     }
/* 240 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public void TDrechargeLog(Player player, RechargeOrderBO bo, int gained) {
/*     */     try {
/* 246 */       String orderid = bo.getCporderid();
/* 247 */       if (orderid != null && !orderid.equals("") && 
/* 248 */         orderid.contains("模拟")) {
/*     */         return;
/*     */       }
/*     */       
/* 252 */       RefRecharge ref = (RefRecharge)RefDataMgr.get(RefRecharge.class, bo.getProductid());
/* 253 */       TDRechargeLogger.getInstance().sendRechargeLog(String.valueOf(bo.getId()), 
/* 254 */           "success", 
/* 255 */           "h5", 
/* 256 */           String.valueOf(player.getPid()), 
/* 257 */           bo.getCporderid(), (
/* 258 */           ref.Price / 10), 
/* 259 */           "CNY", 
/* 260 */           gained, 
/* 261 */           CommTime.nowSecond(), 
/* 262 */           ref.Title, 
/* 263 */           player.getLv());
/* 264 */     } catch (Exception exception) {}
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/recharge/RechargeMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */