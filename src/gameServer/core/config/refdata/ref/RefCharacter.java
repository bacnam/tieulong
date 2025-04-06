/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.CharacterType;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefCharacter
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public CharacterType Type;
/*    */   public String Name;
/*    */   public int RNG;
/*    */   public int SPD;
/*    */   public List<Integer> SkillList;
/*    */   
/*    */   public boolean Assert() {
/* 21 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefCharacter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */