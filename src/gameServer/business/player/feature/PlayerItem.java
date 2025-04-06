/*     */ package business.player.feature;
/*     */ 
/*     */ import business.player.Player;
/*     */ import business.player.item.IItemFilter;
/*     */ import business.player.item.IUniItemContainer;
/*     */ import business.player.item.ItemContainerTable;
/*     */ import business.player.item.ItemUtils;
/*     */ import business.player.item.Reward;
/*     */ import business.player.item.UniformItem;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerItem
/*     */   extends Feature
/*     */ {
/*     */   public PlayerItem(Player data) {
/*  30 */     super(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadDB() {}
/*     */ 
/*     */   
/*     */   public boolean check(int uniformID, int count) {
/*  38 */     return check(uniformID, count, (IItemFilter<?>)null);
/*     */   }
/*     */   
/*     */   public boolean check(int uniformID, int count, IItemFilter<?> filter) {
/*  42 */     RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
/*  43 */     if (ref == null) {
/*  44 */       return false;
/*     */     }
/*  46 */     return check(ref.Type, ref.ItemID, count, filter);
/*     */   }
/*     */   
/*     */   public boolean check(PrizeType type, int itemID, int count) {
/*  50 */     return check(type, itemID, count, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check(PrizeType type, int itemID, int count, IItemFilter<?> filter) {
/*  56 */     Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
/*  57 */     if (clazz != null) {
/*  58 */       IUniItemContainer iContainer = (IUniItemContainer)this.player.getFeature(clazz);
/*  59 */       return iContainer.check(itemID, count, filter);
/*     */     } 
/*  61 */     return ((PlayerCurrency)this.player.getFeature(PlayerCurrency.class)).check(type, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check(List<Integer> uniformIDList, List<Integer> countList) {
/*  66 */     for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
/*  67 */       int uniformid = ((Integer)uniformIDList.get(i)).intValue();
/*  68 */       int count = ((Integer)countList.get(i)).intValue();
/*  69 */       if (!check(uniformid, count)) {
/*  70 */         return false;
/*     */       }
/*     */     } 
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public boolean check(List<Integer> uniformIDList, List<Integer> countList, IItemFilter... filter) {
/*  77 */     Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[])filter);
/*     */     
/*  79 */     for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
/*  80 */       int uniformid = ((Integer)uniformIDList.get(i)).intValue();
/*  81 */       int count = ((Integer)countList.get(i)).intValue();
/*  82 */       RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
/*  83 */       if (ref == null) {
/*  84 */         return false;
/*     */       }
/*  86 */       if (!check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
/*  87 */         return false;
/*     */       }
/*     */     } 
/*  90 */     return true;
/*     */   }
/*     */   
/*     */   public boolean check(List<UniformItem> list) {
/*  94 */     for (UniformItem uniformItem : list) {
/*  95 */       if (!check(uniformItem.getUniformId(), uniformItem.getCount())) {
/*  96 */         return false;
/*     */       }
/*     */     } 
/*  99 */     return true;
/*     */   }
/*     */   
/*     */   public boolean check(List<UniformItem> list, IItemFilter... filter) {
/* 103 */     Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[])filter);
/*     */     
/* 105 */     for (UniformItem uniformItem : list) {
/* 106 */       int uniformid = uniformItem.getUniformId();
/* 107 */       int count = uniformItem.getCount();
/* 108 */       RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
/* 109 */       if (ref == null) {
/* 110 */         return false;
/*     */       }
/* 112 */       if (!check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/* 116 */     return true;
/*     */   }
/*     */   
/*     */   public boolean check(Reward reward) {
/* 120 */     return check(reward.merge());
/*     */   }
/*     */   
/*     */   public boolean check(Reward reward, IItemFilter... filter) {
/* 124 */     return check(reward.merge(), (IItemFilter<?>[])filter);
/*     */   }
/*     */   
/*     */   private void consumeLog(PrizeType type, int itemID, int count, ItemFlow reason) {
/* 128 */     RefUniformItem item = ItemUtils.getRefUniformItem(type, itemID);
/* 129 */     if (item == null) {
/*     */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   public void consume(int uniformID, int count, ItemFlow reason) {
/* 135 */     consume(uniformID, count, reason, (IItemFilter<?>)null);
/*     */   }
/*     */   
/*     */   public void consume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
/* 139 */     RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
/* 140 */     if (ref == null) {
/*     */       return;
/*     */     }
/* 143 */     consume(ref.Type, ref.ItemID, count, reason, filter);
/*     */   }
/*     */   
/*     */   public void consume(PrizeType type, int itemID, int count, ItemFlow reason) {
/* 147 */     consume(type, itemID, count, reason, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
/* 153 */     Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
/* 154 */     if (clazz != null) {
/* 155 */       IUniItemContainer iContainer = (IUniItemContainer)this.player.getFeature(clazz);
/* 156 */       iContainer.consume(itemID, count, reason, filter);
/* 157 */       consumeLog(type, itemID, count, reason);
/*     */     } else {
/* 159 */       ((PlayerCurrency)this.player.getFeature(PlayerCurrency.class)).consume(type, count, reason);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
/* 164 */     for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
/* 165 */       int uniformid = ((Integer)uniformIDList.get(i)).intValue();
/* 166 */       int count = ((Integer)countList.get(i)).intValue();
/* 167 */       consume(uniformid, count, reason);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter... filter) {
/* 172 */     Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[])filter);
/*     */     
/* 174 */     for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
/* 175 */       int uniformid = ((Integer)uniformIDList.get(i)).intValue();
/* 176 */       int count = ((Integer)countList.get(i)).intValue();
/* 177 */       RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
/* 178 */       if (ref != null)
/*     */       {
/*     */         
/* 181 */         consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type)); } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consume(List<UniformItem> list, ItemFlow reason) {
/* 186 */     for (UniformItem uniformItem : list) {
/* 187 */       int uniformid = uniformItem.getUniformId();
/* 188 */       int count = uniformItem.getCount();
/* 189 */       consume(uniformid, count, reason);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consume(List<UniformItem> list, ItemFlow reason, IItemFilter... filter) {
/* 194 */     Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[])filter);
/*     */     
/* 196 */     for (UniformItem uniformItem : list) {
/* 197 */       int uniformid = uniformItem.getUniformId();
/* 198 */       int count = uniformItem.getCount();
/* 199 */       RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
/* 200 */       if (ref == null) {
/*     */         continue;
/*     */       }
/* 203 */       consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void consume(Reward reward, ItemFlow reason) {
/* 208 */     consume(reward.merge(), reason);
/*     */   }
/*     */   
/*     */   public void consume(Reward reward, ItemFlow reason, IItemFilter... filter) {
/* 212 */     consume(reward.merge(), reason, (IItemFilter<?>[])filter);
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(int uniformID, int count, ItemFlow reason) {
/* 216 */     if (!check(uniformID, count)) {
/* 217 */       return false;
/*     */     }
/* 219 */     consume(uniformID, count, reason);
/* 220 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
/* 224 */     if (!check(uniformID, count, filter)) {
/* 225 */       return false;
/*     */     }
/* 227 */     consume(uniformID, count, reason, filter);
/* 228 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason) {
/* 232 */     if (!check(type, itemID, count)) {
/* 233 */       return false;
/*     */     }
/* 235 */     consume(type, itemID, count, reason);
/* 236 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
/* 240 */     if (!check(type, itemID, count, filter)) {
/* 241 */       return false;
/*     */     }
/* 243 */     consume(type, itemID, count, reason, filter);
/* 244 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
/* 248 */     if (!check(uniformIDList, countList)) {
/* 249 */       return false;
/*     */     }
/* 251 */     consume(uniformIDList, countList, reason);
/* 252 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter... filter) {
/* 256 */     if (!check(uniformIDList, countList, (IItemFilter<?>[])filter)) {
/* 257 */       return false;
/*     */     }
/* 259 */     consume(uniformIDList, countList, reason, (IItemFilter<?>[])filter);
/* 260 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason) {
/* 264 */     if (!check(list)) {
/* 265 */       return false;
/*     */     }
/* 267 */     consume(list, reason);
/* 268 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason, IItemFilter... filter) {
/* 272 */     if (!check(list, (IItemFilter<?>[])filter)) {
/* 273 */       return false;
/*     */     }
/* 275 */     consume(list, reason, (IItemFilter<?>[])filter);
/* 276 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(Reward reward, ItemFlow reason) {
/* 280 */     List<UniformItem> list = reward.merge();
/* 281 */     if (!check(list)) {
/* 282 */       return false;
/*     */     }
/* 284 */     consume(list, reason);
/* 285 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkAndConsume(Reward reward, ItemFlow reason, IItemFilter... filter) {
/* 289 */     List<UniformItem> list = reward.merge();
/* 290 */     if (!check(list, (IItemFilter<?>[])filter)) {
/* 291 */       return false;
/*     */     }
/* 293 */     consume(list, reason, (IItemFilter<?>[])filter);
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   public Map<PrizeType, IItemFilter<?>> mapFilter(IItemFilter... filter) {
/* 298 */     Map<PrizeType, IItemFilter<?>> map = new HashMap<>(); byte b; int i; IItemFilter[] arrayOfIItemFilter;
/* 299 */     for (i = (arrayOfIItemFilter = filter).length, b = 0; b < i; ) { IItemFilter<?> f = arrayOfIItemFilter[b];
/* 300 */       map.put(f.getType(), f); b++; }
/*     */     
/* 302 */     return map;
/*     */   }
/*     */   
/*     */   public int gain(int uniformID, int count, ItemFlow reason) {
/* 306 */     RefUniformItem ref = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
/* 307 */     if (ref == null) {
/* 308 */       return 0;
/*     */     }
/* 310 */     return gain(ref.Type, ref.ItemID, count, reason);
/*     */   }
/*     */   
/*     */   public int gain(PrizeType type, int itemID, int count, ItemFlow reason) {
/* 314 */     Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
/* 315 */     if (clazz != null) {
/* 316 */       IUniItemContainer<?> iContainer = (IUniItemContainer)this.player.getFeature(clazz);
/* 317 */       return iContainer.gain(itemID, count, reason);
/*     */     } 
/* 319 */     return ((PlayerCurrency)this.player.getFeature(PlayerCurrency.class)).gain(type, count, reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public Reward gain(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
/* 324 */     Reward rtn = new Reward();
/* 325 */     for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
/* 326 */       int gained = gain(((Integer)uniformIDList.get(i)).intValue(), ((Integer)countList.get(i)).intValue(), reason);
/* 327 */       if (gained != 0) {
/* 328 */         rtn.add(new UniformItem(((Integer)uniformIDList.get(i)).intValue(), gained));
/*     */       }
/*     */     } 
/* 331 */     return rtn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward gain(Reward reward, ItemFlow reason) {
/* 340 */     Iterator<UniformItem> iterator = reward.iterator();
/* 341 */     while (iterator.hasNext()) {
/* 342 */       UniformItem item = iterator.next();
/* 343 */       int gained = gain(item.getUniformId(), item.getCount(), reason);
/* 344 */       if (gained == 0) {
/* 345 */         iterator.remove(); continue;
/*     */       } 
/* 347 */       item.setCount(gained);
/*     */     } 
/*     */     
/* 350 */     return reward;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/PlayerItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */