/*    */ package jsc.swt.help;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Container;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.event.HyperlinkListener;
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
/*    */ public class HelpWindow
/*    */   extends JFrame
/*    */ {
/*    */   JEditorPane pane;
/*    */   
/*    */   public HelpWindow(URL paramURL) throws IOException {
/* 37 */     super("Help");
/*    */ 
/*    */ 
/*    */     
/* 41 */     Container container = getContentPane();
/* 42 */     container.setLayout(new BorderLayout());
/*    */ 
/*    */     
/* 45 */     this.pane = new JEditorPane(paramURL);
/* 46 */     this.pane.setEditable(false);
/* 47 */     this.pane.addHyperlinkListener(new HelpListener(this));
/*    */ 
/*    */     
/* 50 */     container.add(new JScrollPane(this.pane), "Center");
/*    */     
/* 52 */     JButton jButton = new JButton("Close");
/* 53 */     jButton.setMnemonic('C');
/* 54 */     jButton.addActionListener(new CloseButtonListener(this));
/*    */ 
/*    */     
/* 57 */     container.add(jButton, "South");
/*    */     
/* 59 */     setSize(350, 400);
/*    */   }
/*    */ 
/*    */   
/*    */   class CloseButtonListener
/*    */     implements ActionListener
/*    */   {
/*    */     private final HelpWindow this$0;
/*    */     
/*    */     CloseButtonListener(HelpWindow this$0) {
/* 69 */       this.this$0 = this$0;
/*    */     }
/*    */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 72 */       this.this$0.dispose();
/*    */     } }
/*    */   class HelpListener implements HyperlinkListener { HelpListener(HelpWindow this$0) {
/* 75 */       this.this$0 = this$0;
/*    */     } private final HelpWindow this$0;
/*    */     public void hyperlinkUpdate(HyperlinkEvent param1HyperlinkEvent) {
/*    */       
/* 79 */       try { if (param1HyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
/* 80 */           this.this$0.pane.setPage(param1HyperlinkEvent.getURL());  }
/* 81 */       catch (IOException iOException) { iOException.printStackTrace(System.err); }
/*    */     
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/help/HelpWindow.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */