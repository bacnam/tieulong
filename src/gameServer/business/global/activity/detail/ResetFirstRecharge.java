/*    */ package business.global.activity.detail;
/*    */ 
/*    */ import business.global.activity.Activity;
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.recharge.RechargeMgr;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefRecharge;
/*    */ import core.database.game.bo.ActivityBO;
/*    */ import core.server.OpenSeverTime;
/*    */ 
/*    */ public class ResetFirstRecharge extends Activity {
/*    */   private int begin;
/*    */   
/*    */   public ResetFirstRecharge(ActivityBO bo) {
/* 19 */     super(bo);
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.begin = 0;
/*    */   }
/*    */   
/*    */   public void load(JsonObject json) throws WSException {
/* 27 */     this.begin = json.get("begin").getAsInt();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getBeginTime() {
/* 33 */     return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onOpen() {
/* 38 */     for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
/* 39 */       if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
/*    */         continue;
/*    */       }
/* 42 */       RechargeMgr.getInstance().reset(ref.id);
/*    */     } 
/*    */     
/* 45 */     ((FirstRecharge)ActivityMgr.getActivity(FirstRecharge.class)).clearAllRecharge();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onEnd() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void onClosed() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ActivityType getType() {
/* 63 */     return ActivityType.ResetFirstRecharge;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/detail/ResetFirstRecharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */