/*     */ package jsc.swt.text;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
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
/*     */ public class MaxWidthFormat
/*     */   extends RealFormat
/*     */ {
/*     */   int maxWidth;
/*     */   
/*     */   public MaxWidthFormat(int paramInt) {
/*  30 */     this(paramInt, Locale.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MaxWidthFormat(int paramInt, Locale paramLocale) {
/*  41 */     super(paramLocale);
/*  42 */     this.decimalFormat.setMinimumIntegerDigits(1);
/*  43 */     setMaximumWidth(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(double paramDouble) {
/*  49 */     if (Double.isNaN(paramDouble)) return "NaN"; 
/*  50 */     if (Math.abs(paramDouble) == 0.0D) return "0";
/*     */     
/*  52 */     String str = this.decimalFormat.format(paramDouble);
/*  53 */     if (str.length() <= this.maxWidth) return str;
/*     */     
/*  55 */     int i = this.maxWidth;
/*  56 */     SigFigFormat sigFigFormat = new SigFigFormat(i);
/*     */     while (true) {
/*  58 */       sigFigFormat.setSignificantDigits(i--);
/*  59 */       str = sigFigFormat.format(paramDouble);
/*  60 */       if (str.length() <= this.maxWidth)
/*  61 */         return str; 
/*     */     } 
/*     */   }
/*     */   public Number parse(String paramString) throws ParseException {
/*     */     
/*  66 */     try { return this.decimalFormat.parse(paramString); }
/*  67 */     catch (ParseException parseException) { return this.scientificFormat.parse(paramString); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximumWidth(int paramInt) {
/*  77 */     if (paramInt < 7)
/*  78 */       throw new IllegalArgumentException("Width must be at least 7."); 
/*  79 */     this.maxWidth = paramInt;
/*  80 */     this.decimalFormat.setMaximumFractionDigits(paramInt - 2);
/*     */ 
/*     */     
/*  83 */     StringBuffer stringBuffer = new StringBuffer("0.");
/*  84 */     for (byte b = 0; b < paramInt - 6; ) { stringBuffer.append('#'); b++; }
/*  85 */      stringBuffer.append("E0");
/*     */     
/*  87 */     this.scientificFormat.applyPattern(stringBuffer.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/*  97 */       double[] arrayOfDouble = { 1.0E-5D, 1.2E-4D, 0.00123D, 0.01234D, 0.12345D, 0.123456D, 1.23456D, 12.3456D, 12345.0D, 123456.0D, 100000.0D, 0.0D };
/*     */       
/*  99 */       byte b1 = 7;
/* 100 */       DecimalFormat decimalFormat = new DecimalFormat();
/* 101 */       decimalFormat.setMaximumFractionDigits(16);
/* 102 */       decimalFormat.setMaximumIntegerDigits(16);
/* 103 */       MaxWidthFormat maxWidthFormat = new MaxWidthFormat(b1);
/*     */ 
/*     */       
/* 106 */       for (byte b2 = 0; b2 < arrayOfDouble.length; b2++) {
/* 107 */         for (byte b = 0; b < 2; b++) {
/*     */           Number number;
/* 109 */           double d = (b == 0) ? arrayOfDouble[b2] : -arrayOfDouble[b2];
/* 110 */           String str = maxWidthFormat.format(d);
/* 111 */           System.out.println(decimalFormat.format(d) + " formatted to " + b1 + " characters is " + str); 
/* 112 */           try { number = maxWidthFormat.parse(str); }
/* 113 */           catch (ParseException parseException) { System.out.println("Cannot parse " + str); }
/* 114 */            System.out.println(" " + str + " parsed is " + decimalFormat.format(number.doubleValue()));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/MaxWidthFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */