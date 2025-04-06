/*    */ package bsh.util;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.Label;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.net.Socket;
/*    */ import java.net.URL;
/*    */ import javax.swing.JApplet;
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
/*    */ public class JRemoteApplet
/*    */   extends JApplet
/*    */ {
/*    */   OutputStream out;
/*    */   InputStream in;
/*    */   
/*    */   public void init() {
/* 52 */     getContentPane().setLayout(new BorderLayout());
/*    */     
/*    */     try {
/* 55 */       URL base = getDocumentBase();
/*    */ 
/*    */       
/* 58 */       Socket s = new Socket(base.getHost(), base.getPort() + 1);
/* 59 */       this.out = s.getOutputStream();
/* 60 */       this.in = s.getInputStream();
/* 61 */     } catch (IOException e) {
/* 62 */       getContentPane().add("Center", new Label("Remote Connection Failed", 1));
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 67 */     Component console = new JConsole(this.in, this.out);
/* 68 */     getContentPane().add("Center", console);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/JRemoteApplet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */