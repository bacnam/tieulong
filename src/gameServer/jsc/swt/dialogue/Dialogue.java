/*     */ package jsc.swt.dialogue;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Point;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dialogue
/*     */ {
/*     */   protected JOptionPane pane;
/*     */   protected JDialog dialog;
/*     */   JPanel panel;
/*     */   Component parentComponent;
/*     */   
/*     */   public Dialogue(Component paramComponent, String paramString1, String paramString2, int paramInt1, int paramInt2) {
/*  50 */     this.parentComponent = paramComponent;
/*     */ 
/*     */     
/*  53 */     this.panel = new JPanel(new BorderLayout(0, 2));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     if (paramString2.length() > 0) {
/*     */       
/*  60 */       JLabel jLabel = new JLabel(paramString2, 0);
/*  61 */       this.panel.add(jLabel, "North");
/*     */     } 
/*     */ 
/*     */     
/*  65 */     this.pane = new JOptionPane(this.panel, paramInt1, paramInt2);
/*  66 */     this.dialog = this.pane.createDialog(paramComponent, paramString1);
/*  67 */     this.dialog.setModal(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Component paramComponent, String paramString) {
/*  81 */     this.panel.add(paramComponent, paramString); this.dialog.pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultButtonEnabled(boolean paramBoolean) {
/*  91 */     JRootPane jRootPane = this.dialog.getRootPane();
/*  92 */     JButton jButton = jRootPane.getDefaultButton();
/*  93 */     jButton.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(int paramInt1, int paramInt2) {
/* 103 */     this.dialog.setLocation(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(Point paramPoint) {
/* 113 */     this.dialog.setLocation(paramPoint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationRelativeTo(Component paramComponent) {
/* 126 */     this.dialog.setLocationRelativeTo(paramComponent);
/* 127 */     this.parentComponent = paramComponent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(int paramInt1, int paramInt2) {
/* 139 */     this.dialog.setSize(paramInt1, paramInt2);
/* 140 */     this.dialog.setLocationRelativeTo(this.parentComponent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object show() {
/* 152 */     this.dialog.show();
/*     */     
/* 154 */     Object object = this.pane.getValue();
/* 155 */     if (object == null) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     if (object instanceof Integer) {
/*     */       
/* 161 */       int i = ((Integer)object).intValue();
/* 162 */       if (i != 0) {
/* 163 */         return null;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 168 */     return object;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/dialogue/Dialogue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */