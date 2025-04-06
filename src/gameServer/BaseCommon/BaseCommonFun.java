/*    */ package BaseCommon;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
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
/*    */ public class BaseCommonFun
/*    */ {
/*    */   public static long getNowTimeMS() {
/* 19 */     Calendar objCalendar = Calendar.getInstance();
/* 20 */     return objCalendar.getTimeInMillis();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getNowTimeString() {
/* 29 */     Calendar objCalendar = Calendar.getInstance();
/* 30 */     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 31 */     return df.format(objCalendar.getTime());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setSystemTime() {
/* 42 */     String osName = System.getProperty("os.name");
/* 43 */     String cmd = "";
/*    */     try {
/* 45 */       if (osName.matches("^(?i)Windows.*$")) {
/*    */         
/* 47 */         cmd = "  cmd /c time 22:35:00";
/* 48 */         Runtime.getRuntime().exec(cmd);
/*    */         
/* 50 */         cmd = " cmd /c date 2009-03-26";
/* 51 */         Runtime.getRuntime().exec(cmd);
/*    */       } else {
/*    */         
/* 54 */         cmd = "  date -s 20090326";
/* 55 */         Runtime.getRuntime().exec(cmd);
/*    */         
/* 57 */         cmd = "  date -s 22:35:00";
/* 58 */         Runtime.getRuntime().exec(cmd);
/*    */       } 
/* 60 */     } catch (Exception e) {
/* 61 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/BaseCommon/BaseCommonFun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */