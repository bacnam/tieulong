/*    */ package jsc.swt.menu;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.ButtonGroup;
/*    */ import javax.swing.JMenu;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JRadioButtonMenuItem;
/*    */ import javax.swing.SwingUtilities;
/*    */ import javax.swing.UIManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LookAndFeelMenu
/*    */   extends JMenu
/*    */ {
/*    */   static final String WINDOWS_LAF = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
/*    */   static final String MAC_LAF = "javax.swing.plaf.mac.MacLookAndFeel";
/* 21 */   static final String JAVA_LAF = UIManager.getCrossPlatformLookAndFeelClassName();
/*    */ 
/*    */   
/*    */   static final String MOTIF_LAF = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
/*    */ 
/*    */   
/*    */   Component parent;
/*    */ 
/*    */   
/*    */   public LookAndFeelMenu(Component paramComponent) {
/* 31 */     super("Look & feel");
/* 32 */     this.parent = paramComponent;
/* 33 */     ButtonGroup buttonGroup = new ButtonGroup();
/* 34 */     JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem("Windows");
/* 35 */     JRadioButtonMenuItem jRadioButtonMenuItem2 = new JRadioButtonMenuItem("Java");
/* 36 */     JRadioButtonMenuItem jRadioButtonMenuItem3 = new JRadioButtonMenuItem("Motif");
/* 37 */     JRadioButtonMenuItem jRadioButtonMenuItem4 = new JRadioButtonMenuItem("Mac");
/* 38 */     LookAndFeelListener lookAndFeelListener = new LookAndFeelListener(this);
/* 39 */     jRadioButtonMenuItem1.addActionListener(lookAndFeelListener);
/* 40 */     jRadioButtonMenuItem2.addActionListener(lookAndFeelListener);
/* 41 */     jRadioButtonMenuItem3.addActionListener(lookAndFeelListener);
/* 42 */     jRadioButtonMenuItem4.addActionListener(lookAndFeelListener);
/* 43 */     String str = UIManager.getSystemLookAndFeelClassName();
/* 44 */     if (str.equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) { jRadioButtonMenuItem1.setSelected(true); jRadioButtonMenuItem4.setEnabled(false); }
/* 45 */     else if (str.equals(JAVA_LAF)) { jRadioButtonMenuItem2.setSelected(true); }
/* 46 */     else if (str.equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) { jRadioButtonMenuItem3.setSelected(true); }
/* 47 */     else if (str.equals("javax.swing.plaf.mac.MacLookAndFeel")) { jRadioButtonMenuItem4.setSelected(true); }
/* 48 */      buttonGroup.add(jRadioButtonMenuItem1); buttonGroup.add(jRadioButtonMenuItem2); buttonGroup.add(jRadioButtonMenuItem3); buttonGroup.add(jRadioButtonMenuItem4);
/* 49 */     add(jRadioButtonMenuItem1);
/* 50 */     add(jRadioButtonMenuItem2);
/* 51 */     add(jRadioButtonMenuItem3);
/* 52 */     add(jRadioButtonMenuItem4);
/*    */   }
/*    */   class LookAndFeelListener implements ActionListener { LookAndFeelListener(LookAndFeelMenu this$0) {
/* 55 */       this.this$0 = this$0;
/*    */     }
/*    */     private final LookAndFeelMenu this$0;
/*    */     public void actionPerformed(ActionEvent param1ActionEvent) {
/* 59 */       String str = param1ActionEvent.getActionCommand();
/*    */       
/*    */       try {
/*    */         String str1;
/* 63 */         if (str.equals("Java"))
/* 64 */         { str1 = LookAndFeelMenu.JAVA_LAF; }
/* 65 */         else if (str.equals("Windows"))
/* 66 */         { str1 = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; }
/* 67 */         else if (str.equals("Mac"))
/* 68 */         { str1 = "javax.swing.plaf.mac.MacLookAndFeel"; }
/* 69 */         else if (str.equals("Motif"))
/* 70 */         { str1 = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; }
/*    */         else { return; }
/* 72 */          UIManager.setLookAndFeel(str1);
/*    */       } catch (Exception exception) {
/*    */         
/* 75 */         JOptionPane.showMessageDialog(this.this$0.parent, str + " look and feel unavailable on your system.", "Error", 0);
/*    */       } 
/*    */ 
/*    */       
/* 79 */       SwingUtilities.updateComponentTreeUI(this.this$0.parent);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/menu/LookAndFeelMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */