/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefGoodsUnLock
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int ID;
/*    */   public ConstEnum.GoodsUnLockType UnLockType;
/*    */   public int Value;
/*    */   public String Desc;
/*    */   @RefField(isfield = false)
/* 22 */   public static Map<ConstEnum.GoodsUnLockType, List<RefGoodsUnLock>> typeValue = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 26 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     ConstEnum.GoodsUnLockType[] arrayOfGoodsUnLockType;
/* 32 */     for (i = (arrayOfGoodsUnLockType = ConstEnum.GoodsUnLockType.values()).length, b = 0; b < i; ) { ConstEnum.GoodsUnLockType type = arrayOfGoodsUnLockType[b];
/* 33 */       typeValue.put(type, Lists.newArrayList()); b++; }
/*    */     
/* 35 */     for (RefGoodsUnLock ref : all.values()) {
/* 36 */       ((List<RefGoodsUnLock>)typeValue.get(ref.UnLockType)).add(ref);
/*    */     }
/* 38 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGoodsUnLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */