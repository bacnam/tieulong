/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.DressType;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefDress
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public DressType Type;
/*    */   public int CharId;
/*    */   public String Name;
/*    */   public int TimeLimit;
/*    */   public int Material;
/*    */   public int Count;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   
/*    */   public boolean Assert() {
/* 26 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 27 */       return false;
/*    */     }
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */