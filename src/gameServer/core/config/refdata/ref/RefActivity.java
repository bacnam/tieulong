/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.ActivityType;
/*    */ 
/*    */ 
/*    */ public class RefActivity
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public ActivityType id;
/*    */   public int ActNo;
/*    */   public boolean Open;
/*    */   public int BeginTime;
/*    */   public int EndTime;
/*    */   public int CloseTime;
/*    */   public String MailTitle;
/*    */   public String MailContent;
/*    */   public String MailSender;
/*    */   public String Json;
/*    */   
/*    */   public boolean Assert() {
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 29 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefActivity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */