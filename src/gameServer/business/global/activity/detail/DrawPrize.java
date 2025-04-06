/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.rank.Record;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.FetchStatus;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommMath;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.ArrayList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DrawPrize
/*     */   extends Activity
/*     */ {
/*     */   public int price;
/*     */   public int tenPrice;
/*     */   int point;
/*     */   List<Integer> NormalIdList;
/*     */   List<Integer> NormalCountList;
/*     */   List<Integer> NormalWeightList;
/*     */   List<Integer> LeastIdList;
/*     */   List<Integer> LeastCountList;
/*     */   List<Integer> LeastWeightList;
/*     */   public List<DrawTaskInfo> arList;
/*  55 */   List<Reward> RewardList = new ArrayList<>();
/*  56 */   List<RankReward> rankList = new ArrayList<>(); private static class RankReward {
/*     */     NumberRange rank; Reward reward;
/*  58 */     private RankReward() {} } public int maxRank = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DrawPrize(ActivityBO bo) {
/*  66 */     super(bo);
/*     */   }
/*     */   
/*     */   private static class DrawTaskInfo { private int awardId;
/*     */     private int status;
/*     */     private int condition;
/*     */     private Reward prize;
/*     */     
/*     */     private DrawTaskInfo() {}
/*     */     
/*     */     public int getAwardId() {
/*  77 */       return this.awardId;
/*     */     }
/*     */     
/*     */     public void setAwardId(int awardId) {
/*  81 */       this.awardId = awardId;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getStatus() {
/*  86 */       return this.status;
/*     */     }
/*     */     
/*     */     public void setStatus(int status) {
/*  90 */       this.status = status;
/*     */     }
/*     */     
/*     */     public int getCondition() {
/*  94 */       return this.condition;
/*     */     }
/*     */     
/*     */     public void setCondition(int condition) {
/*  98 */       this.condition = condition;
/*     */     }
/*     */     
/*     */     public Reward getPrize() {
/* 102 */       return this.prize;
/*     */     }
/*     */     
/*     */     public void setPrize(Reward prize) {
/* 106 */       this.prize = prize;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/* 112 */     JsonObject draw = json.get("DrawList").getAsJsonObject();
/* 113 */     this.price = draw.get("price").getAsInt();
/* 114 */     this.tenPrice = draw.get("tenprice").getAsInt();
/* 115 */     this.point = draw.get("point").getAsInt();
/* 116 */     this.NormalIdList = StringUtils.string2Integer(draw.get("NormalIdList").getAsString());
/* 117 */     this.NormalCountList = StringUtils.string2Integer(draw.get("NormalCountList").getAsString());
/* 118 */     this.NormalWeightList = StringUtils.string2Integer(draw.get("NormalWeightList").getAsString());
/* 119 */     this.LeastIdList = StringUtils.string2Integer(draw.get("LeastIdList").getAsString());
/* 120 */     this.LeastCountList = StringUtils.string2Integer(draw.get("LeastCountList").getAsString());
/* 121 */     this.LeastWeightList = StringUtils.string2Integer(draw.get("LeastWeightList").getAsString());
/*     */     
/* 123 */     this.arList = Lists.newArrayList();
/* 124 */     for (JsonElement element : json.get("ExtReward").getAsJsonArray()) {
/* 125 */       JsonObject obj = element.getAsJsonObject();
/* 126 */       DrawTaskInfo builder = new DrawTaskInfo(null);
/* 127 */       builder.awardId = obj.get("aid").getAsInt();
/* 128 */       builder.condition = obj.get("condition").getAsInt();
/* 129 */       builder.prize = new Reward(obj.get("items").getAsJsonArray());
/* 130 */       this.arList.add(builder);
/*     */     } 
/*     */     
/* 133 */     for (JsonElement ele : json.get("RankReward").getAsJsonArray()) {
/* 134 */       JsonObject obj = ele.getAsJsonObject();
/* 135 */       RankReward rankreward = new RankReward(null);
/* 136 */       rankreward.rank = NumberRange.parse(obj.get("rank").getAsString());
/* 137 */       rankreward.reward = new Reward(obj.get("reward").getAsJsonArray());
/* 138 */       this.rankList.add(rankreward);
/* 139 */       if (rankreward.rank.getTop() > this.maxRank) {
/* 140 */         this.maxRank = rankreward.rank.getTop();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onOpen() {
/* 149 */     clearActRecord();
/* 150 */     RankManager.getInstance().clear(RankType.DrawPoint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnd() {
/* 155 */     settle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 167 */     return ActivityType.DrawPrize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void settle() {
/* 177 */     List<Record> records = RankManager.getInstance().getRankList(RankType.DrawPoint, this.maxRank);
/* 178 */     for (Record record : records) {
/* 179 */       if (record == null)
/*     */         continue; 
/* 181 */       Reward reward = getReward(record.getRank());
/* 182 */       if (reward != null)
/* 183 */         MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward, new String[0]); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Reward getReward(int rank) {
/* 188 */     for (RankReward reward : this.rankList) {
/* 189 */       if (reward.rank.within(rank))
/* 190 */         return reward.reward; 
/*     */     } 
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reward find(Player player, int times) {
/* 203 */     Reward reward = new Reward();
/*     */     
/* 205 */     for (int i = 0; i < times; i++) {
/* 206 */       if (times > 1 && times - i <= 1) {
/* 207 */         int index = CommMath.getRandomIndexByRate(this.LeastWeightList);
/* 208 */         int uniformID = ((Integer)this.LeastIdList.get(index)).intValue();
/* 209 */         int count = ((Integer)this.LeastCountList.get(index)).intValue();
/* 210 */         reward.combine(new Reward(uniformID, count));
/*     */       } else {
/*     */         
/* 213 */         int index = CommMath.getRandomIndexByRate(this.NormalWeightList);
/* 214 */         int uniformID = ((Integer)this.NormalIdList.get(index)).intValue();
/* 215 */         int count = ((Integer)this.NormalCountList.get(index)).intValue();
/* 216 */         reward.combine(new Reward(uniformID, count));
/*     */       } 
/* 218 */     }  getExtReward(player, times);
/*     */     
/* 220 */     return reward;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getExtReward(Player player, int times) {
/* 230 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 233 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 234 */     bo.setExtInt(0, bo.getExtInt(0) + this.point * times);
/*     */ 
/*     */     
/* 237 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 239 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 241 */     this.arList.forEach(x -> {
/*     */           if (!paramList1.contains(Integer.valueOf(x.awardId)) && paramActivityRecordBO.getExtInt(0) >= x.condition) {
/*     */             paramList1.add(Integer.valueOf(x.awardId));
/*     */             paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
/*     */           } 
/*     */         });
/* 247 */     bo.setExtStr(0, StringUtils.list2String(awardList));
/* 248 */     bo.setExtStr(1, StringUtils.list2String(stateList));
/* 249 */     bo.saveAll();
/* 250 */     RankManager.getInstance().update(RankType.DrawPoint, player.getPid(), bo.getExtInt(0));
/*     */ 
/*     */     
/* 253 */     player.pushProto("DrawPrize", accumRechargeProto(player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public accumRechargeInfoP accumRechargeProto(Player player) {
/* 263 */     ActivityRecordBO bo = getRecord(player);
/*     */     
/* 265 */     int recharge = 0;
/* 266 */     if (bo != null) {
/* 267 */       recharge = bo.getExtInt(0);
/*     */     }
/*     */     
/* 270 */     List<Integer> awardList = null, stateList = null;
/* 271 */     if (bo != null) {
/* 272 */       awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 273 */       stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     } 
/* 275 */     List<DrawTaskInfo> accumerecharges = Lists.newArrayList();
/*     */     
/* 277 */     for (DrawTaskInfo ar : this.arList) {
/* 278 */       DrawTaskInfo builder = new DrawTaskInfo(null);
/* 279 */       builder.setAwardId(ar.getAwardId());
/* 280 */       builder.setCondition(ar.getCondition());
/* 281 */       builder.setPrize(ar.getPrize());
/* 282 */       int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
/* 283 */       if (index != -1) {
/* 284 */         builder.setStatus(((Integer)stateList.get(index)).intValue());
/*     */       } else {
/* 286 */         builder.setStatus(FetchStatus.Cannot.ordinal());
/*     */       } 
/* 288 */       accumerecharges.add(builder);
/*     */     } 
/*     */     
/* 291 */     return new accumRechargeInfoP(this.price, this.tenPrice, this.point, RankManager.getInstance().getRank(RankType.DrawPoint, player.getPid()), recharge, 
/* 292 */         accumerecharges, new Reward(this.NormalIdList, this.NormalCountList), this.rankList, null);
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
/*     */   public DrawTaskInfo doReceivePrize(Player player, int awardId) throws WSException {
/* 304 */     Map<Integer, DrawTaskInfo> arMap = Maps.list2Map(DrawTaskInfo::getAwardId, this.arList);
/*     */     
/* 306 */     DrawTaskInfo arInfo = arMap.get(Integer.valueOf(awardId));
/* 307 */     if (arInfo == null) {
/* 308 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 311 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 313 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 315 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 317 */     int index = awardList.indexOf(Integer.valueOf(awardId));
/* 318 */     if (index == -1) {
/* 319 */       throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[] { Integer.valueOf(bo.getExtInt(0)), Integer.valueOf(arInfo.getCondition()) });
/*     */     }
/* 321 */     int state = ((Integer)stateList.get(index)).intValue();
/* 322 */     if (state != FetchStatus.Can.ordinal()) {
/* 323 */       throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 326 */     stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
/* 327 */     bo.saveExtStr(1, StringUtils.list2String(stateList));
/*     */     
/* 329 */     Reward reward = arInfo.getPrize();
/*     */     
/* 331 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);
/*     */     
/* 333 */     DrawTaskInfo builder = new DrawTaskInfo(null);
/* 334 */     builder.setAwardId(awardId);
/* 335 */     builder.setStatus(FetchStatus.Fetched.ordinal());
/* 336 */     builder.setPrize(reward);
/* 337 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   private class accumRechargeInfoP
/*     */   {
/*     */     int price;
/*     */     int tenprice;
/*     */     int point;
/*     */     int rank;
/*     */     int condition;
/*     */     List<DrawPrize.DrawTaskInfo> accumerecharges;
/*     */     Reward reward;
/*     */     List<DrawPrize.RankReward> rankList;
/*     */     
/*     */     private accumRechargeInfoP(int price, int tenprice, int point, int rank, int condition, List<DrawPrize.DrawTaskInfo> accumerecharges, Reward reward, List<DrawPrize.RankReward> rankList) {
/* 353 */       this.price = price;
/* 354 */       this.tenprice = tenprice;
/* 355 */       this.point = point;
/* 356 */       this.rank = rank;
/* 357 */       this.condition = condition;
/* 358 */       this.accumerecharges = accumerecharges;
/* 359 */       this.reward = reward;
/* 360 */       this.rankList = rankList;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/DrawPrize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */