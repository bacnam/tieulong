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
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.proto.AccumRechargeInfo;
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
/*     */ public class AccumRecharge
/*     */   extends Activity
/*     */ {
/*     */   public List<AccumRechargeInfo> arList;
/*     */   
/*     */   public AccumRecharge(ActivityBO data) {
/*  40 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  48 */     this.arList = Lists.newArrayList();
/*  49 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  50 */       JsonObject obj = element.getAsJsonObject();
/*  51 */       AccumRechargeInfo builder = new AccumRechargeInfo();
/*  52 */       builder.setAwardId(obj.get("aid").getAsInt());
/*  53 */       builder.setRecharge(obj.get("recharge").getAsInt());
/*  54 */       builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
/*  55 */       this.arList.add(builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  61 */     JsonArray awardArray = json.get("awards").getAsJsonArray();
/*  62 */     if (awardArray.size() <= 0) {
/*  63 */       throw new RequestException(4000001, "配置的奖励条数要大于0", new Object[0]);
/*     */     }
/*  65 */     StringBuilder desc = new StringBuilder("【累充配置】\n");
/*  66 */     for (JsonElement element : awardArray) {
/*  67 */       JsonObject obj = element.getAsJsonObject();
/*  68 */       desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
/*  69 */       int recharge = obj.get("recharge").getAsInt();
/*  70 */       if (recharge <= 0) {
/*  71 */         throw new RequestException(4000001, "充值金额必须大于0", new Object[0]);
/*     */       }
/*  73 */       desc.append("充值金额:").append(recharge).append(",");
/*  74 */       desc.append("奖励信息:").append(checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
/*     */     } 
/*  76 */     return desc.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  81 */     return ActivityType.AccumRecharge;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClosed() {
/*  86 */     clearActRecord();
/*  87 */     CommLog.info("累充活动关闭");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlayerChange(Player player, int money) {
/*  97 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 100 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 102 */     bo.setExtInt(0, bo.getExtInt(0) + money);
/*     */     
/* 104 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 106 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 108 */     this.arList.forEach(x -> {
/*     */           if (!paramList1.contains(Integer.valueOf(x.getAwardId())) && paramActivityRecordBO.getExtInt(0) >= x.getRecharge()) {
/*     */             paramList1.add(Integer.valueOf(x.getAwardId()));
/*     */             paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
/*     */           } 
/*     */         });
/* 114 */     bo.setExtStr(0, StringUtils.list2String(awardList));
/* 115 */     bo.setExtStr(1, StringUtils.list2String(stateList));
/* 116 */     bo.saveAll();
/*     */     
/* 118 */     player.pushProto("totalRecharge", accumRechargeProto(player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public accumRechargeInfoP accumRechargeProto(Player player) {
/* 128 */     ActivityRecordBO bo = getRecord(player);
/*     */     
/* 130 */     int recharge = 0;
/* 131 */     if (bo != null) {
/* 132 */       recharge = bo.getExtInt(0);
/*     */     } else {
/* 134 */       handlePlayerChange(player, 0);
/*     */     } 
/*     */     
/* 137 */     List<Integer> awardList = null, stateList = null;
/* 138 */     if (bo != null) {
/* 139 */       awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 140 */       stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     } 
/* 142 */     List<AccumRechargeInfo> accumerecharges = Lists.newArrayList();
/*     */     
/* 144 */     for (AccumRechargeInfo ar : this.arList) {
/* 145 */       AccumRechargeInfo builder = new AccumRechargeInfo();
/* 146 */       builder.setAwardId(ar.getAwardId());
/* 147 */       builder.setRecharge(ar.getRecharge());
/* 148 */       builder.setPrize(ar.getPrize());
/* 149 */       int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
/* 150 */       if (index != -1) {
/* 151 */         builder.setStatus(((Integer)stateList.get(index)).intValue());
/*     */       } else {
/* 153 */         builder.setStatus(FetchStatus.Cannot.ordinal());
/*     */       } 
/* 155 */       accumerecharges.add(builder);
/*     */     } 
/* 157 */     return new accumRechargeInfoP(recharge, accumerecharges, null);
/*     */   }
/*     */   
/*     */   private class accumRechargeInfoP
/*     */   {
/*     */     int recharge;
/*     */     List<AccumRechargeInfo> accumerecharges;
/*     */     
/*     */     private accumRechargeInfoP(int recharge, List<AccumRechargeInfo> accumerecharges) {
/* 166 */       this.recharge = recharge;
/* 167 */       this.accumerecharges = accumerecharges;
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
/* 181 */     Map<Integer, AccumRechargeInfo> arMap = Maps.list2Map(AccumRechargeInfo::getAwardId, this.arList);
/*     */     
/* 183 */     AccumRechargeInfo arInfo = arMap.get(Integer.valueOf(awardId));
/* 184 */     if (arInfo == null) {
/* 185 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 188 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 190 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 192 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 194 */     int index = awardList.indexOf(Integer.valueOf(awardId));
/* 195 */     if (index == -1) {
/* 196 */       throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[] { Integer.valueOf(bo.getExtInt(0)), Integer.valueOf(arInfo.getRecharge()) });
/*     */     }
/* 198 */     int state = ((Integer)stateList.get(index)).intValue();
/* 199 */     if (state != FetchStatus.Can.ordinal()) {
/* 200 */       throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 203 */     stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
/* 204 */     bo.saveExtStr(1, StringUtils.list2String(stateList));
/*     */     
/* 206 */     Reward reward = arInfo.getPrize();
/*     */     
/* 208 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);
/*     */     
/* 210 */     AccumRechargeInfo builder = new AccumRechargeInfo();
/* 211 */     builder.setStatus(FetchStatus.Fetched.ordinal());
/* 212 */     builder.setPrize(reward);
/* 213 */     return builder;
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/AccumRecharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */