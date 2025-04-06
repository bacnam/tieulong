/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefStoreRefresh
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public StoreType id;
/*    */   public int UniformId;
/*    */   public int Count;
/*    */   public int StoreFreeRefreshTimes;
/*    */   public boolean ManualRefresh;
/*    */   public boolean AutoRefresh;
/*    */   public boolean ShowNextRefreshTime;
/*    */   @RefField(isfield = false)
/* 28 */   public static Map<StoreType, Integer> FreeStoreTimes = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 32 */     if (!RefAssert.inRef(Integer.valueOf(this.UniformId), RefUniformItem.class, new Object[0])) {
/* 33 */       return false;
/*    */     }
/*    */     
/* 36 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     StoreType[] arrayOfStoreType;
/* 42 */     for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
/* 43 */       FreeStoreTimes.put(type, Integer.valueOf(10)); b++; }
/*    */     
/* 45 */     for (RefStoreRefresh ref : all.values()) {
/* 46 */       FreeStoreTimes.put(ref.id, Integer.valueOf(ref.StoreFreeRefreshTimes));
/*    */     }
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefStoreRefresh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */