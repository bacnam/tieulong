/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefStrengthenInfo
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public double Attribute;
/*    */   public int Gold;
/*    */   public int Material;
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefStrengthenInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */