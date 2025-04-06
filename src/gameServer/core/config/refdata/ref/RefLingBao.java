/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.feature.character.PowerUtils;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefLingBao
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Level;
/*    */   public int Star;
/*    */   public String Name;
/*    */   public int Exp;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   public List<Integer> CostIdList;
/*    */   public List<Integer> CostCountList;
/*    */   @RefField(isfield = false)
/* 27 */   public int Power = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 31 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 32 */       return false;
/*    */     }
/* 34 */     this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefLingBao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */