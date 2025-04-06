/*    */ package business.player.item;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import com.zhonglian.server.logger.flow.ItemFlow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface IUniItemContainer<ItemType>
/*    */ {
/*    */   PrizeType getType();
/*    */   
/*    */   boolean check(int paramInt1, int paramInt2, IItemFilter<ItemType> paramIItemFilter);
/*    */   
/*    */   default boolean check(int id, int count) {
/* 31 */     return check(id, count, null);
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
/*    */   
/*    */   ItemType consume(int paramInt1, int paramInt2, ItemFlow paramItemFlow, IItemFilter<ItemType> paramIItemFilter);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default ItemType consume(int id, int count, ItemFlow reason) {
/* 55 */     return consume(id, count, reason, null);
/*    */   }
/*    */   
/*    */   int gain(int paramInt1, int paramInt2, ItemFlow paramItemFlow);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/IUniItemContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */