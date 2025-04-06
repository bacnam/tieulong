/*    */ package core.server;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.Config;
/*    */ import com.zhonglian.server.common.utils.CommString;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class ServerConfig
/*    */   extends Config
/*    */ {
/* 15 */   private static List<Integer> serverMergedIds = new ArrayList<>();
/*    */   static {
/* 17 */     serverMergedIds.addAll(CommString.getIntegerList(System.getProperty("SERVER_MIDList", ServerID()), ";"));
/*    */   }
/*    */   
/*    */   public static List<Integer> SERVER_MIDList() {
/* 21 */     return serverMergedIds;
/*    */   }
/*    */   
/*    */   public static int AAY_AppId() {
/* 25 */     return 10294;
/*    */   }
/*    */   
/*    */   public static String AAY_SecretKey() {
/* 29 */     return System.getProperty("AAY_SecretKey", "2767c8cf315a4e8ebd50e5f9bb52fd3a");
/*    */   }
/*    */   
/* 32 */   private static int loginkey = CommTime.nowSecond();
/*    */   
/*    */   public static int refreshLoginKey() {
/* 35 */     return loginkey = CommTime.nowSecond();
/*    */   }
/*    */   
/*    */   public static int getLoginKey() {
/* 39 */     return loginkey;
/*    */   }
/*    */   
/*    */   public static String BattleMapPath() {
/* 43 */     return System.getProperty("GameServer.Maps");
/*    */   }
/*    */   
/*    */   public static void checkMapPath() {
/* 47 */     File path = new File(BattleMapPath());
/* 48 */     if (!path.exists() || !path.isDirectory()) {
/* 49 */       CommLog.error("【【【Map文件夹地址(" + path.getAbsolutePath() + ")配置错误！！！】】】");
/* 50 */       System.exit(-1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/ServerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */