/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.Title;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RefTitle
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public Title Type;
/*    */   public String Name;
/*    */   public NumberRange NumRange;
/*    */   public int Quality;
/*    */   public int TimeLimit;
/*    */   public List<Attribute> AttrTypeList;
/*    */   public List<Integer> AttrValueList;
/*    */   public int MailId;
/*    */   public ConstEnum.AchieveReset Reset;
/*    */   @RefField(iskey = false)
/* 30 */   private static Map<Title, RefTitle> titleMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 34 */     if (!RefAssert.listSize(this.AttrTypeList, this.AttrValueList, new List[0])) {
/* 35 */       return false;
/*    */     }
/* 37 */     titleMap.put(this.Type, this);
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public static RefTitle getTitleByType(Title Type) {
/* 47 */     return titleMap.get(Type);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefTitle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */