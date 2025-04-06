/*     */ package jsc.mathfunction;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import jsc.util.Maths;
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
/*     */ public class StandardMathFunctionDerivatives
/*     */   extends StandardMathFunction
/*     */ {
/*     */   static final String BAD_ORDER = "Order of Taylor coefficients must be > 0.";
/*     */   static final String NO_DERIV = "Derivative does not exist.";
/*     */   static final String NO_STORE = "Implementation error: insufficient storage";
/*     */   public static final double BIG_VAL = 1.7E100D;
/*     */   private int MAX_WORK;
/*     */   private double[][] T;
/*     */   private int wsub;
/*     */   
/*     */   public StandardMathFunctionDerivatives(MathFunctionVariables paramMathFunctionVariables) {
/*  77 */     super(paramMathFunctionVariables);
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
/*     */   public double evalDerivative(int paramInt) throws MathFunctionException {
/* 100 */     double[] arrayOfDouble = evalTaylorCoeffs(paramInt, 1);
/* 101 */     return arrayOfDouble[1];
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
/*     */   public double[] evalDerivatives(int paramInt1, int paramInt2) throws MathFunctionException {
/* 124 */     double[] arrayOfDouble1 = evalTaylorCoeffs(paramInt1, paramInt2);
/* 125 */     double[] arrayOfDouble2 = new double[1 + paramInt2];
/* 126 */     for (byte b = 0; b <= paramInt2; ) { arrayOfDouble2[b] = toDerivative(b, arrayOfDouble1[b]); b++; }
/* 127 */      return arrayOfDouble2;
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
/*     */   public double[] evalTaylorCoeffs(int paramInt1, int paramInt2) throws MathFunctionException {
/* 161 */     if (this.codeList.size() == 0) throw new MathFunctionException("Invalid function");
/*     */     
/* 163 */     if (paramInt2 < 1) throw new MathFunctionException("Order of Taylor coefficients must be > 0.");
/*     */     
/* 165 */     double[] arrayOfDouble = new double[1 + paramInt2];
/*     */ 
/*     */     
/* 168 */     this.MAX_WORK = this.codeList.size() + 10;
/* 169 */     this.T = new double[this.MAX_WORK][1 + paramInt2];
/*     */ 
/*     */ 
/*     */     
/* 173 */     arrayOfDouble[0] = eval();
/*     */ 
/*     */     
/* 176 */     int i = this.codeList.size();
/* 177 */     this.wsub = i;
/*     */     byte b1;
/* 179 */     for (b1 = 1; b1 <= i; b1++) {
/*     */       
/* 181 */       this.T[b1][0] = this.codeList.getValue(b1);
/* 182 */       recur_init(b1, paramInt1);
/*     */     } 
/*     */     
/* 185 */     for (byte b2 = 1; b2 <= paramInt2; b2++) {
/*     */       
/* 187 */       this.wsub = i;
/* 188 */       for (b1 = 1; b1 <= i; b1++) {
/*     */         
/* 190 */         int j, k = this.codeList.getCode(b1);
/* 191 */         int m = this.codeList.getType(b1);
/* 192 */         switch (m) {
/*     */           case 0:
/* 194 */             j = Vartest(k, paramInt1);
/* 195 */             if (j == 0 && b2 == 1) {
/* 196 */               this.T[b1][b2] = 1.0D;
/*     */               break;
/*     */             } 
/*     */           case 3:
/* 200 */             this.T[b1][b2] = 0.0D;
/*     */             break;
/*     */           
/*     */           case 1:
/*     */           case 2:
/* 205 */             recur(b1, b2, paramInt1);
/*     */             break;
/*     */           default:
/* 208 */             throw new MathFunctionException("Implementation error: unrecognized code type");
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 213 */         if (Math.abs(this.T[b1][b2]) > 1.7E100D) {
/* 214 */           throw new MathFunctionException("Evaluation of Taylor coefficient of order " + b2 + " approaching overflow.");
/*     */         }
/*     */       } 
/* 217 */       arrayOfDouble[b2] = this.T[i][b2];
/*     */     } 
/* 219 */     return arrayOfDouble;
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
/*     */   private void recur(int paramInt1, int paramInt2, int paramInt3) throws MathFunctionException {
/* 243 */     int n, i1, i2, i = this.codeList.getLabelLine(this.codeList.getLeft(paramInt1));
/* 244 */     int j = this.codeList.getLabelLine(this.codeList.getRight(paramInt1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     int i3 = this.codeList.getCode(i);
/* 252 */     int k = this.codeList.getType(i);
/* 253 */     if (k == 0 && Vartest(i3, paramInt3) < 0)
/* 254 */       k = 3; 
/* 255 */     i3 = this.codeList.getCode(j);
/* 256 */     int m = this.codeList.getType(j);
/* 257 */     if (m == 0 && Vartest(i3, paramInt3) < 0) {
/* 258 */       m = 3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     double d1 = this.T[i][0];
/* 265 */     double d2 = this.T[j][0];
/*     */ 
/*     */     
/* 268 */     switch (this.codeList.getCode(paramInt1)) {
/*     */ 
/*     */       
/*     */       case 290:
/* 272 */         this.T[paramInt1][paramInt2] = this.T[i][paramInt2] + this.T[j][paramInt2];
/*     */         return;
/*     */       
/*     */       case 291:
/* 276 */         this.T[paramInt1][paramInt2] = this.T[i][paramInt2] - this.T[j][paramInt2];
/*     */         return;
/*     */       case 202:
/* 279 */         recur_multiply(paramInt1, paramInt2, i, j, k, m, d1, d2);
/*     */         return;
/*     */       case 203:
/* 282 */         recur_divide(paramInt1, paramInt2, i, j, k, m, d2);
/*     */         return;
/*     */       case 204:
/* 285 */         recur_power(paramInt1, paramInt2, i, j, k, m, d1, d2);
/*     */         return;
/*     */       case 205:
/* 288 */         throw new MathFunctionException("Cannot differentiate modulus % operator.");
/*     */ 
/*     */ 
/*     */       
/*     */       case 108:
/* 293 */         recur_power(paramInt1, paramInt2, j, j, k, 3, d1, 0.5D);
/*     */         return;
/*     */       case 191:
/* 296 */         this.T[paramInt1][paramInt2] = -this.T[j][paramInt2];
/*     */         return;
/*     */       case 103:
/* 299 */         RECUR_EXP(paramInt1, paramInt2, j);
/*     */         return;
/*     */       case 106:
/* 302 */         recur_ln(paramInt1, paramInt2, j);
/*     */         return;
/*     */       case 110:
/* 305 */         recur_sin_cos(++this.wsub, paramInt1, paramInt2, j);
/*     */         return;
/*     */       case 109:
/* 308 */         recur_sin_cos(paramInt1, ++this.wsub, paramInt2, j);
/*     */         return;
/*     */       case 111:
/* 311 */         i1 = ++this.wsub;
/* 312 */         i2 = ++this.wsub;
/* 313 */         recur_sin_cos(i1, i2, paramInt2, j);
/*     */ 
/*     */         
/* 316 */         recur_divide(paramInt1, paramInt2, i1, i2, k, m, d2);
/*     */         return;
/*     */       case 116:
/* 319 */         recur_sinh_cosh(++this.wsub, paramInt1, paramInt2, j);
/*     */         return;
/*     */       case 115:
/* 322 */         recur_sinh_cosh(paramInt1, ++this.wsub, paramInt2, j);
/*     */         return;
/*     */       case 117:
/* 325 */         i1 = ++this.wsub;
/* 326 */         i2 = ++this.wsub;
/* 327 */         recur_sinh_cosh(i1, i2, paramInt2, j);
/*     */ 
/*     */         
/* 330 */         recur_divide(paramInt1, paramInt2, i1, i2, k, m, d2);
/*     */         return;
/*     */       case 112:
/* 333 */         i1 = ++this.wsub;
/* 334 */         i2 = ++this.wsub;
/*     */ 
/*     */         
/* 337 */         RECUR_SQUARE(i1, paramInt2, j, m, d2);
/*     */ 
/*     */         
/* 340 */         recur_divide(i2, paramInt2, 0, i1, 3, m, d2);
/*     */ 
/*     */         
/* 343 */         recur_unary(paramInt1, paramInt2, i2, j);
/*     */         return;
/*     */       case 113:
/*     */       case 114:
/* 347 */         i1 = ++this.wsub;
/* 348 */         n = ++this.wsub;
/* 349 */         i2 = ++this.wsub;
/*     */ 
/*     */         
/* 352 */         RECUR_SQUARE(i1, paramInt2, j, m, d2);
/* 353 */         this.T[i1][paramInt2] = -this.T[i1][paramInt2];
/*     */ 
/*     */         
/* 356 */         recur_power(n, paramInt2, i1, 0, k, 3, d1, 0.5D);
/*     */ 
/*     */         
/* 359 */         recur_divide(i2, paramInt2, 0, n, 3, m, d2);
/*     */ 
/*     */         
/* 362 */         recur_unary(paramInt1, paramInt2, i2, j);
/*     */         return;
/*     */       case 102:
/* 365 */         if (m == 3)
/* 366 */         { this.T[paramInt1][paramInt2] = Math.abs(this.T[j][paramInt2]); }
/* 367 */         else { if (this.T[j][paramInt2] == 0.0D) {
/* 368 */             throw new MathFunctionException("Derivative does not exist.");
/*     */           }
/* 370 */           this.T[paramInt1][paramInt2] = (this.T[j][paramInt2] < 0.0D) ? -this.T[j][paramInt2] : this.T[j][paramInt2]; }
/*     */          return;
/*     */       case 190:
/* 373 */         this.T[paramInt1][paramInt2] = this.T[j][paramInt2];
/*     */         return;
/*     */ 
/*     */       
/*     */       case 104:
/* 378 */         if (m == 3) {
/* 379 */           this.T[paramInt1][paramInt2] = Maths.truncate(this.T[j][paramInt2]);
/*     */         } else {
/* 381 */           throw new MathFunctionException("Cannot differentiate INT function.");
/*     */         }  return;
/*     */       case 118:
/* 384 */         if (m == 3) {
/* 385 */           this.T[paramInt1][paramInt2] = Maths.sign(this.T[j][paramInt2]);
/*     */         } else {
/* 387 */           throw new MathFunctionException("Cannot differentiate SIGN function.");
/*     */         }  return;
/*     */       case 105:
/* 390 */         if (m == 3) {
/* 391 */           this.T[paramInt1][paramInt2] = Math.rint(this.T[j][paramInt2]);
/*     */         } else {
/* 393 */           throw new MathFunctionException("Cannot differentiate NINT function.");
/*     */         } 
/*     */         return;
/*     */       case 119:
/* 397 */         this.T[paramInt1][paramInt2] = Math.toDegrees(this.T[j][paramInt2]);
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 120:
/* 403 */         this.T[paramInt1][paramInt2] = Math.toRadians(this.T[j][paramInt2]);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 408 */     throw new MathFunctionException("Implementation error: unrecognized code");
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
/*     */   private void recur_divide(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble) throws MathFunctionException {
/* 426 */     double d = 0.0D;
/*     */     
/* 428 */     if (paramInt6 == 3 && paramDouble != 0.0D) {
/* 429 */       this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2] / paramDouble;
/*     */     } else {
/*     */       
/* 432 */       if (TRIM(this.T[paramInt4][0]) == 0.0D)
/* 433 */         throw new MathFunctionException("Derivative does not exist."); 
/* 434 */       for (byte b = 1; b <= paramInt2; b++)
/* 435 */         d += this.T[paramInt1][paramInt2 - b] * this.T[paramInt4][b]; 
/* 436 */       if (paramInt5 == 3) {
/* 437 */         this.T[paramInt1][paramInt2] = -d / this.T[paramInt4][0];
/*     */       } else {
/* 439 */         this.T[paramInt1][paramInt2] = (this.T[paramInt3][paramInt2] - d) / this.T[paramInt4][0];
/*     */       } 
/*     */     } 
/*     */   } private void RECUR_EXP(int paramInt1, int paramInt2, int paramInt3) {
/* 443 */     recur_unary(paramInt1, paramInt2, paramInt1, paramInt3);
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
/*     */   private void recur_init(int paramInt1, int paramInt2) throws MathFunctionException {
/* 457 */     int k, n, i1, i2, i3, m = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 463 */     int i = this.codeList.getLabelLine(this.codeList.getLeft(paramInt1));
/* 464 */     int j = this.codeList.getLabelLine(this.codeList.getRight(paramInt1));
/* 465 */     double d1 = this.T[i][0];
/* 466 */     double d2 = this.T[j][0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 474 */     switch (this.codeList.getCode(paramInt1)) {
/*     */       case 204:
/* 476 */         i3 = this.codeList.getRightCode(paramInt1);
/* 477 */         k = this.codeList.getRightType(paramInt1);
/* 478 */         if (k == 3 || (k == 0 && Vartest(i3, paramInt2) < 0)) {
/*     */           
/* 480 */           if (d2 <= 2.0D || d1 != 0.0D)
/*     */             return;  int i4;
/* 482 */           if (d2 == (i4 = (int)d2)) {
/*     */ 
/*     */             
/* 485 */             for (; m <= i4; m <<= 1);
/* 486 */             m >>= 1;
/*     */             
/* 488 */             while ((m >>= 1) != 0) {
/*     */               
/* 490 */               this.T[winc()][0] = 0.0D;
/* 491 */               if ((i4 & m) != 0) this.T[winc()][0] = 0.0D;
/*     */             
/*     */             } 
/*     */           } else {
/* 495 */             throw new MathFunctionException("Attempted division by zero");
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/* 500 */         i1 = winc();
/* 501 */         i2 = winc();
/* 502 */         this.T[i1][0] = unaryOperation(106, d1);
/* 503 */         this.T[i2][0] = d2 * this.T[i1][0];
/*     */         return;
/*     */       case 110:
/* 506 */         this.T[winc()][0] = unaryOperation(109, d2);
/*     */         return;
/*     */       case 109:
/* 509 */         this.T[winc()][0] = unaryOperation(110, d2);
/*     */         return;
/*     */       case 111:
/* 512 */         this.T[winc()][0] = unaryOperation(109, d2);
/* 513 */         this.T[winc()][0] = unaryOperation(110, d2);
/*     */         return;
/*     */       case 116:
/* 516 */         this.T[winc()][0] = unaryOperation(115, d2);
/*     */         return;
/*     */       case 115:
/* 519 */         this.T[winc()][0] = unaryOperation(116, d2);
/*     */         return;
/*     */       case 117:
/* 522 */         this.T[winc()][0] = unaryOperation(115, d2);
/* 523 */         this.T[winc()][0] = unaryOperation(116, d2);
/*     */         return;
/*     */       case 112:
/* 526 */         i1 = winc();
/* 527 */         i2 = winc();
/* 528 */         this.T[i1][0] = 1.0D + d2 * d2;
/* 529 */         this.T[i2][0] = 1.0D / this.T[i1][0];
/*     */         return;
/*     */       case 113:
/*     */       case 114:
/* 533 */         i1 = winc();
/* 534 */         n = winc();
/* 535 */         i2 = winc();
/* 536 */         this.T[i1][0] = 1.0D - d2 * d2;
/* 537 */         this.T[n][0] = unaryOperation(108, this.T[i1][0]);
/* 538 */         if (this.T[n][0] != 0.0D) {
/* 539 */           this.T[i2][0] = 1.0D / this.T[n][0];
/*     */         } else {
/* 541 */           throw new MathFunctionException("Attempted division by zero");
/* 542 */         }  if (this.codeList.getCode(paramInt1) == 114) {
/* 543 */           this.T[i2][0] = -this.T[i2][0];
/*     */         }
/*     */         return;
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
/*     */ 
/*     */   
/*     */   private void recur_int_power5(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, int paramInt5) {
/* 564 */     int i = 1;
/*     */ 
/*     */     
/* 567 */     int j = paramInt3;
/* 568 */     for (; i <= paramInt5; i <<= 1);
/* 569 */     i >>= 1;
/*     */ 
/*     */ 
/*     */     
/* 573 */     while ((i >>= 1) != 0) {
/*     */       
/* 575 */       RECUR_SQUARE(++this.wsub, paramInt2, j, paramInt4, paramDouble);
/* 576 */       if ((paramInt5 & i) != 0) {
/*     */         
/* 578 */         int k = this.wsub;
/* 579 */         recur_multiply(++this.wsub, paramInt2, paramInt3, k, paramInt4, paramInt4, paramDouble, 0.0D);
/*     */       } 
/* 581 */       j = this.wsub;
/*     */     } 
/* 583 */     this.T[paramInt1][paramInt2] = this.T[this.wsub][paramInt2];
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
/*     */   private void recur_ln(int paramInt1, int paramInt2, int paramInt3) throws MathFunctionException {
/* 596 */     double d2 = 0.0D;
/*     */     
/* 598 */     double d1 = paramInt2;
/*     */     
/* 600 */     if (TRIM(this.T[paramInt3][0]) == 0.0D)
/* 601 */       throw new MathFunctionException("Derivative does not exist."); 
/* 602 */     if (paramInt2 > 1) {
/*     */       
/* 604 */       for (byte b = 1; b <= paramInt2 - 1; b++)
/* 605 */         d2 += (d1 - b) / d1 * this.T[paramInt1][paramInt2 - b] * this.T[paramInt3][b]; 
/* 606 */       this.T[paramInt1][paramInt2] = (this.T[paramInt3][paramInt2] - d2) / this.T[paramInt3][0];
/*     */     } else {
/*     */       
/* 609 */       this.T[paramInt1][1] = this.T[paramInt3][1] / this.T[paramInt3][0];
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
/*     */   
/*     */   private void recur_multiply(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble1, double paramDouble2) {
/* 627 */     double d = 0.0D;
/*     */     
/* 629 */     if (paramInt5 == 3) {
/* 630 */       this.T[paramInt1][paramInt2] = paramDouble1 * this.T[paramInt4][paramInt2];
/* 631 */     } else if (paramInt6 == 3) {
/* 632 */       this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2] * paramDouble2;
/*     */     } else {
/*     */       
/* 635 */       for (byte b = 0; b <= paramInt2; b++) {
/* 636 */         d += this.T[paramInt3][b] * this.T[paramInt4][paramInt2 - b];
/*     */       }
/* 638 */       this.T[paramInt1][paramInt2] = d;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void recur_power(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble1, double paramDouble2) throws MathFunctionException {
/* 660 */     double d = 0.0D;
/*     */     
/* 662 */     if (paramInt6 == 3) {
/*     */       
/* 664 */       if (paramDouble2 == 0.0D) {
/* 665 */         this.T[paramInt1][paramInt2] = 0.0D;
/* 666 */       } else if (paramDouble2 == 1.0D) {
/* 667 */         this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2];
/* 668 */       } else if (paramDouble2 == 2.0D) {
/* 669 */         RECUR_SQUARE(paramInt1, paramInt2, paramInt3, paramInt5, paramDouble1);
/* 670 */       } else if (TRIM(this.T[paramInt3][0]) != 0.0D) {
/*     */         
/* 672 */         double d1 = paramInt2;
/* 673 */         for (byte b = 0; b <= paramInt2 - 1; b++)
/*     */         {
/* 675 */           d += (paramDouble2 - b * (paramDouble2 + 1.0D) / d1) * this.T[paramInt3][paramInt2 - b] * this.T[paramInt1][b];
/*     */         }
/* 677 */         this.T[paramInt1][paramInt2] = d / this.T[paramInt3][0];
/*     */       } else {
/* 679 */         int k; if (paramDouble2 == (k = (int)paramDouble2)) {
/* 680 */           recur_int_power5(paramInt1, paramInt2, paramInt3, paramInt5, paramDouble1, k);
/*     */         } else {
/* 682 */           throw new MathFunctionException("Attempted division by zero");
/*     */         } 
/*     */       }  return;
/*     */     } 
/* 686 */     int i = ++this.wsub;
/* 687 */     int j = ++this.wsub;
/*     */ 
/*     */ 
/*     */     
/* 691 */     if (paramInt5 == 3) {
/*     */       
/* 693 */       recur_multiply(j, paramInt2, i, paramInt4, 3, paramInt6, this.T[i][0], paramDouble2);
/*     */       
/* 695 */       RECUR_EXP(paramInt1, paramInt2, j);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 700 */     recur_ln(i, paramInt2, paramInt3);
/* 701 */     recur_multiply(j, paramInt2, i, paramInt4, paramInt5, paramInt6, paramDouble1, paramDouble2);
/* 702 */     RECUR_EXP(paramInt1, paramInt2, j);
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
/*     */   private void recur_sin_cos(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 714 */     recur_unary(paramInt1, paramInt3, paramInt2, paramInt4);
/* 715 */     recur_unary(paramInt2, paramInt3, paramInt1, paramInt4);
/* 716 */     this.T[paramInt2][paramInt3] = -this.T[paramInt2][paramInt3];
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
/*     */   private void recur_sinh_cosh(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 728 */     recur_unary(paramInt1, paramInt3, paramInt2, paramInt4);
/* 729 */     recur_unary(paramInt2, paramInt3, paramInt1, paramInt4);
/*     */   }
/*     */ 
/*     */   
/*     */   private void RECUR_SQUARE(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble) {
/* 734 */     recur_multiply(paramInt1, paramInt2, paramInt3, paramInt3, paramInt4, paramInt4, paramDouble, paramDouble);
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
/*     */   private void recur_unary(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 746 */     double d2 = 0.0D;
/*     */     
/* 748 */     double d1 = paramInt2;
/*     */     
/* 750 */     for (byte b = 0; b <= paramInt2 - 1; b++)
/* 751 */       d2 += (d1 - b) / d1 * this.T[paramInt3][b] * this.T[paramInt4][paramInt2 - b]; 
/* 752 */     this.T[paramInt1][paramInt2] = d2;
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
/*     */   public static double toDerivative(int paramInt, double paramDouble) {
/* 765 */     return Maths.factorial(paramInt) * paramDouble;
/*     */   }
/*     */   private double TRIM(double paramDouble) {
/* 768 */     return (Math.abs(paramDouble) < 5.0E-12D) ? 0.0D : paramDouble;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int Vartest(int paramInt1, int paramInt2) {
/* 778 */     if (paramInt1 == paramInt2) return 0; 
/* 779 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int winc() throws MathFunctionException {
/* 785 */     if (++this.wsub >= this.MAX_WORK) {
/* 786 */       throw new MathFunctionException("Implementation error: insufficient storage");
/*     */     }
/* 788 */     return this.wsub;
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
/*     */   static class Test
/*     */   {
/*     */     static class XY
/*     */       implements MathFunctionVariables
/*     */     {
/*     */       public double x;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public double y;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int getNumberOfVariables() {
/* 824 */         return 2;
/* 825 */       } public String getVariableName(int param2Int) { return (param2Int == 0) ? "X" : "Y"; } public double getVariableValue(int param2Int) {
/* 826 */         return (param2Int == 0) ? this.x : this.y;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static void main(String[] param1ArrayOfString) {
/* 835 */       String str = "";
/* 836 */       XY xY = new XY();
/* 837 */       StandardMathFunctionDerivatives standardMathFunctionDerivatives = new StandardMathFunctionDerivatives(xY);
/* 838 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*     */       
/* 840 */       System.out.println(standardMathFunctionDerivatives.getSummary()); while (true) {
/*     */         int i;
/*     */         double d1, arrayOfDouble[];
/*     */         String str1;
/* 844 */         System.out.println("Enter expression f(x,y), ? for help, Q to quit or Return to re-evaluate"); 
/* 845 */         try { str1 = bufferedReader.readLine(); }
/* 846 */         catch (IOException iOException) { System.out.println(iOException.getMessage()); continue; }
/* 847 */          if (str1.length() > 0) {
/*     */           
/* 849 */           if (Character.toUpperCase(str1.charAt(0)) == 'Q') System.exit(0); 
/* 850 */           if (str1.charAt(0) == '?') { System.out.println(standardMathFunctionDerivatives.getSummary());
/*     */             continue; }
/*     */           
/* 853 */           str = str1; try {
/* 854 */             d1 = standardMathFunctionDerivatives.parse(str);
/*     */           } catch (MathFunctionException mathFunctionException) {
/* 856 */             System.out.println(mathFunctionException.getMessage()); continue;
/*     */           } 
/* 858 */           if (!standardMathFunctionDerivatives.variableUsed("X")) {
/* 859 */             System.out.println("Expression must be a function of X to evaluate Taylor coeffs."); continue;
/*     */           } 
/* 861 */           if (standardMathFunctionDerivatives.variableUsed("Y")) {
/* 862 */             xY.y = getConstant("Enter value of Y", bufferedReader);
/*     */           }
/*     */         } 
/*     */         
/*     */         while (true) {
/* 867 */           System.out.println("Enter order of Taylor series"); 
/* 868 */           try { str1 = bufferedReader.readLine(); }
/* 869 */           catch (IOException iOException) { System.out.println(iOException.getMessage()); continue; }
/* 870 */            try { i = Integer.parseInt(str1); break; }
/* 871 */           catch (NumberFormatException numberFormatException) { System.out.println("Invalid input: " + numberFormatException.getMessage()); }
/*     */         
/* 873 */         }  double d2 = getConstant("Taylor series about what value? (enter 0 for Maclaurin series)", bufferedReader);
/* 874 */         xY.x = d2; try {
/* 875 */           arrayOfDouble = standardMathFunctionDerivatives.evalTaylorCoeffs(0, i);
/*     */         } catch (MathFunctionException mathFunctionException) {
/* 877 */           System.out.println(mathFunctionException.getMessage()); continue;
/* 878 */         }  System.out.println(i + "th Taylor polynomial about " + d2 + " approximating " + str + " is -");
/*     */         
/* 880 */         System.out.println("P(x) = " + arrayOfDouble[0]); byte b;
/* 881 */         for (b = 1; b <= i; b++) {
/* 882 */           System.out.println(" + " + arrayOfDouble[b] + " * (x - " + d2 + ")^" + b);
/*     */         }
/* 884 */         xY.x = getConstant("Enter value of X", bufferedReader);
/*     */ 
/*     */         
/* 887 */         double d3 = xY.x - d2;
/* 888 */         double d4 = arrayOfDouble[0];
/* 889 */         for (b = 1; b <= i; ) { d4 += arrayOfDouble[b] * Math.pow(d3, b); b++; }
/* 890 */          System.out.println("P(x) approximation to f(x) = " + d4);
/*     */         
/*     */         try {
/* 893 */           d1 = standardMathFunctionDerivatives.eval();
/*     */         } catch (MathFunctionException mathFunctionException) {
/* 895 */           System.out.println(mathFunctionException.getMessage()); continue;
/* 896 */         }  System.out.println("    Exact value of f(x) is = " + d1);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static double getConstant(String param1String, BufferedReader param1BufferedReader) {
/*     */       double d;
/* 904 */       StandardMathFunction standardMathFunction = new StandardMathFunction();
/*     */       while (true) {
/*     */         String str;
/* 907 */         System.out.println(param1String); 
/* 908 */         try { str = param1BufferedReader.readLine(); }
/* 909 */         catch (IOException iOException) { System.out.println(iOException.getMessage()); continue; }
/* 910 */          try { d = standardMathFunction.parse(str); break; }
/* 911 */         catch (MathFunctionException mathFunctionException) { System.out.println(mathFunctionException.getMessage()); } 
/* 912 */       }  return d;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/StandardMathFunctionDerivatives.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */