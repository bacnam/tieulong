/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
/*    */ import jsc.distributions.Normal;
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
/*    */ public class LargeSampleProportionCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   private final double pHat;
/*    */   
/*    */   public LargeSampleProportionCI(long paramLong1, long paramLong2, double paramDouble) {
/* 32 */     super(paramDouble);
/* 33 */     if (paramLong2 < 1L)
/* 34 */       throw new IllegalArgumentException("Invalid number of trials."); 
/* 35 */     if (paramLong1 < 0L || paramLong1 > paramLong2) {
/* 36 */       throw new IllegalArgumentException("Invalid number of successes.");
/*    */     }
/* 38 */     Normal normal = new Normal();
/* 39 */     double d1 = 1.0D - paramDouble;
/* 40 */     double d2 = normal.inverseCdf(1.0D - 0.5D * d1);
/*    */ 
/*    */     
/* 43 */     this.pHat = paramLong1 / paramLong2;
/* 44 */     double d3 = d2 * Math.sqrt(this.pHat * (1.0D - this.pHat) / paramLong2);
/* 45 */     this.lowerLimit = this.pHat - d3;
/* 46 */     this.upperLimit = this.pHat + d3;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getP() {
/* 54 */     return this.pHat;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/LargeSampleProportionCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */