/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.SignInType;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefSignIn;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignInPrize
/*     */   extends Activity
/*     */ {
/*     */   public SignInPrize(ActivityBO data) {
/*  32 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {}
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  41 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  46 */     return ActivityType.SignInPrize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/*  56 */     ActivityRecordBO bo = new ActivityRecordBO();
/*  57 */     bo.setPid(player.getPid());
/*  58 */     bo.setAid(this.bo.getId());
/*  59 */     bo.setActivity(getType().toString());
/*  60 */     bo.setExtInt(0, 0);
/*  61 */     bo.setExtInt(1, 0);
/*  62 */     bo.insert();
/*  63 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handDailyRefresh(Player player) {
/*     */     try {
/*  71 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/*  74 */       ActivityRecordBO bo = getRecord(player);
/*  75 */       if (bo == null) {
/*     */         return;
/*     */       }
/*  78 */       if (bo.getExtInt(0) >= RefSignIn.SignInPrizeMaxDay) {
/*     */         return;
/*     */       }
/*  81 */       bo.setExtInt(1, 0);
/*  82 */       bo.saveAll();
/*  83 */       player.pushProto("signInPrizeRefresh", dailySignProto(player));
/*  84 */     } catch (Exception e) {
/*     */       
/*  86 */       e.printStackTrace();
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
/*     */   public signInfo dailySignProto(Player player) {
/*  99 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 100 */     signInfo info = new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1));
/* 101 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getKey(int signInCount) {
/* 110 */     return SignInType.SignInPrize.ordinal() * 1000 + signInCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public signInfo doSignIn(Player player) throws WSException {
/* 121 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 122 */     if (bo.getExtInt(1) == 1) {
/* 123 */       throw new WSException(ErrorCode.Signin_AlreadySigned, "今天已签到过");
/*     */     }
/* 125 */     bo.setExtInt(0, bo.getExtInt(0) + 1);
/* 126 */     bo.setExtInt(1, 1);
/* 127 */     bo.saveAll();
/*     */     
/* 129 */     RefSignIn refSignIn = (RefSignIn)RefDataMgr.get(RefSignIn.class, Integer.valueOf(getKey(bo.getExtInt(0))));
/* 130 */     List<Integer> count = refSignIn.Count;
/* 131 */     if (player.getVipLevel() >= refSignIn.VIPLevel) {
/* 132 */       count = (List<Integer>)refSignIn.Count.stream().map(x -> Integer.valueOf(x.intValue() * paramRefSignIn.VIPTimes)).collect(Collectors.toList());
/*     */     }
/*     */     
/* 135 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(refSignIn.UniformitemId, count, ItemFlow.Activity_SignInDaily);
/*     */     
/* 137 */     return new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), pack);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class signInfo
/*     */   {
/*     */     int count;
/*     */     
/*     */     boolean isSigned;
/*     */     Reward reward;
/*     */     
/*     */     public signInfo(int count, boolean isSigned) {
/* 149 */       this.count = count;
/* 150 */       this.isSigned = isSigned;
/*     */     }
/*     */     
/*     */     public signInfo(int count, boolean isSigned, Reward reward) {
/* 154 */       this.count = count;
/* 155 */       this.isSigned = isSigned;
/* 156 */       this.reward = reward;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/SignInPrize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */