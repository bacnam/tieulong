/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
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
/*    */ public class PoissonMeanCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   public PoissonMeanCI(double paramDouble1, double paramDouble2) {
/* 25 */     super(paramDouble2);
/*    */     
/* 27 */     double d1 = 1.0D - paramDouble2;
/* 28 */     double d2 = 0.5D * d1;
/* 29 */     if (paramDouble1 > 0.0D) {
/*    */       
/* 31 */       ChiSquared chiSquared1 = new ChiSquared(paramDouble1 + paramDouble1);
/* 32 */       this.lowerLimit = 0.5D * chiSquared1.inverseCdf(d2);
/*    */     } else {
/*    */       
/* 35 */       this.lowerLimit = 0.0D;
/* 36 */     }  ChiSquared chiSquared = new ChiSquared(paramDouble1 + paramDouble1 + 2.0D);
/* 37 */     this.upperLimit = 0.5D * chiSquared.inverseCdf(1.0D - d2);
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
/*    */   
/*    */   public PoissonMeanCI(double paramDouble1, int paramInt, double paramDouble2) {
/* 51 */     this(paramDouble2, paramInt * paramDouble1);
/* 52 */     if (paramInt < 1)
/* 53 */       throw new IllegalArgumentException("Invalid number of observations."); 
/* 54 */     this.lowerLimit /= paramInt;
/* 55 */     this.upperLimit /= paramInt;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 65 */       PoissonMeanCI poissonMeanCI1 = new PoissonMeanCI(3.0D, 0.95D);
/* 66 */       System.out.println("CI = [" + poissonMeanCI1.getLowerLimit() + ", " + poissonMeanCI1.getUpperLimit() + "]");
/*    */       
/* 68 */       PoissonMeanCI poissonMeanCI2 = new PoissonMeanCI(3.0D, 7, 0.95D);
/* 69 */       System.out.println("CI = [" + poissonMeanCI2.getLowerLimit() + ", " + poissonMeanCI2.getUpperLimit() + "]");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/PoissonMeanCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */