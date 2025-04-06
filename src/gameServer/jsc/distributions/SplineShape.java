/*     */ package jsc.distributions;
/*     */ 
/*     */ import jsc.goodnessfit.KolmogorovTest;
/*     */ import jsc.numerical.Function;
/*     */ import jsc.numerical.Integration;
/*     */ import jsc.numerical.NumericalException;
/*     */ import jsc.numerical.Spline;
/*     */ import jsc.tests.H1;
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
/*     */ public class SplineShape
/*     */   extends AbstractContinuousDistribution
/*     */ {
/*     */   double pdfMax;
/*     */   Spline spline;
/*     */   double mean;
/*     */   double variance;
/*     */   
/*     */   public SplineShape(int paramInt, double[] paramArrayOfdouble1, double[] paramArrayOfdouble2) throws NumericalException {
/*  44 */     this(new Spline(paramInt, paramArrayOfdouble1, paramArrayOfdouble2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SplineShape(Spline paramSpline) throws NumericalException {
/*  55 */     super(paramSpline.getMinX(), paramSpline.getMaxX(), false);
/*  56 */     int i = paramSpline.getN();
/*  57 */     this.minX = paramSpline.getMinX(); this.maxX = paramSpline.getMaxX();
/*  58 */     this.spline = paramSpline;
/*     */ 
/*     */     
/*  61 */     SplineCurve splineCurve = new SplineCurve(this);
/*  62 */     double d = Integration.romberg(splineCurve, this.minX, this.maxX, this.tolerance, 20);
/*     */ 
/*     */     
/*  65 */     if (d <= 0.0D) {
/*  66 */       throw new IllegalArgumentException("Invalid spline shape.");
/*     */     }
/*  68 */     if (d != 1.0D) {
/*     */       
/*  70 */       double[] arrayOfDouble1 = new double[i];
/*  71 */       double[] arrayOfDouble2 = new double[i];
/*  72 */       for (byte b = 0; b < i; b++) {
/*     */         
/*  74 */         arrayOfDouble1[b] = paramSpline.getX(b);
/*  75 */         arrayOfDouble2[b] = paramSpline.getY(b);
/*  76 */         arrayOfDouble2[b] = arrayOfDouble2[b] / d;
/*     */       } 
/*  78 */       this.spline = new Spline(i, arrayOfDouble1, arrayOfDouble2);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     calculateMaxPdf();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.mean = Integration.romberg(new SplineMean(this), this.minX, this.maxX, this.tolerance, 20);
/*  94 */     this.variance = Integration.romberg(new SplineVar(this), this.minX, this.maxX, this.tolerance, 20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void calculateMaxPdf() {
/* 101 */     byte b1 = 100;
/* 102 */     double d1 = (this.maxX - this.minX) / b1;
/* 103 */     this.pdfMax = 0.0D;
/*     */     
/* 105 */     double d2 = this.minX;
/* 106 */     for (byte b2 = 0; b2 <= b1; b2++) {
/*     */       
/* 108 */       d2 = this.minX + b2 * d1;
/* 109 */       double d = this.spline.splint(d2);
/* 110 */       if (d > this.pdfMax) this.pdfMax = d;
/*     */     
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
/*     */   public Spline getSpline() {
/* 128 */     return this.spline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double mean() {
/* 135 */     return this.mean;
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
/*     */   public double pdf(double paramDouble) {
/* 148 */     if (paramDouble < this.minX || paramDouble > this.maxX)
/* 149 */       throw new IllegalArgumentException("Invalid variate-value."); 
/* 150 */     return this.spline.splint(paramDouble);
/*     */   }
/*     */   
/*     */   public double random() {
/*     */     double d1;
/*     */     double d2;
/*     */     do {
/* 157 */       d1 = this.minX + (this.maxX - this.minX) * this.rand.nextDouble();
/* 158 */       d2 = this.pdfMax * this.rand.nextDouble();
/* 159 */     } while (d2 > this.spline.splint(d1));
/*     */     
/* 161 */     return d1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double variance() {
/* 167 */     return this.variance;
/*     */   }
/*     */   class SplineCurve implements Function { private final SplineShape this$0;
/*     */     
/* 171 */     SplineCurve(SplineShape this$0) { this.this$0 = this$0; } public double function(double param1Double) {
/* 172 */       return this.this$0.spline.splint(param1Double);
/*     */     } }
/*     */   
/* 175 */   class SplineMean implements Function { SplineMean(SplineShape this$0) { this.this$0 = this$0; } private final SplineShape this$0; public double function(double param1Double) {
/* 176 */       return param1Double * this.this$0.spline.splint(param1Double);
/*     */     } } class SplineVar implements Function { private final SplineShape this$0;
/*     */     SplineVar(SplineShape this$0) {
/* 179 */       this.this$0 = this$0;
/*     */     }
/*     */     
/*     */     public double function(double param1Double) {
/* 183 */       double d = param1Double - this.this$0.mean;
/* 184 */       return d * d * this.this$0.spline.splint(param1Double);
/*     */     } }
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
/*     */     public static void main(String[] param1ArrayOfString) throws NumericalException {
/* 197 */       byte b2 = 11;
/* 198 */       double[] arrayOfDouble1 = { 0.0D, 0.1D, 0.2D, 0.3D, 0.4D, 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 1.0D };
/* 199 */       double[] arrayOfDouble2 = new double[b2];
/* 200 */       double d1 = 2.0D;
/* 201 */       double d2 = 3.0D;
/* 202 */       Beta beta = new Beta(d1, d2); byte b1;
/* 203 */       for (b1 = 0; b1 < b2; b1++)
/*     */       {
/* 205 */         arrayOfDouble2[b1] = 2.0D * beta.pdf(arrayOfDouble1[b1]);
/*     */       }
/*     */       
/* 208 */       SplineShape splineShape = new SplineShape(b2, arrayOfDouble1, arrayOfDouble2);
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
/* 228 */       char c = 'Ϩ';
/* 229 */       double[] arrayOfDouble3 = new double[c];
/* 230 */       for (b1 = 0; b1 < c; b1++)
/*     */       {
/* 232 */         arrayOfDouble3[b1] = splineShape.random();
/*     */       }
/*     */ 
/*     */       
/* 236 */       KolmogorovTest kolmogorovTest = new KolmogorovTest(arrayOfDouble3, splineShape, H1.NOT_EQUAL, true);
/* 237 */       System.out.println("m = " + c + " D = " + kolmogorovTest.getTestStatistic() + " SP = " + kolmogorovTest.getSP());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/SplineShape.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */