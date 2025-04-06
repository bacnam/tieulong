/*     */ package business.global.activity;
/*     */ import business.global.activity.detail.VipAward;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.proto.OpenServerRankRewardInfo;
/*     */ import core.network.proto.VipAwardInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public class RankActivity extends Activity {
/*     */   private int require_cost;
/*     */   private Reward reward;
/*     */   
/*     */   public RankActivity(ActivityBO bo) {
/*  31 */     super(bo);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.require_cost = 0;
/*     */   } public List<RankAward> rankrewardList; List<VipAward> vipawardList; public ConstEnum.VIPGiftType getAwardType() {
/*     */     return null;
/*     */   }
/*     */   public Reward getReward() {
/*  44 */     return this.reward;
/*     */   }
/*     */   
/*     */   public int getRequire_cost() {
/*  48 */     return this.require_cost;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class RankAward
/*     */   {
/*     */     public int aid;
/*     */     
/*     */     public NumberRange rankrange;
/*     */     
/*     */     public Reward reward;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class VipAward
/*     */   {
/*     */     int aid;
/*     */     int vip;
/*     */     String icon;
/*     */     Reward reward;
/*     */     int price;
/*     */     int discount;
/*     */     List<Integer> buytimes;
/*     */     
/*     */     private VipAward() {}
/*     */   }
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  76 */     this.reward = new Reward(json.get("awards").getAsJsonArray());
/*  77 */     this.require_cost = json.get("cost").getAsInt();
/*     */     
/*  79 */     this.vipawardList = new ArrayList<>();
/*  80 */     for (JsonElement element : json.get("vipawards").getAsJsonArray()) {
/*  81 */       JsonObject obeject = element.getAsJsonObject();
/*  82 */       VipAward ref = new VipAward(null);
/*  83 */       ref.aid = obeject.get("aid").getAsInt();
/*  84 */       ref.vip = obeject.get("vip").getAsInt();
/*  85 */       ref.icon = obeject.get("icon").getAsString();
/*  86 */       ref.reward = new Reward(obeject.get("items").getAsJsonArray());
/*  87 */       ref.price = obeject.get("price").getAsInt();
/*  88 */       ref.discount = obeject.get("discount").getAsInt();
/*  89 */       ref.buytimes = StringUtils.string2Integer(obeject.get("buytimes").getAsString());
/*  90 */       this.vipawardList.add(ref);
/*     */     } 
/*     */     
/*  93 */     this.rankrewardList = new ArrayList<>();
/*  94 */     for (JsonElement element : json.get("rankawards").getAsJsonArray()) {
/*  95 */       JsonObject obeject = element.getAsJsonObject();
/*  96 */       RankAward ref = new RankAward();
/*  97 */       ref.aid = obeject.get("aid").getAsInt();
/*  98 */       ref.rankrange = NumberRange.parse(obeject.get("rankrange").getAsString());
/*  99 */       ref.reward = new Reward(obeject.get("items").getAsJsonArray());
/* 100 */       this.rankrewardList.add(ref);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCost(Player player) {
/* 106 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 107 */     return bo.getExtInt(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void UpdateMaxRequire_cost(Player player, int crystal) {
/* 112 */     synchronized (this) {
/* 113 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/* 116 */       ActivityRecordBO bo = getOrCreateRecord(player);
/* 117 */       if (crystal > bo.getExtInt(0)) {
/* 118 */         bo.saveExtInt(0, crystal);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public OpenServerRankRewardInfo pickUpReward(Player player) throws WSException {
/* 125 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 126 */     int cost = getCost(player);
/* 127 */     if (cost < getRequire_cost()) {
/* 128 */       throw new WSException(ErrorCode.Not_Enough, "条件未达成");
/*     */     }
/*     */     
/* 131 */     if (bo.getExtInt(1) != 0) {
/* 132 */       throw new WSException(ErrorCode.Already_Picked, "已领取");
/*     */     }
/* 134 */     bo.saveExtInt(1, 1);
/* 135 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(getReward(), ItemFlow.RankReward);
/* 136 */     return new OpenServerRankRewardInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), getReward());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPicked(Player player) {
/* 141 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 142 */     return (bo.getExtInt(1) == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VipAwardInfo> vipAwardProto(Player player) {
/* 152 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 154 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 155 */     List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 157 */     List<VipAward> refList = this.vipawardList;
/*     */     
/* 159 */     List<VipAwardInfo> weeks = (List<VipAwardInfo>)refList.stream().map(x -> {
/*     */           VipAwardInfo builder = new VipAwardInfo();
/*     */           builder.setAwardId(x.aid);
/*     */           builder.setMaxTimes(((Integer)x.buytimes.get(paramPlayer.getVipLevel())).intValue());
/*     */           builder.setReward(x.reward);
/*     */           builder.setIcon(x.icon);
/*     */           builder.setVip(x.vip);
/*     */           builder.setPrice(x.price);
/*     */           builder.setDiscount(x.discount);
/*     */           builder.setTimeslist(StringUtils.list2String(x.buytimes));
/*     */           int index = paramList1.indexOf(Integer.valueOf(x.aid));
/*     */           if (index < 0) {
/*     */             builder.setBuyTimes(0);
/*     */           } else {
/*     */             builder.setBuyTimes(((Integer)paramList2.get(index)).intValue());
/*     */           } 
/*     */           return builder;
/* 176 */         }).collect(Collectors.toList());
/* 177 */     return weeks;
/*     */   }
/*     */ 
/*     */   
/*     */   public VipAward getVipRef(int rewardId) {
/* 182 */     for (VipAward ref : this.vipawardList) {
/* 183 */       if (ref.aid == rewardId) {
/* 184 */         return ref;
/*     */       }
/*     */     } 
/*     */     
/* 188 */     return null;
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
/*     */   public VipAward.VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
/* 201 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 203 */     VipAward refVipAward = getVipRef(awardId);
/* 204 */     if (refVipAward == null) {
/* 205 */       throw new WSException(ErrorCode.NotFound_RefVipAward, "未找到[%s]的VIP礼包配置", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/* 207 */     if (refVipAward.vip > player.getVipLevel()) {
/* 208 */       throw new WSException(ErrorCode.VipAward_WeekNoNowVIPGift, "礼包VIP等级:%s != 玩家VIP等级:%s", new Object[] { Integer.valueOf(refVipAward.vip), Integer.valueOf(player.getVipLevel()) });
/*     */     }
/*     */     
/* 211 */     int maxTimes = ((Integer)refVipAward.buytimes.get(player.getVipLevel())).intValue();
/*     */     
/* 213 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 214 */     List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 216 */     int buyTimes = 0;
/* 217 */     int index = awardList.indexOf(Integer.valueOf(refVipAward.aid));
/* 218 */     if (index >= 0) {
/* 219 */       buyTimes = ((Integer)hasBuyList.get(index)).intValue();
/*     */     }
/* 221 */     if (buyTimes >= maxTimes) {
/* 222 */       throw new WSException(ErrorCode.VipAward_WeekVIPGiftSold, "该礼包已卖光");
/*     */     }
/*     */     
/* 225 */     int cryStal = refVipAward.discount;
/* 226 */     if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).check(PrizeType.Crystal, cryStal)) {
/* 227 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家水晶:%s<购买水晶:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cryStal) });
/*     */     }
/* 229 */     Reward reward = refVipAward.reward;
/*     */     
/* 231 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_WeeklyVipAward);
/*     */     
/* 233 */     ((PlayerCurrency)player.getFeature(PlayerCurrency.class)).consume(PrizeType.Crystal, cryStal, ItemFlow.Activity_WeeklyVipAward);
/*     */     
/* 235 */     if (index < 0) {
/* 236 */       awardList.add(Integer.valueOf(refVipAward.aid));
/* 237 */       hasBuyList.add(Integer.valueOf(1));
/*     */     } else {
/* 239 */       hasBuyList.set(index, Integer.valueOf(buyTimes + 1));
/*     */     } 
/* 241 */     bo.saveExtStr(0, StringUtils.list2String(awardList));
/* 242 */     bo.saveExtStr(1, StringUtils.list2String(hasBuyList));
/*     */     
/* 244 */     return new VipAward.VipAwardReceive(awardId, buyTimes + 1, pack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onOpen() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEnd() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 261 */     clearActRecord();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 267 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/RankActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */