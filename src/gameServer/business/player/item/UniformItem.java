/*    */ package business.player.item;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import core.config.refdata.ref.RefUniformItem;
/*    */ 
/*    */ 
/*    */ public class UniformItem
/*    */ {
/*    */   private Integer uniformId;
/*    */   private int count;
/*    */   
/*    */   public UniformItem(RefUniformItem ref, int count) {
/* 13 */     this.uniformId = Integer.valueOf(ref.id);
/* 14 */     this.count = count;
/*    */   }
/*    */   
/*    */   public UniformItem(int uniformid, int count) {
/* 18 */     this.uniformId = Integer.valueOf(uniformid);
/* 19 */     this.count = count;
/*    */   }
/*    */   
/*    */   public UniformItem(PrizeType type, int itemid, int count) {
/* 23 */     this.uniformId = Integer.valueOf(ItemUtils.getUniformId(type, itemid));
/* 24 */     this.count = count;
/*    */   }
/*    */   
/*    */   public UniformItem(PrizeType currency, int count) {
/* 28 */     this.uniformId = Integer.valueOf(ItemUtils.getUniformId(currency, 0));
/* 29 */     this.count = count;
/*    */   }
/*    */   
/*    */   public void setUniformId(int uniformId) {
/* 33 */     this.uniformId = Integer.valueOf(uniformId);
/*    */   }
/*    */   
/*    */   public int getUniformId() {
/* 37 */     return this.uniformId.intValue();
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 41 */     return this.count;
/*    */   }
/*    */   
/*    */   public void addCount(int count) {
/* 45 */     this.count += count;
/*    */   }
/*    */   
/*    */   public void setCount(int count) {
/* 49 */     this.count = count;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "{\"uniformId\":" + this.uniformId + ",\"count\":" + this.count + "},";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/UniformItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */