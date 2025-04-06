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
/*     */ import com.zhonglian.server.common.utils.CommTime;
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
/*     */ public class DailySign
/*     */   extends Activity
/*     */ {
/*     */   public DailySign(ActivityBO data) {
/*  33 */     super(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {}
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  42 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  47 */     return ActivityType.DailySign;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityStatus getStatus() {
/*  52 */     return ActivityStatus.Open;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean needNotify(Player player) {
/*  57 */     ActivityRecordBO bo = (ActivityRecordBO)this.playerActRecords.get(Long.valueOf(player.getPid()));
/*  58 */     if (bo == null) {
/*  59 */       return true;
/*     */     }
/*  61 */     if (bo.getExtInt(1) == 0) {
/*  62 */       return true;
/*     */     }
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ActivityRecordBO createPlayerActRecord(Player player) {
/*  74 */     ActivityRecordBO bo = new ActivityRecordBO();
/*  75 */     bo.setPid(player.getPid());
/*  76 */     bo.setAid(this.bo.getId());
/*  77 */     bo.setActivity(getType().toString());
/*  78 */     bo.setExtInt(0, 0);
/*  79 */     bo.setExtInt(1, 0);
/*  80 */     bo.insert();
/*  81 */     return bo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handDailyRefresh(Player player, int day) {
/*     */     try {
/*  89 */       ActivityRecordBO bo = getOrCreateRecord(player);
/*  90 */       if (bo.getExtInt(1) == 0) {
/*     */         return;
/*     */       }
/*  93 */       if (CommTime.getFirstDayOfMonth() - day <= 0) {
/*  94 */         bo.setExtInt(0, 0);
/*     */       }
/*  96 */       if (bo.getExtInt(0) >= RefSignIn.DailySignInMaxDay) {
/*  97 */         bo.setExtInt(0, 0);
/*     */       }
/*  99 */       bo.setExtInt(1, 0);
/* 100 */       bo.saveAll();
/* 101 */       player.pushProto("dailySignRefresh", dailySignProto(player));
/* 102 */     } catch (Exception e) {
/*     */       
/* 104 */       e.printStackTrace();
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
/* 117 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 118 */     signInfo info = new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1));
/* 119 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getKey(int signInCount) {
/* 128 */     return SignInType.SignIn.ordinal() * 1000 + signInCount;
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
/* 139 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 140 */     if (bo.getExtInt(1) == 1) {
/* 141 */       throw new WSException(ErrorCode.Signin_AlreadySigned, "今天已签到过");
/*     */     }
/* 143 */     bo.setExtInt(0, bo.getExtInt(0) + 1);
/* 144 */     bo.setExtInt(1, 1);
/* 145 */     bo.saveAll();
/*     */     
/* 147 */     RefSignIn refSignIn = (RefSignIn)RefDataMgr.get(RefSignIn.class, Integer.valueOf(getKey(bo.getExtInt(0))));
/* 148 */     List<Integer> count = refSignIn.Count;
/* 149 */     if (player.getVipLevel() >= refSignIn.VIPLevel) {
/* 150 */       count = (List<Integer>)refSignIn.Count.stream().map(x -> Integer.valueOf(x.intValue() * paramRefSignIn.VIPTimes)).collect(Collectors.toList());
/*     */     }
/* 152 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(refSignIn.UniformitemId, count, ItemFlow.Activity_SignInDaily);
/*     */     
/* 154 */     return new signInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), pack);
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
/* 166 */       this.count = count;
/* 167 */       this.isSigned = isSigned;
/*     */     }
/*     */     
/*     */     public signInfo(int count, boolean isSigned, Reward reward) {
/* 171 */       this.count = count;
/* 172 */       this.isSigned = isSigned;
/* 173 */       this.reward = reward;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOpen() {}
/*     */   
/*     */   public void onEnd() {}
/*     */   
/*     */   public void onClosed() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/DailySign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */