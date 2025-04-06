/*     */ package jsc.independentsamples;
/*     */ 
/*     */ import jsc.datastructures.GroupedData;
/*     */ import jsc.distributions.MannWhitneyU;
/*     */ import jsc.distributions.Normal;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
/*     */ import jsc.util.Arrays;
/*     */ import jsc.util.Maths;
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
/*     */ public class JonckheereTest
/*     */   implements SignificanceTest
/*     */ {
/*  29 */   static final int MAX_PRODUCT = (int)Math.pow(15.0D, 6.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int N;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   double W;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int[] n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private double SP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JonckheereTest(GroupedData paramGroupedData, String[] paramArrayOfString, double paramDouble, boolean paramBoolean) {
/*  69 */     int i = paramGroupedData.getGroupCount();
/*  70 */     if (i < 2)
/*  71 */       throw new IllegalArgumentException("Less than two samples."); 
/*  72 */     this.N = paramGroupedData.getN();
/*  73 */     if (this.N < 5)
/*  74 */       throw new IllegalArgumentException("Less than five data values."); 
/*  75 */     if (paramArrayOfString.length != i)
/*  76 */       throw new IllegalArgumentException("Alternative array wrong length."); 
/*  77 */     this.n = new int[i];
/*     */ 
/*     */     
/*  80 */     this.W = 0.0D;
/*  81 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  83 */       double[] arrayOfDouble = paramGroupedData.getData(paramArrayOfString[b]);
/*  84 */       int k = arrayOfDouble.length;
/*  85 */       this.n[b] = k;
/*  86 */       for (int j = b + 1; j < i; j++) {
/*     */         
/*  88 */         double[] arrayOfDouble1 = paramGroupedData.getData(paramArrayOfString[j]);
/*  89 */         if (arrayOfDouble1 == null)
/*  90 */           throw new IllegalArgumentException("Invalid alternative sample label."); 
/*  91 */         int m = arrayOfDouble1.length;
/*  92 */         MannWhitneyTest mannWhitneyTest = new MannWhitneyTest(arrayOfDouble, arrayOfDouble1, H1.LESS_THAN, paramDouble, true);
/*  93 */         double d = mannWhitneyTest.getTestStatistic();
/*  94 */         this.W += d;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  99 */     if (paramBoolean) {
/* 100 */       this.SP = approxSP(this.n, this.W);
/*     */     } else {
/*     */       
/* 103 */       try { this.SP = exactSP(this.n, this.W); }
/* 104 */       catch (RuntimeException runtimeException) { this.SP = approxSP(this.n, this.W); }
/*     */     
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JonckheereTest(GroupedData paramGroupedData, String[] paramArrayOfString) {
/* 123 */     this(paramGroupedData, paramArrayOfString, 0.0D, (paramGroupedData.getMaxSize() > 4));
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
/*     */   public static double approxSP(int[] paramArrayOfint, double paramDouble) {
/* 137 */     int i = 0;
/* 138 */     double d1 = 0.0D;
/* 139 */     double d2 = 0.0D;
/* 140 */     for (byte b = 0; b < paramArrayOfint.length; b++) {
/*     */       
/* 142 */       if (paramArrayOfint[b] < 2)
/* 143 */         throw new IllegalArgumentException("Less than two data values in a sample."); 
/* 144 */       d1 += (paramArrayOfint[b] * paramArrayOfint[b]);
/* 145 */       d2 += (paramArrayOfint[b] * paramArrayOfint[b]) * ((paramArrayOfint[b] + paramArrayOfint[b]) + 3.0D);
/* 146 */       i += paramArrayOfint[b];
/*     */     } 
/* 148 */     double d3 = 0.25D * ((i * i) - d1);
/* 149 */     double d4 = Math.sqrt(((i * i) * ((i + i) + 3.0D) - d2) / 72.0D);
/* 150 */     double d5 = (paramDouble - d3 + 0.5D) / d4;
/*     */     
/* 152 */     return Normal.standardTailProb(d5, false);
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
/*     */   public static double exactSP(int[] paramArrayOfint, double paramDouble) {
/* 164 */     int i = (int)Arrays.sum(paramArrayOfint);
/*     */ 
/*     */     
/* 167 */     if (paramDouble > (MAX_PRODUCT / 2))
/* 168 */       throw new RuntimeException("Insufficient memory for exact distribution: try normal approximation."); 
/* 169 */     int j = (int)Math.ceil(paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     double[] arrayOfDouble = new double[1 + j];
/*     */ 
/*     */     
/* 178 */     i -= paramArrayOfint[0];
/* 179 */     MannWhitneyU.harding(false, paramArrayOfint[0], i, j, arrayOfDouble);
/*     */ 
/*     */     
/* 182 */     for (byte b1 = 1; b1 < paramArrayOfint.length - 1; b1++) {
/*     */       
/* 184 */       i -= paramArrayOfint[b1];
/* 185 */       MannWhitneyU.harding(true, paramArrayOfint[b1], i, j, arrayOfDouble);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 190 */     double d2 = Maths.logMultinomialCoefficient(paramArrayOfint);
/*     */     
/* 192 */     double d1 = 0.0D;
/*     */     
/* 194 */     for (byte b2 = 0; b2 <= j; b2++) {
/*     */ 
/*     */ 
/*     */       
/* 198 */       double d = Math.exp(Math.log(arrayOfDouble[b2]) - d2);
/*     */       
/* 200 */       if (Double.isNaN(d))
/*     */       {
/*     */         
/* 203 */         throw new RuntimeException("Cannot calculate exact distribution: try normal approximation.");
/*     */       }
/* 205 */       d1 += d;
/*     */     } 
/* 207 */     return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 217 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 224 */     return this.W;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 240 */       double[] arrayOfDouble = { 19.0D, 21.0D, 40.0D, 49.0D, 20.0D, 61.0D, 99.0D, 110.0D, 60.0D, 80.0D, 100.0D, 151.0D, 130.0D, 129.0D, 149.0D, 160.0D };
/* 241 */       String[] arrayOfString1 = { "I", "II", "III", "IV", "I", "II", "III", "IV", "I", "II", "III", "IV", "I", "II", "III", "IV" };
/* 242 */       String[] arrayOfString2 = { "I", "II", "III", "IV" };
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
/* 258 */       GroupedData groupedData = new GroupedData(arrayOfDouble, arrayOfString1);
/* 259 */       JonckheereTest jonckheereTest = new JonckheereTest(groupedData, arrayOfString2, 0.0D, false);
/* 260 */       double d = jonckheereTest.getTestStatistic();
/* 261 */       System.out.println("W = " + d);
/* 262 */       System.out.println("       SP = " + jonckheereTest.getSP());
/* 263 */       System.out.println("Approx SP = " + JonckheereTest.approxSP(groupedData.getSizes(), d));
/* 264 */       System.out.println(" Exact SP = " + JonckheereTest.exactSP(groupedData.getSizes(), d));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/JonckheereTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */