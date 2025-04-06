/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.NameSpace;
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sessiond
/*    */   extends Thread
/*    */ {
/*    */   private ServerSocket ss;
/*    */   NameSpace globalNameSpace;
/*    */   
/*    */   public Sessiond(NameSpace globalNameSpace, int port) throws IOException {
/* 62 */     this.ss = new ServerSocket(port);
/* 63 */     this.globalNameSpace = globalNameSpace;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/*    */       while (true)
/* 71 */         (new SessiondConnection(this.globalNameSpace, this.ss.accept())).start(); 
/*    */     } catch (IOException e) {
/* 73 */       System.out.println(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/Sessiond.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */