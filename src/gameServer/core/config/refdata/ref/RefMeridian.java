/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.feature.character.PowerUtils;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RefMeridian
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Num;
/*    */   public int Level;
/*    */   public String Name;
/*    */   public int Material;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   @RefField(isfield = false)
/* 22 */   public int Power = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 26 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 27 */       return false;
/*    */     }
/* 29 */     this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefMeridian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */