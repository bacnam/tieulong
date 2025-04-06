/*    */ package jsc.swt.text;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.DecimalFormatSymbols;
/*    */ import java.text.NumberFormat;
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
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
/*    */ public class IntegerFormat
/*    */ {
/*    */   protected DecimalFormat decimalFormat;
/*    */   
/*    */   public IntegerFormat() {
/* 27 */     this(Locale.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IntegerFormat(Locale paramLocale) {
/* 39 */     NumberFormat numberFormat = NumberFormat.getInstance(paramLocale);
/* 40 */     if (numberFormat instanceof DecimalFormat) {
/* 41 */       this.decimalFormat = (DecimalFormat)numberFormat;
/*    */     } else {
/* 43 */       throw new IllegalArgumentException("DecimalFormat not available for locale.");
/*    */     } 
/* 45 */     this.decimalFormat.setParseIntegerOnly(true);
/* 46 */     this.decimalFormat.setMinimumIntegerDigits(1);
/* 47 */     setGroupingUsed(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String format(int paramInt) {
/* 57 */     return this.decimalFormat.format(paramInt);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Number parse(String paramString) throws ParseException {
/* 68 */     return this.decimalFormat.parse(paramString);
/*    */   }
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
/*    */   public void setGroupingSeparator(char paramChar) {
/* 81 */     DecimalFormatSymbols decimalFormatSymbols = this.decimalFormat.getDecimalFormatSymbols();
/* 82 */     decimalFormatSymbols.setGroupingSeparator(paramChar);
/* 83 */     this.decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
/*    */   }
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
/*    */   public void setGroupingUsed(boolean paramBoolean) {
/* 96 */     this.decimalFormat.setGroupingUsed(paramBoolean);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/IntegerFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */