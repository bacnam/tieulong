/*     */ package jsc.curvefitting;
/*     */ 
/*     */ import jsc.datastructures.PairedData;
/*     */ import jsc.numerical.Function;
/*     */ import jsc.numerical.NumericalException;
/*     */ import jsc.numerical.Roots;
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
/*     */ public class ExponentialFit
/*     */ {
/*  19 */   private final int MAX_STEPS = 1000;
/*  20 */   private final int N_INTERVALS = 20;
/*  21 */   private final double INTERVAL = 0.05D; private final int n;
/*     */   private final double[] x;
/*     */   private final double[] y;
/*     */   private final double[] p;
/*     */   private double a;
/*     */   private double b;
/*     */   private double ab;
/*     */   private double eb;
/*     */   private int k;
/*     */   private double h1;
/*     */   private double h2;
/*     */   private double h3;
/*     */   private double h4;
/*     */   private double h5;
/*     */   private double h6;
/*     */   private double h7;
/*     */   private double h8;
/*     */   private double b1;
/*     */   private double b2;
/*     */   private double F1;
/*     */   private double F2;
/*     */   private double F3;
/*     */   private double F4;
/*     */   private double h;
/*     */   
/*     */   public ExponentialFit(PairedData paramPairedData, double[] paramArrayOfdouble, double paramDouble) {
/*  47 */     this.n = paramPairedData.getN();
/*  48 */     this.x = paramPairedData.getX();
/*  49 */     this.y = paramPairedData.getY();
/*     */     
/*  51 */     if (paramArrayOfdouble == null) {
/*     */       
/*  53 */       this.p = new double[this.n];
/*  54 */       for (byte b = 0; b < this.n; ) { this.p[b] = 1.0D; b++; }
/*     */     
/*     */     } else {
/*     */       
/*  58 */       if (this.n != paramArrayOfdouble.length)
/*  59 */         throw new IllegalArgumentException("Weights array is wrong length."); 
/*  60 */       this.p = paramArrayOfdouble;
/*     */     } 
/*     */     
/*  63 */     if (!abfit(paramDouble, this.ab, this.eb, false))
/*     */     {
/*     */       
/*  66 */       if (!abfit(paramDouble, this.ab, this.eb, true)) {
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
/*  85 */         LineFit lineFit = new LineFit(paramPairedData, this.p);
/*  86 */         this.a = lineFit.getA();
/*  87 */         if (this.a == 0.0D)
/*  88 */           throw new IllegalArgumentException("Unable to fit an exponential curve to these data."); 
/*  89 */         double d = lineFit.getB();
/*  90 */         this.b = -Math.log((this.a + d) / this.a);
/*     */       } 
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
/*     */   public ExponentialFit(PairedData paramPairedData, double paramDouble) {
/* 104 */     this(paramPairedData, null, paramDouble);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean abfit(double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean) {
/* 115 */     FbFunction fbFunction = new FbFunction(this);
/* 116 */     if (paramBoolean) {
/*     */       
/* 118 */       try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
/* 119 */       catch (NumericalException numericalException) { return false; }
/* 120 */       catch (IllegalArgumentException illegalArgumentException) { return false; }
/* 121 */        return true;
/*     */     } 
/*     */ 
/*     */     
/* 125 */     this.h1 = 0.0D; this.h2 = 0.0D; this.h3 = 0.0D; this.h4 = 0.0D; this.h5 = 0.0D;
/*     */     
/*     */     byte b;
/* 128 */     for (b = 0; b < this.n; b++) {
/*     */       
/* 130 */       if (this.y[b] <= 0.0D)
/*     */       {
/* 132 */         throw new IllegalArgumentException("Y data not all positive."); } 
/* 133 */       this.h8 = Math.log(this.y[b]);
/* 134 */       this.h6 = this.p[b] * this.y[b] * this.y[b];
/* 135 */       this.h7 = this.h6 * this.x[b];
/* 136 */       this.h1 += this.h6;
/* 137 */       this.h2 += this.h7 * this.x[b];
/* 138 */       this.h3 += this.h7;
/* 139 */       this.h4 += this.h7 * this.h8;
/* 140 */       this.h5 += this.h6 * this.h8;
/*     */     } 
/* 142 */     this.h8 = 1.0D / (this.h1 * this.h2 - this.h3 * this.h3);
/* 143 */     this.b = -this.h8 * (this.h1 * this.h4 - this.h3 * this.h5);
/*     */     
/* 145 */     this.b1 = this.b;
/* 146 */     this.b2 = this.b;
/* 147 */     this.h = 0.0D;
/* 148 */     this.F1 = Fb(this.b);
/* 149 */     if (this.F1 == 0.0D) return true; 
/* 150 */     this.F2 = this.F1;
/* 151 */     for (b = 1; b <= 20; b++) {
/*     */ 
/*     */       
/* 154 */       this.h += 0.05D;
/* 155 */       paramDouble2 = this.b1 * (1.0D - this.h);
/*     */       
/* 157 */       this.F3 = Fb(paramDouble2);
/* 158 */       if (this.F1 * this.F3 < 0.0D) {
/*     */         
/* 160 */         paramDouble3 = this.b1;
/*     */         
/* 162 */         try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
/* 163 */         catch (NumericalException numericalException) { return false; }
/* 164 */         catch (IllegalArgumentException illegalArgumentException) { throw illegalArgumentException; }
/* 165 */          return true;
/*     */       } 
/* 167 */       paramDouble3 = this.b2 * (1.0D + this.h);
/*     */       
/* 169 */       this.F4 = Fb(paramDouble3);
/* 170 */       if (this.F2 * this.F4 < 0.0D) {
/*     */         
/* 172 */         paramDouble2 = this.b2;
/*     */         
/* 174 */         try { this.b = Roots.secant(fbFunction, paramDouble2, paramDouble3, paramDouble1, 1000); }
/* 175 */         catch (NumericalException numericalException) { return false; }
/* 176 */         catch (IllegalArgumentException illegalArgumentException) { throw illegalArgumentException; }
/* 177 */          return true;
/*     */       } 
/* 179 */       this.b1 = paramDouble2;
/* 180 */       this.b2 = paramDouble3;
/* 181 */       this.F1 = this.F3;
/* 182 */       this.F2 = this.F4;
/*     */     } 
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double Fb(double paramDouble) {
/* 190 */     this.h1 = 0.0D; this.h2 = 0.0D; this.h3 = 0.0D; this.h4 = 0.0D;
/* 191 */     for (byte b = 0; b < this.n; b++) {
/*     */       
/* 193 */       this.h5 = Math.exp(-paramDouble * this.x[b]);
/* 194 */       this.h6 = this.p[b] * this.y[b];
/* 195 */       this.h8 = this.h5 * this.h6;
/* 196 */       this.h7 = this.p[b] * this.h5 * this.h5;
/* 197 */       this.h1 += this.h8;
/* 198 */       this.h2 += this.h7;
/* 199 */       this.h3 += this.x[b] * this.h8;
/* 200 */       this.h4 += this.x[b] * this.h7;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 205 */     this.a = this.h1 / this.h2;
/*     */     
/* 207 */     return this.h3 * this.h2 - this.h1 * this.h4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getA() {
/* 215 */     return this.a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getB() {
/* 222 */     return this.b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getN() {
/* 229 */     return this.n;
/*     */   }
/*     */   class FbFunction implements Function {
/* 232 */     FbFunction(ExponentialFit this$0) { this.this$0 = this$0; } private final ExponentialFit this$0; public double function(double param1Double) {
/* 233 */       return this.this$0.Fb(param1Double);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/curvefitting/ExponentialFit.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */