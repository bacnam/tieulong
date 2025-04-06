/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.utils.CommString;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RefGoodsInfo
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public long ID;
/*    */   public int UniformID;
/*    */   public ConstEnum.DiscountType DiscountType;
/*    */   public List<Integer> CostWeight;
/*    */   public List<String> CostUniformId;
/*    */   public List<String> Price;
/*    */   public List<String> Discount;
/*    */   public int TimeLimit;
/*    */   
/*    */   public boolean Assert() {
/* 28 */     if (this.DiscountType == null) {
/* 29 */       this.DiscountType = ConstEnum.DiscountType.None;
/*    */     }
/* 31 */     if (this.Discount == null || this.Discount.size() == 0) {
/* 32 */       this.Discount = this.Price;
/*    */     }
/* 34 */     if (!RefAssert.inRef(Integer.valueOf(this.UniformID), RefUniformItem.class, new Object[0])) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (!RefAssert.listSize(this.CostWeight, this.CostUniformId, new List[] { this.Price, this.Discount })) {
/* 38 */       return false;
/*    */     }
/* 40 */     if (this.CostUniformId != null) {
/* 41 */       for (String costs : this.CostUniformId) {
/* 42 */         List<Integer> list = CommString.getIntegerList(costs, "&");
/* 43 */         for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) { int uniformId = ((Integer)iterator.next()).intValue();
/* 44 */           if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId)) == null) {
/* 45 */             CommLog.error("表GoodsInfo中ID={}的CostUniformId={}中的数值在物品总表UniformItem中无定义", Long.valueOf(this.ID), this.CostUniformId);
/* 46 */             return false;
/*    */           }  }
/*    */       
/*    */       } 
/*    */     }
/* 51 */     for (int index = 0; index < this.CostUniformId.size(); index++) {
/* 52 */       List<Integer> costs = CommString.getIntegerList(this.CostUniformId.get(index), "&");
/* 53 */       List<Integer> prices = CommString.getIntegerList(this.Price.get(index), "&");
/* 54 */       if (costs.size() != prices.size()) {
/* 55 */         CommLog.error("表GoodsInfo中ID={}的CostUniformId={}和Price={}配置长度不一致", new Object[] { Long.valueOf(this.ID), this.CostUniformId, this.Price });
/* 56 */         return false;
/*    */       } 
/* 58 */       if (this.Discount != null && this.Discount.size() > 0) {
/* 59 */         List<Integer> discounts = CommString.getIntegerList(this.Discount.get(index), "&");
/* 60 */         if (costs.size() != discounts.size()) {
/* 61 */           CommLog.error("表GoodsInfo中ID={}的Price={}和Discount={}配置长度不一致", new Object[] { Long.valueOf(this.ID), this.Price, this.Discount });
/* 62 */           return false;
/*    */         } 
/*    */       } 
/*    */     } 
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 71 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGoodsInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */