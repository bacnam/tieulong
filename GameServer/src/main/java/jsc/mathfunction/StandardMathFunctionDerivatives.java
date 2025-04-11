package jsc.mathfunction;

import jsc.util.Maths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StandardMathFunctionDerivatives
        extends StandardMathFunction {
    public static final double BIG_VAL = 1.7E100D;
    static final String BAD_ORDER = "Order of Taylor coefficients must be > 0.";
    static final String NO_DERIV = "Derivative does not exist.";
    static final String NO_STORE = "Implementation error: insufficient storage";
    private int MAX_WORK;
    private double[][] T;
    private int wsub;

    public StandardMathFunctionDerivatives(MathFunctionVariables paramMathFunctionVariables) {
        super(paramMathFunctionVariables);
    }

    public static double toDerivative(int paramInt, double paramDouble) {
        return Maths.factorial(paramInt) * paramDouble;
    }

    public double evalDerivative(int paramInt) throws MathFunctionException {
        double[] arrayOfDouble = evalTaylorCoeffs(paramInt, 1);
        return arrayOfDouble[1];
    }

    public double[] evalDerivatives(int paramInt1, int paramInt2) throws MathFunctionException {
        double[] arrayOfDouble1 = evalTaylorCoeffs(paramInt1, paramInt2);
        double[] arrayOfDouble2 = new double[1 + paramInt2];
        for (byte b = 0; b <= paramInt2; ) {
            arrayOfDouble2[b] = toDerivative(b, arrayOfDouble1[b]);
            b++;
        }
        return arrayOfDouble2;
    }

    public double[] evalTaylorCoeffs(int paramInt1, int paramInt2) throws MathFunctionException {
        if (this.codeList.size() == 0) throw new MathFunctionException("Invalid function");

        if (paramInt2 < 1) throw new MathFunctionException("Order of Taylor coefficients must be > 0.");

        double[] arrayOfDouble = new double[1 + paramInt2];

        this.MAX_WORK = this.codeList.size() + 10;
        this.T = new double[this.MAX_WORK][1 + paramInt2];

        arrayOfDouble[0] = eval();

        int i = this.codeList.size();
        this.wsub = i;
        byte b1;
        for (b1 = 1; b1 <= i; b1++) {

            this.T[b1][0] = this.codeList.getValue(b1);
            recur_init(b1, paramInt1);
        }

        for (byte b2 = 1; b2 <= paramInt2; b2++) {

            this.wsub = i;
            for (b1 = 1; b1 <= i; b1++) {

                int j, k = this.codeList.getCode(b1);
                int m = this.codeList.getType(b1);
                switch (m) {
                    case 0:
                        j = Vartest(k, paramInt1);
                        if (j == 0 && b2 == 1) {
                            this.T[b1][b2] = 1.0D;
                            break;
                        }
                    case 3:
                        this.T[b1][b2] = 0.0D;
                        break;

                    case 1:
                    case 2:
                        recur(b1, b2, paramInt1);
                        break;
                    default:
                        throw new MathFunctionException("Implementation error: unrecognized code type");
                }

                if (Math.abs(this.T[b1][b2]) > 1.7E100D) {
                    throw new MathFunctionException("Evaluation of Taylor coefficient of order " + b2 + " approaching overflow.");
                }
            }
            arrayOfDouble[b2] = this.T[i][b2];
        }
        return arrayOfDouble;
    }

    private void recur(int paramInt1, int paramInt2, int paramInt3) throws MathFunctionException {
        int n, i1, i2, i = this.codeList.getLabelLine(this.codeList.getLeft(paramInt1));
        int j = this.codeList.getLabelLine(this.codeList.getRight(paramInt1));

        int i3 = this.codeList.getCode(i);
        int k = this.codeList.getType(i);
        if (k == 0 && Vartest(i3, paramInt3) < 0)
            k = 3;
        i3 = this.codeList.getCode(j);
        int m = this.codeList.getType(j);
        if (m == 0 && Vartest(i3, paramInt3) < 0) {
            m = 3;
        }

        double d1 = this.T[i][0];
        double d2 = this.T[j][0];

        switch (this.codeList.getCode(paramInt1)) {

            case 290:
                this.T[paramInt1][paramInt2] = this.T[i][paramInt2] + this.T[j][paramInt2];
                return;

            case 291:
                this.T[paramInt1][paramInt2] = this.T[i][paramInt2] - this.T[j][paramInt2];
                return;
            case 202:
                recur_multiply(paramInt1, paramInt2, i, j, k, m, d1, d2);
                return;
            case 203:
                recur_divide(paramInt1, paramInt2, i, j, k, m, d2);
                return;
            case 204:
                recur_power(paramInt1, paramInt2, i, j, k, m, d1, d2);
                return;
            case 205:
                throw new MathFunctionException("Cannot differentiate modulus % operator.");

            case 108:
                recur_power(paramInt1, paramInt2, j, j, k, 3, d1, 0.5D);
                return;
            case 191:
                this.T[paramInt1][paramInt2] = -this.T[j][paramInt2];
                return;
            case 103:
                RECUR_EXP(paramInt1, paramInt2, j);
                return;
            case 106:
                recur_ln(paramInt1, paramInt2, j);
                return;
            case 110:
                recur_sin_cos(++this.wsub, paramInt1, paramInt2, j);
                return;
            case 109:
                recur_sin_cos(paramInt1, ++this.wsub, paramInt2, j);
                return;
            case 111:
                i1 = ++this.wsub;
                i2 = ++this.wsub;
                recur_sin_cos(i1, i2, paramInt2, j);

                recur_divide(paramInt1, paramInt2, i1, i2, k, m, d2);
                return;
            case 116:
                recur_sinh_cosh(++this.wsub, paramInt1, paramInt2, j);
                return;
            case 115:
                recur_sinh_cosh(paramInt1, ++this.wsub, paramInt2, j);
                return;
            case 117:
                i1 = ++this.wsub;
                i2 = ++this.wsub;
                recur_sinh_cosh(i1, i2, paramInt2, j);

                recur_divide(paramInt1, paramInt2, i1, i2, k, m, d2);
                return;
            case 112:
                i1 = ++this.wsub;
                i2 = ++this.wsub;

                RECUR_SQUARE(i1, paramInt2, j, m, d2);

                recur_divide(i2, paramInt2, 0, i1, 3, m, d2);

                recur_unary(paramInt1, paramInt2, i2, j);
                return;
            case 113:
            case 114:
                i1 = ++this.wsub;
                n = ++this.wsub;
                i2 = ++this.wsub;

                RECUR_SQUARE(i1, paramInt2, j, m, d2);
                this.T[i1][paramInt2] = -this.T[i1][paramInt2];

                recur_power(n, paramInt2, i1, 0, k, 3, d1, 0.5D);

                recur_divide(i2, paramInt2, 0, n, 3, m, d2);

                recur_unary(paramInt1, paramInt2, i2, j);
                return;
            case 102:
                if (m == 3) {
                    this.T[paramInt1][paramInt2] = Math.abs(this.T[j][paramInt2]);
                } else {
                    if (this.T[j][paramInt2] == 0.0D) {
                        throw new MathFunctionException("Derivative does not exist.");
                    }
                    this.T[paramInt1][paramInt2] = (this.T[j][paramInt2] < 0.0D) ? -this.T[j][paramInt2] : this.T[j][paramInt2];
                }
                return;
            case 190:
                this.T[paramInt1][paramInt2] = this.T[j][paramInt2];
                return;

            case 104:
                if (m == 3) {
                    this.T[paramInt1][paramInt2] = Maths.truncate(this.T[j][paramInt2]);
                } else {
                    throw new MathFunctionException("Cannot differentiate INT function.");
                }
                return;
            case 118:
                if (m == 3) {
                    this.T[paramInt1][paramInt2] = Maths.sign(this.T[j][paramInt2]);
                } else {
                    throw new MathFunctionException("Cannot differentiate SIGN function.");
                }
                return;
            case 105:
                if (m == 3) {
                    this.T[paramInt1][paramInt2] = Math.rint(this.T[j][paramInt2]);
                } else {
                    throw new MathFunctionException("Cannot differentiate NINT function.");
                }
                return;
            case 119:
                this.T[paramInt1][paramInt2] = Math.toDegrees(this.T[j][paramInt2]);
                return;

            case 120:
                this.T[paramInt1][paramInt2] = Math.toRadians(this.T[j][paramInt2]);
                return;
        }

        throw new MathFunctionException("Implementation error: unrecognized code");
    }

    private void recur_divide(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble) throws MathFunctionException {
        double d = 0.0D;

        if (paramInt6 == 3 && paramDouble != 0.0D) {
            this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2] / paramDouble;
        } else {

            if (TRIM(this.T[paramInt4][0]) == 0.0D)
                throw new MathFunctionException("Derivative does not exist.");
            for (byte b = 1; b <= paramInt2; b++)
                d += this.T[paramInt1][paramInt2 - b] * this.T[paramInt4][b];
            if (paramInt5 == 3) {
                this.T[paramInt1][paramInt2] = -d / this.T[paramInt4][0];
            } else {
                this.T[paramInt1][paramInt2] = (this.T[paramInt3][paramInt2] - d) / this.T[paramInt4][0];
            }
        }
    }

    private void RECUR_EXP(int paramInt1, int paramInt2, int paramInt3) {
        recur_unary(paramInt1, paramInt2, paramInt1, paramInt3);
    }

    private void recur_init(int paramInt1, int paramInt2) throws MathFunctionException {
        int k, n, i1, i2, i3, m = 1;

        int i = this.codeList.getLabelLine(this.codeList.getLeft(paramInt1));
        int j = this.codeList.getLabelLine(this.codeList.getRight(paramInt1));
        double d1 = this.T[i][0];
        double d2 = this.T[j][0];

        switch (this.codeList.getCode(paramInt1)) {
            case 204:
                i3 = this.codeList.getRightCode(paramInt1);
                k = this.codeList.getRightType(paramInt1);
                if (k == 3 || (k == 0 && Vartest(i3, paramInt2) < 0)) {

                    if (d2 <= 2.0D || d1 != 0.0D)
                        return;
                    int i4;
                    if (d2 == (i4 = (int) d2)) {

                        for (; m <= i4; m <<= 1) ;
                        m >>= 1;

                        while ((m >>= 1) != 0) {

                            this.T[winc()][0] = 0.0D;
                            if ((i4 & m) != 0) this.T[winc()][0] = 0.0D;

                        }
                    } else {
                        throw new MathFunctionException("Attempted division by zero");
                    }

                    return;
                }
                i1 = winc();
                i2 = winc();
                this.T[i1][0] = unaryOperation(106, d1);
                this.T[i2][0] = d2 * this.T[i1][0];
                return;
            case 110:
                this.T[winc()][0] = unaryOperation(109, d2);
                return;
            case 109:
                this.T[winc()][0] = unaryOperation(110, d2);
                return;
            case 111:
                this.T[winc()][0] = unaryOperation(109, d2);
                this.T[winc()][0] = unaryOperation(110, d2);
                return;
            case 116:
                this.T[winc()][0] = unaryOperation(115, d2);
                return;
            case 115:
                this.T[winc()][0] = unaryOperation(116, d2);
                return;
            case 117:
                this.T[winc()][0] = unaryOperation(115, d2);
                this.T[winc()][0] = unaryOperation(116, d2);
                return;
            case 112:
                i1 = winc();
                i2 = winc();
                this.T[i1][0] = 1.0D + d2 * d2;
                this.T[i2][0] = 1.0D / this.T[i1][0];
                return;
            case 113:
            case 114:
                i1 = winc();
                n = winc();
                i2 = winc();
                this.T[i1][0] = 1.0D - d2 * d2;
                this.T[n][0] = unaryOperation(108, this.T[i1][0]);
                if (this.T[n][0] != 0.0D) {
                    this.T[i2][0] = 1.0D / this.T[n][0];
                } else {
                    throw new MathFunctionException("Attempted division by zero");
                }
                if (this.codeList.getCode(paramInt1) == 114) {
                    this.T[i2][0] = -this.T[i2][0];
                }
                return;
        }
    }

    private void recur_int_power5(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble, int paramInt5) {
        int i = 1;

        int j = paramInt3;
        for (; i <= paramInt5; i <<= 1) ;
        i >>= 1;

        while ((i >>= 1) != 0) {

            RECUR_SQUARE(++this.wsub, paramInt2, j, paramInt4, paramDouble);
            if ((paramInt5 & i) != 0) {

                int k = this.wsub;
                recur_multiply(++this.wsub, paramInt2, paramInt3, k, paramInt4, paramInt4, paramDouble, 0.0D);
            }
            j = this.wsub;
        }
        this.T[paramInt1][paramInt2] = this.T[this.wsub][paramInt2];
    }

    private void recur_ln(int paramInt1, int paramInt2, int paramInt3) throws MathFunctionException {
        double d2 = 0.0D;

        double d1 = paramInt2;

        if (TRIM(this.T[paramInt3][0]) == 0.0D)
            throw new MathFunctionException("Derivative does not exist.");
        if (paramInt2 > 1) {

            for (byte b = 1; b <= paramInt2 - 1; b++)
                d2 += (d1 - b) / d1 * this.T[paramInt1][paramInt2 - b] * this.T[paramInt3][b];
            this.T[paramInt1][paramInt2] = (this.T[paramInt3][paramInt2] - d2) / this.T[paramInt3][0];
        } else {

            this.T[paramInt1][1] = this.T[paramInt3][1] / this.T[paramInt3][0];
        }
    }

    private void recur_multiply(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble1, double paramDouble2) {
        double d = 0.0D;

        if (paramInt5 == 3) {
            this.T[paramInt1][paramInt2] = paramDouble1 * this.T[paramInt4][paramInt2];
        } else if (paramInt6 == 3) {
            this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2] * paramDouble2;
        } else {

            for (byte b = 0; b <= paramInt2; b++) {
                d += this.T[paramInt3][b] * this.T[paramInt4][paramInt2 - b];
            }
            this.T[paramInt1][paramInt2] = d;
        }
    }

    private void recur_power(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, double paramDouble1, double paramDouble2) throws MathFunctionException {
        double d = 0.0D;

        if (paramInt6 == 3) {

            if (paramDouble2 == 0.0D) {
                this.T[paramInt1][paramInt2] = 0.0D;
            } else if (paramDouble2 == 1.0D) {
                this.T[paramInt1][paramInt2] = this.T[paramInt3][paramInt2];
            } else if (paramDouble2 == 2.0D) {
                RECUR_SQUARE(paramInt1, paramInt2, paramInt3, paramInt5, paramDouble1);
            } else if (TRIM(this.T[paramInt3][0]) != 0.0D) {

                double d1 = paramInt2;
                for (byte b = 0; b <= paramInt2 - 1; b++) {
                    d += (paramDouble2 - b * (paramDouble2 + 1.0D) / d1) * this.T[paramInt3][paramInt2 - b] * this.T[paramInt1][b];
                }
                this.T[paramInt1][paramInt2] = d / this.T[paramInt3][0];
            } else {
                int k;
                if (paramDouble2 == (k = (int) paramDouble2)) {
                    recur_int_power5(paramInt1, paramInt2, paramInt3, paramInt5, paramDouble1, k);
                } else {
                    throw new MathFunctionException("Attempted division by zero");
                }
            }
            return;
        }
        int i = ++this.wsub;
        int j = ++this.wsub;

        if (paramInt5 == 3) {

            recur_multiply(j, paramInt2, i, paramInt4, 3, paramInt6, this.T[i][0], paramDouble2);

            RECUR_EXP(paramInt1, paramInt2, j);

            return;
        }

        recur_ln(i, paramInt2, paramInt3);
        recur_multiply(j, paramInt2, i, paramInt4, paramInt5, paramInt6, paramDouble1, paramDouble2);
        RECUR_EXP(paramInt1, paramInt2, j);
    }

    private void recur_sin_cos(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        recur_unary(paramInt1, paramInt3, paramInt2, paramInt4);
        recur_unary(paramInt2, paramInt3, paramInt1, paramInt4);
        this.T[paramInt2][paramInt3] = -this.T[paramInt2][paramInt3];
    }

    private void recur_sinh_cosh(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        recur_unary(paramInt1, paramInt3, paramInt2, paramInt4);
        recur_unary(paramInt2, paramInt3, paramInt1, paramInt4);
    }

    private void RECUR_SQUARE(int paramInt1, int paramInt2, int paramInt3, int paramInt4, double paramDouble) {
        recur_multiply(paramInt1, paramInt2, paramInt3, paramInt3, paramInt4, paramInt4, paramDouble, paramDouble);
    }

    private void recur_unary(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        double d2 = 0.0D;

        double d1 = paramInt2;

        for (byte b = 0; b <= paramInt2 - 1; b++)
            d2 += (d1 - b) / d1 * this.T[paramInt3][b] * this.T[paramInt4][paramInt2 - b];
        this.T[paramInt1][paramInt2] = d2;
    }

    private double TRIM(double paramDouble) {
        return (Math.abs(paramDouble) < 5.0E-12D) ? 0.0D : paramDouble;
    }

    private int Vartest(int paramInt1, int paramInt2) {
        if (paramInt1 == paramInt2) return 0;
        return -1;
    }

    private int winc() throws MathFunctionException {
        if (++this.wsub >= this.MAX_WORK) {
            throw new MathFunctionException("Implementation error: insufficient storage");
        }
        return this.wsub;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            String str = "";
            XY xY = new XY();
            StandardMathFunctionDerivatives standardMathFunctionDerivatives = new StandardMathFunctionDerivatives(xY);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println(standardMathFunctionDerivatives.getSummary());
            while (true) {
                int i;
                double d1, arrayOfDouble[];
                String str1;
                System.out.println("Enter expression f(x,y), ? for help, Q to quit or Return to re-evaluate");
                try {
                    str1 = bufferedReader.readLine();
                } catch (IOException iOException) {
                    System.out.println(iOException.getMessage());
                    continue;
                }
                if (str1.length() > 0) {

                    if (Character.toUpperCase(str1.charAt(0)) == 'Q') System.exit(0);
                    if (str1.charAt(0) == '?') {
                        System.out.println(standardMathFunctionDerivatives.getSummary());
                        continue;
                    }

                    str = str1;
                    try {
                        d1 = standardMathFunctionDerivatives.parse(str);
                    } catch (MathFunctionException mathFunctionException) {
                        System.out.println(mathFunctionException.getMessage());
                        continue;
                    }
                    if (!standardMathFunctionDerivatives.variableUsed("X")) {
                        System.out.println("Expression must be a function of X to evaluate Taylor coeffs.");
                        continue;
                    }
                    if (standardMathFunctionDerivatives.variableUsed("Y")) {
                        xY.y = getConstant("Enter value of Y", bufferedReader);
                    }
                }

                while (true) {
                    System.out.println("Enter order of Taylor series");
                    try {
                        str1 = bufferedReader.readLine();
                    } catch (IOException iOException) {
                        System.out.println(iOException.getMessage());
                        continue;
                    }
                    try {
                        i = Integer.parseInt(str1);
                        break;
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Invalid input: " + numberFormatException.getMessage());
                    }

                }
                double d2 = getConstant("Taylor series about what value? (enter 0 for Maclaurin series)", bufferedReader);
                xY.x = d2;
                try {
                    arrayOfDouble = standardMathFunctionDerivatives.evalTaylorCoeffs(0, i);
                } catch (MathFunctionException mathFunctionException) {
                    System.out.println(mathFunctionException.getMessage());
                    continue;
                }
                System.out.println(i + "th Taylor polynomial about " + d2 + " approximating " + str + " is -");

                System.out.println("P(x) = " + arrayOfDouble[0]);
                byte b;
                for (b = 1; b <= i; b++) {
                    System.out.println(" + " + arrayOfDouble[b] + " * (x - " + d2 + ")^" + b);
                }
                xY.x = getConstant("Enter value of X", bufferedReader);

                double d3 = xY.x - d2;
                double d4 = arrayOfDouble[0];
                for (b = 1; b <= i; ) {
                    d4 += arrayOfDouble[b] * Math.pow(d3, b);
                    b++;
                }
                System.out.println("P(x) approximation to f(x) = " + d4);

                try {
                    d1 = standardMathFunctionDerivatives.eval();
                } catch (MathFunctionException mathFunctionException) {
                    System.out.println(mathFunctionException.getMessage());
                    continue;
                }
                System.out.println("    Exact value of f(x) is = " + d1);
            }
        }

        static double getConstant(String param1String, BufferedReader param1BufferedReader) {
            double d;
            StandardMathFunction standardMathFunction = new StandardMathFunction();
            while (true) {
                String str;
                System.out.println(param1String);
                try {
                    str = param1BufferedReader.readLine();
                } catch (IOException iOException) {
                    System.out.println(iOException.getMessage());
                    continue;
                }
                try {
                    d = standardMathFunction.parse(str);
                    break;
                } catch (MathFunctionException mathFunctionException) {
                    System.out.println(mathFunctionException.getMessage());
                }
            }
            return d;
        }

        static class XY
                implements MathFunctionVariables {
            public double x;

            public double y;

            public int getNumberOfVariables() {
                return 2;
            }

            public String getVariableName(int param2Int) {
                return (param2Int == 0) ? "X" : "Y";
            }

            public double getVariableValue(int param2Int) {
                return (param2Int == 0) ? this.x : this.y;
            }
        }
    }
}

