/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ 
/*    */ public class RefGuildWarCenter
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public NumberRange OpenTime;
/*    */   public int MailId;
/*    */   public int FailMail;
/*    */   public int TakeMail;
/*    */   
/*    */   public boolean Assert() {
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isOpenTime() {
/* 29 */     return this.OpenTime.within(CommTime.getDayOfWeek());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildWarCenter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */