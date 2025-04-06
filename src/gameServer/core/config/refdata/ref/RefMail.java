/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefMail
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Title;
/*    */   public String Content;
/*    */   public int RewardId;
/*    */   public int ExistTime;
/*    */   public int PickUpExistTime;
/*    */   
/*    */   public boolean Assert() {
/* 22 */     if (!RefAssert.inRef(Integer.valueOf(this.RewardId), RefReward.class, new Object[] { Integer.valueOf(0) })) {
/* 23 */       return false;
/*    */     }
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 30 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */