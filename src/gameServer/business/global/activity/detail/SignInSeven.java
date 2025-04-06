/*     */ package business.global.activity.detail;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerCurrency;
/*     */ import business.player.feature.PlayerItem;
/*     */ import business.player.item.Reward;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.PrizeType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.common.utils.Lists;
/*     */ import com.zhonglian.server.common.utils.StringUtils;
/*     */ import com.zhonglian.server.http.server.RequestException;
/*     */ import com.zhonglian.server.logger.flow.ItemFlow;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignInSeven
/*     */   extends Activity
/*     */ {
/*     */   public List<SignIn> ardList;
/*     */   private int ReSignCost;
/*     */   
/*     */   private static class SignIn
/*     */   {
/*     */     int aid;
/*     */     int day;
/*     */     Reward reward;
/*     */     
/*     */     private SignIn() {}
/*     */   }
/*     */   
/*     */   public SignInSeven(ActivityBO data) {
/*  47 */     super(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(JsonObject json) throws WSException {
/*  52 */     this.ardList = Lists.newArrayList();
/*  53 */     for (JsonElement element : json.get("awards").getAsJsonArray()) {
/*  54 */       JsonObject obj = element.getAsJsonObject();
/*  55 */       SignIn builder = new SignIn(null);
/*  56 */       builder.aid = obj.get("aid").getAsInt();
/*  57 */       builder.day = obj.get("day").getAsInt();
/*  58 */       builder.reward = new Reward(obj.get("items").getAsJsonArray());
/*  59 */       this.ardList.add(builder);
/*     */     } 
/*     */     
/*  62 */     this.ReSignCost = json.get("ReSignCost").getAsInt();
/*     */   }
/*     */ 
/*     */   
/*     */   public String check(JsonObject json) throws RequestException {
/*  67 */     return "ok";
/*     */   }
/*     */ 
/*     */   
/*     */   public ActivityType getType() {
/*  72 */     return ActivityType.SignInSeven;
/*     */   }
/*     */   
/*     */   public int getDay() {
/*  76 */     int second = CommTime.nowSecond() - CommTime.getZeroClockS(getBeginTime());
/*  77 */     return second / 86400;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handDailyRefresh() {
/*     */     try {
/*  85 */       if (getStatus() != ActivityStatus.Open) {
/*     */         return;
/*     */       }
/*  88 */       this.bo.setExtInt(0, getDay());
/*  89 */       this.bo.saveAll();
/*     */       
/*  91 */       for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
/*  92 */         player.pushProto("signInSevenRefresh", dailySignProto(player));
/*     */       }
/*     */     }
/*  95 */     catch (Exception e) {
/*  96 */       e.printStackTrace();
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
/* 109 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 110 */     int today = this.bo.getExtInt(0) + 1;
/* 111 */     signInfo info = new signInfo(today, bo.getExtStr(0), this.ReSignCost, this.ardList);
/* 112 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SignIn getKey(int day) {
/* 121 */     for (SignIn signin : this.ardList) {
/* 122 */       if (signin.day == day) {
/* 123 */         return signin;
/*     */       }
/*     */     } 
/*     */     
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public signInfo doSignIn(Player player, int day) throws WSException {
/* 138 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 139 */     List<Integer> list = StringUtils.string2Integer(bo.getExtStr(0));
/* 140 */     int today = this.bo.getExtInt(0) + 1;
/*     */     
/* 142 */     if (list.contains(Integer.valueOf(day))) {
/* 143 */       throw new WSException(ErrorCode.SignIn_AlreadyPick, "奖励已领取");
/*     */     }
/*     */     
/* 146 */     if (today != day) {
/* 147 */       throw new WSException(ErrorCode.SignIn_OnlyToday, "只能签到当天");
/*     */     }
/*     */ 
/*     */     
/* 151 */     SignIn signIn = getKey(today);
/* 152 */     if (signIn == null) {
/* 153 */       throw new WSException(ErrorCode.SignIn_NotFound, "签到未找到");
/*     */     }
/* 155 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(signIn.reward, ItemFlow.Activity_SignInDaily);
/* 156 */     list.add(Integer.valueOf(day));
/* 157 */     bo.saveExtStr(0, StringUtils.list2String(list));
/*     */     
/* 159 */     return new signInfo(today, bo.getExtStr(0), pack);
/*     */   }
/*     */   
/*     */   public signInfo reSign(Player player, int day) throws WSException {
/* 163 */     ActivityRecordBO bo = getOrCreateRecord(player);
/* 164 */     List<Integer> list = StringUtils.string2Integer(bo.getExtStr(0));
/*     */     
/* 166 */     int today = this.bo.getExtInt(0) + 1;
/* 167 */     if (list.contains(Integer.valueOf(day))) {
/* 168 */       throw new WSException(ErrorCode.SignIn_AlreadyPick, "奖励已领取");
/*     */     }
/*     */     
/* 171 */     if (today <= day) {
/* 172 */       throw new WSException(ErrorCode.SignIn_OnlyToday, "只能补签以前的日子");
/*     */     }
/*     */     
/* 175 */     if (!((PlayerCurrency)player.getFeature(PlayerCurrency.class)).checkAndConsume(PrizeType.Crystal, this.ReSignCost, ItemFlow.ReSign)) {
/* 176 */       throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
/*     */     }
/*     */     
/* 179 */     SignIn signIn = getKey(today);
/* 180 */     if (signIn == null) {
/* 181 */       throw new WSException(ErrorCode.SignIn_NotFound, "签到未找到");
/*     */     }
/* 183 */     Reward pack = ((PlayerItem)player.getFeature(PlayerItem.class)).gain(signIn.reward, ItemFlow.Activity_SignInDaily);
/* 184 */     list.add(Integer.valueOf(day));
/* 185 */     bo.saveExtStr(0, StringUtils.list2String(list));
/*     */     
/* 187 */     return new signInfo(today, bo.getExtStr(0), pack);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class signInfo
/*     */   {
/*     */     int day;
/*     */     String gain;
/*     */     Reward reward;
/*     */     int reSignCost;
/*     */     public List<SignInSeven.SignIn> ardList;
/*     */     
/*     */     public signInfo(int day, String gain, int reSignCost, List<SignInSeven.SignIn> ardList) {
/* 200 */       this.day = day;
/* 201 */       this.gain = gain;
/* 202 */       this.reSignCost = reSignCost;
/* 203 */       this.ardList = ardList;
/*     */     }
/*     */     
/*     */     public signInfo(int day, String gain, Reward reward) {
/* 207 */       this.day = day;
/* 208 */       this.gain = gain;
/* 209 */       this.reward = reward;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onOpen() {
/* 215 */     if (this.bo.getExtInt(0) < 0) {
/* 216 */       this.bo.saveExtInt(0, 0);
/*     */     }
/*     */   }
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
/* 229 */     clearActRecord();
/* 230 */     this.bo.saveExtInt(0, 0);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/SignInSeven.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */