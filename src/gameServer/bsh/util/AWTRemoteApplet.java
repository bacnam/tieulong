/*    */ package bsh.util;
/*    */ 
/*    */ import java.applet.Applet;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.Label;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.Socket;
/*    */ import java.net.URL;
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
/*    */ public class AWTRemoteApplet
/*    */   extends Applet
/*    */ {
/*    */   OutputStream out;
/*    */   InputStream in;
/*    */   
/*    */   public void init() {
/* 52 */     setLayout(new BorderLayout());
/*    */     
/*    */     try {
/* 55 */       URL base = getDocumentBase();
/*    */ 
/*    */       
/* 58 */       Socket s = new Socket(base.getHost(), base.getPort() + 1);
/* 59 */       this.out = s.getOutputStream();
/* 60 */       this.in = s.getInputStream();
/* 61 */     } catch (IOException e) {
/* 62 */       add("Center", new Label("Remote Connection Failed", 1));
/*    */       
/*    */       return;
/*    */     } 
/* 66 */     Component console = new AWTConsole(this.in, this.out);
/* 67 */     add("Center", console);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/AWTRemoteApplet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */