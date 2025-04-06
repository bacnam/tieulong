/*    */ package business.player.item;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefUniformItem;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemUtils
/*    */ {
/* 17 */   private static Map<PrizeType, Map<Integer, Integer>> uniformitems = new HashMap<>(); static {
/* 18 */     RefContainer<RefUniformItem> container = RefDataMgr.getAll(RefUniformItem.class);
/* 19 */     for (RefUniformItem ref : container.values()) {
/* 20 */       Map<Integer, Integer> items = uniformitems.get(ref.Type);
/* 21 */       if (items == null) {
/* 22 */         items = new HashMap<>();
/* 23 */         uniformitems.put(ref.Type, items);
/*    */       } 
/* 25 */       items.put(Integer.valueOf(ref.ItemID), Integer.valueOf(ref.id));
/*    */     } 
/*    */   }
/*    */   
/*    */   public static int getUniformId(PrizeType type, int itemId) {
/* 30 */     Map<Integer, Integer> items = uniformitems.get(type);
/* 31 */     if (items == null) {
/* 32 */       return 0;
/*    */     }
/* 34 */     if (items.size() == 0) {
/* 35 */       return ((Integer)items.values().iterator().next()).intValue();
/*    */     }
/* 37 */     Integer item = items.get(Integer.valueOf(itemId));
/* 38 */     if (item == null) {
/* 39 */       return 0;
/*    */     }
/* 41 */     return item.intValue();
/*    */   }
/*    */   
/*    */   public static RefUniformItem getRefUniformItem(PrizeType type, int itemId) {
/* 45 */     Map<Integer, Integer> items = uniformitems.get(type);
/* 46 */     if (items == null || items.size() == 0) {
/* 47 */       return null;
/*    */     }
/* 49 */     Integer item = items.get(Integer.valueOf(itemId));
/* 50 */     if (item == null) {
/* 51 */       return null;
/*    */     }
/* 53 */     return (RefUniformItem)RefDataMgr.get(RefUniformItem.class, item);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/ItemUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */