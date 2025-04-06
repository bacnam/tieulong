/*    */ package jsc.distributions;
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
/*    */ public class Rayleigh
/*    */   extends Weibull
/*    */ {
/*    */   public Rayleigh(double paramDouble) {
/* 18 */     super(paramDouble * Math.sqrt(2.0D), 2.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static class Test
/*    */   {
/*    */     public static void main(String[] param1ArrayOfString) {
/* 28 */       double d = 1.0D;
/* 29 */       double[] arrayOfDouble = { 0.1D, 0.25D, 0.5D, 0.75D, 1.0D, 2.0D, 3.0D, 10.0D };
/*    */       
/* 31 */       Rayleigh rayleigh = new Rayleigh(d);
/* 32 */       System.out.println("Rayleigh distribution: b = " + d); byte b;
/* 33 */       for (b = 0; b < arrayOfDouble.length; b++) {
/* 34 */         System.out.println("X=" + arrayOfDouble[b] + " pdf=" + rayleigh.pdf(arrayOfDouble[b]) + " cdf=" + rayleigh.cdf(arrayOfDouble[b]) + " X=" + rayleigh.inverseCdf(rayleigh.cdf(arrayOfDouble[b])));
/*    */       }
/*    */       
/* 37 */       System.out.println("Random numbers");
/* 38 */       for (b = 0; b <= 10; ) { System.out.println(rayleigh.random()); b++; }
/*    */     
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Rayleigh.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */