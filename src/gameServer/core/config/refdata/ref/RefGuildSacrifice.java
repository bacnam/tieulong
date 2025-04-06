/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.SacrificeType;
/*    */ 
/*    */ public class RefGuildSacrifice extends RefBaseGame {
/*    */   @RefField(iskey = true)
/*    */   public SacrificeType id;
/*    */   public int CostItemID;
/*    */   public int CostCount;
/*    */   public int SacrificeProgress;
/*    */   public int GuildExp;
/*    */   public int GuildDonate;
/*    */   public int Critical;
/*    */   public int CriticalValue;
/*    */   public String SacrificeName;
/*    */   
/*    */   public boolean Assert() {
/* 21 */     if (!RefAssert.inRef(Integer.valueOf(this.CostItemID), RefUniformItem.class, new Object[0])) {
/* 22 */       return false;
/*    */     }
/* 24 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 29 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildSacrifice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */