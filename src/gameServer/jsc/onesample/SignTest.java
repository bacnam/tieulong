/*     */ package jsc.onesample;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.distributions.Binomial;
/*     */ import jsc.tests.H1;
/*     */ import jsc.tests.SignificanceTest;
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
/*     */ public class SignTest
/*     */   implements SignificanceTest
/*     */ {
/*     */   private int n1;
/*     */   private int S;
/*     */   private double SP;
/*     */   
/*     */   public SignTest(double[] paramArrayOfdouble, double paramDouble, H1 paramH1) {
/*  41 */     int i = paramArrayOfdouble.length;
/*  42 */     byte b1 = 0;
/*  43 */     this.S = 0;
/*  44 */     this.n1 = 0;
/*     */     
/*  46 */     for (byte b2 = 0; b2 < i; b2++) {
/*     */       
/*  48 */       if (paramArrayOfdouble[b2] < paramDouble) {
/*  49 */         this.S++;
/*  50 */       } else if (paramArrayOfdouble[b2] <= paramDouble) {
/*     */ 
/*     */         
/*  53 */         b1++;
/*     */       } 
/*     */     } 
/*  56 */     this.n1 = i - b1;
/*  57 */     if (this.n1 < 1)
/*  58 */       throw new IllegalArgumentException("No non-zero differences."); 
/*  59 */     Binomial binomial = new Binomial(this.n1, 0.5D);
/*     */     
/*  61 */     if (paramH1 == H1.NOT_EQUAL) {
/*     */       
/*  63 */       this.S = Math.min(this.S, this.n1 - this.S);
/*  64 */       this.SP = 2.0D * binomial.cdf(this.S);
/*  65 */       if (this.SP > 1.0D) this.SP = 1.0D;
/*     */     
/*  67 */     } else if (paramH1 == H1.LESS_THAN) {
/*  68 */       this.SP = 1.0D - binomial.cdf((this.S - 1));
/*     */     } else {
/*     */       
/*  71 */       this.S = this.n1 - this.S;
/*  72 */       this.SP = 1.0D - binomial.cdf((this.S - 1));
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
/*     */   public SignTest(double[] paramArrayOfdouble, double paramDouble) {
/*  86 */     this(paramArrayOfdouble, paramDouble, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SignTest(PairedData paramPairedData, H1 paramH1) {
/*  95 */     this(paramPairedData.differences(), 0.0D, paramH1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SignTest(PairedData paramPairedData) {
/* 103 */     this(paramPairedData.differences(), 0.0D, H1.NOT_EQUAL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 112 */     return this.n1;
/*     */   } public double getSP() {
/* 114 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 121 */     return this.S;
/*     */   }
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
/* 133 */       double[] arrayOfDouble1 = { 70.0D, 65.0D, 75.0D, 58.0D, 56.0D, 60.0D, 80.0D, 75.0D, 71.0D, 69.0D, 58.0D, 75.0D };
/* 134 */       double d = 60.0D;
/* 135 */       SignTest signTest = new SignTest(arrayOfDouble1, d, H1.NOT_EQUAL);
/* 136 */       System.out.println("H1: median not equal " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/* 137 */       signTest = new SignTest(arrayOfDouble1, d, H1.LESS_THAN);
/* 138 */       System.out.println("H1: median < " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/*     */ 
/*     */       
/* 141 */       double[] arrayOfDouble2 = { 0.0D, 50.0D, 56.0D, 72.0D, 80.0D, 80.0D, 80.0D, 99.0D, 101.0D, 110.0D, 110.0D, 110.0D, 120.0D, 140.0D, 144.0D, 145.0D, 150.0D, 180.0D, 201.0D, 210.0D, 220.0D, 240.0D, 290.0D, 309.0D, 320.0D, 325.0D, 400.0D, 500.0D, 507.0D };
/* 142 */       d = 115.0D;
/* 143 */       signTest = new SignTest(arrayOfDouble2, d, H1.GREATER_THAN);
/* 144 */       System.out.println("H1: median > " + d + " N for test = " + signTest.getN() + " S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/*     */ 
/*     */       
/* 147 */       double[] arrayOfDouble3 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
/* 148 */       double[] arrayOfDouble4 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
/* 149 */       PairedData pairedData = new PairedData(arrayOfDouble3, arrayOfDouble4);
/* 150 */       signTest = new SignTest(pairedData);
/* 151 */       System.out.println("H1: averages not equal: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/* 152 */       signTest = new SignTest(pairedData, H1.LESS_THAN);
/* 153 */       System.out.println("H1: average A < average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/* 154 */       signTest = new SignTest(pairedData, H1.GREATER_THAN);
/* 155 */       System.out.println("H1: average A > average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/*     */ 
/*     */       
/* 158 */       double[] arrayOfDouble5 = { 17.4D, 15.7D, 12.9D, 9.8D, 13.4D, 18.7D, 13.9D, 11.0D, 5.4D, 10.4D, 16.4D, 5.6D };
/* 159 */       double[] arrayOfDouble6 = { 13.6D, 10.1D, 10.3D, 9.2D, 11.1D, 20.4D, 10.4D, 11.4D, 4.9D, 8.9D, 11.2D, 4.8D };
/* 160 */       pairedData = new PairedData(arrayOfDouble5, arrayOfDouble6);
/* 161 */       signTest = new SignTest(pairedData, H1.GREATER_THAN);
/* 162 */       System.out.println("H1: average A > average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/*     */ 
/*     */       
/* 165 */       double[] arrayOfDouble7 = { 4.0D, 4.0D, 5.0D, 5.0D, 3.0D, 2.0D, 5.0D, 3.0D, 1.0D, 5.0D, 5.0D, 5.0D, 4.0D, 5.0D, 5.0D, 5.0D, 5.0D };
/* 166 */       double[] arrayOfDouble8 = { 2.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 3.0D, 2.0D, 3.0D, 2.0D, 2.0D, 5.0D, 2.0D, 5.0D, 3.0D, 1.0D };
/* 167 */       pairedData = new PairedData(arrayOfDouble7, arrayOfDouble8);
/* 168 */       signTest = new SignTest(pairedData, H1.GREATER_THAN);
/* 169 */       System.out.println("H1: average A < average B: S = " + signTest.getTestStatistic() + " SP = " + signTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/SignTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */