/*    */ package jsc.onesample;
/*    */ 
/*    */ import jsc.ci.AbstractConfidenceInterval;
/*    */ import jsc.datastructures.PairedData;
/*    */ import jsc.descriptive.MeanVar;
/*    */ import jsc.distributions.StudentsT;
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
/*    */ public class NormalMeanCI
/*    */   extends AbstractConfidenceInterval
/*    */ {
/*    */   private final MeanVar mv;
/*    */   
/*    */   public NormalMeanCI(double[] paramArrayOfdouble, double paramDouble) {
/* 33 */     super(paramDouble);
/* 34 */     this.mv = new MeanVar(paramArrayOfdouble);
/* 35 */     int i = this.mv.getN();
/* 36 */     StudentsT studentsT = new StudentsT((i - 1));
/* 37 */     double d1 = 1.0D - paramDouble;
/* 38 */     double d2 = studentsT.inverseCdf(1.0D - 0.5D * d1);
/*    */     
/* 40 */     double d3 = this.mv.getMean();
/*    */ 
/*    */     
/* 43 */     double d4 = d2 * this.mv.getSd() / Math.sqrt(i);
/* 44 */     this.lowerLimit = d3 - d4;
/* 45 */     this.upperLimit = d3 + d4;
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
/*    */   public NormalMeanCI(PairedData paramPairedData, double paramDouble) {
/* 59 */     this(paramPairedData.differences(), paramDouble);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getMean() {
/* 66 */     return this.mv.getMean();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getSd() {
/* 73 */     return this.mv.getSd();
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
/* 86 */       double[] arrayOfDouble1 = { 4.9D, 5.1D, 4.6D, 5.0D, 5.1D, 4.7D, 4.4D, 4.7D, 4.6D };
/* 87 */       NormalMeanCI normalMeanCI = new NormalMeanCI(arrayOfDouble1, 90.0D);
/* 88 */       System.out.println("CI = [" + normalMeanCI.getLowerLimit() + ", " + normalMeanCI.getUpperLimit() + "]");
/*    */ 
/*    */       
/* 91 */       double[] arrayOfDouble2 = { 70.0D, 80.0D, 62.0D, 50.0D, 70.0D, 30.0D, 49.0D, 60.0D };
/* 92 */       double[] arrayOfDouble3 = { 75.0D, 82.0D, 65.0D, 58.0D, 68.0D, 41.0D, 55.0D, 67.0D };
/* 93 */       normalMeanCI = new NormalMeanCI(new PairedData(arrayOfDouble2, arrayOfDouble3), 0.9D);
/* 94 */       System.out.println("CI = [" + normalMeanCI.getLowerLimit() + ", " + normalMeanCI.getUpperLimit() + "]");
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/onesample/NormalMeanCI.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */