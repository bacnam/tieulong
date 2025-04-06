/*     */ package jsc.swt.control;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JSlider;
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
/*     */ public class RealSlider
/*     */   extends JSlider
/*     */ {
/*     */   double minVal;
/*     */   double maxVal;
/*     */   double range;
/*     */   
/*     */   public RealSlider(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, String paramString) {
/*  40 */     super(0);
/*  41 */     rescale(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5, paramDouble6, paramDouble7, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaximumValue() {
/*  50 */     return this.maxVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMinimumValue() {
/*  57 */     return this.minVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRealValue() {
/*  66 */     return this.minVal + this.range * getValue() / getMaximum();
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
/*     */   public void rescale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, String paramString) {
/*  87 */     this.minVal = paramDouble1;
/*  88 */     this.maxVal = paramDouble2;
/*  89 */     this.range = paramDouble2 - paramDouble1;
/*  90 */     int i = (int)(this.range / paramDouble4);
/*  91 */     setMinimum(0);
/*  92 */     setMaximum(i);
/*     */     
/*  94 */     setValue((int)((paramDouble3 - paramDouble1) / paramDouble4));
/*  95 */     setMinorTickSpacing((int)(paramDouble5 / paramDouble4));
/*  96 */     setMajorTickSpacing((int)(paramDouble6 / paramDouble4));
/*  97 */     if (paramDouble7 > 0.0D) {
/*     */       
/*  99 */       int j = (int)(1.0D + this.range / paramDouble7);
/* 100 */       int k = i / (j - 1);
/* 101 */       setPaintTicks(true);
/*     */       
/* 103 */       Hashtable hashtable = new Hashtable(j);
/*     */       
/* 105 */       DecimalFormat decimalFormat = new DecimalFormat(paramString);
/* 106 */       for (byte b = 0; b < j; b++) {
/*     */         
/* 108 */         double d = paramDouble1 + b * paramDouble7;
/* 109 */         hashtable.put(new Integer(b * k), new JLabel(decimalFormat.format(d)));
/*     */       } 
/* 111 */       setLabelTable(hashtable);
/* 112 */       setPaintLabels(true);
/*     */     } else {
/*     */       
/* 115 */       setPaintLabels(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getSliderValue(double paramDouble) {
/* 123 */     double d = (paramDouble - this.minVal) / this.range * getMaximum();
/*     */     
/* 125 */     return (int)Math.round(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabels(double[] paramArrayOfdouble, String paramString) {
/* 136 */     int i = paramArrayOfdouble.length;
/* 137 */     setPaintTicks(true);
/*     */     
/* 139 */     Hashtable hashtable = new Hashtable(i);
/* 140 */     DecimalFormat decimalFormat = new DecimalFormat(paramString);
/* 141 */     for (byte b = 0; b < i; b++)
/* 142 */       hashtable.put(new Integer(getSliderValue(paramArrayOfdouble[b])), new JLabel(decimalFormat.format(paramArrayOfdouble[b]))); 
/* 143 */     setLabelTable(hashtable);
/* 144 */     setPaintLabels(true);
/* 145 */     updateUI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealValue(double paramDouble) {
/* 155 */     setValue(getSliderValue(paramDouble));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/RealSlider.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */