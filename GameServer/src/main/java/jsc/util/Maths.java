package jsc.util;

import jsc.swt.text.SigFigFormat;

public class Maths {
    public static final double EULER = 0.577215664901533D;
    public static final double LOG2 = Math.log(2.0D);

    public static final double LOG10 = Math.log(10.0D);

    static final double[] R = new double[]{0.0D, 0.0D, LOG2, Math.log(6.0D), Math.log(24.0D), Math.log(120.0D), Math.log(720.0D)};

    static final double[] R1 = new double[]{0.0D, -2.66685511495D, -24.4387534237D, -21.9698958928D, 11.1667541262D, 3.13060547623D, 0.607771387771D, 11.9400905721D, 31.4690115749D, 15.234687407D};

    static final double[] R2 = new double[]{0.0D, -78.3359299449D, -142.046296688D, 137.519416416D, 78.6994924154D, 4.16438922228D, 47.066876606D, 313.399215894D, 263.505074721D, 43.3400022514D};

    static final double[] R3 = new double[]{0.0D, -212159.572323D, 230661.510616D, 27464.7644705D, -40262.1119975D, -2296.6072978D, -116328.495004D, -146025.937511D, -24235.7409629D, -570.691009324D};

    static final double[] R4 = new double[]{0.0D, 0.279195317918525D, 0.4917317610505968D, 0.0692910599291889D, 3.350343815022304D, 6.012459259764103D};

    static final double ALR2PI = 0.918938533204673D;

    static final double XLGE = 5100000.0D;

    static final double XLGST = 1.0E305D;

    public static double binomialCoefficient(long paramLong1, long paramLong2) {
        return Math.rint(Math.exp(logBinomialCoefficient(paramLong1, paramLong2)));
    }

    public static double factorial(long paramLong) {
        if (paramLong < 0L)
            throw new IllegalArgumentException("Invalid argument of factorial function.");
        if (paramLong > 8L)
            return Math.rint(Math.exp(logFactorial(paramLong)));
        int i = 1;
        for (byte b = 2; b <= paramLong; ) {
            i *= b;
            b++;
        }
        return i;
    }

    public static double fmod(double paramDouble1, double paramDouble2) {
        double d = paramDouble1 / Math.abs(paramDouble2);
        return d - truncate(d);
    }

    public static boolean isNoneNegativeInteger(double paramDouble) {
        return (paramDouble >= 0.0D && paramDouble == Math.floor(paramDouble));
    }

    public static boolean isPositiveInteger(double paramDouble) {
        return (paramDouble > 0.0D && paramDouble == Math.floor(paramDouble));
    }

    public static double lnB(double paramDouble1, double paramDouble2) {
        return logGamma(paramDouble1) + logGamma(paramDouble2) - logGamma(paramDouble1 + paramDouble2);
    }

    public static double log2(double paramDouble) {
        return Math.log(paramDouble) / LOG2;
    }

    public static double log10(double paramDouble) {
        return Math.log(paramDouble) / LOG10;
    }

    public static double logBinomialCoefficient(long paramLong1, long paramLong2) {
        return logFactorial(paramLong1) - logFactorial(paramLong2) - logFactorial(paramLong1 - paramLong2);
    }

    public static double logFactorial(long paramLong) {
        if (paramLong < 0L) {
            throw new IllegalArgumentException("Invalid argument of log factorial function.");
        }

        if (paramLong < 7L) return R[(int) paramLong];
        double d1 = (paramLong + 1L);
        double d2 = 1.0D / d1 * d1;
        return (d1 - 0.5D) * Math.log(d1) - d1 + 0.918938533205D + (((4.0D - 3.0D * d2) * d2 - 14.0D) * d2 + 420.0D) / 5040.0D * d1;
    }

    public static double logGamma(double paramDouble) {
        double d1 = paramDouble;
        double d5 = 0.0D;

        if (d1 <= 0.0D || d1 >= 1.0E305D) {
            throw new IllegalArgumentException("Invalid argument of log Gamma function.");
        }

        if (d1 < 1.5D) {
            double d;
            if (d1 < 0.5D) {

                d5 = -Math.log(d1);
                d = d1 + 1.0D;

                if (d == 1.0D) return d5;

            } else {

                d5 = 0.0D;
                d = d1;
                d1 = d1 - 0.5D - 0.5D;
            }
            d5 += d1 * ((((R1[5] * d + R1[4]) * d + R1[3]) * d + R1[2]) * d + R1[1]) / ((((d + R1[9]) * d + R1[8]) * d + R1[7]) * d + R1[6]);

            return d5;
        }

        if (d1 < 4.0D) {

            double d = d1 - 1.0D - 1.0D;
            d5 = d * ((((R2[5] * d1 + R2[4]) * d1 + R2[3]) * d1 + R2[2]) * d1 + R2[1]) / ((((d1 + R2[9]) * d1 + R2[8]) * d1 + R2[7]) * d1 + R2[6]);

            return d5;
        }

        if (d1 < 12.0D) {

            d5 = ((((R3[5] * d1 + R3[4]) * d1 + R3[3]) * d1 + R3[2]) * d1 + R3[1]) / ((((d1 + R3[9]) * d1 + R3[8]) * d1 + R3[7]) * d1 + R3[6]);

            return d5;
        }

        double d4 = Math.log(d1);
        d5 = d1 * (d4 - 1.0D) - 0.5D * d4 + 0.918938533204673D;
        if (d1 > 5100000.0D) return d5;
        double d2 = 1.0D / d1;
        double d3 = d2 * d2;
        d5 += d2 * ((R4[3] * d3 + R4[2]) * d3 + R4[1]) / ((d3 + R4[5]) * d3 + R4[4]);

        return d5;
    }

    public static double logMultinomialCoefficient(int[] paramArrayOfint) {
        int i = 0;
        double d = 0.0D;
        int j = paramArrayOfint.length;

        for (byte b = 0; b < j; b++) {

            if (paramArrayOfint[b] < 0)
                throw new IllegalArgumentException("Negative number of objects.");
            i += paramArrayOfint[b];
            d += logFactorial(paramArrayOfint[b]);
        }
        return logFactorial(i) - d;
    }

    public static double multinomialCoefficient(int[] paramArrayOfint) {
        return Math.rint(Math.exp(logMultinomialCoefficient(paramArrayOfint)));
    }

    public static boolean multOverflow(double paramDouble1, double paramDouble2) {
        paramDouble1 = Math.abs(paramDouble1);
        paramDouble2 = Math.abs(paramDouble2);

        if (paramDouble1 > paramDouble2) {
            double d = paramDouble2;
            paramDouble2 = paramDouble1;
            paramDouble1 = d;
        }

        if (paramDouble2 <= 1.0D || paramDouble1 < Double.MAX_VALUE / paramDouble2) {
            return false;
        }
        return true;
    }

    public static double round(double paramDouble, int paramInt) {
        double d = Math.pow(10.0D, paramInt);
        return Math.floor(paramDouble * d + 0.5D) / d;
    }

    public static double roundSigFigs(double paramDouble, int paramInt) {
        SigFigFormat sigFigFormat = new SigFigFormat(paramInt);
        return sigFigFormat.round(paramDouble);
    }

    public static double sign(double paramDouble) {
        if (paramDouble > 0.0D)
            return 1.0D;
        if (paramDouble < 0.0D) {
            return -1.0D;
        }
        return 0.0D;
    }

    public static double truncate(double paramDouble) {
        if (paramDouble >= 0.0D) {
            return Math.floor(paramDouble);
        }
        return -Math.floor(Math.abs(paramDouble));
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            for (byte b = 0; b <= 20; b++) {

                System.out.println("j = " + b + " " + Maths.logFactorial(b));
                System.out.println("j = " + b + " " + Maths.logGamma(b + 1.0D));
            }
        }
    }
}

