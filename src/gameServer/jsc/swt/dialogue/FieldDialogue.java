/*     */ package jsc.swt.dialogue;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JToggleButton;
/*     */ import jsc.swt.panel.FieldPanel;
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
/*     */ public class FieldDialogue
/*     */   extends Dialogue
/*     */ {
/*     */   FieldPanel panel;
/*     */   
/*     */   public FieldDialogue(Component paramComponent, String paramString1, String paramString2, int paramInt1, int paramInt2) {
/*  37 */     super(paramComponent, paramString1, paramString2, paramInt1, paramInt2);
/*     */     
/*  39 */     this.panel = new FieldPanel();
/*  40 */     add((Component)this.panel, "Center");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldDialogue(Component paramComponent, String paramString1, String paramString2) {
/*  51 */     this(paramComponent, paramString1, paramString2, -1, 2);
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
/*     */   public void addComponent(JLabel paramJLabel, Component paramComponent, int paramInt) {
/*  63 */     this.panel.addComponent(paramJLabel, paramComponent, paramInt, 0); this.dialog.pack();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(String paramString, JTextField paramJTextField, int paramInt) {
/*  73 */     this.panel.addField(paramString, paramJTextField, paramInt, 0); this.dialog.pack();
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
/*     */   public void addField(String paramString, JTextField paramJTextField, int paramInt, char paramChar) {
/*  85 */     this.panel.addField(paramString, paramJTextField, paramInt, 0, paramChar); this.dialog.pack();
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
/*     */   public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt) {
/*  98 */     this.panel.addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt, 0); this.dialog.pack();
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
/*     */   
/*     */   public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt, char paramChar) {
/* 113 */     this.panel.addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt, 0, paramChar); this.dialog.pack();
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
/*     */   public void addToggleButton(JToggleButton paramJToggleButton, int paramInt, char paramChar) {
/* 125 */     this.panel.addToggleButton(paramJToggleButton, paramInt, 0, paramChar); this.dialog.pack();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/dialogue/FieldDialogue.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */