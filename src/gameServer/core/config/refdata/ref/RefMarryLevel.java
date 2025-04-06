/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ public class RefMarryLevel
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int NeedExp;
/*    */   public int RewardId;
/*    */   
/*    */   public boolean Assert() {
/* 15 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 21 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefMarryLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */