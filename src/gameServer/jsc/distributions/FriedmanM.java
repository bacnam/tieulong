/*    */ package jsc.distributions;
/*    */ 
/*    */ import jsc.descriptive.DoubleTally;
/*    */ import jsc.goodnessfit.ChiSquaredFitTest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FriedmanM
/*    */   extends RankSumOfSquares
/*    */ {
/*    */   public FriedmanM(int paramInt1, int paramInt2) {
/* 28 */     super(paramInt1, paramInt2);
/*    */ 
/*    */     
/* 31 */     double d = 12.0D * (paramInt2 - 1.0D) / (paramInt1 * paramInt2) * ((paramInt2 * paramInt2) - 1.0D);
/* 32 */     for (byte b = 0; b < getValueCount(); ) { this.values[b] = d * this.values[b]; b++; }
/*    */   
/*    */   } public String toString() {
/* 35 */     return new String("Friedman's M distribution: n = " + this.n + ", k = " + this.k + ".");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 48 */       byte b2 = 10, b3 = 3;
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
/*    */ 
/*    */       
/* 83 */       char c = '✐';
/* 84 */       FriedmanM friedmanM = new FriedmanM(10, 3);
/*    */ 
/*    */ 
/*    */       
/* 88 */       double[] arrayOfDouble = new double[c];
/* 89 */       for (byte b1 = 0; b1 < c; ) { arrayOfDouble[b1] = friedmanM.random(); b1++; }
/* 90 */        ChiSquaredFitTest chiSquaredFitTest = new ChiSquaredFitTest(new DoubleTally(arrayOfDouble), friedmanM, 0);
/* 91 */       System.out.println("All E > 5 " + chiSquaredFitTest.poolBins());
/* 92 */       System.out.println("m = " + c + " Chi-squared = " + chiSquaredFitTest.getTestStatistic() + " SP = " + chiSquaredFitTest.getSP());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/FriedmanM.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */