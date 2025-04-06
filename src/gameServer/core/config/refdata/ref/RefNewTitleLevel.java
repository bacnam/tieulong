/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RefNewTitleLevel
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int Level;
/*    */   public int TitleId;
/*    */   public String Name;
/*    */   public int Quality;
/*    */   public List<Integer> UniformIdList;
/*    */   public List<Integer> UniformCountList;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   @RefField(iskey = false)
/* 26 */   private static Map<Integer, Map<Integer, RefNewTitleLevel>> titleMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 30 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 31 */       return false;
/*    */     }
/* 33 */     Map<Integer, RefNewTitleLevel> map = titleMap.get(Integer.valueOf(this.TitleId));
/* 34 */     if (map == null) {
/* 35 */       map = new HashMap<>();
/* 36 */       titleMap.put(Integer.valueOf(this.TitleId), map);
/*    */     } 
/* 38 */     map.put(Integer.valueOf(this.Level), this);
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 44 */     return true;
/*    */   }
/*    */   
/*    */   public static Map<Integer, RefNewTitleLevel> getTitleByType(int TitleId) {
/* 48 */     return titleMap.get(Integer.valueOf(TitleId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefNewTitleLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */