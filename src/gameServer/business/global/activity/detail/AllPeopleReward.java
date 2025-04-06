/*     */ package business.global.activity.detail;
/*     */ 
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
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.proto.AccumRechargeInfo;
/*     */ import core.server.OpenSeverTime;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AllPeopleReward
/*     */   extends Activity
/*     */ {
/*     */   private int begin;
/*     */   public List<AccumRechargeInfo> arList;
/*     */   
/*     */   public AllPeopleReward(ActivityBO data) {
/*  40 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  50 */     this.arList = Lists.newArrayList();
/*  51 */     this.begin = json.get("begin").getAsInt();
/*  52 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  53 */       JsonObject obj = element.getAsJsonObject();
/*  54 */       AccumRechargeInfo builder = new AccumRechargeInfo();
/*  55 */       builder.setAwardId(obj.get("aid").getAsInt());
/*  56 */       builder.setRecharge(obj.get("times").getAsInt());
/*  57 */       builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
/*  58 */       this.arList.add(builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeginTime() {
/*  64 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  69 */     JsonArray awardArray = json.get("awards").getAsJsonArray();
/*  70 */     if (awardArray.size() <= 0) {
/*  71 */       throw new RequestException(4000001, "配置的奖励条数要大于0", new Object[0]);
/*     */     }
/*  73 */     StringBuilder desc = new StringBuilder("【累充配置】\n");
/*  74 */     for (JsonElement element : awardArray) {
/*  75 */       JsonObject obj = element.getAsJsonObject();
/*  76 */       desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
/*  77 */       int recharge = obj.get("times").getAsInt();
/*  78 */       if (recharge <= 0) {
/*  79 */         throw new RequestException(4000001, "充值金额必须大于0", new Object[0]);
/*     */       }
/*  81 */       desc.append("充值金额:").append(recharge).append(",");
/*  82 */       desc.append("奖励信息:").append(checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
/*     */     } 
/*  84 */     return desc.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  89 */     return ActivityType.AllPeopleReward;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClosed() {
/*  94 */     clearActRecord();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlayerChange(int money) {
/* 104 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 107 */     synchronized (this) {
/*     */       
/* 109 */       this.bo.saveExtInt(0, this.bo.getExtInt(0) + money);
/*     */       
/* 111 */       List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
/*     */       
/* 113 */       List<Integer> stateList = StringUtils.string2Integer(this.bo.getExtStr(1));
/*     */       
/* 115 */       this.arList.forEach(x -> {
/*     */             if (!paramList1.contains(Integer.valueOf(x.getAwardId())) && this.bo.getExtInt(0) >= x.getRecharge()) {
/*     */               paramList1.add(Integer.valueOf(x.getAwardId()));
/*     */               paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
/*     */             } 
/*     */           });
/* 121 */       this.bo.setExtStr(0, StringUtils.list2String(awardList));
/* 122 */       this.bo.setExtStr(1, StringUtils.list2String(stateList));
/* 123 */       this.bo.saveAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public accumRechargeInfoP accumRechargeProto(Player player) {
/* 134 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 136 */     List<Integer> stateList_tmp = StringUtils.string2Integer(this.bo.getExtStr(1));
/*     */     
/* 138 */     int recharge = this.bo.getExtInt(0);
/*     */     
/* 140 */     List<Integer> awardList = null, stateList = null;
/* 141 */     awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
/* 142 */     stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 144 */     int begin = stateList.size();
/* 145 */     int end = stateList_tmp.size();
/*     */     
/* 147 */     for (int i = begin; i < end; i++) {
/* 148 */       stateList.add(stateList_tmp.get(i));
/*     */     }
/*     */     
/* 151 */     bo.saveExtStr(1, StringUtils.list2String(stateList));
/*     */     
/* 153 */     List<AccumRechargeInfo> accumerecharges = Lists.newArrayList();
/*     */     
/* 155 */     for (AccumRechargeInfo ar : this.arList) {
/* 156 */       AccumRechargeInfo builder = new AccumRechargeInfo();
/* 157 */       builder.setAwardId(ar.getAwardId());
/* 158 */       builder.setRecharge(ar.getRecharge());
/* 159 */       builder.setPrize(ar.getPrize());
/* 160 */       int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
/* 161 */       if (index != -1) {
/* 162 */         builder.setStatus(((Integer)stateList.get(index)).intValue());
/*     */       } else {
/* 164 */         builder.setStatus(FetchStatus.Cannot.ordinal());
/*     */       } 
/* 166 */       accumerecharges.add(builder);
/*     */     } 
/* 168 */     return new accumRechargeInfoP(recharge, accumerecharges, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 178 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 179 */     bo.setPid(player.getPid());
/* 180 */     bo.setAid(this.bo.getId());
/* 181 */     bo.setActivity(getType().toString());
/* 182 */     bo.setExtStr(1, this.bo.getExtStr(1));
/* 183 */     bo.insert();
/* 184 */     return bo;
/*     */   }
/*     */   
/*     */   private class accumRechargeInfoP
/*     */   {
/*     */     int times;
/*     */     List<AccumRechargeInfo> accumerecharges;
/*     */     
/*     */     private accumRechargeInfoP(int times, List<AccumRechargeInfo> accumerecharges) {
/* 193 */       this.times = times;
/* 194 */       this.accumerecharges = accumerecharges;
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
/*     */   
/*     */   public AccumRechargeInfo doReceivePrize(Player player, int awardId) throws WSException {
/* 208 */     Map<Integer, AccumRechargeInfo> arMap = Maps.list2Map(AccumRechargeInfo::getAwardId, this.arList);
/*     */     
/* 210 */     AccumRechargeInfo arInfo = arMap.get(Integer.valueOf(awardId));
/* 211 */     if (arInfo == null) {
/* 212 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 215 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 217 */     List<Integer> awardList = StringUtils.string2Integer(this.bo.getExtStr(0));
/*     */     
/* 219 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 221 */     int index = awardList.indexOf(Integer.valueOf(awardId));
/* 222 */     if (index == -1) {
/* 223 */       throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[] { Integer.valueOf(this.bo.getExtInt(0)), Integer.valueOf(arInfo.getRecharge()) });
/*     */     }
/* 225 */     int state = ((Integer)stateList.get(index)).intValue();
/* 226 */     if (state != FetchStatus.Can.ordinal()) {
/* 227 */       throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 230 */     stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
/* 231 */     bo.saveExtStr(1, StringUtils.list2String(stateList));
/*     */     
/* 233 */     Reward reward = arInfo.getPrize();
/*     */     
/* 235 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);
/*     */     
/* 237 */     AccumRechargeInfo builder = new AccumRechargeInfo();
/* 238 */     builder.setAwardId(awardId);
/* 239 */     builder.setStatus(FetchStatus.Fetched.ordinal());
/* 240 */     builder.setRecharge(arInfo.getRecharge());
/* 241 */     builder.setPrize(reward);
/* 242 */     return builder;
/*     */   }
/*     */   
/*     */   public void weekEvent() {
/*     */     try {
/* 247 */       clearActRecord();
/* 248 */       this.bo.saveExtIntAll(0);
/* 249 */       this.bo.saveExtStrAll("");
/* 250 */     } catch (Exception e) {
/*     */       
/* 252 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/AllPeopleReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */