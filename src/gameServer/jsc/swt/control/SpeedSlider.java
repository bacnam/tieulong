/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Font;
/*    */ import java.util.Hashtable;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JSlider;
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
/*    */ public class SpeedSlider
/*    */   extends JSlider
/*    */ {
/*    */   int minDelay;
/*    */   int maxDelay;
/*    */   JLabel slow;
/*    */   JLabel fast;
/*    */   
/*    */   public SpeedSlider(int paramInt1, int paramInt2, int paramInt3) {
/* 45 */     super(0, paramInt1, paramInt2, paramInt2 - paramInt3);
/* 46 */     this.minDelay = paramInt1;
/* 47 */     this.maxDelay = paramInt2;
/*    */ 
/*    */ 
/*    */     
/* 51 */     int i = paramInt2 - paramInt1;
/* 52 */     setMinorTickSpacing(i / 10);
/* 53 */     setMajorTickSpacing(i / 2);
/* 54 */     setPaintTicks(true);
/*    */ 
/*    */ 
/*    */     
/* 58 */     this.slow = new JLabel("Slow");
/* 59 */     this.fast = new JLabel("Fast");
/* 60 */     Hashtable hashtable = new Hashtable(2);
/* 61 */     hashtable.put(new Integer(paramInt1), this.slow);
/* 62 */     hashtable.put(new Integer(paramInt2), this.fast);
/* 63 */     setLabelTable(hashtable);
/* 64 */     setPaintLabels(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getDelay() {
/* 74 */     return this.maxDelay - getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDelay(int paramInt) {
/* 82 */     setValue(this.maxDelay - paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLabelColour(Color paramColor) {
/* 90 */     this.slow.setForeground(paramColor); this.fast.setForeground(paramColor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLabelFont(Font paramFont) {
/* 98 */     this.slow.setFont(paramFont); this.fast.setFont(paramFont);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/SpeedSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */