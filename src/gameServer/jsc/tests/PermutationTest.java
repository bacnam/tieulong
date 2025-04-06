/*     */ package jsc.tests;
/*     */ 
/*     */ import jsc.combinatorics.Enumerator;
/*     */ import jsc.combinatorics.Selection;
/*     */ import jsc.distributions.Tail;
/*     */ import jsc.event.StatisticEvent;
/*     */ import jsc.event.StatisticListener;
/*     */ import jsc.independentsamples.MannWhitneyTest;
/*     */ import jsc.statistics.PermutableStatistic;
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
/*     */ public class PermutationTest
/*     */   implements SignificanceTest
/*     */ {
/*  57 */   private long critCount = 0L;
/*  58 */   private double totalRepCount = 0.0D;
/*     */   private final double tObs;
/*  60 */   private double SP = 0.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Tail tail;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int N;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final double permCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Enumerator perm;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PermutableStatistic permutableStatistic;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StatisticListener statisticListener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt, double paramDouble, StatisticListener paramStatisticListener) {
/*  97 */     this.tail = paramTail;
/*  98 */     this.permutableStatistic = paramPermutableStatistic;
/*  99 */     this.tObs = paramPermutableStatistic.getStatistic();
/*     */ 
/*     */     
/* 102 */     if (paramStatisticListener != null) this.statisticListener = paramStatisticListener;
/*     */     
/* 104 */     this.N = paramPermutableStatistic.getN();
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
/* 123 */     this.perm = paramPermutableStatistic.getEnumerator();
/* 124 */     this.permCount = this.perm.countSelections();
/*     */ 
/*     */     
/* 127 */     if (paramBoolean && paramInt < this.permCount) {
/*     */       
/* 129 */       if (paramDouble <= 0.0D) {
/* 130 */         calculateSP(paramInt);
/*     */       } else {
/* 132 */         calculateSP(paramInt, paramDouble);
/*     */       } 
/*     */     } else {
/*     */       
/* 136 */       calculateSP();
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
/*     */   
/*     */   public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt, double paramDouble) {
/* 155 */     this(paramPermutableStatistic, paramTail, paramBoolean, paramInt, paramDouble, null);
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
/*     */   public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail, boolean paramBoolean, int paramInt) {
/* 168 */     this(paramPermutableStatistic, paramTail, paramBoolean, paramInt, -1.0D, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PermutationTest(PermutableStatistic paramPermutableStatistic, Tail paramTail) {
/* 178 */     this(paramPermutableStatistic, paramTail, false, 0, 0.0D, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PermutationTest(PermutableStatistic paramPermutableStatistic) {
/* 187 */     this(paramPermutableStatistic, Tail.UPPER, false, 0, 0.0D, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double calculateSP() {
/* 198 */     this.critCount = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     while (this.perm.hasNext()) { Selection selection = this.perm.nextSelection(); processPermutation(selection); }
/*     */     
/* 207 */     this.totalRepCount = this.permCount;
/* 208 */     this.SP = this.critCount / this.permCount;
/* 209 */     return this.SP;
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
/*     */   public double calculateSP(int paramInt) {
/* 223 */     if (this.totalRepCount >= this.permCount) return this.SP;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 228 */     for (byte b = 0; b < paramInt; b++) {
/*     */       
/* 230 */       Selection selection = this.perm.randomSelection();
/* 231 */       if (selection == null)
/* 232 */         break;  processPermutation(selection);
/* 233 */       this.totalRepCount++;
/*     */     } 
/* 235 */     this.SP = this.critCount / this.totalRepCount;
/* 236 */     return this.SP;
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
/*     */   public double calculateSP(int paramInt, double paramDouble) {
/*     */     while (true) {
/* 253 */       double d = this.SP;
/* 254 */       calculateSP(paramInt);
/* 255 */       if (Math.abs(this.SP - d) < paramDouble) {
/* 256 */         return this.SP;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getPermutationCount() {
/* 265 */     return this.permCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSP() {
/* 272 */     return this.SP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTestStatistic() {
/* 279 */     return this.tObs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getTotalRepCount() {
/* 286 */     return this.totalRepCount;
/*     */   }
/*     */ 
/*     */   
/*     */   private void processPermutation(Selection paramSelection) {
/* 291 */     double d = this.permutableStatistic.permuteStatistic(paramSelection);
/*     */ 
/*     */     
/* 294 */     if (this.statisticListener != null) {
/* 295 */       this.statisticListener.statisticCreated(new StatisticEvent(this, d));
/*     */     }
/*     */     
/* 298 */     if (this.tail == Tail.UPPER)
/* 299 */     { if (d >= this.tObs) this.critCount++;
/*     */        }
/* 301 */     else if (this.tail == Tail.LOWER)
/* 302 */     { if (d <= this.tObs) this.critCount++;
/*     */        }
/*     */     
/* 305 */     else if (Math.abs(d) >= Math.abs(this.tObs)) { this.critCount++; }
/*     */   
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeed(long paramLong) {
/* 347 */     this.perm.setSeed(paramLong);
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
/*     */   static class Test
/*     */   {
/*     */     static class StatListener
/*     */       implements StatisticListener
/*     */     {
/*     */       public void statisticCreated(StatisticEvent param2StatisticEvent) {
/* 411 */         System.out.print(" " + param2StatisticEvent.getStatistic());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static void main(String[] param1ArrayOfString) {
/* 419 */       double[] arrayOfDouble1 = { 78.0D, 64.0D, 75.0D, 45.0D, 82.0D };
/* 420 */       double[] arrayOfDouble2 = { 110.0D, 70.0D, 53.0D, 51.0D };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 430 */       MannWhitneyTest mannWhitneyTest = new MannWhitneyTest(arrayOfDouble1, arrayOfDouble2, H1.LESS_THAN, 0.0D, false);
/* 431 */       System.out.println("SP = " + mannWhitneyTest.getSP() + " U = " + mannWhitneyTest.getTestStatistic());
/* 432 */       PermutationTest permutationTest = new PermutationTest((PermutableStatistic)mannWhitneyTest, Tail.LOWER, false, 0, 0.0D, null);
/* 433 */       System.out.println("SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
/* 434 */       mannWhitneyTest = new MannWhitneyTest(arrayOfDouble1, arrayOfDouble2, H1.GREATER_THAN, 0.0D, false);
/* 435 */       System.out.println("SP = " + mannWhitneyTest.getSP() + " U = " + mannWhitneyTest.getTestStatistic());
/* 436 */       permutationTest = new PermutationTest((PermutableStatistic)mannWhitneyTest, Tail.LOWER, false, 0, 0.0D, null);
/* 437 */       System.out.println("SP = " + permutationTest.getSP() + " tObs = " + permutationTest.getTestStatistic());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/tests/PermutationTest.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */