/*    */ package jsc.swt.control;
/*    */ 
/*    */ import java.text.DecimalFormat;
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
/*    */ public class RealLogSlider
/*    */   extends JSlider
/*    */ {
/*    */   double minVal;
/*    */   double logRange;
/*    */   
/*    */   public RealLogSlider(double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, double[] paramArrayOfdouble, String paramString) {
/* 40 */     super(0);
/* 41 */     if (paramDouble1 <= 0.0D || paramDouble2 <= paramDouble1)
/* 42 */       throw new IllegalArgumentException("Illegal range for logarithmic slider."); 
/* 43 */     this.minVal = paramDouble1;
/* 44 */     this.logRange = Math.log(paramDouble2 / paramDouble1);
/* 45 */     setMinimum(0);
/* 46 */     setMaximum(paramInt);
/* 47 */     setValue(getSliderValue(paramDouble3));
/*    */ 
/*    */ 
/*    */     
/* 51 */     setPaintTicks(false);
/*    */ 
/*    */     
/* 54 */     int i = paramArrayOfdouble.length;
/* 55 */     Hashtable hashtable = new Hashtable(i);
/* 56 */     DecimalFormat decimalFormat = new DecimalFormat(paramString);
/*    */     
/* 58 */     for (byte b = 0; b < i; b++) {
/*    */       
/* 60 */       double d = paramArrayOfdouble[b];
/* 61 */       if (d >= paramDouble1 && d <= paramDouble2)
/* 62 */         hashtable.put(new Integer(getSliderValue(d)), new JLabel(decimalFormat.format(d))); 
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
/*    */   public double getRealValue() {
/* 74 */     return getRealValue(getValue());
/*    */   }
/*    */   
/*    */   double getRealValue(int paramInt) {
/* 78 */     return this.minVal * Math.exp(paramInt * this.logRange / getMaximum());
/*    */   }
/*    */ 
/*    */   
/*    */   int getSliderValue(double paramDouble) {
/* 83 */     double d = getMaximum() * Math.log(paramDouble / this.minVal) / this.logRange;
/* 84 */     return (int)Math.round(d);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRealValue(double paramDouble) {
/* 92 */     setValue(getSliderValue(paramDouble));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/RealLogSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */