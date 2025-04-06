/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
/*    */ import jsc.distributions.FishersF;
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
/*    */ public class ProportionCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   public ProportionCI(long paramLong1, long paramLong2, double paramDouble) {
/* 29 */     super(paramDouble);
/* 30 */     if (paramLong2 < 1L)
/* 31 */       throw new IllegalArgumentException("Invalid number of trials."); 
/* 32 */     if (paramLong1 < 0L || paramLong1 > paramLong2)
/* 33 */       throw new IllegalArgumentException("Invalid number of successes."); 
/* 34 */     double d1 = 1.0D - paramDouble;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     if (paramLong1 > 0L) {
/*    */       
/* 42 */       double d3 = (paramLong2 - paramLong1 + 1L);
/* 43 */       FishersF fishersF = new FishersF((paramLong1 + paramLong1), d3 + d3);
/* 44 */       double d4 = fishersF.inverseCdf(0.5D * d1);
/* 45 */       this.lowerLimit = 1.0D / (1.0D + d3 / paramLong1 * d4);
/*    */     } else {
/*    */       
/* 48 */       this.lowerLimit = 0.0D;
/*    */     } 
/* 50 */     double d2 = (paramLong2 - paramLong1);
/*    */     
/* 52 */     if (d2 > 0.0D) {
/*    */       
/* 54 */       FishersF fishersF = new FishersF((2L * (paramLong1 + 1L)), d2 + d2);
/* 55 */       double d = fishersF.inverseCdf(1.0D - 0.5D * d1);
/* 56 */       this.upperLimit = 1.0D / (1.0D + d2 / (paramLong1 + 1L) * d);
/*    */     } else {
/*    */       
/* 59 */       this.upperLimit = 1.0D;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 70 */       ProportionCI proportionCI = new ProportionCI(1L, 1000L, 0.95D);
/* 71 */       System.out.println("CI = [" + proportionCI.getLowerLimit() + ", " + proportionCI.getUpperLimit() + "]");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/ProportionCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */