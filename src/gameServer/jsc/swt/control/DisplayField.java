/*     */ package jsc.swt.control;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Toolkit;
/*     */ import java.text.ParseException;
/*     */ import javax.swing.JTextField;
/*     */ import jsc.swt.text.IntegerFormat;
/*     */ import jsc.swt.text.MaxWidthFormat;
/*     */ import jsc.swt.text.RealFormat;
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
/*     */ public class DisplayField
/*     */   extends JTextField
/*     */ {
/*     */   private RealFormat realFormatter;
/*     */   private IntegerFormat integerFormatter;
/*     */   private Toolkit toolkit;
/*     */   
/*     */   public DisplayField(String paramString, int paramInt) {
/*  43 */     this(paramString, paramInt, (RealFormat)new MaxWidthFormat(7));
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
/*     */   public DisplayField(String paramString, int paramInt, RealFormat paramRealFormat) {
/*  57 */     this(paramString, paramInt, new IntegerFormat(), paramRealFormat);
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
/*     */   public DisplayField(String paramString, int paramInt, IntegerFormat paramIntegerFormat, RealFormat paramRealFormat) {
/*  73 */     super(paramString, paramInt);
/*  74 */     setEditable(false);
/*  75 */     setBackground(Color.white);
/*  76 */     this.toolkit = Toolkit.getDefaultToolkit();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.integerFormatter = paramIntegerFormat;
/*     */ 
/*     */     
/*  85 */     this.realFormatter = paramRealFormat;
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
/*     */   public int getIntegerValue() {
/*  97 */     int i = 0;
/*     */     try {
/*  99 */       i = this.integerFormatter.parse(getText()).intValue();
/*     */     } catch (ParseException parseException) {
/* 101 */       this.toolkit.beep();
/* 102 */     }  return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getRealValue() {
/* 113 */     double d = 0.0D;
/*     */     try {
/* 115 */       d = this.realFormatter.parse(getText()).doubleValue();
/*     */     } catch (ParseException parseException) {
/* 117 */       this.toolkit.beep();
/* 118 */     }  return d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFocusable() {
/* 126 */     return false;
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
/*     */   public void setGroupingSeparator(char paramChar) {
/* 139 */     this.integerFormatter.setGroupingSeparator(paramChar);
/* 140 */     this.realFormatter.setGroupingSeparator(paramChar);
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
/*     */   public void setGroupingUsed(boolean paramBoolean) {
/* 154 */     this.integerFormatter.setGroupingUsed(paramBoolean);
/* 155 */     this.realFormatter.setGroupingUsed(paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIntegerFormat(IntegerFormat paramIntegerFormat) {
/* 163 */     this.integerFormatter = paramIntegerFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIntegerValue(int paramInt) {
/* 171 */     setText(this.integerFormatter.format(paramInt));
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
/*     */   public void setRealFormat(RealFormat paramRealFormat) {
/* 207 */     this.realFormatter = paramRealFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealValue(double paramDouble) {
/* 215 */     setText(this.realFormatter.format(paramDouble));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/control/DisplayField.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */