/*     */ package jsc.swt.text;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Locale;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class SigFigFormat
/*     */   extends RealFormat
/*     */ {
/*     */   private static final double TOLERANCE = 8.0E-15D;
/*     */   int n;
/*     */   double minDecimal;
/*     */   double maxDecimal;
/*     */   
/*     */   public SigFigFormat(int paramInt) {
/*  31 */     this(paramInt, Locale.getDefault());
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
/*     */   public SigFigFormat(int paramInt, Locale paramLocale) {
/*  43 */     super(paramLocale);
/*  44 */     this.decimalFormat.setMinimumIntegerDigits(1);
/*  45 */     setSignificantDigits(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(double paramDouble) {
/*     */     double d2;
/*  52 */     if (Double.isNaN(paramDouble)) return "NaN"; 
/*  53 */     double d1 = Math.abs(paramDouble);
/*  54 */     if (d1 == 0.0D) return "0"; 
/*  55 */     String str1 = this.scientificFormat.format(paramDouble);
/*     */ 
/*     */ 
/*     */     
/*  59 */     if (d1 < this.minDecimal || d1 >= this.maxDecimal) return str1;
/*     */     
/*  61 */     if (d1 < 1.0D)
/*     */     {
/*  63 */       if (d1 >= 0.1D) {
/*     */         
/*  65 */         this.decimalFormat.setMaximumFractionDigits(this.n);
/*  66 */         return this.decimalFormat.format(paramDouble);
/*     */       } 
/*     */     }
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
/*  83 */     StringTokenizer stringTokenizer = new StringTokenizer(str1, "E");
/*  84 */     String str2 = stringTokenizer.nextToken();
/*     */     
/*  86 */     String str3 = stringTokenizer.nextToken();
/*     */     
/*  88 */     int i = Integer.parseInt(str3);
/*     */ 
/*     */     
/*  91 */     try { Number number = this.localFormat.parse(str2); d2 = number.doubleValue(); }
/*  92 */     catch (ParseException parseException) { d2 = Double.NaN; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     int j = str2.length() - 1 - i;
/*  98 */     if (paramDouble < 0.0D) j--; 
/*  99 */     this.decimalFormat.setMaximumFractionDigits(j);
/*     */     
/* 101 */     return this.decimalFormat.format(d2 * Math.pow(10.0D, i));
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
/*     */   public int getSignificantDigits() {
/* 115 */     return this.n;
/*     */   }
/*     */   public Number parse(String paramString) throws ParseException {
/*     */     try {
/* 119 */       return this.decimalFormat.parse(paramString);
/*     */     } catch (ParseException parseException) {
/* 121 */       return this.scientificFormat.parse(paramString);
/*     */     } 
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
/*     */   public double round(double paramDouble) {
/* 135 */     String str = format(paramDouble); 
/* 136 */     try { Number number = parse(str); return number.doubleValue(); }
/* 137 */     catch (ParseException parseException) { return Double.NaN; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignificantDigits(int paramInt) {
/* 148 */     if (paramInt < 1 || paramInt > 16)
/* 149 */       throw new IllegalArgumentException("Invalid number of significant figures."); 
/* 150 */     this.n = paramInt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     StringBuffer stringBuffer = new StringBuffer("0.");
/* 160 */     for (byte b = 0; b < paramInt - 1; ) { stringBuffer.append('#'); b++; }
/* 161 */      stringBuffer.append("E0");
/*     */ 
/*     */     
/* 164 */     this.scientificFormat.applyPattern(stringBuffer.toString());
/*     */     
/* 166 */     this.minDecimal = Math.pow(10.0D, -paramInt);
/* 167 */     this.maxDecimal = Math.pow(10.0D, paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 178 */       double[] arrayOfDouble = { 1.0E-5D, 1.2E-4D, 0.00123D, 0.01234D, 0.12345D, 0.123456D, 1.23456D, 12.3456D, 12345.0D, 123456.0D, 100000.0D, 0.0D, 1.23456789E8D, 0.123D, 5.0E-4D, 6.0E-4D, 6.789E-4D, 5.67E-4D, 0.003454D, 0.0034501D, 0.00345005D, 0.00345004D, 0.0025000000000000005D, 0.003000000000000001D, 1.234567E-14D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 185 */       byte b1 = 5;
/* 186 */       DecimalFormat decimalFormat = new DecimalFormat("##########.####################");
/*     */ 
/*     */       
/* 189 */       SigFigFormat sigFigFormat = new SigFigFormat(b1);
/*     */       
/* 191 */       Object object = null;
/*     */       
/* 193 */       for (byte b2 = 0; b2 < arrayOfDouble.length; b2++) {
/* 194 */         for (byte b = 0; b < 2; b++) {
/*     */           
/* 196 */           double d = (b == 0) ? arrayOfDouble[b2] : -arrayOfDouble[b2];
/* 197 */           String str = sigFigFormat.format(d);
/* 198 */           System.out.println(decimalFormat.format(d) + " formatted to " + b1 + " sig.figs. is " + str);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/SigFigFormat.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */