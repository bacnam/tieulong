/*    */ package bsh.util;
/*    */ 
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
/*    */ public class Httpd
/*    */   extends Thread
/*    */ {
/*    */   ServerSocket ss;
/*    */   
/*    */   public static void main(String[] argv) throws IOException {
/* 56 */     (new Httpd(Integer.parseInt(argv[0]))).start();
/*    */   }
/*    */ 
/*    */   
/*    */   public Httpd(int port) throws IOException {
/* 61 */     this.ss = new ServerSocket(port);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/*    */       while (true) {
/* 70 */         (new HttpdConnection(this.ss.accept())).start();
/*    */       }
/* 72 */     } catch (IOException e) {
/*    */       
/* 74 */       System.out.println(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/Httpd.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */