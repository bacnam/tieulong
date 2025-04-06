/*     */ package business.player.feature.store;
/*     */ 
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.activity.detail.AllPeopleReward;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.feature.achievement.AchievementFeature;
/*     */ import business.player.item.Reward;
/*     */ import com.zhonglian.server.common.enums.Achievement;
/*     */ import com.zhonglian.server.common.enums.StoreType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.CommString;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefGoodsInfo;
/*     */ import core.config.refdata.ref.RefStore;
/*     */ import core.config.refdata.ref.RefStoreRefresh;
/*     */ import core.config.refdata.ref.RefVIP;
/*     */ import core.database.game.bo.PlayerGoodsBO;
/*     */ import core.network.proto.Store;
/*     */ import core.network.proto.StoreRefresh;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerStore
/*     */ {
/*     */   private StoreType storeType;
/*     */   private Player player;
/*     */   private List<PlayerGoodsBO> goodsList;
/*     */   
/*     */   public PlayerStore(StoreType storeType, Player player) {
/*  40 */     this.storeType = storeType;
/*  41 */     this.player = player;
/*  42 */     this.goodsList = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public List<Store.Goods> getGoodsList() {
/*  46 */     List<PlayerGoodsBO> removeList = (List<PlayerGoodsBO>)this.goodsList.stream().filter(b -> (RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(b.getGoodsId())) == null))
/*  47 */       .collect(Collectors.toList());
/*  48 */     for (PlayerGoodsBO bo : removeList) {
/*  49 */       delPlayerGoods(bo);
/*     */     }
/*  51 */     if (this.goodsList.isEmpty()) {
/*  52 */       doAutoRefresh();
/*     */     }
/*     */     
/*  55 */     List<Store.Goods> goods = new ArrayList<>();
/*  56 */     for (PlayerGoodsBO bo : this.goodsList) {
/*  57 */       goods.add(new Store.Goods(bo));
/*     */     }
/*  59 */     return goods;
/*     */   }
/*     */   
/*     */   void addGoods(PlayerGoodsBO bo) {
/*  63 */     this.goodsList.add(bo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doAutoRefresh() {
/*  72 */     List<PlayerGoodsBO> brushList = brushGoodsList();
/*     */     
/*  74 */     List<PlayerGoodsBO> removeList = new ArrayList<>();
/*  75 */     for (PlayerGoodsBO bo : this.goodsList) {
/*  76 */       if (brushList.stream().filter(b -> (b.getStoreId() == paramPlayerGoodsBO1.getStoreId())).count() == 0L) {
/*  77 */         removeList.add(bo);
/*     */       }
/*     */     } 
/*  80 */     removeList.forEach(x -> delPlayerGoods(x));
/*     */     
/*  82 */     Map<Integer, PlayerGoodsBO> remainGoods = Maps.list2Map(PlayerGoodsBO::getStoreId, this.goodsList);
/*  83 */     for (PlayerGoodsBO form : brushList) {
/*  84 */       PlayerGoodsBO to = remainGoods.get(Integer.valueOf(form.getStoreId()));
/*  85 */       if (to == null) {
/*  86 */         updatePlayerGoods(form, to = new PlayerGoodsBO());
/*     */         continue;
/*     */       } 
/*  89 */       RefStore storeRef = (RefStore)RefDataMgr.get(RefStore.class, Integer.valueOf(to.getStoreId()));
/*  90 */       if (storeRef != null && storeRef.IsDailyRefresh) {
/*  91 */         updatePlayerGoods(form, to);
/*     */       }
/*     */     } 
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StoreRefresh refreshInfo() {
/* 104 */     StoreType storeType = this.storeType;
/*     */     
/* 106 */     StoreRecord storeRecordL = (StoreRecord)this.player.getFeature(StoreRecord.class);
/*     */     
/* 108 */     StoreRefresh storeRefresh = new StoreRefresh();
/* 109 */     storeRefresh.setStoreType(storeType);
/*     */     
/* 111 */     int nextRefreshTime = nextRefreshTime();
/* 112 */     storeRefresh.setNextRefreshTime(nextRefreshTime);
/* 113 */     storeRefresh.setRemainsec(Math.max(nextRefreshTime - CommTime.nowSecond(), 0));
/*     */     
/* 115 */     int freeTimes = storeRecordL.getFreeRefreshTimes(storeType);
/*     */     
/* 117 */     storeRefresh.setFreeRefreshTimes(freeTimes);
/* 118 */     storeRefresh.setPaidRefreshTimes(storeRecordL.getPaidRefreshTimes(storeType));
/*     */ 
/*     */     
/* 121 */     return storeRefresh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nextRefreshTime() {
/* 130 */     int time = CommTime.getTodayZeroClockS();
/* 131 */     for (int i = 0; i < 8; i++) {
/* 132 */       time += 10800;
/* 133 */       if (time > CommTime.nowSecond()) {
/* 134 */         return time;
/*     */       }
/*     */     } 
/* 137 */     return CommTime.getTodayZeroClockS() + 86400;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<RefStore> newStoreRefList() {
/* 146 */     return (List<RefStore>)RefStore.StoreByType.get(this.storeType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PlayerGoodsBO> brushGoodsList() {
/* 155 */     List<PlayerGoodsBO> brushList = new ArrayList<>();
/*     */     
/* 157 */     for (RefStore storeRef : newStoreRefList()) {
/* 158 */       if (!storeRef.LevelRange.within(this.player.getLv())) {
/*     */         continue;
/*     */       }
/*     */       
/* 162 */       int index = CommMath.getRandomIndexByRate(storeRef.GoodsWeightList);
/* 163 */       if (index < 0 || index >= storeRef.GoodsIDList.size()) {
/* 164 */         index = 0;
/*     */       }
/* 166 */       long goodsId = ((Long)storeRef.GoodsIDList.get(index)).longValue();
/*     */       
/* 168 */       RefGoodsInfo goodsRef = (RefGoodsInfo)RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(goodsId));
/* 169 */       if (goodsRef == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 173 */       int amount = ((Integer)storeRef.GoodsCountList.get(index)).intValue();
/*     */ 
/*     */       
/* 176 */       PlayerGoodsBO bo = new PlayerGoodsBO();
/*     */       
/* 178 */       bo.setPid(this.player.getPid());
/*     */       
/* 180 */       bo.setGoodsId(goodsRef.ID);
/* 181 */       bo.setStoreId(storeRef.ID);
/* 182 */       bo.setStoreType(storeRef.StoreType.ordinal());
/*     */       
/* 184 */       int costIndex = CommMath.getRandomIndexByRate(goodsRef.CostWeight);
/* 185 */       bo.setCostUniformId(goodsRef.CostUniformId.get(costIndex));
/* 186 */       bo.setPrice(goodsRef.Price.get(costIndex));
/* 187 */       bo.setDiscount(goodsRef.Discount.get(costIndex));
/*     */       
/* 189 */       bo.setAmount(amount);
/* 190 */       bo.setBuyTimes(0);
/* 191 */       bo.setTotalBuyTimes(storeRef.BuyLimit);
/* 192 */       bo.setSoldout(false);
/*     */       
/* 194 */       bo.setCreateTime(CommTime.nowSecond());
/*     */       
/* 196 */       brushList.add(bo);
/*     */     } 
/* 198 */     return brushList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePlayerGoods(PlayerGoodsBO form, PlayerGoodsBO to) {
/* 208 */     to.setPid(form.getPid());
/* 209 */     to.setGoodsId(form.getGoodsId());
/* 210 */     to.setStoreId(form.getStoreId());
/* 211 */     to.setStoreType(form.getStoreType());
/* 212 */     to.setCostUniformId(form.getCostUniformId());
/* 213 */     to.setPrice(form.getPrice());
/* 214 */     to.setDiscount(form.getDiscount());
/* 215 */     to.setAmount(form.getAmount());
/* 216 */     to.setBuyTimes(form.getBuyTimes());
/* 217 */     to.setTotalBuyTimes(form.getTotalBuyTimes());
/* 218 */     to.setSoldout(form.getSoldout());
/* 219 */     to.setCreateTime(form.getCreateTime());
/* 220 */     if (to.getId() == 0L) {
/* 221 */       to.insert();
/* 222 */       this.goodsList.add(to);
/*     */     } else {
/* 224 */       to.saveAll();
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
/*     */   public boolean manualRefresh() throws WSException {
/* 237 */     StoreType storeType = this.storeType;
/*     */     
/* 239 */     StoreRecord storeRecordL = (StoreRecord)this.player.getFeature(StoreRecord.class);
/*     */     
/* 241 */     if (storeRecordL.getFreeRefreshTimes(storeType) > 0) {
/* 242 */       storeRecordL.doHandleRefresh(storeType, true);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 247 */       int paidRefreshTimes = storeRecordL.getPaidRefreshTimes(storeType);
/*     */       
/* 249 */       int limitPaidTimes = ((RefVIP)RefDataMgr.getOrLast(RefVIP.class, Integer.valueOf(this.player.getPlayerBO().getVipLevel()))).DailyRefreshStoreTimes;
/* 250 */       if (limitPaidTimes >= 0 && paidRefreshTimes >= limitPaidTimes) {
/* 251 */         throw new WSException(ErrorCode.Store_RefreshFull, "商店类型:%s的刷新次数已满", new Object[] { storeType });
/*     */       }
/*     */       
/* 254 */       RefStoreRefresh storeRefresh = (RefStoreRefresh)RefDataMgr.get(RefStoreRefresh.class, storeType);
/* 255 */       if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).checkAndConsume(storeRefresh.UniformId, storeRefresh.Count, ItemFlow.RefreshStore)) {
/* 256 */         throw new WSException(ErrorCode.NotEnough_Currency, "商店类型:%s的刷新代币不足", new Object[] { storeType });
/*     */       }
/*     */       
/* 259 */       storeRecordL.doHandleRefresh(storeType, false);
/*     */     } 
/* 261 */     return doAutoRefresh();
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
/*     */   public Reward doBuyGoods(long sId, int buyTimes) throws WSException {
/* 273 */     PlayerGoodsBO bo = null;
/* 274 */     for (PlayerGoodsBO model : this.goodsList) {
/* 275 */       if (model.getId() == sId) {
/* 276 */         bo = model;
/*     */       }
/*     */     } 
/* 279 */     if (bo == null) {
/* 280 */       throw new WSException(ErrorCode.Goods_HasRefresh, "商品:%s已刷新,请重新获取", new Object[] { Long.valueOf(sId) });
/*     */     }
/* 282 */     if (bo.getSoldout()) {
/* 283 */       throw new WSException(ErrorCode.Goods_Soldout, "商品:%s已售空", new Object[] { Long.valueOf(sId) });
/*     */     }
/* 285 */     if (bo.getTotalBuyTimes() > 0 && 
/* 286 */       bo.getTotalBuyTimes() - bo.getBuyTimes() < buyTimes) {
/* 287 */       throw new WSException(ErrorCode.Goods_NotEnough, "商品:%s的当前可购买次数不足:%s", new Object[] { Long.valueOf(sId), Integer.valueOf(buyTimes) });
/*     */     }
/*     */     
/* 290 */     return buyStoreGoods(bo, buyTimes);
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
/*     */   public Reward buyStoreGoods(PlayerGoodsBO bo, int buyTimes) throws WSException {
/* 302 */     RefGoodsInfo goodsRef = (RefGoodsInfo)RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(bo.getGoodsId()));
/* 303 */     if (goodsRef == null) {
/* 304 */       throw new WSException(ErrorCode.NotFound_RefGoodsInfo, "商品资源不存在");
/*     */     }
/*     */     
/* 307 */     int count = bo.getAmount() * buyTimes;
/*     */     
/* 309 */     List<Integer> costItem = CommString.getIntegerList(bo.getCostUniformId(), "&");
/* 310 */     List<Integer> costCount = new ArrayList<>();
/* 311 */     getTotalPrice(CommString.getDoubleList(bo.getDiscount(), "&"), count, goodsRef.DiscountType.ordinal()).stream().forEach(x -> paramList.add(Integer.valueOf(x.intValue())));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).check(costItem, costCount)) {
/* 317 */       throw new WSException(ErrorCode.Goods_PriceLess, "商品购买的价钱不足");
/*     */     }
/*     */     
/* 320 */     ((PlayerItem)this.player.getFeature(PlayerItem.class)).consume(costItem, costCount, ItemFlow.BuyItem);
/*     */     
/* 322 */     bo.saveBuyTimes(bo.getBuyTimes() + buyTimes);
/* 323 */     if (bo.getTotalBuyTimes() > 0 && bo.getBuyTimes() >= bo.getTotalBuyTimes()) {
/* 324 */       bo.saveSoldout(true);
/*     */     }
/*     */     
/* 327 */     Reward reward = new Reward(goodsRef.UniformID, count);
/*     */     
/* 329 */     Reward rspns = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.BuyItem);
/*     */     
/* 331 */     this.player.pushProto("store.Buy", new Store.Goods(bo));
/*     */     
/* 333 */     ((StoreRecord)this.player.getFeature(StoreRecord.class)).doBuyGoods(this.storeType);
/*     */     
/* 335 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ShoppingTimes_M1);
/* 336 */     ((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ShoppingTimes_M2);
/*     */ 
/*     */     
/* 339 */     if (this.storeType == StoreType.MysteryStore) {
/* 340 */       ((AllPeopleReward)ActivityMgr.getActivity(AllPeopleReward.class)).handlePlayerChange(buyTimes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 346 */     return rspns;
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
/*     */   public List<Double> getTotalPrice(List<Double> discount, int count, int discountType) {
/* 358 */     List<Double> costCount = (List<Double>)discount.stream().map(x -> Double.valueOf(x.doubleValue() * paramInt)).collect(Collectors.toList());
/* 359 */     return costCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delPlayerGoods(PlayerGoodsBO bo) {
/* 368 */     bo.del();
/* 369 */     this.goodsList.remove(bo);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/store/PlayerStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */