package jsc.mathfunction;

import jsc.util.Maths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StatisticalMathFunction
        extends AbstractMathFunction {
    public static final int PLUS = 190;
    public static final int MINUS = 191;
    public static final int ABS = 102;
    public static final int EXP = 103;
    public static final int INT = 104;
    public static final int NINT = 105;
    public static final int LOG = 106;
    public static final int SQRT = 108;
    public static final int SIN = 109;
    public static final int COS = 110;
    public static final int TAN = 111;
    public static final int ATAN = 112;
    public static final int ASIN = 113;
    public static final int ACOS = 114;
    public static final int SINH = 115;
    public static final int COSH = 116;
    public static final int TANH = 117;
    public static final int SIGN = 118;
    public static final int DEG = 119;
    public static final int RAD = 120;
    public static final int GAMMA = 130;
    public static final int LOG_GAMMA = 131;
    public static final int NOT = 132;
    public static final int ADD = 290;
    public static final int SUBTRACT = 291;
    public static final int MULTIPLY = 202;
    public static final int DIVIDE = 203;
    public static final int POWER = 204;
    public static final int MODULUS = 205;
    public static final int PI_CONST = 300;
    public static final int E_CONST = 301;
    public static final int LT = 210;
    public static final int LE = 211;
    public static final int EQ = 212;
    public static final int GE = 213;
    public static final int GT = 214;
    public static final int NE = 215;
    public static final int AND = 216;
    public static final int OR = 217;
    static final String IMP_ERROR = "Implementation error";
    static final String TOO_BIG = "Result of a multiplication is too large";
    static final String DIV_ZERO = "Attempted division by zero";
    Token[] defaultTab = new Token[]{new Token(this, "+", 190, 8), new Token(this, "+", 290, 6), new Token(this, "-", 191, 8), new Token(this, "-", 291, 6), new Token(this, "*", 202, 7), new Token(this, "/", 203, 7), new Token(this, "%", 205, 7), new Token(this, "^", 204, 9), new Token(this, "<>", 215, 4), new Token(this, "~=", 215, 4), new Token(this, "~", 132, 8), new Token(this, "<=", 211, 5), new Token(this, ">=", 213, 5), new Token(this, "<", 210, 5), new Token(this, ">", 214, 5), new Token(this, "=", 212, 4), new Token(this, "&", 216, 3), new Token(this, "|", 217, 2), new Token(this, "LGAM", 131, 10), new Token(this, "GAM", 130, 10), new Token(this, "ABS", 102, 10), new Token(this, "SIGN", 118, 10), new Token(this, "EXP", 103, 10), new Token(this, "NINT", 105, 10), new Token(this, "INT", 104, 10), new Token(this, "LOG", 106, 10), new Token(this, "SQRT", 108, 10), new Token(this, "ATAN", 112, 10), new Token(this, "ASIN", 113, 10), new Token(this, "ACOS", 114, 10), new Token(this, "SINH", 115, 10), new Token(this, "COSH", 116, 10), new Token(this, "TANH", 117, 10), new Token(this, "SIN", 109, 10), new Token(this, "COS", 110, 10), new Token(this, "TAN", 111, 10), new Token(this, "DEG", 119, 10), new Token(this, "RAD", 120, 10), new Token(this, "NOT", 132, 8), new Token(this, "AND", 216, 3), new Token(this, "OR", 217, 2), new Token(this, "PI", 300, 0), new Token(this, "E", 301, 0)};

    public StatisticalMathFunction(MathFunctionVariables paramMathFunctionVariables) {
        super(paramMathFunctionVariables);
    }

    public StatisticalMathFunction() {
    }

    public int getCode(int paramInt) {
        return (this.defaultTab[paramInt]).code;
    }

    public double getConstant(int paramInt) {
        return (paramInt == 300) ? Math.PI : Math.E;
    }

    public int getImplicitCode() {
        return 202;
    }

    public int getNumberOfTokens() {
        return this.defaultTab.length;
    }

    public int getPrecedence(int paramInt) {
        return (this.defaultTab[paramInt]).prec;
    }

    public String getToken(int paramInt) {
        return (this.defaultTab[paramInt]).string;
    }

    public int getType(int paramInt) {
        return paramInt / 100;
    }

    public boolean isAmbiguous(int paramInt) {
        return ((paramInt >= 190 && paramInt <= 199) || (paramInt >= 290 && paramInt <= 299));
    }

    public double unaryOperation(int paramInt, double paramDouble) throws MathFunctionException {
        double d2, d1 = 0.0D;

        switch (paramInt) {
            case 191:
                return -paramDouble;
            case 102:
                return Math.abs(paramDouble);
            case 103:
                d1 = Math.exp(paramDouble);

                error_check(d1, paramInt);
                return d1;
            case 106:
                d1 = Math.log(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 108:
                d1 = Math.sqrt(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 109:
                return Math.sin(paramDouble);
            case 110:
                return Math.cos(paramDouble);
            case 111:
                d1 = Math.tan(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 112:
                d1 = Math.atan(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 113:
                d1 = Math.asin(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 114:
                d1 = Math.acos(paramDouble);
                error_check(d1, paramInt);
                return d1;
            case 115:
                d1 = 0.5D * (Math.exp(paramDouble) - Math.exp(-paramDouble));
                error_check(d1, paramInt);
                return d1;
            case 116:
                d1 = 0.5D * (Math.exp(paramDouble) + Math.exp(-paramDouble));
                error_check(d1, paramInt);
                return d1;
            case 117:
                d2 = Math.exp(-paramDouble - paramDouble);
                d1 = (1.0D - d2) / (1.0D + d2);
                error_check(d1, paramInt);
                return d1;
            case 105:
                return Math.rint(paramDouble);
            case 104:
                return Maths.truncate(paramDouble);
            case 118:
                return Maths.sign(paramDouble);
            case 190:
                return paramDouble;
            case 119:
                return Math.toDegrees(paramDouble);
            case 120:
                return Math.toRadians(paramDouble);
            case 132:
                return (paramDouble == 0.0D) ? 1.0D : 0.0D;
            case 130:
                try {
                    d1 = Maths.logGamma(paramDouble);
                } catch (IllegalArgumentException illegalArgumentException) {
                    d1 = Double.NaN;
                }
                d1 = Math.exp(d1);
                error_check(d1, paramInt);
                return d1;
            case 131:
                try {
                    d1 = Maths.logGamma(paramDouble);
                } catch (IllegalArgumentException illegalArgumentException) {
                    d1 = Double.NaN;
                }
                error_check(d1, paramInt);
                return d1;
        }
        throw new MathFunctionException("Implementation error");
    }

    public double binaryOperation(int paramInt, double paramDouble1, double paramDouble2) throws MathFunctionException {
        double d = 0.0D;

        switch (paramInt) {
            case 290:
                return paramDouble1 + paramDouble2;
            case 291:
                return paramDouble1 - paramDouble2;
            case 202:
                if (Maths.multOverflow(paramDouble1, paramDouble2)) {
                    throw new MathFunctionException("Result of a multiplication is too large");
                }
                return paramDouble1 * paramDouble2;
            case 203:
                if (paramDouble2 == 0.0D) {
                    throw new MathFunctionException("Attempted division by zero");
                }
                return paramDouble1 / paramDouble2;
            case 204:
                d = Math.pow(paramDouble1, paramDouble2);

                error_check(d, paramInt);
                return d;
            case 216:
                return (paramDouble1 != 0.0D && paramDouble2 != 0.0D) ? 1.0D : 0.0D;
            case 217:
                return (paramDouble1 != 0.0D || paramDouble2 != 0.0D) ? 1.0D : 0.0D;
            case 211:
                return (paramDouble1 <= paramDouble2) ? 1.0D : 0.0D;
            case 213:
                return (paramDouble1 >= paramDouble2) ? 1.0D : 0.0D;
            case 215:
                return (paramDouble1 != paramDouble2) ? 1.0D : 0.0D;
            case 210:
                return (paramDouble1 < paramDouble2) ? 1.0D : 0.0D;
            case 214:
                return (paramDouble1 > paramDouble2) ? 1.0D : 0.0D;
            case 212:
                return (paramDouble1 == paramDouble2) ? 1.0D : 0.0D;
            case 205:
                if (paramDouble2 == 0.0D)
                    throw new MathFunctionException("Attempted division by zero");
                return Maths.fmod(paramDouble1, paramDouble2);
        }
        throw new MathFunctionException("Implementation error");
    }

    private void error_check(double paramDouble, int paramInt) throws MathFunctionException {
        if (Double.isNaN(paramDouble))
            throw new MathFunctionException("Inappropriate argument to " + getTokenFromCode(paramInt));
        if (Double.isInfinite(paramDouble)) {
            throw new MathFunctionException("Result from " + getTokenFromCode(paramInt) + " is infinite");
        }
    }

    public boolean replaceToken(String paramString1, String paramString2) {
        for (byte b = 0; b < getNumberOfTokens(); b++) {

            if (getToken(b).equalsIgnoreCase(paramString1)) {
                (this.defaultTab[b]).string = paramString2;
                return true;
            }
        }
        return false;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d = Double.NaN;
            X x = new X(10);
            StandardMathFunction standardMathFunction = new StandardMathFunction();
            StatisticalMathFunction statisticalMathFunction = new StatisticalMathFunction(x);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(statisticalMathFunction.getSummary());

            while (true) {
                String str;
                System.out.println("Enter expression f(X), or ? for help or Q to quit or Return to re-evaluate f(X).");
                try {
                    str = bufferedReader.readLine();
                } catch (IOException iOException) {
                    System.out.println(iOException.getMessage());
                    continue;
                }
                if (str.length() > 0) {

                    if (Character.toUpperCase(str.charAt(0)) == 'Q') System.exit(0);
                    if (str.charAt(0) == '?') {
                        System.out.println(statisticalMathFunction.getSummary());
                        continue;
                    }

                    try {
                        d = statisticalMathFunction.parse(str);
                    } catch (MathFunctionException mathFunctionException) {
                        System.out.println(mathFunctionException.getMessage());
                        continue;
                    }
                }
                if (statisticalMathFunction.getNumberOfVariablesUsed() == 0) {

                    System.out.println("Constant value is " + d);

                    continue;
                }
                for (byte b = 0; b < x.getNumberOfVariables(); b++) {

                    String str1 = "X" + (b + 1);
                    if (statisticalMathFunction.variableUsed(str1)) {
                        String str2;
                        System.out.println("Enter value of " + str1);
                        try {
                            str2 = bufferedReader.readLine();
                        } catch (IOException iOException) {
                            System.out.println(iOException.getMessage());
                        }
                        try {
                            x.setVariableValue(b, standardMathFunction.parse(str2));
                        } catch (MathFunctionException mathFunctionException) {
                            System.out.println(mathFunctionException.getMessage());
                        }

                    }
                }
                try {
                    d = statisticalMathFunction.eval();
                } catch (MathFunctionException mathFunctionException) {
                    System.out.println(mathFunctionException.getMessage());
                    continue;
                }
                System.out.println("f(X) = " + d);
                System.out.println(statisticalMathFunction.getEvalCount() + " evaluations");
            }
        }

        static class X
                implements MathFunctionVariables {
            int n;

            double[] x;

            public X(int param2Int) {
                this.n = param2Int;
                this.x = new double[param2Int];
            }

            public int getNumberOfVariables() {
                return this.n;
            }

            public String getVariableName(int param2Int) {
                return "X" + (param2Int + 1);
            }

            public double getVariableValue(int param2Int) {
                return this.x[param2Int];
            }

            public void setVariableValue(int param2Int, double param2Double) {
                this.x[param2Int] = param2Double;
            }
        }
    }

    class Token {
        private final StatisticalMathFunction this$0;
        String string;
        int code;
        int prec;

        Token(StatisticalMathFunction this$0, String param1String, int param1Int1, int param1Int2) {
            this.this$0 = this$0;
            this.string = param1String;
            this.code = param1Int1;
            this.prec = param1Int2;
        }
    }
}

