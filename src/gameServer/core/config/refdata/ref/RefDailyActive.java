/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefDailyActive
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange LvlRange;
/*    */   public ArrayList<Integer> Condition;
/*    */   public ArrayList<Integer> RewardID;
/*    */   @RefField(isfield = false)
/* 24 */   public static Map<Integer, Integer> level2refId = Maps.newConcurrentHashMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 28 */     if (!RefAssert.inRef(this.RewardID, RefReward.class, new Object[0])) {
/* 29 */       return false;
/*    */     }
/* 31 */     if (!RefAssert.listSize(this.Condition, this.RewardID, new java.util.List[0])) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     for (int index = this.LvlRange.getLow(); index <= this.LvlRange.getTop(); index++) {
/* 36 */       level2refId.put(Integer.valueOf(index), Integer.valueOf(this.id));
/*    */     }
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 43 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDailyActive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */