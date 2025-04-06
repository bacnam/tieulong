/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefWarSpiritStar
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int WarspiritId;
/*    */   public int Level;
/*    */   public int WarspiritNum;
/*    */   public int ExtraId;
/*    */   public int ExtraCount;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   public int SkillLevel;
/*    */   public int NeedLv;
/* 29 */   public static Map<Integer, Map<Integer, RefWarSpiritStar>> spiritMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 33 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 44 */     for (RefWarSpirit refspirit : RefDataMgr.getAll(RefWarSpirit.class).values()) {
/* 45 */       spiritMap.put(Integer.valueOf(refspirit.id), new HashMap<>());
/*    */     }
/*    */     
/* 48 */     for (RefWarSpiritStar ref : all.values()) {
/* 49 */       ((Map<Integer, RefWarSpiritStar>)spiritMap.get(Integer.valueOf(ref.WarspiritId))).put(Integer.valueOf(ref.Level), ref);
/*    */     }
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWarSpiritStar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */