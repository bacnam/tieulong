/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefWarSpirit
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public int RNG;
/*    */   public int SPD;
/*    */   public int ActiveId;
/*    */   public int ActiveCount;
/*    */   public int StarMaterial;
/*    */   public List<Integer> SkillList;
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWarSpirit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */