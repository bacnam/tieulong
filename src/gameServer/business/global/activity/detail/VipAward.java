/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.proto.VipAwardInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VipAward
/*     */   extends Activity
/*     */ {
/*     */   private List<VipAwardRef> arList;
/*     */   
/*     */   private static class VipAwardRef
/*     */   {
/*     */     int aid;
/*     */     int vip;
/*     */     String icon;
/*     */     Reward reward;
/*     */     int price;
/*     */     int discount;
/*     */     List<Integer> buytimes;
/*     */     
/*     */     private VipAwardRef() {}
/*     */   }
/*     */   
/*     */   public VipAward(ActivityBO data) {
/*  48 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  54 */     this.arList = new ArrayList<>();
/*  55 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  56 */       JsonObject obeject = element.getAsJsonObject();
/*  57 */       VipAwardRef ref = new VipAwardRef(null);
/*  58 */       ref.aid = obeject.get("aid").getAsInt();
/*  59 */       ref.vip = obeject.get("vip").getAsInt();
/*  60 */       ref.icon = obeject.get("icon").getAsString();
/*  61 */       ref.reward = new Reward(obeject.get("items").getAsJsonArray());
/*  62 */       ref.price = obeject.get("price").getAsInt();
/*  63 */       ref.discount = obeject.get("discount").getAsInt();
/*  64 */       ref.buytimes = StringUtils.string2Integer(obeject.get("buytimes").getAsString());
/*  65 */       this.arList.add(ref);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  72 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  77 */     return ActivityType.VipAward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VipAwardInfo> vipAwardProto(Player player) {
/*  87 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/*  89 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*  90 */     List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/*  92 */     List<VipAwardRef> refList = this.arList;
/*     */     
/*  94 */     List<VipAwardInfo> weeks = (List<VipAwardInfo>)refList.stream().map(x -> {
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
/* 111 */         }).collect(Collectors.toList());
/* 112 */     return weeks;
/*     */   }
/*     */ 
/*     */   
/*     */   public VipAwardRef getVipRef(int rewardId) {
/* 117 */     for (VipAwardRef ref : this.arList) {
/* 118 */       if (ref.aid == rewardId) {
/* 119 */         return ref;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return null;
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
/*     */   public VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
/* 136 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 138 */     VipAwardRef refVipAward = getVipRef(awardId);
/* 139 */     if (refVipAward == null) {
/* 140 */       throw new WSException(ErrorCode.NotFound_RefVipAward, "未找到[%s]的VIP礼包配置", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/* 142 */     if (refVipAward.vip > player.getVipLevel()) {
/* 143 */       throw new WSException(ErrorCode.VipAward_WeekNoNowVIPGift, "礼包VIP等级:%s != 玩家VIP等级:%s", new Object[] { Integer.valueOf(refVipAward.vip), Integer.valueOf(player.getVipLevel()) });
/*     */     }
/*     */     
/* 146 */     int maxTimes = ((Integer)refVipAward.buytimes.get(player.getVipLevel())).intValue();
/*     */     
/* 148 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 149 */     List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 151 */     int buyTimes = 0;
/* 152 */     int index = awardList.indexOf(Integer.valueOf(refVipAward.aid));
/* 153 */     if (index >= 0) {
/* 154 */       buyTimes = ((Integer)hasBuyList.get(index)).intValue();
/*     */     }
/* 156 */     if (buyTimes >= maxTimes) {
/* 157 */       throw new WSException(ErrorCode.VipAward_WeekVIPGiftSold, "该礼包已卖光");
/*     */     }
/*     */     
/* 160 */     int cryStal = refVipAward.discount;
/* 161 */     if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).check(PrizeType.Crystal, cryStal)) {
/* 162 */       throw new WSException(ErrorCode.NotEnough_Crystal, "玩家水晶:%s<购买水晶:%s", new Object[] { Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cryStal) });
/*     */     }
/* 164 */     Reward reward = refVipAward.reward;
/*     */     
/* 166 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_WeeklyVipAward);
/*     */     
/* 168 */     ((PlayerCurrency)player.getFeature(PlayerCurrency.class)).consume(PrizeType.Crystal, cryStal, ItemFlow.Activity_WeeklyVipAward);
/*     */     
/* 170 */     if (index < 0) {
/* 171 */       awardList.add(Integer.valueOf(refVipAward.aid));
/* 172 */       hasBuyList.add(Integer.valueOf(1));
/*     */     } else {
/* 174 */       hasBuyList.set(index, Integer.valueOf(buyTimes + 1));
/*     */     } 
/* 176 */     bo.saveExtStr(0, StringUtils.list2String(awardList));
/* 177 */     bo.saveExtStr(1, StringUtils.list2String(hasBuyList));
/*     */     
/* 179 */     return new VipAwardReceive(awardId, buyTimes + 1, pack);
/*     */   }
/*     */   
/*     */   public static class VipAwardReceive {
/*     */     int awardId;
/*     */     int buyTimes;
/*     */     Reward reward;
/*     */     
/*     */     public VipAwardReceive(int awardId, int buyTimes, Reward reward) {
/* 188 */       this.awardId = awardId;
/* 189 */       this.buyTimes = buyTimes;
/* 190 */       this.reward = reward;
/*     */     }
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
/* 208 */     clearActRecord();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/VipAward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */