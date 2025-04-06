/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefUseItem
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int ID;
/*    */   public ConstEnum.BoxMatcherType MatcherType;
/*    */   public int Times;
/*    */   public ArrayList<Integer> Probabilities;
/*    */   public ArrayList<Integer> Items;
/*    */   public ArrayList<NumberRange> NumberRange;
/*    */   
/*    */   public Reward reward() {
/* 28 */     Reward reward = new Reward();
/* 29 */     return reward;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 34 */     if (!RefAssert.inRef(this.Items, RefUniformItem.class, new Object[0])) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (!RefAssert.listSize(this.Probabilities, this.Items, new List[] { this.NumberRange })) {
/* 38 */       return false;
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefUseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */