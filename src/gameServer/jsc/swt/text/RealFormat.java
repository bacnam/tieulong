/*     */ package jsc.swt.text;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.NumberFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RealFormat
/*     */ {
/*     */   public DecimalFormat decimalFormat;
/*     */   public DecimalFormat scientificFormat;
/*     */   DecimalFormat localFormat;
/*     */   
/*     */   public RealFormat(Locale paramLocale) {
/*  36 */     NumberFormat numberFormat1 = NumberFormat.getInstance(paramLocale);
/*  37 */     if (numberFormat1 instanceof DecimalFormat) {
/*  38 */       this.decimalFormat = (DecimalFormat)numberFormat1;
/*     */     } else {
/*  40 */       throw new IllegalArgumentException("DecimalFormat not available for locale.");
/*  41 */     }  NumberFormat numberFormat2 = NumberFormat.getInstance(paramLocale);
/*  42 */     this.scientificFormat = (DecimalFormat)numberFormat2;
/*  43 */     NumberFormat numberFormat3 = NumberFormat.getInstance(paramLocale);
/*  44 */     this.localFormat = (DecimalFormat)numberFormat3;
/*  45 */     setGroupingUsed(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getDecimalSeparator() {
/*  56 */     DecimalFormatSymbols decimalFormatSymbols = this.localFormat.getDecimalFormatSymbols();
/*  57 */     return decimalFormatSymbols.getDecimalSeparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getGroupingSeparator() {
/*  68 */     DecimalFormatSymbols decimalFormatSymbols = this.localFormat.getDecimalFormatSymbols();
/*  69 */     return decimalFormatSymbols.getGroupingSeparator();
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
/*     */   public abstract String format(double paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Number parse(String paramString) throws ParseException;
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
/* 101 */     DecimalFormatSymbols decimalFormatSymbols = this.decimalFormat.getDecimalFormatSymbols();
/* 102 */     decimalFormatSymbols.setGroupingSeparator(paramChar);
/* 103 */     this.decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
/* 104 */     this.scientificFormat.setDecimalFormatSymbols(decimalFormatSymbols);
/* 105 */     this.localFormat.setDecimalFormatSymbols(decimalFormatSymbols);
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
/* 119 */     this.decimalFormat.setGroupingUsed(paramBoolean);
/* 120 */     this.scientificFormat.setGroupingUsed(paramBoolean);
/* 121 */     this.localFormat.setGroupingUsed(paramBoolean);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/RealFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */