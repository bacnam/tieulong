/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
/*    */ import jsc.descriptive.MeanVar;
/*    */ import jsc.distributions.ChiSquared;
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
/*    */ public class NormalVarianceCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   private final double var;
/*    */   
/*    */   public NormalVarianceCI(double[] paramArrayOfdouble, double paramDouble) {
/* 30 */     super(paramDouble);
/* 31 */     MeanVar meanVar = new MeanVar(paramArrayOfdouble);
/* 32 */     int i = meanVar.getN() - 1;
/* 33 */     ChiSquared chiSquared = new ChiSquared(i);
/* 34 */     double d1 = 1.0D - paramDouble;
/* 35 */     double d2 = 0.5D * d1;
/* 36 */     double d3 = chiSquared.inverseCdf(d2);
/* 37 */     double d4 = chiSquared.inverseCdf(1.0D - d2);
/*    */     
/* 39 */     this.var = meanVar.getVariance();
/*    */ 
/*    */     
/* 42 */     double d5 = i * this.var;
/* 43 */     this.lowerLimit = d5 / d4;
/* 44 */     this.upperLimit = d5 / d3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getVariance() {
/* 52 */     return this.var;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 63 */       double[] arrayOfDouble = { 293.7D, 296.2D, 296.4D, 294.0D, 297.3D, 293.7D, 294.3D, 291.3D, 295.1D, 296.1D };
/* 64 */       NormalVarianceCI normalVarianceCI = new NormalVarianceCI(arrayOfDouble, 0.9D);
/* 65 */       System.out.println("Variance = " + normalVarianceCI.getVariance() + " CI = [" + normalVarianceCI.getLowerLimit() + ", " + normalVarianceCI.getUpperLimit() + "]");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/NormalVarianceCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */