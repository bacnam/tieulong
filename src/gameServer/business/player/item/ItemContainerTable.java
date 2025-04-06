/*    */ package business.player.item;
/*    */ 
/*    */ import BaseCommon.CommClass;
/*    */ import BaseCommon.CommLog;
/*    */ import business.player.feature.Feature;
/*    */ import com.zhonglian.server.common.enums.PrizeType;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemContainerTable
/*    */ {
/* 19 */   private static Map<PrizeType, Class<? extends Feature>> table = new HashMap<>();
/*    */   
/*    */   static {
/* 22 */     List<Class<?>> clazzList = CommClass.getAllClassByInterface(IUniItemContainer.class, "business.player.feature");
/* 23 */     for (Class<?> clazz : clazzList) {
/*    */       try {
/* 25 */         Method method = clazz.getDeclaredMethod("getType", new Class[0]);
/* 26 */         Constructor<?> constructor = clazz.getConstructors()[0];
/* 27 */         PrizeType type = (PrizeType)method.invoke(constructor.newInstance(new Object[1]), new Object[0]);
/* 28 */         table.put(type, clazz);
/* 29 */       } catch (Exception e) {
/* 30 */         CommLog.error("[ItemContainerTable]初始化物品管理列表失败", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Class<? extends Feature> getItemLogic(PrizeType type) {
/* 36 */     return table.get(type);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/ItemContainerTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */