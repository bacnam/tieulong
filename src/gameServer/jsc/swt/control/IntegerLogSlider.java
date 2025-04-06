/*    */ package jsc.swt.control;
/*    */ 
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
/*    */ public class IntegerLogSlider
/*    */   extends JSlider
/*    */ {
/*    */   double dminVal;
/*    */   double logRange;
/*    */   
/*    */   public IntegerLogSlider(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
/* 38 */     super(0);
/* 39 */     if (paramInt1 <= 0 || paramInt2 <= paramInt1)
/* 40 */       throw new IllegalArgumentException("Illegal range for logarithmic slider."); 
/* 41 */     this.dminVal = paramInt1;
/* 42 */     this.logRange = Math.log(paramInt2 / this.dminVal);
/* 43 */     setMinimum(0);
/*    */     
/* 45 */     setMaximum(paramInt4);
/*    */     
/* 47 */     setValue(getSliderValue(paramInt3));
/*    */ 
/*    */ 
/*    */     
/* 51 */     setPaintTicks(false);
/*    */ 
/*    */     
/* 54 */     int i = paramArrayOfint.length;
/* 55 */     Hashtable hashtable = new Hashtable(i);
/*    */ 
/*    */     
/* 58 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 60 */       int j = paramArrayOfint[b];
/* 61 */       if (j >= paramInt1 && j <= paramInt2)
/* 62 */         hashtable.put(new Integer(getSliderValue(j)), new JLabel(Integer.toString(j))); 
/*    */     } 
/* 64 */     setLabelTable(hashtable);
/* 65 */     setPaintLabels(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getIntegerValue() {
/* 74 */     return getIntegerValue(getValue());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   int getIntegerValue(int paramInt) {
/* 80 */     return (int)Math.round(this.dminVal * Math.exp(paramInt * this.logRange / getMaximum()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getSliderValue(int paramInt) {
/* 87 */     double d = getMaximum() * Math.log(paramInt / this.dminVal) / this.logRange;
/*    */     
/* 89 */     return (int)Math.round(d);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIntegerValue(int paramInt) {
/* 97 */     setValue(getSliderValue(paramInt));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/IntegerLogSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */