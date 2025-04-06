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
/*     */ import core.server.OpenSeverTime;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignInOpenServer
/*     */   extends Activity
/*     */ {
/*     */   public SignInOpenServer(ActivityBO data) {
/*  34 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {}
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  43 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  48 */     return ActivityType.SignInOpenServer;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBeginTime() {
/*  53 */     return OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("SevenDayActivityOpenTime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndTime() {
/*  58 */     return OpenSeverTime.getInstance().getOpenZeroTime() + 604800 - 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCloseTime() {
/*  63 */     return OpenSeverTime.getInstance().getOpenZeroTime() + 604800 + RefDataMgr.getFactor("SevenDayActivitycloseOffset", 86400);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/*  73 */     ActivityRecordBO bo = new ActivityRecordBO();
/*  74 */     bo.setPid(player.getPid());
/*  75 */     bo.setAid(this.bo.getId());
/*  76 */     bo.setActivity(getType().toString());
/*  77 */     bo.setExtInt(0, 0);
/*  78 */     bo.setExtInt(1, 0);
/*  79 */     bo.insert();
/*  80 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handDailyRefresh(Player player) {
/*     */     try {
/*  88 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/*  91 */       ActivityRecordBO bo = getRecord(player);
/*  92 */       if (bo == null) {
/*     */         return;
/*     */       }
/*  95 */       if (bo.getExtInt(1) == 0) {
/*     */         return;
/*     */       }
/*  98 */       if (bo.getExtInt(0) >= RefSignIn.SignInOpenServerMaxDay) {
/*     */         return;
/*     */       }
/* 101 */       bo.setExtInt(1, 0);
/* 102 */       bo.saveAll();
/* 103 */       player.pushProto("signInOpenServerRefresh", dailySignProto(player));
/* 104 */     } catch (Exception e) {
/*     */       
/* 106 */       e.printStackTrace();
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
/* 119 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 120 */     signInfo info = new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1));
/* 121 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getKey(int signInCount) {
/* 130 */     return SignInType.SignInOpenServer.ordinal() * 1000 + signInCount;
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
/* 141 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 142 */     if (bo.getExtInt(1) == 1) {
/* 143 */       throw new WSException(ErrorCode.Signin_AlreadySigned, "今天已签到过");
/*     */     }
/* 145 */     bo.setExtInt(0, bo.getExtInt(0) + 1);
/* 146 */     bo.setExtInt(1, 1);
/* 147 */     bo.saveAll();
/*     */     
/* 149 */     RefSignIn refSignIn = (RefSignIn)RefDataMgr.get(RefSignIn.class, Integer.valueOf(getKey(bo.getExtInt(0))));
/* 150 */     List<Integer> count = refSignIn.Count;
/* 151 */     if (player.getVipLevel() >= refSignIn.VIPLevel) {
/* 152 */       count = (List<Integer>)refSignIn.Count.stream().map(x -> Integer.valueOf(x.intValue() * paramRefSignIn.VIPTimes)).collect(Collectors.toList());
/*     */     }
/*     */     
/* 155 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(refSignIn.UniformitemId, count, ItemFlow.Activity_SignInDaily);
/*     */     
/* 157 */     return new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), pack);
/*     */   }
/*     */   
/*     */   private static class signInfo
/*     */   {
/*     */     int count;
/*     */     boolean isSigned;
/*     */     Reward reward;
/*     */     
/*     */     public signInfo(int count, boolean isSigned) {
/* 167 */       this.count = count;
/* 168 */       this.isSigned = isSigned;
/*     */     }
/*     */     
/*     */     public signInfo(int count, boolean isSigned, Reward reward) {
/* 172 */       this.count = count;
/* 173 */       this.isSigned = isSigned;
/* 174 */       this.reward = reward;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/SignInOpenServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */