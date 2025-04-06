/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RefGuildSkillLevel
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int SkillID;
/*    */   public int SkillLevel;
/*    */   public int ResearchCost;
/*    */   public int NeedGuildLevel;
/*    */   public List<Integer> UpgradeCostList;
/*    */   public List<Integer> CostItemList;
/*    */   
/*    */   public boolean Assert() {
/* 21 */     if (!RefAssert.inRef(Integer.valueOf(this.SkillID), RefGuildSkill.class, new Object[0])) {
/* 22 */       return false;
/*    */     }
/*    */     
/* 25 */     if (!RefAssert.listSize(this.UpgradeCostList, this.CostItemList, new List[0])) {
/* 26 */       return false;
/*    */     }
/* 28 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 33 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildSkillLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */