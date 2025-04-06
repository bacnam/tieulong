/*     */ package jsc.ci;
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
/*     */ public class AbstractConfidenceInterval
/*     */   implements ConfidenceInterval
/*     */ {
/*     */   protected double confidenceCoeff;
/*     */   protected double lowerLimit;
/*     */   protected double upperLimit;
/*     */   
/*     */   public AbstractConfidenceInterval() {
/*  32 */     setConfidenceCoeff(0.95D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractConfidenceInterval(double paramDouble) {
/*  41 */     setConfidenceCoeff(paramDouble);
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
/*     */   public AbstractConfidenceInterval(double paramDouble1, double paramDouble2, double paramDouble3) {
/*  53 */     this(paramDouble1);
/*  54 */     this.lowerLimit = paramDouble2;
/*  55 */     this.upperLimit = paramDouble3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getAlpha() {
/*  64 */     return 1.0D - this.confidenceCoeff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getConfidenceCoeff() {
/*  71 */     return this.confidenceCoeff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getConfidenceLevel() {
/*  78 */     return 100.0D * this.confidenceCoeff;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLowerLimit() {
/*  85 */     return this.lowerLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getUpperLimit() {
/*  92 */     return this.upperLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfidenceCoeff(double paramDouble) {
/* 102 */     if (paramDouble <= 0.0D || paramDouble >= 1.0D)
/* 103 */       throw new IllegalArgumentException("Invalid confidence coefficient."); 
/* 104 */     this.confidenceCoeff = paramDouble;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 108 */     return new String((100.0D * this.confidenceCoeff) + "% confidence interval [" + this.lowerLimit + ", " + this.upperLimit + "]");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/ci/AbstractConfidenceInterval.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */