/*    */ package core.network.proto;
/*    */ 
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefGoodsInfo;
/*    */ import core.database.game.bo.PlayerGoodsBO;
/*    */ 
/*    */ public class Store {
/*    */   public static class Goods {
/*    */     public long sid;
/*    */     public int storeType;
/*    */     public long goodsId;
/*    */     public int count;
/*    */     public int leftBuyTimes;
/*    */     public int totalBuyTimes;
/*    */     public int uniformId;
/*    */     public int costItem;
/*    */     public Double price;
/*    */     public Double discount;
/*    */     public int discountType;
/*    */     public int timeLeft;
/*    */     public boolean isSoldout;
/*    */     
/*    */     public Goods(PlayerGoodsBO bo) {
/* 24 */       this.sid = bo.getId();
/* 25 */       this.storeType = bo.getStoreType();
/* 26 */       this.goodsId = bo.getGoodsId();
/* 27 */       this.count = bo.getAmount();
/* 28 */       this.leftBuyTimes = bo.getTotalBuyTimes() - bo.getBuyTimes();
/* 29 */       this.totalBuyTimes = bo.getTotalBuyTimes();
/* 30 */       this.uniformId = ((RefGoodsInfo)RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(this.goodsId))).UniformID;
/* 31 */       this.costItem = Integer.valueOf(bo.getCostUniformId()).intValue();
/* 32 */       this.price = Double.valueOf(Double.valueOf(bo.getPrice()).doubleValue() * bo.getAmount());
/* 33 */       this.discount = Double.valueOf(Double.valueOf(bo.getDiscount()).doubleValue() * bo.getAmount());
/* 34 */       this.isSoldout = bo.getSoldout();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Store.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */