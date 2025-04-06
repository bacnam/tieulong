/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefUniformItem
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public PrizeType Type;
/*    */   public int ItemID;
/*    */   public String Name;
/*    */   public int Price;
/*    */   public int RecoveryReward;
/*    */   public int Quality;
/*    */   
/*    */   public boolean Assert() {
/* 26 */     if (this.Type == PrizeType.Equip && 
/* 27 */       this.Price != 0) {
/* 28 */       CommLog.error(String.format("UniformID=%s的类型为装备，不能配置出售价格，如需价格请配置在对应的Equip表中", new Object[] { Integer.valueOf(this.id) }));
/* 29 */       return false;
/*    */     } 
/*    */     
/* 32 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefUniformItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */