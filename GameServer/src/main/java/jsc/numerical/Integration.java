package jsc.numerical;

import jsc.distributions.Beta;
import jsc.distributions.ChiSquared;
import jsc.distributions.Normal;
import jsc.distributions.PowerFunction;
import jsc.util.Maths;

public class Integration {
    private static final int JMAX = 14;
    private static final int JMAXP = 15;
    private static final int K = 5;
    private static final double RT3 = Math.sqrt(3.0D);
    private static final double HAFRT3 = 0.5D * RT3;
    private static final double TWOPI = 6.283185307179586D;
    private static final int M_MAX = 8;
    private static double dy;
    private static double esterr;

    private static int used;

    private static double[] csxfrm;

    public static double clenshawCurtis(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
        int[] arrayOfInt = new int[9];

        double d1 = (paramDouble1 + paramDouble2) * 0.5D;
        double d2 = (paramDouble2 - paramDouble1) * 0.5D;
        int j = (int) Math.min(paramInt, 2.0D * Math.pow(3.0D, 9.0D));
        int k;
        for (k = 1; k <= 8; ) {
            arrayOfInt[k] = 1;
            k++;
        }

        csxfrm = new double[j + 1];

        int i = 6;

        csxfrm[1] = paramFunction.function(paramDouble1);
        csxfrm[7] = paramFunction.function(paramDouble2);
        double d3 = d2 * RT3 * 0.5D;
        csxfrm[2] = paramFunction.function(d1 - d3);
        csxfrm[6] = paramFunction.function(d1 + d3);
        d3 = d2 * 0.5D;
        csxfrm[3] = paramFunction.function(d1 - d3);
        csxfrm[5] = paramFunction.function(d1 + d3);
        csxfrm[4] = paramFunction.function(d1);

        double d5 = csxfrm[1] + csxfrm[7];
        double d6 = csxfrm[1] - csxfrm[7];
        double d7 = 2.0D * csxfrm[4];
        double d8 = csxfrm[2] + csxfrm[6];
        double d9 = (csxfrm[2] - csxfrm[6]) * RT3;
        double d10 = csxfrm[3] + csxfrm[5];
        double d11 = csxfrm[3] - csxfrm[5];
        double d12 = d5 + d10 + d10;
        double d13 = d8 + d8 + d7;
        double d14 = d6 + d11;
        double d15 = d5 - d10;
        double d16 = d8 - d7;
        csxfrm[1] = d12 + d13;
        csxfrm[2] = d14 + d9;
        csxfrm[3] = d15 + d16;
        csxfrm[4] = d6 - d11 - d11;
        csxfrm[5] = d15 - d16;
        csxfrm[6] = d14 - d9;
        csxfrm[7] = d12 - d13;
        used = 7;

        double d4 = (d5 + d7 + d7) / 3.0D;

        while (true) {
            int i2 = i - 3;
            double d18 = 0.5D * csxfrm[used] / (1.0D - (i * i));
            for (k = 1; k <= i2; k += 2) {

                int i5 = i - k;
                d18 += csxfrm[i5] / (i5 * (2 - i5));
            }
            d18 += 0.5D * csxfrm[1];

            esterr = Math.abs(d4 * 3.0D - d18);

            if (Math.abs(d18) * paramDouble3 >= esterr || 3 * i + 1 > j) {

                double d = d2 * d18 / 0.5D * i;
                esterr = d2 * esterr / 0.5D * i;
                csxfrm = null;
                return d;
            }

            d4 = d18;

            for (k = 2; k <= 8; k++)
                arrayOfInt[k - 1] = arrayOfInt[k];
            arrayOfInt[8] = 3 * arrayOfInt[7];
            k = used;
            double d17 = Math.PI / (3 * i);
            int i4;
            for (i4 = 1; i4 <= arrayOfInt[1]; i4++) {
                for (int i5 = i4; i5 <= arrayOfInt[2]; i5 += arrayOfInt[1]) {
                    for (int i6 = i5; i6 <= arrayOfInt[3]; i6 += arrayOfInt[2]) {
                        for (int i7 = i6; i7 <= arrayOfInt[4]; i7 += arrayOfInt[3]) {
                            for (int i8 = i7; i8 <= arrayOfInt[5]; i8 += arrayOfInt[4]) {
                                for (int i9 = i8; i9 <= arrayOfInt[6]; i9 += arrayOfInt[5]) {
                                    for (int i10 = i9; i10 <= arrayOfInt[7]; i10 += arrayOfInt[6]) {
                                        for (int i11 = i10; i11 <= arrayOfInt[8]; i11 += arrayOfInt[7]) {
                                            double d = d17 * (3 * i11 - 2);
                                            d3 = d2 * Math.cos(d);
                                            d5 = paramFunction.function(d1 - d3);
                                            d7 = paramFunction.function(d1 + d3);
                                            d3 = d2 * Math.sin(d);
                                            d6 = paramFunction.function(d1 + d3);
                                            d8 = paramFunction.function(d1 - d3);
                                            d9 = d5 + d7;
                                            d10 = d6 + d8;
                                            csxfrm[k + 1] = d9 + d10;
                                            csxfrm[k + 2] = d5 - d7;
                                            csxfrm[k + 3] = d9 - d10;
                                            csxfrm[k + 4] = d6 - d8;
                                            k += 4;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int m = i + i;
            int i3 = 4;
            do {
                i4 = used + i3;
                int i5 = used + i3 + i3;
                int i6 = m - i3 - i3;
                r3pass(m, i3, i6, used, i4, i5);
                i3 *= 3;
            } while (i3 < i);

            d5 = csxfrm[1];
            d6 = csxfrm[used + 1];
            csxfrm[1] = d5 + d6 + d6;
            csxfrm[used + 1] = d5 - d6;
            d5 = csxfrm[i + 1];
            d6 = csxfrm[m + 2];
            csxfrm[i + 1] = d5 + d6;
            csxfrm[m + 2] = d5 - d6 - d6;

            int n = 3 * i;
            int i1 = i - 1;
            for (k = 1; k <= i1; k++) {

                i4 = i + k;
                int i5 = n - k;
                double d19 = d17 * k;
                double d20 = Math.cos(d19);
                double d21 = Math.sin(d19);
                d5 = d20 * csxfrm[i4 + 2] - d21 * csxfrm[i5 + 2];
                d6 = (d21 * csxfrm[i4 + 2] + d20 * csxfrm[i5 + 2]) * RT3;
                csxfrm[i4 + 2] = csxfrm[k + 1] - d5 - d6;
                csxfrm[i5 + 2] = csxfrm[k + 1] - d5 + d6;
                csxfrm[k + 1] = csxfrm[k + 1] + d5 + d5;
            }

            d5 = csxfrm[m + 1];
            d6 = csxfrm[m + 2];
            for (k = 1; k <= i1; k++) {

                i4 = used + k;
                int i5 = m + k;
                csxfrm[i5] = csxfrm[i4];
                csxfrm[i4] = csxfrm[i5 + 2];
            }
            csxfrm[n] = d5;
            csxfrm[n + 1] = d6;
            i = n;
            used = i + 1;
        }
    }

    private static void r3pass(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        int i = (paramInt2 - 1) / 2;
        int j = paramInt2 * 3;
        double d = 6.283185307179586D / j;
        int k;
        for (k = 1; k <= paramInt1; k += j) {

            double d1 = csxfrm[k + paramInt5] + csxfrm[k + paramInt6];
            double d2 = (csxfrm[k + paramInt5] - csxfrm[k + paramInt6]) * HAFRT3;
            csxfrm[k + paramInt5] = csxfrm[k + paramInt4] - d1 * 0.5D;
            csxfrm[k + paramInt6] = d2;
            csxfrm[k + paramInt4] = csxfrm[k + paramInt4] + d1;
        }

        int m = paramInt2 / 2 + 1;
        for (k = m; k <= paramInt1; k += j) {

            double d1 = (csxfrm[k + paramInt5] + csxfrm[k + paramInt6]) * HAFRT3;
            double d2 = csxfrm[k + paramInt5] - csxfrm[k + paramInt6];
            csxfrm[k + paramInt5] = csxfrm[k + paramInt4] - d2;
            csxfrm[k + paramInt6] = d1;
            csxfrm[k + paramInt4] = csxfrm[k + paramInt4] + d2 * 0.5D;
        }
        for (m = 1; m <= i; m++) {

            int i1 = m + 1;
            int i2 = paramInt2 - m + 1;

            double d1 = d * m;
            double d2 = Math.cos(d1);
            double d3 = Math.sin(d1);
            double d4 = d2 * d2 - d3 * d3;
            double d5 = 2.0D * d3 * d2;

            for (int n = i1; n <= paramInt1; n += j) {

                int i3 = n - i1 + i2;

                double d12 = csxfrm[n + paramInt4];
                double d15 = csxfrm[i3 + paramInt4];
                double d13 = d2 * csxfrm[n + paramInt5] - d3 * csxfrm[i3 + paramInt5];
                double d16 = d3 * csxfrm[n + paramInt5] + d2 * csxfrm[i3 + paramInt5];
                double d14 = d4 * csxfrm[n + paramInt6] - d5 * csxfrm[i3 + paramInt6];
                double d17 = d5 * csxfrm[n + paramInt6] + d4 * csxfrm[i3 + paramInt6];

                double d6 = d13 + d14;
                double d7 = (d13 - d14) * HAFRT3;
                double d8 = d12 - 0.5D * d6;
                double d9 = d16 + d17;
                double d10 = (d16 - d17) * HAFRT3;
                double d11 = d15 - 0.5D * d9;
                csxfrm[n + paramInt4] = d12 + d6;
                csxfrm[i3 + paramInt4] = d8 + d10;
                csxfrm[n + paramInt5] = d8 - d10;
                csxfrm[i3 + paramInt5] = d7 + d11;
                csxfrm[n + paramInt6] = d7 - d11;
                csxfrm[i3 + paramInt6] = d15 + d9;
            }
        }
    }

    public static double integrate(Function paramFunction, double paramDouble1, double paramDouble2, boolean paramBoolean, double paramDouble3, int paramInt) throws NumericalException {
        if (Double.isInfinite(paramDouble1)) {

            if (Double.isInfinite(paramDouble2)) {
                return romberg(paramFunction, paramDouble1, 0.0D, paramDouble3, new ExtendedMidpointNegExpRule()) + romberg(paramFunction, 0.0D, paramDouble2, paramDouble3, new ExtendedMidpointPosExpRule());
            }

            return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointNegExpRule());
        }

        if (Double.isInfinite(paramDouble2)) {
            return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointPosExpRule());
        }

        if (paramBoolean) {
            return romberg(paramFunction, paramDouble1, paramDouble2, paramDouble3, new ExtendedMidpointRule());
        }
        return clenshawCurtis(paramFunction, paramDouble1, paramDouble2, paramDouble3, paramInt);
    }

    private static double polint(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2, int paramInt, double paramDouble) throws NumericalException {
        int i = 1;

        double[] arrayOfDouble1 = new double[paramInt + 1];
        double[] arrayOfDouble2 = new double[paramInt + 1];

        double d1 = Math.abs(paramDouble - paramArrayOfdouble1[1]);
        byte b1;
        for (b1 = 1; b1 <= paramInt; b1++) {
            double d;
            if ((d = Math.abs(paramDouble - paramArrayOfdouble1[b1])) < d1) {
                i = b1;
                d1 = d;
            }
            arrayOfDouble1[b1] = paramArrayOfdouble2[b1];
            arrayOfDouble2[b1] = paramArrayOfdouble2[b1];
        }
        double d2 = paramArrayOfdouble2[i--];
        for (byte b2 = 1; b2 < paramInt; b2++) {

            for (b1 = 1; b1 <= paramInt - b2; b1++) {

                double d4 = paramArrayOfdouble1[b1] - paramDouble;
                double d5 = paramArrayOfdouble1[b1 + b2] - paramDouble;
                double d6 = arrayOfDouble1[b1 + 1] - arrayOfDouble2[b1];
                double d3;
                if ((d3 = d4 - d5) == 0.0D)
                    throw new NumericalException("Cannot interpolate: identical x values");
                d3 = d6 / d3;
                arrayOfDouble2[b1] = d5 * d3;
                arrayOfDouble1[b1] = d4 * d3;
            }
            d2 += dy = (2 * i < paramInt - b2) ? arrayOfDouble1[i + 1] : arrayOfDouble2[i--];
        }
        return d2;
    }

    public static double romberg(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt) throws NumericalException {
        int i = paramInt + 1;

        double[] arrayOfDouble1 = new double[i + 1];
        double[] arrayOfDouble2 = new double[i + 1];
        double[] arrayOfDouble3 = new double[6];
        double[] arrayOfDouble4 = new double[6];

        ExtendedTrapezoidalRule extendedTrapezoidalRule = new ExtendedTrapezoidalRule();
        arrayOfDouble2[1] = 1.0D;
        for (byte b = 1; b <= paramInt; b++) {

            arrayOfDouble1[b] = extendedTrapezoidalRule.getIntegral(paramFunction, paramDouble1, paramDouble2, b);

            if (b >= 5) {

                System.arraycopy(arrayOfDouble2, b - 5 + 1, arrayOfDouble4, 1, 5);
                System.arraycopy(arrayOfDouble1, b - 5 + 1, arrayOfDouble3, 1, 5);
                double d = polint(arrayOfDouble4, arrayOfDouble3, 5, 0.0D);

                if (Math.abs(dy) <= paramDouble3 * Math.abs(d) / 300.0D) return d;
            }
            arrayOfDouble2[b + 1] = 0.25D * arrayOfDouble2[b];
        }

        throw new NumericalException("Required accuracy not achieved in integration.");
    }

    public static double romberg(Function paramFunction, double paramDouble1, double paramDouble2, double paramDouble3, IntegratingFunction paramIntegratingFunction) throws NumericalException {
        double d = 0.0D;
        double[] arrayOfDouble1 = new double[16];
        double[] arrayOfDouble2 = new double[16];
        double[] arrayOfDouble3 = new double[6];
        double[] arrayOfDouble4 = new double[6];

        arrayOfDouble1[1] = 1.0D;

        for (byte b = 1; b <= 14; b++) {

            try {
                arrayOfDouble2[b] = paramIntegratingFunction.getIntegral(paramFunction, paramDouble1, paramDouble2, b);
            } catch (NumericalException numericalException) {
                return d;
            }

            if (b >= 5) {

                System.arraycopy(arrayOfDouble1, b - 5 + 1, arrayOfDouble3, 1, 5);
                System.arraycopy(arrayOfDouble2, b - 5 + 1, arrayOfDouble4, 1, 5);
                d = polint(arrayOfDouble3, arrayOfDouble4, 5, 0.0D);

                if (Math.abs(dy) <= paramDouble3 * Math.abs(d)) return d;
            }
            arrayOfDouble2[b + 1] = arrayOfDouble2[b];
            arrayOfDouble1[b + 1] = arrayOfDouble1[b] / 9.0D;
        }

        throw new NumericalException("Required accuracy not achieved in integration.");
    }

    static class Test {
        static int count;

        public static void main(String[] param1ArrayOfString) {
            double d = 1.0E-6D;

            byte b1 = 9;

            for (byte b2 = 51; b2 <= 55; b2++) {

                Test50 test50 = new Test50(b2);
                try {
                    double d1 = Integration.clenshawCurtis(test50, test50.getA(), test50.getB(), d, 200);
                    System.out.println("F(" + b2 + ") CC = " + Maths.roundSigFigs(d1, b1) + " " + count + " ");
                } catch (NumericalException numericalException) {
                    System.out.println("F(" + b2 + ") CC " + numericalException.getMessage());
                }

                count = 0;
                try {
                    double d1 = Integration.romberg(test50, test50.getA(), test50.getB(), d, 20);
                    System.out.println("F(" + b2 + ") R2 = " + Maths.roundSigFigs(d1, b1) + " " + count + " ");
                } catch (NumericalException numericalException) {
                    System.out.println("F(" + b2 + ") R2 " + numericalException.getMessage());
                }

            }
        }

        static class Test50
                implements Function {
            int i;

            double p = 2.0D;
            double q = 4.0D;
            Beta B = new Beta(this.p, this.q);
            double b = 1.0D;
            double c = 2.0D;
            PowerFunction PF = new PowerFunction(this.b, this.c);

            public Test50(int param2Int) {
                this.i = param2Int;
                Integration.Test.count = 0;
            }

            public double function(double param2Double) {
                Integration.Test.count++;
                switch (this.i) {
                    case 0:
                        return Math.pow(param2Double, -5.0D);
                    case 1:
                        return 1.0D;
                    case 2:
                        return param2Double - 2.0D;
                    case 3:
                        return param2Double * param2Double - 2.0D * param2Double + 3.0D;
                    case 4:
                        return param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D;
                    case 5:
                        return param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D;
                    case 6:
                        return param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D;
                    case 7:
                        return param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D;
                    case 8:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D;
                    case 9:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D;
                    case 10:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D;
                    case 11:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D;
                    case 12:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D;
                    case 13:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D;
                    case 14:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D;
                    case 15:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D;
                    case 16:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D;
                    case 17:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D;
                    case 18:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D;
                    case 19:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D) + 19.0D;
                    case 20:
                        return param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * (param2Double * param2Double * param2Double - 2.0D * param2Double * param2Double + 3.0D * param2Double - 4.0D) + 5.0D) - 6.0D) + 7.0D) - 8.0D) + 9.0D) - 10.0D) + 11.0D) - 12.0D) + 13.0D) - 14.0D) + 15.0D) - 16.0D) + 17.0D) - 18.0D) + 19.0D) - 20.0D;
                    case 21:
                        return Math.exp(param2Double);
                    case 22:
                        return Math.sin(Math.PI * param2Double);
                    case 23:
                        return Math.cos(param2Double);
                    case 24:
                        return param2Double / (Math.exp(param2Double) - 1.0D);
                    case 25:
                        return 1.0D / (1.0D + param2Double * param2Double);
                    case 26:
                        return 2.0D / (2.0D + Math.sin(31.41592653589793D * param2Double));
                    case 27:
                        return 1.0D / (1.0D + param2Double * param2Double * param2Double * param2Double);
                    case 28:
                        return 1.0D / (1.0D + Math.exp(param2Double));
                    case 29:
                        return param2Double * Math.sin(30.0D * param2Double) * Math.cos(param2Double);
                    case 30:
                        return param2Double * Math.sin(30.0D * param2Double) * Math.cos(50.0D * param2Double);
                    case 31:
                        return param2Double * Math.sin(30.0D * param2Double) / Math.sqrt(1.0D - param2Double * param2Double / 39.47841760435743D);
                    case 32:
                        return 0.46D * (Math.exp(param2Double) + Math.exp(-param2Double)) - Math.cos(param2Double);
                    case 33:
                        return 1.0D / (param2Double * param2Double * param2Double * param2Double + param2Double * param2Double + 0.9D);
                    case 34:
                        return Math.sqrt(98696.04401089359D - param2Double * param2Double) * Math.sin(param2Double);
                    case 35:
                        return 1.0D / (1.0D + param2Double);
                    case 36:
                        return Math.sqrt(param2Double);
                    case 37:
                        return Math.pow(param2Double, 0.25D);
                    case 38:
                        return Math.pow(param2Double, 0.125D);
                    case 39:
                        return Math.pow(param2Double, 0.0625D);
                    case 40:
                        return Math.sqrt(Math.abs(param2Double * param2Double - 0.25D));
                    case 41:
                        return Math.pow(param2Double, 1.5D);
                    case 42:
                        return Math.pow(Math.abs(param2Double * param2Double - 0.25D), 1.5D);
                    case 43:
                        return Math.pow(param2Double, 2.5D);
                    case 44:
                        return Math.pow(Math.abs(param2Double * param2Double - 0.25D), 2.5D);
                    case 46:
                        if (param2Double >= 0.0D && param2Double <= 0.333D) return param2Double;
                        if (param2Double > 0.333D && param2Double <= 0.667D) return param2Double + 1.0D;
                        if (param2Double > 0.667D && param2Double <= 1.0D) return param2Double + 2.0D;
                    case 47:
                        if (param2Double > 0.49D && param2Double < 0.5D) return 0.0D;
                        return -1000.0D * (param2Double * param2Double - param2Double);
                    case 48:
                        if (param2Double >= 0.0D && param2Double <= 0.7182818284590451D)
                            return 1.0D / (param2Double + 2.0D);
                        if (param2Double > 0.7182818284590451D && param2Double <= 1.0D) return 0.0D;
                    case 49:
                        return 10000.0D * (param2Double - 0.1D) * (param2Double - 0.11D) * (param2Double - 0.12D) * (param2Double - 0.13D);
                    case 50:
                        return Math.sin(314.1592653589793D * param2Double);
                    case 51:
                        return Math.sqrt(1.0D - param2Double * param2Double);
                    case 52:
                        return this.B.pdf(param2Double);
                    case 53:
                        return param2Double * this.B.pdf(param2Double);
                    case 54:
                        return this.PF.pdf(param2Double);
                    case 55:
                        return param2Double * this.PF.pdf(param2Double);
                }

                return 0.0D;
            }

            public double getA() {
                if (this.i == 32 || this.i == 33) return -1.0D;
                if (this.i == 0) return 0.01D;
                return 0.0D;
            }

            public double getB() {
                if (this.i >= 29 && this.i <= 31) return 6.283185307179586D;
                if (this.i == 34) return 314.1592653589793D;
                if (this.i == 0) return 1.1D;
                if (this.i == 54 || this.i == 55) return this.b;
                return 1.0D;
            }
        }

        static class Func1 implements Function {
            public double function(double param2Double) {
                return Math.sqrt(1.0D - param2Double * param2Double);
            }
        }

        static class Func2 implements Function {
            public double function(double param2Double) {
                double d = 1.0D;
                if (param2Double < 0.0D) throw new IllegalArgumentException("Invalid variate-value.");
                return Math.exp(-param2Double / d) / d;
            }
        }

        static class Func3 implements Function {
            ChiSquared D = new ChiSquared(4.0D);

            public double function(double param2Double) {
                return this.D.pdf(param2Double);
            }
        }

        static class Func4 implements Function {
            Normal D = new Normal(1.0D, 1.0D);

            public double function(double param2Double) {
                return this.D.pdf(param2Double);
            }
        }
    }
}

