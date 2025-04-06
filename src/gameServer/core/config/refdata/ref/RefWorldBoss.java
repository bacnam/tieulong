/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefWorldBoss
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public String MapId;
/*    */   public float UpMultiple;
/*    */   public float DownMultiple;
/*    */   public int BeginTime;
/*    */   public int EndTime;
/*    */   public int MailId;
/*    */   public int Level;
/*    */   public int MaxHP;
/*    */   public int BossId;
/*    */   
/*    */   public boolean isInOpenHour() {
/* 28 */     return (this.BeginTime <= CommTime.getTodayHour() && this.EndTime > CommTime.getTodayHour());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 33 */     if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (this.BeginTime > this.EndTime) {
/* 37 */       return false;
/*    */     }
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWorldBoss.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */