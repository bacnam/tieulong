/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.InstanceType;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefInstance
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public InstanceType Type;
/*    */   public int Material;
/*    */   public int RewardId;
/*    */   @RefField(iskey = false)
/* 23 */   public static Map<InstanceType, List<RefInstance>> instanceMap = Maps.newConcurrentMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 27 */     if (this.Type != InstanceType.GuaidInstance && !RefAssert.inRef(Integer.valueOf(this.RewardId), RefReward.class, new Object[0])) {
/* 28 */       return false;
/*    */     }
/* 30 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     InstanceType[] arrayOfInstanceType;
/* 36 */     for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) { InstanceType instanceType = arrayOfInstanceType[b];
/* 37 */       instanceMap.put(instanceType, Lists.newArrayList()); b++; }
/*    */     
/* 39 */     for (RefInstance ref : all.values()) {
/* 40 */       ((List<RefInstance>)instanceMap.get(ref.Type)).add(ref);
/*    */     }
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */