/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefBuff
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Double> AttrValueList;
/*    */   public List<Double> AttrIncList;
/*    */   public List<Boolean> AttrFixedList;
/*    */   public int Time;
/*    */   public int CD;
/*    */   public int HPCondition;
/*    */   
/*    */   public boolean Assert() {
/* 24 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[] { this.AttrIncList, this.AttrFixedList })) {
/* 25 */       return false;
/*    */     }
/* 27 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 32 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefBuff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */