/*    */ package jsc.swt.text;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ public class PatternFormat
/*    */   extends RealFormat
/*    */ {
/*    */   public PatternFormat(String paramString) {
/* 25 */     this(paramString, Locale.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PatternFormat(String paramString, Locale paramLocale) {
/* 36 */     super(paramLocale);
/* 37 */     this.decimalFormat.applyPattern(paramString);
/*    */   }
/*    */   
/*    */   public String format(double paramDouble) {
/* 41 */     return this.decimalFormat.format(paramDouble);
/*    */   }
/*    */   public Number parse(String paramString) throws ParseException {
/* 44 */     return this.decimalFormat.parse(paramString);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/PatternFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */