/*    */ package core.server;
/*    */ 
/*    */ import ConsoleTask._AConsoleTaskRunner;
/*    */ import bsh.Interpreter;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BshRunner
/*    */   extends _AConsoleTaskRunner
/*    */ {
/* 17 */   private Interpreter it = new Interpreter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run(String statements) {
/* 24 */     eval(statements);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object eval(String statements) {
/*    */     try {
/* 31 */       String filterdCmd = statements.trim().toLowerCase();
/* 32 */       int foundExit = filterdCmd.indexOf("exit");
/* 33 */       if (foundExit >= 0) {
/* 34 */         Pattern pName = Pattern.compile("exit[\\s]?([\\s]?)[\\s]?");
/* 35 */         Matcher matcherName = pName.matcher(filterdCmd);
/* 36 */         if (matcherName.find()) {
/* 37 */           String res = "exit() not allowed!\n";
/* 38 */           return res;
/*    */         } 
/*    */       } 
/*    */       
/* 42 */       return this.it.eval(statements);
/* 43 */     } catch (Exception ex) {
/* 44 */       this.it.getErr().println(ex);
/* 45 */       return ex;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/server/BshRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */