/*    */ package business.player.feature.character;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PowerUtils
/*    */ {
/* 14 */   private static Map<Attribute, Double> factors = new HashMap<>();
/* 15 */   private static double pwrFacoter = RefDataMgr.getFactor("Power_Factor", 650) / 10000.0D;
/*    */ 
/*    */   
/*    */   static {
/* 19 */     factors.put(Attribute.MaxHP, Double.valueOf(RefDataMgr.getFactor("Power_MaxHP", 6900) / 10000.0D));
/* 20 */     factors.put(Attribute.ATK, Double.valueOf(RefDataMgr.getFactor("Power_ATK", 40000) / 10000.0D));
/* 21 */     factors.put(Attribute.DEF, Double.valueOf(RefDataMgr.getFactor("Power_DEF", 110000) / 10000.0D));
/* 22 */     factors.put(Attribute.RGS, Double.valueOf(RefDataMgr.getFactor("Power_RGS", 110000) / 10000.0D));
/* 23 */     factors.put(Attribute.Hit, Double.valueOf(RefDataMgr.getFactor("Power_Hit", 30000) / 10000.0D));
/* 24 */     factors.put(Attribute.Dodge, Double.valueOf(RefDataMgr.getFactor("Power_Dodge", 130000) / 10000.0D));
/* 25 */     factors.put(Attribute.Critical, Double.valueOf(RefDataMgr.getFactor("Power_Critical", 90000) / 10000.0D));
/* 26 */     factors.put(Attribute.Tenacity, Double.valueOf(RefDataMgr.getFactor("Power_Tenacity", 30000) / 10000.0D));
/*    */   }
/*    */   
/*    */   public static int getPower(Map<Attribute, Integer> attr) {
/* 30 */     int power = 0;
/* 31 */     for (Map.Entry<Attribute, Integer> pair : attr.entrySet()) {
/* 32 */       power += getPower(pair.getKey(), ((Integer)pair.getValue()).intValue());
/*    */     }
/* 34 */     return power;
/*    */   }
/*    */   
/*    */   public static int getPower(List<Attribute> attrType, List<Integer> attrValue) {
/* 38 */     Map<Attribute, Integer> map = new HashMap<>();
/* 39 */     for (int i = 0; i < attrType.size(); i++) {
/* 40 */       map.put(attrType.get(i), attrValue.get(i));
/*    */     }
/* 42 */     return getPower(map);
/*    */   }
/*    */   
/*    */   public static int getPower(Attribute strengthenType, int value) {
/* 46 */     Double factor = factors.get(strengthenType);
/* 47 */     return (factor == null) ? 0 : (int)(value * factor.doubleValue() * pwrFacoter);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/character/PowerUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */