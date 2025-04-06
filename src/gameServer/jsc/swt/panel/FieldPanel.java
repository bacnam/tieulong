/*     */ package jsc.swt.panel;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JToggleButton;
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
/*     */ public class FieldPanel
/*     */   extends JPanel
/*     */ {
/*     */   public GridBagConstraints c;
/*     */   
/*     */   public FieldPanel() {
/*  29 */     super(new GridBagLayout());
/*  30 */     setOpaque(false);
/*  31 */     this.c = new GridBagConstraints();
/*  32 */     this.c.insets = new Insets(2, 2, 2, 2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComponent(JLabel paramJLabel, Component paramComponent, int paramInt1, int paramInt2) {
/*  83 */     addComponents(paramJLabel, paramComponent, paramInt1, paramInt2);
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
/*     */   public void addComponents(Component paramComponent1, Component paramComponent2, int paramInt1, int paramInt2) {
/*  96 */     this.c.gridy = paramInt1;
/*  97 */     this.c.anchor = 13;
/*     */     
/*  99 */     this.c.gridx = 2 * paramInt2;
/* 100 */     add(paramComponent1, this.c);
/* 101 */     this.c.anchor = 17;
/*     */     
/* 103 */     this.c.gridx++;
/* 104 */     add(paramComponent2, this.c);
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
/*     */   public void addField(String paramString, JTextField paramJTextField, int paramInt1, int paramInt2) {
/* 134 */     addField(new JLabel(paramString), paramJTextField, paramInt1, paramInt2);
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
/*     */   public void addField(String paramString, JTextField paramJTextField, int paramInt1, int paramInt2, char paramChar) {
/* 147 */     addField(new JLabel(paramString), paramJTextField, paramInt1, paramInt2, paramChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(JLabel paramJLabel, JTextField paramJTextField, int paramInt1, int paramInt2) {
/* 158 */     addComponent(paramJLabel, paramJTextField, paramInt1, paramInt2);
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
/*     */   public void addField(JLabel paramJLabel, JTextField paramJTextField, int paramInt1, int paramInt2, char paramChar) {
/* 172 */     paramJLabel.setDisplayedMnemonic(paramChar);
/* 173 */     paramJTextField.setFocusAccelerator(paramChar);
/* 174 */     addField(paramJLabel, paramJTextField, paramInt1, paramInt2);
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
/*     */   
/*     */   public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt1, int paramInt2) {
/* 190 */     JPanel jPanel = new JPanel(new FlowLayout(0, 0, 2));
/* 191 */     jPanel.setOpaque(false);
/* 192 */     jPanel.add(paramJTextField);
/* 193 */     jPanel.add(paramJLabel2);
/* 194 */     addComponent(paramJLabel1, jPanel, paramInt1, paramInt2);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(JLabel paramJLabel1, JTextField paramJTextField, JLabel paramJLabel2, int paramInt1, int paramInt2, char paramChar) {
/* 212 */     paramJLabel1.setDisplayedMnemonic(paramChar);
/* 213 */     paramJTextField.setFocusAccelerator(paramChar);
/* 214 */     addField(paramJLabel1, paramJTextField, paramJLabel2, paramInt1, paramInt2);
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
/*     */   public void addToggleButton(JToggleButton paramJToggleButton, int paramInt1, int paramInt2, char paramChar) {
/* 229 */     JLabel jLabel = new JLabel(paramJToggleButton.getText());
/* 230 */     paramJToggleButton.setText("");
/* 231 */     jLabel.setDisplayedMnemonic(paramChar);
/* 232 */     paramJToggleButton.setMnemonic(paramChar);
/* 233 */     addComponents(jLabel, paramJToggleButton, paramInt1, paramInt2);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/panel/FieldPanel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */