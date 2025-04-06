/*     */ package jsc.swt.menu;
/*     */ 
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JRadioButtonMenuItem;
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
/*     */ public class DigitsMenu
/*     */   extends JMenu
/*     */ {
/*     */   int lo;
/*     */   int hi;
/*     */   JRadioButtonMenuItem[] rb;
/*     */   
/*     */   public DigitsMenu(String paramString, int paramInt1, int paramInt2, ActionListener paramActionListener) {
/*  41 */     this(paramString, 1, paramInt1, paramInt2, paramActionListener);
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
/*     */   public DigitsMenu(String paramString, int paramInt1, int paramInt2, int paramInt3, ActionListener paramActionListener) {
/*  85 */     super(paramString);
/*  86 */     this.lo = paramInt1;
/*  87 */     this.hi = paramInt2;
/*  88 */     ButtonGroup buttonGroup = new ButtonGroup();
/*  89 */     int i = paramInt2 - paramInt1 + 1;
/*  90 */     if (i < 2) throw new IllegalArgumentException("Invalid menu.");
/*     */     
/*  92 */     this.rb = new JRadioButtonMenuItem[i];
/*     */     
/*  94 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  96 */       int j = paramInt1 + b;
/*  97 */       String str = String.valueOf(j);
/*  98 */       this.rb[b] = new JRadioButtonMenuItem(str);
/*  99 */       this.rb[b].addActionListener(paramActionListener);
/* 100 */       buttonGroup.add(this.rb[b]);
/* 101 */       JMenuItem jMenuItem = add(this.rb[b]);
/* 102 */       if (b < 10) {
/*     */         
/* 104 */         int k = j % 10;
/* 105 */         char[] arrayOfChar = String.valueOf(k).toCharArray();
/* 106 */         jMenuItem.setMnemonic(arrayOfChar[0]);
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     this.rb[paramInt3 - paramInt1].setSelected(true);
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
/*     */   public int getSelectedValue() {
/* 123 */     for (byte b = 0; b < this.hi - this.lo + 1; b++) {
/* 124 */       if (this.rb[b].isSelected()) return this.lo + b; 
/* 125 */     }  return -1;
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
/*     */   public void setSelectedValue(int paramInt) {
/* 137 */     if (paramInt < this.lo || paramInt > this.hi)
/* 138 */       throw new IllegalArgumentException("Invalid value."); 
/* 139 */     this.rb[paramInt - this.lo].setSelected(true);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/menu/DigitsMenu.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */