/*     */ package jsc.swt.control;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.border.EtchedBorder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ToolBar
/*     */   extends JToolBar
/*     */ {
/*  22 */   private Color focusColour = Color.red;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dimension buttonSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToolBar(String paramString, Dimension paramDimension) {
/*  36 */     super(paramString);
/*  37 */     this.buttonSize = paramDimension;
/*  38 */     setBorder(new EtchedBorder());
/*     */ 
/*     */ 
/*     */     
/*  42 */     setFloatable(false);
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
/*     */   public JButton add(Action paramAction, String paramString) {
/*  56 */     return add(paramAction, paramString, false);
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
/*     */   public JButton add(Action paramAction, String paramString, boolean paramBoolean) {
/*  69 */     JButton jButton = add(paramAction);
/*  70 */     jButton.setPreferredSize(this.buttonSize);
/*  71 */     jButton.setText("");
/*  72 */     jButton.setToolTipText(paramString);
/*  73 */     jButton.setFocusable(paramBoolean);
/*  74 */     if (paramBoolean)
/*  75 */       jButton.addFocusListener(new KeyboardFocusListener(this, jButton)); 
/*  76 */     return jButton;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addToDesktop(JDesktopPane paramJDesktopPane) {
/*  86 */     Dimension dimension = getPreferredSize();
/*  87 */     setBounds(0, 0, dimension.width, dimension.height);
/*     */     
/*  89 */     paramJDesktopPane.add(this, JLayeredPane.PALETTE_LAYER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFocusColour(Color paramColor) {
/*  98 */     this.focusColour = paramColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVisibleHeight() {
/* 108 */     return isVisible() ? (getPreferredSize()).height : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   class KeyboardFocusListener
/*     */     extends FocusAdapter
/*     */   {
/*     */     JButton button;
/*     */     
/*     */     Color colour;
/*     */     
/*     */     private final ToolBar this$0;
/*     */ 
/*     */     
/*     */     public KeyboardFocusListener(ToolBar this$0, JButton param1JButton) {
/* 123 */       this.this$0 = this$0; this.button = param1JButton;
/*     */     }
/*     */     public void focusGained(FocusEvent param1FocusEvent) {
/* 126 */       this.colour = this.button.getBackground();
/* 127 */       this.button.setBackground(this.this$0.focusColour);
/*     */     }
/*     */     
/*     */     public void focusLost(FocusEvent param1FocusEvent) {
/* 131 */       this.button.setBackground(this.colour);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/ToolBar.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */