/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ 
/*    */ public class RefRedPacket
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange Range;
/*    */   public int PickNum;
/*    */   public int Money;
/*    */   public NumberRange PickRange;
/*    */   public String Name;
/*    */   
/*    */   public boolean Assert() {
/* 19 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 24 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefRedPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */