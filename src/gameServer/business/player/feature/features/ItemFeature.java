/*     */ package business.player.feature.features;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.IItemFilter;
/*     */ import business.player.item.IUniItemContainer;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefItem;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.database.game.bo.ItemBO;
/*     */ import core.logger.flow.FlowLogger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ItemFeature
/*     */   extends Feature
/*     */   implements IUniItemContainer<ItemBO>
/*     */ {
/*  31 */   public final Map<Integer, ItemBO> itemMap = new HashMap<>();
/*     */   
/*     */   public ItemFeature(Player owner) {
/*  34 */     super(owner);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {
/*  39 */     List<ItemBO> boList = BM.getBM(ItemBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
/*  40 */     for (ItemBO bo : boList) {
/*  41 */       this.itemMap.put(Integer.valueOf(bo.getItemId()), bo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemBO getItem(int itemId) {
/*  52 */     return this.itemMap.get(Integer.valueOf(itemId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<UniformItem> getAllItems() {
/*  61 */     List<UniformItem> list = new ArrayList<>();
/*  62 */     for (ItemBO i : this.itemMap.values()) {
/*  63 */       list.add(new UniformItem(PrizeType.Item, i.getItemId(), i.getCount()));
/*     */     }
/*  65 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cacheItem(ItemBO item) {
/*  74 */     this.itemMap.put(Integer.valueOf(item.getItemId()), item);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrizeType getType() {
/*  79 */     return PrizeType.Item;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check(int itemId, int count, IItemFilter<ItemBO> filter) {
/*  84 */     ItemBO bo = getItem(itemId);
/*  85 */     return (bo != null && bo.getCount() >= count);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemBO consume(int itemId, int count, ItemFlow reason, IItemFilter<ItemBO> filter) {
/*  90 */     if (count <= 0) {
/*  91 */       return null;
/*     */     }
/*  93 */     RefItem ref = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));
/*  94 */     if (ref == null) {
/*  95 */       return null;
/*     */     }
/*  97 */     ItemBO bo = getItem(itemId);
/*  98 */     if (bo == null) {
/*  99 */       return null;
/*     */     }
/* 101 */     int beforeNum = bo.getCount();
/* 102 */     int remain = bo.getCount() - count;
/* 103 */     if (remain > 0) {
/* 104 */       bo.saveCount(remain);
/*     */     } else {
/* 106 */       bo.setCount(0);
/* 107 */       bo.del();
/* 108 */       this.itemMap.remove(Integer.valueOf(itemId));
/*     */     } 
/* 110 */     this.player.pushProto("delItem", new UniformItem(getType(), itemId, count));
/* 111 */     itemLog(itemId, reason.value(), -count, bo.getCount(), beforeNum, ConstEnum.ResOpType.Lose);
/* 112 */     return bo;
/*     */   }
/*     */ 
/*     */   
/*     */   public int gain(int itemId, int count, ItemFlow reason) {
/* 117 */     RefItem refItem = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));
/*     */     
/* 119 */     if (count <= 0 || refItem == null) {
/* 120 */       return 0;
/*     */     }
/* 122 */     ItemBO bo = getItem(itemId);
/* 123 */     int beforeNum = 0;
/* 124 */     if (bo == null) {
/* 125 */       bo = new ItemBO();
/* 126 */       bo.setPid(this.player.getPid());
/* 127 */       bo.setItemId(itemId);
/* 128 */       bo.setCount(count);
/* 129 */       bo.setGainTime(CommTime.nowSecond());
/* 130 */       bo.insert();
/* 131 */       this.itemMap.put(Integer.valueOf(itemId), bo);
/*     */     } else {
/* 133 */       beforeNum = bo.getCount();
/* 134 */       int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ItemMaterial", 999999999), beforeNum + count);
/* 135 */       bo.setCount(finalMaterial);
/* 136 */       bo.setGainTime(CommTime.nowSecond());
/* 137 */       bo.saveAll();
/*     */     } 
/* 139 */     this.player.pushProto("addItem", new UniformItem(getType(), itemId, count));
/* 140 */     itemLog(itemId, reason.value(), count, bo.getCount(), beforeNum, ConstEnum.ResOpType.Gain);
/*     */     
/* 142 */     if (refItem.CanUse && refItem.UseNow) {
/*     */       try {
/* 144 */         doUseGenericItem(itemId, count);
/* 145 */       } catch (WSException e) {
/*     */         
/* 147 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 151 */     return count;
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
/*     */   public Reward doUseGenericItem(int itemId, int count) throws WSException {
/* 163 */     RefItem refItem = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));
/* 164 */     if (!check(itemId, count)) {
/* 165 */       throw new WSException(ErrorCode.NotEnough_UseItem, "物品:%s数量不足", new Object[] { Integer.valueOf(itemId) });
/*     */     }
/*     */     
/* 168 */     Reward reward = new Reward();
/* 169 */     for (int i = 0; i < count; i++) {
/* 170 */       for (Integer rewardID : refItem.RewardList) {
/* 171 */         reward.combine(((RefReward)RefDataMgr.get(RefReward.class, rewardID)).genReward());
/*     */       }
/*     */     } 
/*     */     
/* 175 */     consume(itemId, count, ItemFlow.UseGenericItem);
/*     */     
/* 177 */     Reward gain = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.UseGenericItem);
/*     */     
/* 179 */     return gain;
/*     */   }
/*     */   
/*     */   public void itemLog(int itemId, int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
/* 183 */     FlowLogger.itemLog(
/* 184 */         this.player.getPid(), 
/* 185 */         this.player.getVipLevel(), 
/* 186 */         this.player.getLv(), 
/* 187 */         itemId, 
/* 188 */         reason, 
/* 189 */         crystal, 
/* 190 */         finalCrystal, 
/* 191 */         before, 
/* 192 */         opType.ordinal());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/features/ItemFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */