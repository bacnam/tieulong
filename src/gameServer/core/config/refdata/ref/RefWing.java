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
/*    */ public class RefWing
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
/*    */   public int Gold;
/*    */   public int GoldExp;
/*    */   public float GoldCrit;
/*    */   public int Material;
/*    */   public int MaterialExp;
/*    */   public float MaterialCrit;
/*    */   @RefField(isfield = false)
/* 31 */   public int Power = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 35 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 36 */       return false;
/*    */     }
/* 38 */     this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */