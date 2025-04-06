/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ public class RefDungeon
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Exp;
/*    */   public int Gold;
/*    */   public int Drop;
/*    */   public int BossDrop;
/*    */   public int BossID;
/*    */   
/*    */   public boolean Assert() {
/* 19 */     if (!RefAssert.inRef(Integer.valueOf(this.Drop), RefReward.class, new Object[0])) {
/* 20 */       return false;
/*    */     }
/* 22 */     if (!RefAssert.inRef(Integer.valueOf(this.BossDrop), RefReward.class, new Object[0])) {
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDungeon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */