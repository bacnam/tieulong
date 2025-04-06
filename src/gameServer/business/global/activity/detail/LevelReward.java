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
/*     */ public class LevelReward
/*     */   extends Activity
/*     */ {
/*     */   private List<Level> rewardList;
/*     */   
/*     */   private static class Level
/*     */   {
/*     */     int aid;
/*     */     int level;
/*     */     Reward reward;
/*     */     
/*     */     private Level() {}
/*     */   }
/*     */   
/*     */   public LevelReward(ActivityBO bo) {
/*  45 */     super(bo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  51 */     this.rewardList = Lists.newArrayList();
/*  52 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  53 */       JsonObject obj = element.getAsJsonObject();
/*  54 */       Level builder = new Level(null);
/*  55 */       builder.aid = obj.get("aid").getAsInt();
/*  56 */       builder.level = obj.get("level").getAsInt();
/*  57 */       builder.reward = new Reward(obj.get("items").getAsJsonArray());
/*  58 */       this.rewardList.add(builder);
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
/*     */   public ActivityType getType() {
/*  81 */     return ActivityType.LevelReward;
/*     */   }
/*     */   
/*     */   public void handLevelChange(Player player) {
/*  85 */     if (getStatus() != ActivityStatus.Open) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     ActivityRecordBO bo = getOrCreateRecord(player);
/*  90 */     if (bo.getExtInt(0) >= player.getLv()) {
/*     */       return;
/*     */     }
/*  93 */     bo.saveExtInt(0, player.getLv());
/*     */   }
/*     */   
/*     */   public static class LevelRewardProtocol {
/*     */     int aid;
/*     */     int level;
/*     */     Reward reward;
/*     */     int status;
/*     */   }
/*     */   
/*     */   public List<LevelRewardProtocol> getList(Player player) {
/* 104 */     List<LevelRewardProtocol> list = new ArrayList<>();
/* 105 */     this.rewardList.stream().forEach(x -> {
/*     */           LevelRewardProtocol protocol = toProtocol(paramPlayer, x);
/*     */           paramList.add(protocol);
/*     */         });
/* 109 */     return list;
/*     */   }
/*     */   
/*     */   public LevelRewardProtocol toProtocol(Player player, Level recharge) {
/* 113 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 114 */     List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 115 */     LevelRewardProtocol protocol = new LevelRewardProtocol();
/* 116 */     protocol.aid = recharge.aid;
/* 117 */     protocol.level = recharge.level;
/* 118 */     protocol.reward = recharge.reward;
/* 119 */     if (rewardList.contains(Integer.valueOf(recharge.aid))) {
/* 120 */       protocol.status = FetchStatus.Fetched.ordinal();
/* 121 */     } else if (bo.getExtInt(0) >= recharge.level) {
/* 122 */       protocol.status = FetchStatus.Can.ordinal();
/*     */     } else {
/* 124 */       protocol.status = FetchStatus.Cannot.ordinal();
/*     */     } 
/* 126 */     return protocol;
/*     */   }
/*     */   
/*     */   public LevelRewardProtocol pickReward(Player player, int rewardId) throws WSException {
/* 130 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 131 */     List<Integer> rewardList = StringUtils.string2Integer(bo.getExtStr(0));
/* 132 */     if (rewardList.contains(Integer.valueOf(rewardId))) {
/* 133 */       throw new WSException(ErrorCode.Already_Picked, "奖励已领取");
/*     */     }
/* 135 */     Level recharge = null;
/* 136 */     Optional<Level> find = this.rewardList.stream().filter(x -> (x.aid == paramInt)).findFirst();
/* 137 */     if (find.isPresent()) {
/* 138 */       recharge = find.get();
/*     */     } else {
/* 140 */       throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励未找到");
/*     */     } 
/*     */     
/* 143 */     if (bo.getExtInt(0) < recharge.level) {
/* 144 */       throw new WSException(ErrorCode.Not_Enough, "等级不足");
/*     */     }
/*     */     
/* 147 */     ((PlayerItem)player.getFeature(PlayerItem.class)).gain(recharge.reward, ItemFlow.LevelReward);
/* 148 */     rewardList.add(Integer.valueOf(rewardId));
/* 149 */     bo.saveExtStr(0, StringUtils.list2String(rewardList));
/* 150 */     return toProtocol(player, recharge);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/* 161 */     ActivityRecordBO bo = new ActivityRecordBO();
/* 162 */     bo.setPid(player.getPid());
/* 163 */     bo.setAid(this.bo.getId());
/* 164 */     bo.setActivity(getType().toString());
/* 165 */     bo.setExtInt(0, player.getLv());
/* 166 */     bo.insert();
/* 167 */     return bo;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/LevelReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */