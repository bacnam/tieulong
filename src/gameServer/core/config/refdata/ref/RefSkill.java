/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefSkill
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public String Name;
/*    */   public String Describe;
/*    */   public double Attr;
/*    */   public double AttrAdd;
/*    */   public String Icon;
/*    */   public int Act;
/*    */   public int Effect;
/*    */   public int CD;
/*    */   public int Gold;
/*    */   public int GoldAdd;
/*    */   public int Require;
/*    */   public int CE;
/*    */   public String CastTarget;
/*    */   public String DefAttr;
/*    */   public String SettleType;
/*    */   public int Buff;
/*    */   public List<Integer> ClearBuffList;
/*    */   public int Priority;
/*    */   public String SelectTarget;
/*    */   public String SelectStrategy;
/*    */   public String SelectArea;
/*    */   public String SelectParam;
/*    */   public String Bullet;
/*    */   public int BulletSpeed;
/*    */   
/*    */   public boolean Assert() {
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefSkill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */