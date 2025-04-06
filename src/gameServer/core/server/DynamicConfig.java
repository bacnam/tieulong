/*    */ package core.server;
/*    */ 
/*    */ import com.zhonglian.server.common.db.BM;
/*    */ import core.database.game.bo.ServerConfigBO;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicConfig
/*    */ {
/* 13 */   private static DynamicConfig instance = new DynamicConfig();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   private Map<String, ServerConfigBO> confs = new HashMap<>();
/*    */   
/*    */   public static void init() {
/* 22 */     List<ServerConfigBO> bos = BM.getBM(ServerConfigBO.class).findAll();
/* 23 */     for (ServerConfigBO bo : bos) {
/* 24 */       instance.confs.put(bo.getKey(), bo);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void set(String key, String value) {
/* 29 */     ServerConfigBO bo = instance.confs.get(key);
/* 30 */     if (bo == null) {
/* 31 */       bo = new ServerConfigBO();
/* 32 */       bo.setKey(key);
/* 33 */       bo.setValue(value);
/* 34 */       bo.insert();
/* 35 */       instance.confs.put(key, bo);
/*    */     } 
/* 37 */     bo.saveValue(value);
/*    */   }
/*    */   
/*    */   public static String get(String key) {
/* 41 */     ServerConfigBO bo = instance.confs.get(key);
/* 42 */     if (bo == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     return bo.getValue();
/*    */   }
/*    */   
/*    */   public static String get(String key, String defaultvalue) {
/* 49 */     ServerConfigBO bo = instance.confs.get(key);
/* 50 */     if (bo == null) {
/* 51 */       return defaultvalue;
/*    */     }
/* 53 */     return bo.getValue();
/*    */   }
/*    */   
/*    */   public static boolean get(String key, boolean defaultvalue) {
/* 57 */     ServerConfigBO bo = instance.confs.get(key);
/* 58 */     if (bo == null) {
/* 59 */       return defaultvalue;
/*    */     }
/* 61 */     return Boolean.valueOf(bo.getValue()).booleanValue();
/*    */   }
/*    */   
/*    */   public static int get(String key, int defaultvalue) {
/* 65 */     ServerConfigBO bo = instance.confs.get(key);
/* 66 */     if (bo == null) {
/* 67 */       return defaultvalue;
/*    */     }
/* 69 */     return Integer.valueOf(bo.getValue()).intValue();
/*    */   }
/*    */   
/*    */   public static Map<String, ServerConfigBO> getAllConfig() {
/* 73 */     return new HashMap<>(instance.confs);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/DynamicConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */