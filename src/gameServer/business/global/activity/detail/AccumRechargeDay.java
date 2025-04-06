/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.FetchStatus;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccumRechargeDay
/*     */   extends Activity
/*     */ {
/*     */   public List<DayRecharge> ardList;
/*     */   
/*     */   private static class DayRecharge
/*     */   {
/*     */     int aid;
/*     */     int day;
/*     */     Reward reward;
/*     */     
/*     */     private DayRecharge() {}
/*     */   }
/*     */   
/*     */   public AccumRechargeDay(ActivityBO bo) {
/*  45 */     super(bo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  51 */     this.ardList = Lists.newArrayList();
/*  52 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  53 */       JsonObject obj = element.getAsJsonObject();
/*  54 */       DayRecharge builder = new DayRecharge(null);
/*  55 */       builder.aid = obj.get("aid").getAsInt();
/*  56 */       builder.day = obj.get("day").getAsInt();
/*  57 */       builder.reward = new Reward(obj.get("items").getAsJsonArray());
/*  58 */       this.ardList.add(builder);
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
/*  76 */     clearActRecord();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  82 */     return ActivityType.AccumDayRecharge;
/*     */   }
/*     */   
/*     */   public void handleRecharge(Player player) {
/*  86 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/*  89 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*  90 */     if (bo.getExtInt(0) != 0) {
/*     */       return;
/*     */     }
/*  93 */     bo.saveExtInt(0, 1);
/*  94 */     bo.saveExtInt(1, bo.getExtInt(1) + 1);
/*  95 */     player.pushProto("AccumRechargeDay", getList(player));
/*     */   }
/*     */   
/*     */   public DayRechargeProtocol pickReward(Player player, int rewardId) throws WSException {
/*  99 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 100 */     List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 101 */     if (rewardList.contains(Integer.valueOf(rewardId))) {
/* 102 */       throw new WSException(ErrorCode.AccumRechargeDay_AlreadyPick, "奖励已领取");
/*     */     }
/* 104 */     DayRecharge recharge = null;
/* 105 */     Optional<DayRecharge> find = this.ardList.stream().filter(x -> (x.aid == paramInt)).findFirst();
/* 106 */     if (find.isPresent()) {
/* 107 */       recharge = find.get();
/*     */     } else {
/* 109 */       throw new WSException(ErrorCode.AccumRechargeDay_NotFound, "奖励未找到");
/*     */     } 
/*     */     
/* 112 */     if (bo.getExtInt(1) < recharge.day) {
/* 113 */       throw new WSException(ErrorCode.AccumRechargeDay_NotEnough, "天数不足");
/*     */     }
/*     */     
/* 116 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(recharge.reward, ItemFlow.AccumRechargeDay);
/* 117 */     rewardList.add(Integer.valueOf(rewardId));
/* 118 */     bo.saveExtStr(0, StringUtils.list2String(rewardList));
/* 119 */     return toProtocol(player, recharge);
/*     */   }
/*     */   
/*     */   public static class DayRechargeProtocol {
/*     */     int aid;
/*     */     int nowDay;
/*     */     int requireDay;
/*     */     Reward reward;
/*     */     int status;
/*     */   }
/*     */   
/*     */   public List<DayRechargeProtocol> getList(Player player) {
/* 131 */     List<DayRechargeProtocol> list = new ArrayList<>();
/* 132 */     this.ardList.stream().forEach(x -> {
/*     */           DayRechargeProtocol protocol = toProtocol(paramPlayer, x);
/*     */           paramList.add(protocol);
/*     */         });
/* 136 */     return list;
/*     */   }
/*     */   
/*     */   public DayRechargeProtocol toProtocol(Player player, DayRecharge recharge) {
/* 140 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 141 */     List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 142 */     DayRechargeProtocol protocol = new DayRechargeProtocol();
/* 143 */     protocol.aid = recharge.aid;
/* 144 */     protocol.nowDay = bo.getExtInt(1);
/* 145 */     protocol.requireDay = recharge.day;
/* 146 */     protocol.reward = recharge.reward;
/* 147 */     if (rewardList.contains(Integer.valueOf(recharge.aid))) {
/* 148 */       protocol.status = FetchStatus.Fetched.ordinal();
/* 149 */     } else if (protocol.nowDay >= protocol.requireDay) {
/* 150 */       protocol.status = FetchStatus.Can.ordinal();
/*     */     } else {
/* 152 */       protocol.status = FetchStatus.Cannot.ordinal();
/*     */     } 
/* 154 */     return protocol;
/*     */   }
/*     */   
/*     */   public void dailyRefresh(Player player) {
/*     */     try {
/* 159 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/* 162 */       ActivityRecordBO bo = getOrCreateRecord(player);
/* 163 */       if (bo.getExtInt(0) == 0) {
/*     */         return;
/*     */       }
/* 166 */       bo.saveExtInt(0, 0);
/* 167 */     } catch (Exception e) {
/*     */       
/* 169 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/AccumRechargeDay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */