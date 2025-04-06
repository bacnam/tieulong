/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ 
/*    */ 
/*    */ public class RefGuildWarPersonReward
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange Num;
/*    */   public int Reward;
/*    */   
/*    */   public boolean Assert() {
/* 19 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 24 */     return true;
/*    */   }
/*    */   
/*    */   public static Reward getReward(int num) {
/* 28 */     for (RefGuildWarPersonReward ref : RefDataMgr.getAll(RefGuildWarPersonReward.class).values()) {
/* 29 */       if (ref.Num.within(num)) {
/* 30 */         return ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Reward))).genReward();
/*    */       }
/*    */     } 
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildWarPersonReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */