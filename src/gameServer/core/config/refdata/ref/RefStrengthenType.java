/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.EquipPos;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefStrengthenType
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public EquipPos id;
/*    */   public Attribute StrengthenType;
/*    */   public Attribute GemType;
/*    */   public List<Attribute> ArtificeType;
/*    */   
/*    */   public boolean Assert() {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 28 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefStrengthenType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */