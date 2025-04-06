/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
/*    */ import jsc.descriptive.MeanVar;
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
/*    */ public class LargeSampleMeanCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   private final MeanVar mv;
/*    */   
/*    */   public LargeSampleMeanCI(double[] paramArrayOfdouble, double paramDouble) {
/* 30 */     super(paramDouble);
/* 31 */     Normal normal = new Normal();
/* 32 */     double d1 = 1.0D - paramDouble;
/* 33 */     double d2 = normal.inverseCdf(1.0D - 0.5D * d1);
/*    */     
/* 35 */     this.mv = new MeanVar(paramArrayOfdouble);
/* 36 */     double d3 = this.mv.getMean();
/*    */ 
/*    */     
/* 39 */     double d4 = d2 * this.mv.getSd() / Math.sqrt(this.mv.getN());
/* 40 */     this.lowerLimit = d3 - d4;
/* 41 */     this.upperLimit = d3 + d4;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getMean() {
/* 49 */     return this.mv.getMean();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getSd() {
/* 56 */     return this.mv.getSd();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/LargeSampleMeanCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */