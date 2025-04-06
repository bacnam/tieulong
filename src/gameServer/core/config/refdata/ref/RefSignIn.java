/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.SignInType;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefSignIn
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Day;
/*    */   public SignInType Type;
/*    */   public List<Integer> UniformitemId;
/*    */   public List<Integer> Count;
/*    */   public int VIPLevel;
/*    */   public int VIPTimes;
/*    */   public int CardID;
/*    */   @RefField(isfield = false)
/* 24 */   public static int DailySignInMaxDay = 1;
/*    */   @RefField(isfield = false)
/* 26 */   public static int DailySignInMinDay = 1;
/*    */   @RefField(isfield = false)
/* 28 */   public static int DailySignInCount = 0;
/*    */   @RefField(isfield = false)
/* 30 */   public static int SignInOpenServerMaxDay = 1;
/*    */   @RefField(isfield = false)
/* 32 */   public static int SignInPrizeMaxDay = 1;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 36 */     if (this.id % 1000 != this.Day) {
/* 37 */       CommLog.error("[DailySignIn]数据出错,ID%1000要等于day");
/* 38 */       return false;
/*    */     } 
/* 40 */     if (this.Type == SignInType.SignIn) {
/* 41 */       DailySignInCount++;
/* 42 */       DailySignInMaxDay = (this.Day > DailySignInMaxDay) ? this.Day : DailySignInMaxDay;
/* 43 */       DailySignInMinDay = (this.Day < DailySignInMinDay) ? this.Day : DailySignInMinDay;
/*    */     } 
/* 45 */     if (this.Type == SignInType.SignInOpenServer && this.Day > SignInOpenServerMaxDay) {
/* 46 */       SignInOpenServerMaxDay = this.Day;
/*    */     }
/* 48 */     if (this.Type == SignInType.SignInPrize && this.Day > SignInPrizeMaxDay) {
/* 49 */       SignInPrizeMaxDay = this.Day;
/*    */     }
/* 51 */     if (!RefAssert.listSize(this.UniformitemId, this.Count, new List[0])) {
/* 52 */       return false;
/*    */     }
/* 54 */     if (!RefAssert.inRef(this.UniformitemId, RefUniformItem.class, "id", new Object[0])) {
/* 55 */       return false;
/*    */     }
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 62 */     if (DailySignInMinDay != 1) {
/* 63 */       CommLog.error("[RefSignIn表DailySignIn]签到天数最小为1");
/* 64 */       return false;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     if (DailySignInCount == 0) {
/* 73 */       CommLog.error("[RefSignIn表DailySignIn]缺少数据,签到的记录数目为0");
/* 74 */       return false;
/*    */     } 
/*    */     
/* 77 */     if (DailySignInMaxDay != DailySignInCount) {
/* 78 */       CommLog.error("[RefSignIn表DailySignIn]活动数目出错,天数出错");
/* 79 */       return false;
/*    */     } 
/* 81 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefSignIn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */