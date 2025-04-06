/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefStore
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int ID;
/*    */   public StoreType StoreType;
/*    */   public NumberRange LevelRange;
/*    */   public int BuyLimit;
/*    */   public int PublicBuyLimit;
/*    */   public ArrayList<Long> GoodsIDList;
/*    */   public ArrayList<Integer> GoodsCountList;
/*    */   public ArrayList<Integer> GoodsWeightList;
/*    */   public boolean IsRandomCount;
/*    */   public boolean IsDailyRefresh;
/*    */   public int UnlockCondition;
/*    */   public int Sheet;
/*    */   @RefField(isfield = false)
/* 36 */   public static Map<StoreType, List<RefStore>> StoreByType = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 40 */     if (this.BuyLimit == 0) {
/* 41 */       this.BuyLimit = 1;
/*    */     }
/* 43 */     if (!RefAssert.listSize(this.GoodsIDList, this.GoodsCountList, new List[] { this.GoodsWeightList })) {
/* 44 */       return false;
/*    */     }
/* 46 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     StoreType[] arrayOfStoreType;
/* 52 */     for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
/* 53 */       StoreByType.put(type, Lists.newArrayList()); b++; }
/*    */     
/* 55 */     for (RefStore ref : all.values()) {
/* 56 */       ((List<RefStore>)StoreByType.get(ref.StoreType)).add(ref);
/*    */     }
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */