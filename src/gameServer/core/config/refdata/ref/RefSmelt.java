/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Quality;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefSmelt
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public String id;
/*    */   public int Level;
/*    */   public Quality Quality;
/*    */   public int Strengthen;
/*    */   public int Gold;
/*    */   public int RedPiece;
/*    */   
/*    */   public boolean Assert() {
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefSmelt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */