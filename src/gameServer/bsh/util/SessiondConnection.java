/*    */ package bsh.util;
/*    */ 
/*    */ import bsh.Interpreter;
/*    */ import bsh.NameSpace;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.PrintStream;
/*    */ import java.net.Socket;
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
/*    */ class SessiondConnection
/*    */   extends Thread
/*    */ {
/*    */   NameSpace globalNameSpace;
/*    */   Socket client;
/*    */   
/*    */   SessiondConnection(NameSpace globalNameSpace, Socket client) {
/* 84 */     this.client = client;
/* 85 */     this.globalNameSpace = globalNameSpace;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 92 */       InputStream in = this.client.getInputStream();
/* 93 */       PrintStream out = new PrintStream(this.client.getOutputStream());
/* 94 */       Interpreter i = new Interpreter(new InputStreamReader(in), out, out, true, this.globalNameSpace);
/*    */       
/* 96 */       i.setExitOnEOF(false);
/* 97 */       i.run();
/*    */     } catch (IOException e) {
/* 99 */       System.out.println(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/SessiondConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */