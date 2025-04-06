/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class RefGuildSkill
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public Attribute Attribute;
/*    */   public String Description;
/*    */   public int BaseValue;
/*    */   public int GrowthValue;
/*    */   public int UnlockLevel;
/*    */   @RefField(isfield = false)
/* 21 */   public static Map<ConstEnum.BuffType, RefGuildSkill> buffMap = Maps.newMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static RefGuildSkill getGuildSkillRef(ConstEnum.BuffType buffType) {
/* 41 */     return buffMap.get(buffType);
/*    */   }
/*    */   
/*    */   public int getSkillValue(int level) {
/* 45 */     if (level == 0) {
/* 46 */       return 0;
/*    */     }
/* 48 */     return this.BaseValue + this.GrowthValue * (level - 1);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildSkill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */