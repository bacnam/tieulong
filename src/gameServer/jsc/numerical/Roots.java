/*     */ package jsc.numerical;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Roots
/*     */ {
/*     */   public static double bisection(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
/*  37 */     double d1, d4, d2 = paramFunction.function(paramDouble1);
/*  38 */     double d3 = paramFunction.function(paramDouble2);
/*  39 */     if (d2 * d3 >= 0.0D)
/*  40 */       throw new IllegalArgumentException("Root not bracketed in bisection method"); 
/*  41 */     if (d2 < 0.0D) {
/*  42 */       d1 = paramDouble2 - paramDouble1; d4 = paramDouble1;
/*     */     } else {
/*  44 */       d1 = paramDouble1 - paramDouble2; d4 = paramDouble2;
/*     */     } 
/*  46 */     for (byte b = 1; b <= paramInt; b++) {
/*     */       double d;
/*  48 */       d3 = paramFunction.function(d = d4 + (d1 *= 0.5D));
/*  49 */       if (d3 <= 0.0D) d4 = d; 
/*  50 */       if (Math.abs(d1) < paramDouble3 || d3 == 0.0D) return d4;
/*     */     
/*     */     } 
/*  53 */     throw new NumericalException("Maximum number of iterations exceeded in bisection method");
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static double secant(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
/*     */     double d3, d4;
/* 125 */     if (paramDouble1 == paramDouble2) {
/* 126 */       throw new IllegalArgumentException("Equal starting values.");
/*     */     }
/*     */     
/* 129 */     double d1 = paramFunction.function(paramDouble1);
/* 130 */     double d2 = paramFunction.function(paramDouble2);
/*     */     
/* 132 */     if (Math.abs(d1) < Math.abs(d2)) {
/*     */       
/* 134 */       d3 = paramDouble1;
/* 135 */       d4 = paramDouble2;
/* 136 */       double d = d1;
/* 137 */       d1 = d2;
/* 138 */       d2 = d;
/*     */     } else {
/*     */       
/* 141 */       d4 = paramDouble1; d3 = paramDouble2;
/*     */     } 
/* 143 */     for (byte b = 1; b <= paramInt; b++) {
/*     */       
/* 145 */       double d6 = d2 - d1;
/* 146 */       if (d6 == 0.0D)
/* 147 */         throw new NumericalException("Identical function values " + d2 + " in secant method."); 
/* 148 */       double d5 = (d4 - d3) * d2 / d6;
/* 149 */       d4 = d3;
/* 150 */       d1 = d2;
/* 151 */       d3 += d5;
/* 152 */       d2 = paramFunction.function(d3);
/* 153 */       if (Math.abs(d5) < paramDouble3 || d2 == 0.0D) {
/* 154 */         return d3;
/*     */       }
/*     */     } 
/* 157 */     throw new NumericalException("Maximum number of iterations exceeded in secant method.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) throws NumericalException {
/* 166 */       Func1 func1 = new Func1();
/* 167 */       System.out.println("secant root =" + Roots.secant(func1, 2.0D, 1.0D, 1.0E-7D, 1000));
/* 168 */       System.out.println("bisection root =" + Roots.bisection(func1, 0.0D, 10.0D, 1.0E-7D, 1000));
/*     */       
/* 170 */       Func2 func2 = new Func2();
/* 171 */       System.out.println("secant root =" + Roots.secant(func2, 2.0D, 1.0D, 1.0E-7D, 1000));
/*     */       
/* 173 */       Func3 func3 = new Func3();
/* 174 */       System.out.println("secant root =" + Roots.secant(func3, 2.0D, 1.0D, 1.0E-7D, 1000));
/* 175 */       System.out.println("bisection root =" + Roots.bisection(func3, 0.0D, 10.0D, 1.0E-7D, 1000));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static class Func1
/*     */       implements Function
/*     */     {
/*     */       public double function(double param2Double) {
/* 186 */         return param2Double * param2Double * param2Double * param2Double - param2Double - 10.0D;
/*     */       } }
/*     */     
/*     */     static class Func2 implements Function {
/*     */       public double function(double param2Double) {
/* 191 */         return Math.exp(-param2Double) - Math.sin(param2Double);
/*     */       }
/*     */     }
/*     */     
/*     */     static class Func3 implements Function { public double function(double param2Double) {
/* 196 */         return param2Double * param2Double * param2Double - 2.0D * param2Double - 5.0D;
/*     */       } }
/*     */     
/*     */     static class Func0 implements Function {
/*     */       public double function(double param2Double) {
/* 201 */         return param2Double * param2Double + 1.0D;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/Roots.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */