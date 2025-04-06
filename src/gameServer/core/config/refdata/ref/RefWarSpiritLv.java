/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.feature.character.PowerUtils;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.utils.CommMath;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RefWarSpiritLv
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
/*    */   public int UniformId;
/*    */   public int UniformCount;
/*    */   public int GainExp;
/*    */   public List<Integer> ExpCrit;
/*    */   public List<Integer> ExpCritWeight;
/* 31 */   public static Map<Integer, List<RefWarSpiritLv>> sameLevel = new HashMap<>();
/*    */   
/* 33 */   public int Power = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 37 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 38 */       return false;
/*    */     }
/* 40 */     if (!RefAssert.listSize(this.ExpCrit, this.ExpCritWeight, new List[0])) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     if (sameLevel.get(Integer.valueOf(this.Level)) == null) {
/* 45 */       sameLevel.put(Integer.valueOf(this.Level), new ArrayList<>());
/*    */     }
/* 47 */     if (this.id != 0) {
/* 48 */       ((List<RefWarSpiritLv>)sameLevel.get(Integer.valueOf(this.Level))).add(this);
/* 49 */       this.Power = PowerUtils.getPower(this.AttrTypeList, this.AttrValueList);
/*    */     } 
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 56 */     return true;
/*    */   }
/*    */   
/*    */   public int getCrit() {
/* 60 */     int index = CommMath.getRandomIndexByRate(this.ExpCritWeight);
/* 61 */     return ((Integer)this.ExpCrit.get(index)).intValue();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWarSpiritLv.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */