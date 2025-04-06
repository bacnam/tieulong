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
/*     */ import core.network.proto.AccumConsumeInfo;
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
/*     */ public class AccumConsume
/*     */   extends Activity
/*     */ {
/*     */   public List<AccumConsumeInfo> arList;
/*     */   
/*     */   public AccumConsume(ActivityBO data) {
/*  41 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  49 */     this.arList = Lists.newArrayList();
/*  50 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  51 */       JsonObject obj = element.getAsJsonObject();
/*  52 */       AccumConsumeInfo builder = new AccumConsumeInfo();
/*  53 */       builder.setAwardId(obj.get("aid").getAsInt());
/*  54 */       builder.setConsume(obj.get("consume").getAsInt());
/*  55 */       builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
/*  56 */       this.arList.add(builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  62 */     JsonArray awardArray = json.get("awards").getAsJsonArray();
/*  63 */     if (awardArray.size() <= 0) {
/*  64 */       throw new RequestException(4000001, "配置的奖励条数要大于0", new Object[0]);
/*     */     }
/*  66 */     StringBuilder desc = new StringBuilder("【累充配置】\n");
/*  67 */     for (JsonElement element : awardArray) {
/*  68 */       JsonObject obj = element.getAsJsonObject();
/*  69 */       desc.append("奖励Id:").append(obj.get("aid").getAsInt()).append(",");
/*  70 */       int recharge = obj.get("consume").getAsInt();
/*  71 */       if (recharge <= 0) {
/*  72 */         throw new RequestException(4000001, "充值金额必须大于0", new Object[0]);
/*     */       }
/*  74 */       desc.append("充值金额:").append(recharge).append(",");
/*  75 */       desc.append("奖励信息:").append(checkAndSubscribeItem(obj.get("items").getAsJsonArray())).append("\n");
/*     */     } 
/*  77 */     return desc.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  82 */     return ActivityType.AccumConsume;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onClosed() {
/* 102 */     clearActRecord();
/* 103 */     CommLog.info("累消活动关闭");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handlePlayerChange(Player player, int money) {
/* 113 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/* 116 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 118 */     bo.setExtInt(0, bo.getExtInt(0) + money);
/*     */     
/* 120 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 122 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 124 */     this.arList.forEach(x -> {
/*     */           if (!paramList1.contains(Integer.valueOf(x.getAwardId())) && paramActivityRecordBO.getExtInt(0) >= x.getConsume()) {
/*     */             paramList1.add(Integer.valueOf(x.getAwardId()));
/*     */             paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
/*     */           } 
/*     */         });
/* 130 */     bo.setExtStr(0, StringUtils.list2String(awardList));
/* 131 */     bo.setExtStr(1, StringUtils.list2String(stateList));
/* 132 */     bo.saveAll();
/*     */     
/* 134 */     player.pushProto("totalConsume", accumRechargeProto(player));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public accumRechargeInfoP accumRechargeProto(Player player) {
/* 144 */     ActivityRecordBO bo = getRecord(player);
/*     */     
/* 146 */     int recharge = 0;
/* 147 */     if (bo != null) {
/* 148 */       recharge = bo.getExtInt(0);
/*     */     }
/*     */     
/* 151 */     List<Integer> awardList = null, stateList = null;
/* 152 */     if (bo != null) {
/* 153 */       awardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 154 */       stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     } 
/* 156 */     List<AccumConsumeInfo> accumerecharges = Lists.newArrayList();
/*     */     
/* 158 */     for (AccumConsumeInfo ar : this.arList) {
/* 159 */       AccumConsumeInfo builder = new AccumConsumeInfo();
/* 160 */       builder.setAwardId(ar.getAwardId());
/* 161 */       builder.setConsume(ar.getConsume());
/* 162 */       builder.setPrize(ar.getPrize());
/* 163 */       int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
/* 164 */       if (index != -1) {
/* 165 */         builder.setStatus(((Integer)stateList.get(index)).intValue());
/*     */       } else {
/* 167 */         builder.setStatus(FetchStatus.Cannot.ordinal());
/*     */       } 
/* 169 */       accumerecharges.add(builder);
/*     */     } 
/* 171 */     return new accumRechargeInfoP(recharge, accumerecharges, null);
/*     */   }
/*     */   
/*     */   private class accumRechargeInfoP
/*     */   {
/*     */     int consume;
/*     */     List<AccumConsumeInfo> accumerecharges;
/*     */     
/*     */     private accumRechargeInfoP(int consume, List<AccumConsumeInfo> accumerecharges) {
/* 180 */       this.consume = consume;
/* 181 */       this.accumerecharges = accumerecharges;
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
/* 195 */     Map<Integer, AccumConsumeInfo> arMap = Maps.list2Map(AccumConsumeInfo::getAwardId, this.arList);
/*     */     
/* 197 */     AccumConsumeInfo arInfo = arMap.get(Integer.valueOf(awardId));
/* 198 */     if (arInfo == null) {
/* 199 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 202 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*     */     
/* 204 */     List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 206 */     List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));
/*     */     
/* 208 */     int index = awardList.indexOf(Integer.valueOf(awardId));
/* 209 */     if (index == -1) {
/* 210 */       throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[] { Integer.valueOf(bo.getExtInt(0)), Integer.valueOf(arInfo.getConsume()) });
/*     */     }
/* 212 */     int state = ((Integer)stateList.get(index)).intValue();
/* 213 */     if (state != FetchStatus.Can.ordinal()) {
/* 214 */       throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[] { Integer.valueOf(awardId) });
/*     */     }
/*     */     
/* 217 */     stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
/* 218 */     bo.saveExtStr(1, StringUtils.list2String(stateList));
/*     */     
/* 220 */     Reward reward = arInfo.getPrize();
/*     */     
/* 222 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);
/*     */     
/* 224 */     AccumRechargeInfo builder = new AccumRechargeInfo();
/* 225 */     builder.setAwardId(awardId);
/* 226 */     builder.setStatus(FetchStatus.Fetched.ordinal());
/* 227 */     builder.setPrize(reward);
/* 228 */     return builder;
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/AccumConsume.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */