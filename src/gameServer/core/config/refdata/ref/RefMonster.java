/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefMonster
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public int Level;
/*    */   public int MaxHP;
/*    */   public int ATK;
/*    */   public int DEF;
/*    */   public int RGS;
/*    */   public int Hit;
/*    */   public int Dodge;
/*    */   public int Critical;
/*    */   public int Tenacity;
/*    */   public int RNG;
/*    */   public int SPD;
/*    */   public List<Integer> SkillList;
/*    */   public List<Integer> BuffList;
/*    */   
/*    */   public boolean Assert() {
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 34 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefMonster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */