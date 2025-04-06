/*     */ package business.player.item;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefUniformItem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reward
/*     */   extends ArrayList<UniformItem>
/*     */ {
/*     */   private static final long serialVersionUID = -7652426360495764584L;
/*     */   
/*     */   public Reward() {}
/*     */   
/*     */   public Reward(PrizeType crystal, int count) {
/*  32 */     add(crystal, count);
/*     */   }
/*     */   
/*     */   public Reward(int uniformId, int count) {
/*  36 */     add(uniformId, count);
/*     */   }
/*     */   
/*     */   public Reward(List<Integer> uniformIdList, List<Integer> countList) {
/*  40 */     add(uniformIdList, countList);
/*     */   }
/*     */   
/*     */   public Reward(String str_uniformIds, String str_counts) {
/*  44 */     List<Integer> uniformIdList = StringUtils.string2Integer(str_uniformIds);
/*  45 */     List<Integer> countList = StringUtils.string2Integer(str_counts);
/*  46 */     add(uniformIdList, countList);
/*     */   }
/*     */   
/*     */   public Reward(JsonArray itemArray) throws WSException {
/*  50 */     for (JsonElement itemElement : itemArray) {
/*  51 */       JsonObject itemObj = itemElement.getAsJsonObject();
/*  52 */       int uniformId = itemObj.get("uniformId").getAsInt();
/*  53 */       if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId)) == null) {
/*  54 */         throw new WSException(ErrorCode.InvalidParam, "不存在物品%s", new Object[] { Integer.valueOf(uniformId) });
/*     */       }
/*  56 */       int count = itemObj.get("count").getAsInt();
/*  57 */       if (count <= 0) {
/*  58 */         throw new WSException(ErrorCode.InvalidParam, "不存在物品数量不允许小于等于0", new Object[] { Integer.valueOf(count) });
/*     */       }
/*  60 */       add(uniformId, count);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Reward add(PrizeType type, int itemId, int count) {
/*  65 */     int uniformid = ItemUtils.getUniformId(type, itemId);
/*  66 */     if (uniformid == 0) {
/*  67 */       CommLog.error("添加奖励道具错误,Type:{}, ID:{} 不存在", type, Integer.valueOf(itemId));
/*     */     }
/*  69 */     return add(uniformid, count);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward add(PrizeType currency, int count) {
/*  77 */     int uniformid = ItemUtils.getUniformId(currency, 0);
/*  78 */     if (uniformid == 0) {
/*  79 */       CommLog.error("添加奖励货币错误,Type:{}不存在", currency);
/*     */     }
/*  81 */     return add(uniformid, count);
/*     */   }
/*     */   
/*     */   public Reward add(List<Integer> uniformId, List<Integer> count) {
/*  85 */     for (int index = 0; index < uniformId.size(); index++) {
/*  86 */       add(((Integer)uniformId.get(index)).intValue(), ((Integer)count.get(index)).intValue());
/*     */     }
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Reward add(int uniformId, int count) {
/*  92 */     if (count <= 0) {
/*  93 */       return this;
/*     */     }
/*  95 */     if (uniformId != -1 && !RefDataMgr.getAll(RefUniformItem.class).containsKey(Integer.valueOf(uniformId))) {
/*  96 */       return this;
/*     */     }
/*  98 */     add(new UniformItem(uniformId, count));
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public boolean add(UniformItem item) {
/* 103 */     if (item != null) {
/* 104 */       return super.add(item);
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void combine(Reward reward) {
/* 115 */     if (reward == null) {
/*     */       return;
/*     */     }
/* 118 */     for (UniformItem item : reward) {
/* 119 */       add(item.getUniformId(), item.getCount());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<UniformItem> merge() {
/* 129 */     Map<Integer, UniformItem> map = new HashMap<>();
/* 130 */     for (UniformItem item : this) {
/* 131 */       UniformItem ui = map.get(Integer.valueOf(item.getUniformId()));
/* 132 */       if (ui == null) {
/* 133 */         ui = new UniformItem(item.getUniformId(), item.getCount());
/* 134 */         map.put(Integer.valueOf(item.getUniformId()), ui); continue;
/*     */       } 
/* 136 */       ui.addCount(item.getCount());
/*     */     } 
/*     */     
/* 139 */     return new ArrayList<>(map.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void multiply(int number) {
/* 148 */     if (number == 1) {
/*     */       return;
/*     */     }
/* 151 */     stream().forEach(x -> x.setCount(x.getCount() * paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceItem(int replaceId) {
/* 160 */     for (UniformItem uniformItem : this) {
/* 161 */       if (uniformItem.getUniformId() == -1) {
/* 162 */         uniformItem.setUniformId(replaceId);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String uniformItemIds() {
/* 173 */     List<Integer> list = Lists.newArrayList();
/* 174 */     for (UniformItem uniform : this) {
/* 175 */       list.add(Integer.valueOf(uniform.getUniformId()));
/*     */     }
/* 177 */     return StringUtils.list2String(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String uniformItemCounts() {
/* 186 */     List<Integer> list = Lists.newArrayList();
/* 187 */     for (UniformItem uniform : this) {
/* 188 */       list.add(Integer.valueOf(uniform.getCount()));
/*     */     }
/* 190 */     return StringUtils.list2String(list);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 194 */     StringBuilder sb = new StringBuilder();
/* 195 */     for (UniformItem item : this) {
/* 196 */       sb.append(item.getUniformId()).append(":").append(item.getCount()).append(";");
/*     */     }
/* 198 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void modify(int remain) {
/* 202 */     int amount = 0;
/* 203 */     for (UniformItem item : this) {
/* 204 */       amount += item.getCount();
/*     */     }
/* 206 */     if (amount >= remain)
/*     */       return; 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/Reward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */