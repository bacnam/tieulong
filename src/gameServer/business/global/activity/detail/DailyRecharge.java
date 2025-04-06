/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.FetchStatus;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.Maps;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.proto.DailyRechargeInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DailyRecharge
/*     */   extends Activity
/*     */ {
/*     */   public List<DailyRechargeInfo> drList;
/*     */   private List<Integer> rechargeList;
/*     */   
/*     */   public DailyRecharge(ActivityBO data) {
/*  44 */     super(data);
/*     */ 
/*     */ 
/*     */     
/*  48 */     this.rechargeList = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  52 */     this.drList = Lists.newArrayList();
/*  53 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  54 */       JsonObject obj = element.getAsJsonObject();
/*  55 */       DailyRechargeInfo builder = new DailyRechargeInfo();
/*  56 */       builder.setAwardId(obj.get("aid").getAsInt());
/*  57 */       builder.setMaxTimes(obj.get("receiveTimes").getAsInt());
/*  58 */       builder.setRecharge(obj.get("recharge").getAsInt());
/*  59 */       builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
/*  60 */       this.drList.add(builder);
/*  61 */       this.rechargeList.add(Integer.valueOf(obj.get("recharge").getAsInt()));
/*     */     } 
/*  63 */     this.rechargeList.sort((left, right) -> left.intValue() - right.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  71 */     JsonArray awardArray = json.get("awards").getAsJsonArray();
/*  72 */     if (awardArray.size() <= 0) {
/*  73 */       throw new RequestException(4000001, "配置的奖励条数要大于0", new Object[0]);
/*     */     }
/*  75 */     List<Integer> moneyList = Lists.newArrayList();
/*  76 */     StringBuilder desc = new StringBuilder("【单笔充值指定额度】\n");
/*  77 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  78 */       JsonObject obj = element.getAsJsonObject();
/*  79 */       desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
/*  80 */       int receiveTimes = obj.get("receiveTimes").getAsInt();
/*  81 */       if (receiveTimes <= 0) {
/*  82 */         throw new RequestException(4000001, "购买次数<=0", new Object[0]);
/*     */       }
/*  84 */       desc.append("购买次数:").append(receiveTimes).append(",");
/*  85 */       int money = obj.get("recharge").getAsInt();
/*  86 */       if (money <= 0) {
/*  87 */         throw new RequestException(4000001, "充值金额<=0", new Object[0]);
/*     */       }
/*  89 */       RefRecharge ref = null;
/*  90 */       for (RefRecharge model : RefDataMgr.getAll(RefRecharge.class).values()) {
/*  91 */         if (model.Price == money) {
/*  92 */           ref = model;
/*     */         }
/*     */       } 
/*  95 */       if (ref == null) {
/*  96 */         throw new RequestException(4000001, "【充值金额】必须在策划配置的recharge表中配置", new Object[0]);
/*     */       }
/*  98 */       if (moneyList.contains(Integer.valueOf(money))) {
/*  99 */         throw new RequestException(4000001, "【充值金额】不能重复", new Object[0]);
/*     */       }
/* 101 */       moneyList.add(Integer.valueOf(money));
/* 102 */       desc.append("充值金额:").append(money).append(",");
/* 103 */       desc.append("奖励信息:").append(checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
/*     */     } 
/* 105 */     return desc.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/* 110 */     return ActivityType.DailyRecharge;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 115 */     clearActRecord();
/* 116 */     CommLog.info("单充活动关闭");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dailyRechargeRefresh(Player player) {
/*     */     try {
/* 124 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/* 127 */       ActivityRecordBO bo = getOrCreateRecord(player);
/* 128 */       List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
/* 129 */       List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
/* 130 */       Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getAwardId, this.drList);
/* 131 */       for (int i = 0; i < awardsList.size(); i++) {
/* 132 */         DailyRechargeInfo tmp_info = drMap.get(awardsList.get(i));
/* 133 */         leftRecList.set(i, Integer.valueOf(tmp_info.getMaxTimes()));
/*     */       } 
/* 135 */       bo.setExtStr(3, StringUtils.list2String(leftRecList));
/* 136 */       bo.saveAll();
/* 137 */       player.pushProto("dailyRechargeRefresh", dailyRechargeProto(player));
/* 138 */     } catch (Exception e) {
/*     */       
/* 140 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlayerChange(Player player, int money) {
/* 151 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 154 */     Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getRecharge, this.drList);
/*     */     
/* 156 */     DailyRechargeInfo drInfo = drMap.get(Integer.valueOf(money));
/* 157 */     if (drInfo == null) {
/* 158 */       for (int i = this.rechargeList.size() - 1; i >= 0; i--) {
/* 159 */         if (((Integer)this.rechargeList.get(i)).intValue() < money) {
/* 160 */           drInfo = drMap.get(this.rechargeList.get(i));
/*     */         }
/*     */       } 
/* 163 */       if (drInfo == null) {
/*     */         return;
/*     */       }
/*     */     } 
/* 167 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 169 */     if (money > bo.getExtInt(0)) {
/* 170 */       bo.setExtInt(0, money);
/*     */     }
/* 172 */     bo.setExtInt(1, bo.getExtInt(1) + money);
/*     */     
/* 174 */     int awardId = drInfo.getAwardId();
/*     */     
/* 176 */     List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 178 */     List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 180 */     List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
/*     */     
/* 182 */     List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
/*     */     
/* 184 */     int index = awardsList.indexOf(Integer.valueOf(awardId));
/* 185 */     if (index == -1) {
/* 186 */       awardsList.add(Integer.valueOf(awardId));
/* 187 */       canRecList.add(Integer.valueOf(1));
/* 188 */       hasRecList.add(Integer.valueOf(0));
/* 189 */       leftRecList.add(Integer.valueOf(drInfo.getMaxTimes() - 1));
/*     */     } else {
/* 191 */       int leftTimes = ((Integer)leftRecList.get(index)).intValue();
/* 192 */       int canTimes = ((Integer)canRecList.get(index)).intValue();
/* 193 */       if (leftTimes != 0) {
/* 194 */         canTimes++;
/* 195 */         leftTimes--;
/*     */       } 
/* 197 */       canRecList.set(index, Integer.valueOf(canTimes));
/* 198 */       leftRecList.set(index, Integer.valueOf(leftTimes));
/*     */     } 
/* 200 */     bo.setExtStr(0, StringUtils.list2String(awardsList));
/* 201 */     bo.setExtStr(1, StringUtils.list2String(canRecList));
/* 202 */     bo.setExtStr(2, StringUtils.list2String(hasRecList));
/* 203 */     bo.setExtStr(3, StringUtils.list2String(leftRecList));
/* 204 */     bo.saveAll();
/*     */     
/* 206 */     player.pushProto("dailyRecharge", dailyRechargeProto(player));
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
/*     */   public FetchStatus fetchStatus(int leftTimes, int canTimes, int hasTimes) {
/* 218 */     if (leftTimes != 0) {
/* 219 */       return (canTimes - hasTimes > 0) ? FetchStatus.Can : FetchStatus.Cannot;
/*     */     }
/* 221 */     return (canTimes - hasTimes > 0) ? FetchStatus.Can : FetchStatus.Fetched;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<DailyRechargeInfo> dailyRechargeProto(Player player) {
/* 231 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 233 */     List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 235 */     List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 237 */     List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
/*     */     
/* 239 */     List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
/*     */     
/* 241 */     List<DailyRechargeInfo> dailyrecharges = (List<DailyRechargeInfo>)this.drList.stream().map(x -> {
/*     */           DailyRechargeInfo builder = new DailyRechargeInfo(); builder.setAwardId(x.getAwardId()); builder.setMaxTimes(x.getMaxTimes());
/*     */           int canTimes = 0;
/*     */           int hasTimes = 0;
/*     */           int leftTimes = x.getMaxTimes();
/*     */           int index = paramList1.indexOf(Integer.valueOf(x.getAwardId()));
/*     */           if (index != -1) {
/*     */             canTimes = ((Integer)paramList2.get(index)).intValue();
/*     */             hasTimes = ((Integer)paramList3.get(index)).intValue();
/*     */             leftTimes = ((Integer)paramList4.get(index)).intValue();
/*     */           } 
/*     */           builder.setReceivedTimes(hasTimes);
/*     */           builder.setStatus(fetchStatus(leftTimes, canTimes, hasTimes).ordinal());
/*     */           builder.setRecharge(x.getRecharge());
/*     */           builder.setPrize(x.getPrize());
/*     */           builder.setLeftTimes(leftTimes);
/*     */           return builder;
/* 258 */         }).collect(Collectors.toList());
/* 259 */     return dailyrecharges;
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
/*     */   public doReceivePrizeInfo doReceivePrize(Player player, int awardId) throws WSException {
/* 271 */     Map<Integer, DailyRechargeInfo> drMap = Maps.list2Map(DailyRechargeInfo::getAwardId, this.drList);
/*     */     
/* 273 */     DailyRechargeInfo drInfo = drMap.get(Integer.valueOf(awardId));
/* 274 */     if (drInfo == null) {
/* 275 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 278 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 280 */     List<Integer> awardsList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 282 */     List<Integer> canRecList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 284 */     List<Integer> hasRecList = StringUtils.string2Integer(bo.getExtStr(2));
/*     */     
/* 286 */     List<Integer> leftRecList = StringUtils.string2Integer(bo.getExtStr(3));
/*     */     
/* 288 */     int index = awardsList.indexOf(Integer.valueOf(awardId));
/* 289 */     if (index == -1) {
/* 290 */       throw new WSException(ErrorCode.DailyRecharge_CanNotReceive, "充值金额:%s!=需求金额:%s", new Object[] { Integer.valueOf(bo.getExtInt(0)), Integer.valueOf(drInfo.getRecharge()) });
/*     */     }
/* 292 */     int canTimes = ((Integer)canRecList.get(index)).intValue();
/* 293 */     int hasTimes = ((Integer)hasRecList.get(index)).intValue();
/* 294 */     int leftTimes = ((Integer)leftRecList.get(index)).intValue();
/* 295 */     if (hasTimes >= canTimes) {
/* 296 */       throw new WSException(ErrorCode.DailyRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/* 298 */     hasTimes++;
/* 299 */     bo.saveExtStr(3, StringUtils.list2String(leftRecList));
/*     */     
/* 301 */     hasRecList.set(index, Integer.valueOf(hasTimes));
/* 302 */     bo.saveExtStr(2, StringUtils.list2String(hasRecList));
/*     */     
/* 304 */     Reward reward = drInfo.getPrize();
/*     */     
/* 306 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_DailyRecharge);
/*     */     
/* 308 */     FetchStatus fetchState = fetchStatus(leftTimes, canTimes, hasTimes);
/*     */     
/* 310 */     return new doReceivePrizeInfo(awardId, hasTimes, fetchState.ordinal(), reward, leftTimes, null);
/*     */   }
/*     */   
/*     */   private class doReceivePrizeInfo
/*     */   {
/*     */     int awardId;
/*     */     int hasTimes;
/*     */     int state;
/*     */     Reward prize;
/*     */     int leftTimes;
/*     */     
/*     */     private doReceivePrizeInfo(int awardId, int hasTimes, int state, Reward prize, int leftTimes) {
/* 322 */       this.awardId = awardId;
/* 323 */       this.hasTimes = hasTimes;
/* 324 */       this.state = state;
/* 325 */       this.prize = prize;
/* 326 */       this.leftTimes = leftTimes;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/DailyRecharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */