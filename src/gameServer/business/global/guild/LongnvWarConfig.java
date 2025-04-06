/*    */ package business.global.guild;
/*    */ 
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LongnvWarConfig
/*    */ {
/*    */   public static int challengeCD() {
/* 14 */     return RefDataMgr.getFactor("Longnv_ChallengeCD", 7200);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int overTime() {
/* 21 */     return RefDataMgr.getFactor("Longnv_OverTime", 60);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int fightTime() {
/* 28 */     return RefDataMgr.getFactor("Longnv_FightTime", 1800);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int oneFightTime() {
/* 35 */     return RefDataMgr.getFactor("Longnv_OneFight", 30000);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean pickRewardTime() {
/* 42 */     int begin = RefDataMgr.getFactor("Longnv_RewardBegin", 14);
/* 43 */     int end = RefDataMgr.getFactor("Longnv_RewardEnd", 18);
/* 44 */     NumberRange range = new NumberRange(begin, end);
/* 45 */     return range.within(CommTime.getTodayHour());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int ResultMail() {
/* 52 */     return RefDataMgr.getFactor("Longnv_ResultMail", 30000);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/guild/LongnvWarConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */