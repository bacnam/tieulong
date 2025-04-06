/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ public class RefNewTitle
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int level;
/*    */   public String Name;
/*    */   public int Quality;
/*    */   public int ActiveId;
/*    */   public int ActiveCount;
/*    */   
/*    */   public boolean Assert() {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 23 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefNewTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */