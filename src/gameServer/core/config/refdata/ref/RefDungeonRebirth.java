/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefDungeonRebirth
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public List<Attribute> AttrList;
/*    */   public List<Integer> AttrValue;
/*    */   public int Cost;
/*    */   
/*    */   public boolean Assert() {
/* 20 */     if (!RefAssert.listSize(this.AttrList, this.AttrValue, new List[0])) {
/* 21 */       return false;
/*    */     }
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 28 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDungeonRebirth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */