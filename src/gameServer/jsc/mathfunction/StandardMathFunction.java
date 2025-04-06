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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardMathFunction
/*     */   extends AbstractMathFunction
/*     */ {
/*     */   static final String IMP_ERROR = "Implementation error";
/*     */   static final String TOO_BIG = "Result of a multiplication is too large";
/*     */   static final String DIV_ZERO = "Attempted division by zero";
/*     */   public static final int PLUS = 190;
/*     */   public static final int MINUS = 191;
/*     */   public static final int ABS = 102;
/*     */   public static final int EXP = 103;
/*     */   public static final int INT = 104;
/*     */   public static final int NINT = 105;
/*     */   public static final int LOG = 106;
/*     */   public static final int SQRT = 108;
/*     */   public static final int SIN = 109;
/*     */   public static final int COS = 110;
/*     */   public static final int TAN = 111;
/*     */   public static final int ATAN = 112;
/*     */   public static final int ASIN = 113;
/*     */   public static final int ACOS = 114;
/*     */   public static final int SINH = 115;
/*     */   public static final int COSH = 116;
/*     */   public static final int TANH = 117;
/*     */   public static final int SIGN = 118;
/*     */   public static final int DEG = 119;
/*     */   public static final int RAD = 120;
/*     */   public static final int ADD = 290;
/*     */   public static final int SUBTRACT = 291;
/*     */   public static final int MULTIPLY = 202;
/*     */   public static final int DIVIDE = 203;
/*     */   public static final int POWER = 204;
/*     */   public static final int MODULUS = 205;
/*     */   public static final int PI_CONST = 300;
/*     */   public static final int E_CONST = 301;
/*     */   
/*     */   class Token
/*     */   {
/*     */     String string;
/*     */     int code;
/*     */     int prec;
/*     */     private final StandardMathFunction this$0;
/*     */     
/*     */     Token(StandardMathFunction this$0, String param1String, int param1Int1, int param1Int2) {
/* 153 */       this.this$0 = this$0; this.string = param1String; this.code = param1Int1; this.prec = param1Int2;
/*     */     }
/*     */   }
/*     */   
/* 157 */   Token[] defaultTab = new Token[] { new Token(this, "+", 190, 8), new Token(this, "+", 290, 6), new Token(this, "-", 191, 8), new Token(this, "-", 291, 6), new Token(this, "*", 202, 7), new Token(this, "/", 203, 7), new Token(this, "%", 205, 7), new Token(this, "^", 204, 9), new Token(this, "ABS", 102, 10), new Token(this, "SIGN", 118, 10), new Token(this, "EXP", 103, 10), new Token(this, "NINT", 105, 10), new Token(this, "INT", 104, 10), new Token(this, "LOG", 106, 10), new Token(this, "SQRT", 108, 10), new Token(this, "ATAN", 112, 10), new Token(this, "ASIN", 113, 10), new Token(this, "ACOS", 114, 10), new Token(this, "SINH", 115, 10), new Token(this, "COSH", 116, 10), new Token(this, "TANH", 117, 10), new Token(this, "SIN", 109, 10), new Token(this, "COS", 110, 10), new Token(this, "TAN", 111, 10), new Token(this, "DEG", 119, 10), new Token(this, "RAD", 120, 10), new Token(this, "PI", 300, 0), new Token(this, "E", 301, 0) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardMathFunction(MathFunctionVariables paramMathFunctionVariables) {
/* 200 */     super(paramMathFunctionVariables);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardMathFunction() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCode(int paramInt) {
/* 211 */     return (this.defaultTab[paramInt]).code;
/*     */   }
/*     */   public double getConstant(int paramInt) {
/* 214 */     return (paramInt == 300) ? Math.PI : Math.E;
/*     */   }
/*     */   public int getImplicitCode() {
/* 217 */     return 202;
/*     */   }
/*     */   public int getNumberOfTokens() {
/* 220 */     return this.defaultTab.length;
/*     */   }
/*     */   public int getPrecedence(int paramInt) {
/* 223 */     return (this.defaultTab[paramInt]).prec;
/*     */   }
/*     */   public String getToken(int paramInt) {
/* 226 */     return (this.defaultTab[paramInt]).string;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getType(int paramInt) {
/* 231 */     return paramInt / 100;
/*     */   }
/*     */   public boolean isAmbiguous(int paramInt) {
/* 234 */     return ((paramInt >= 190 && paramInt <= 199) || (paramInt >= 290 && paramInt <= 299));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double unaryOperation(int paramInt, double paramDouble) throws MathFunctionException {
/* 243 */     double d2, d1 = 0.0D;
/*     */     
/* 245 */     switch (paramInt)
/*     */     { case 191:
/* 247 */         return -paramDouble;
/*     */       case 102:
/* 249 */         return Math.abs(paramDouble);
/*     */       case 103:
/* 251 */         d1 = Math.exp(paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 304 */         error_check(d1, paramInt);
/* 305 */         return d1;case 106: d1 = Math.log(paramDouble); error_check(d1, paramInt); return d1;case 108: d1 = Math.sqrt(paramDouble); error_check(d1, paramInt); return d1;case 109: return Math.sin(paramDouble);case 110: return Math.cos(paramDouble);case 111: d1 = Math.tan(paramDouble); error_check(d1, paramInt); return d1;case 112: d1 = Math.atan(paramDouble); error_check(d1, paramInt); return d1;case 113: d1 = Math.asin(paramDouble); error_check(d1, paramInt); return d1;case 114: d1 = Math.acos(paramDouble); error_check(d1, paramInt); return d1;case 115: d1 = 0.5D * (Math.exp(paramDouble) - Math.exp(-paramDouble)); error_check(d1, paramInt); return d1;case 116: d1 = 0.5D * (Math.exp(paramDouble) + Math.exp(-paramDouble)); error_check(d1, paramInt); return d1;case 117: d2 = Math.exp(-paramDouble - paramDouble); d1 = (1.0D - d2) / (1.0D + d2); error_check(d1, paramInt); return d1;
/*     */       case 105: return Math.rint(paramDouble);
/*     */       case 104: return Maths.truncate(paramDouble);
/*     */       case 118: return Maths.sign(paramDouble);
/*     */       case 190:
/*     */         return paramDouble;
/*     */       case 119:
/*     */         return Math.toDegrees(paramDouble);
/*     */       case 120:
/* 314 */         return Math.toRadians(paramDouble); }  throw new MathFunctionException("Implementation error"); } public double binaryOperation(int paramInt, double paramDouble1, double paramDouble2) throws MathFunctionException { double d = 0.0D;
/*     */ 
/*     */     
/* 317 */     switch (paramInt) {
/*     */       case 290:
/* 319 */         return paramDouble1 + paramDouble2;
/*     */       case 291:
/* 321 */         return paramDouble1 - paramDouble2;
/*     */       case 202:
/* 323 */         if (Maths.multOverflow(paramDouble1, paramDouble2)) {
/* 324 */           throw new MathFunctionException("Result of a multiplication is too large");
/*     */         }
/* 326 */         return paramDouble1 * paramDouble2;
/*     */       case 203:
/* 328 */         if (paramDouble2 == 0.0D) {
/* 329 */           throw new MathFunctionException("Attempted division by zero");
/*     */         }
/* 331 */         return paramDouble1 / paramDouble2;
/*     */       case 204:
/* 333 */         d = Math.pow(paramDouble1, paramDouble2);
/* 334 */         error_check(d, paramInt);
/* 335 */         return d;
/*     */       case 205:
/* 337 */         if (paramDouble2 == 0.0D) {
/* 338 */           throw new MathFunctionException("Attempted division by zero");
/*     */         }
/* 340 */         return Maths.fmod(paramDouble1, paramDouble2);
/*     */     } 
/* 342 */     throw new MathFunctionException("Implementation error"); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void error_check(double paramDouble, int paramInt) throws MathFunctionException {
/* 351 */     if (Double.isNaN(paramDouble))
/* 352 */       throw new MathFunctionException("Inappropriate argument to " + getTokenFromCode(paramInt)); 
/* 353 */     if (Double.isInfinite(paramDouble)) {
/* 354 */       throw new MathFunctionException("Result from " + getTokenFromCode(paramInt) + " is infinite");
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
/*     */   public boolean replaceToken(String paramString1, String paramString2) {
/* 366 */     for (byte b = 0; b < getNumberOfTokens(); b++) {
/*     */       
/* 368 */       if (getToken(b).equalsIgnoreCase(paramString1)) {
/* 369 */         (this.defaultTab[b]).string = paramString2; return true;
/*     */       } 
/* 371 */     }  return false;
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
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 425 */       double d = Double.NaN;
/*     */       
/* 427 */       SubscriptedVariable subscriptedVariable = new SubscriptedVariable("X", 10);
/*     */ 
/*     */       
/* 430 */       StandardMathFunction standardMathFunction1 = new StandardMathFunction();
/* 431 */       StandardMathFunction standardMathFunction2 = new StandardMathFunction(subscriptedVariable);
/* 432 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
/*     */       
/* 434 */       System.out.println(standardMathFunction2.getSummary());
/*     */       
/*     */       while (true) {
/*     */         String str;
/* 438 */         System.out.println("Enter expression f(X), or ? for help or Q to quit or Return to re-evaluate f(X)."); 
/* 439 */         try { str = bufferedReader.readLine(); }
/* 440 */         catch (IOException iOException) { System.out.println(iOException.getMessage()); continue; }
/* 441 */          if (str.length() > 0) {
/*     */           
/* 443 */           if (Character.toUpperCase(str.charAt(0)) == 'Q') System.exit(0); 
/* 444 */           if (str.charAt(0) == '?') { System.out.println(standardMathFunction2.getSummary()); continue; }
/*     */           
/*     */           try {
/* 447 */             d = standardMathFunction2.parse(str);
/*     */           } catch (MathFunctionException mathFunctionException) {
/* 449 */             System.out.println(mathFunctionException.getMessage());
/*     */             continue;
/*     */           } 
/*     */         } 
/* 453 */         if (standardMathFunction2.getNumberOfVariablesUsed() == 0) {
/*     */           
/* 455 */           System.out.println("Constant value is " + d);
/*     */           
/*     */           continue;
/*     */         } 
/* 459 */         for (byte b = 0; b < subscriptedVariable.getNumberOfVariables(); b++) {
/*     */           
/* 461 */           String str1 = subscriptedVariable.getVariableName(b);
/* 462 */           if (standardMathFunction2.variableUsed(str1)) {
/*     */             String str2;
/* 464 */             System.out.println("Enter value of " + str1); 
/* 465 */             try { str2 = bufferedReader.readLine(); }
/* 466 */             catch (IOException iOException) { System.out.println(iOException.getMessage()); }
/* 467 */              try { subscriptedVariable.setVariableValue(b, standardMathFunction1.parse(str2)); }
/*     */             catch (MathFunctionException mathFunctionException)
/* 469 */             { System.out.println(mathFunctionException.getMessage()); }
/*     */           
/*     */           } 
/*     */         }  try {
/* 473 */           d = standardMathFunction2.eval();
/*     */         } catch (MathFunctionException mathFunctionException) {
/* 475 */           System.out.println(mathFunctionException.getMessage()); continue;
/*     */         } 
/* 477 */         System.out.println("f(X) = " + d);
/* 478 */         System.out.println(standardMathFunction2.getEvalCount() + " evaluations");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/mathfunction/StandardMathFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */