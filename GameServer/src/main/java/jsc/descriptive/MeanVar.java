package jsc.descriptive;

public class MeanVar
        implements Cloneable {
    private int n;
    private double mean;
    private double variance;

    public MeanVar(double[] paramArrayOfdouble) {
        double d = 0.0D;
        this.n = paramArrayOfdouble.length;
        if (this.n < 2)
            throw new IllegalArgumentException("Less than two data values.");
        byte b;
        for (b = 0; b < this.n; ) {
            d += paramArrayOfdouble[b];
            b++;
        }
        this.mean = d / this.n;
        d = 0.0D;
        for (b = 0; b < this.n; ) {
            d += (paramArrayOfdouble[b] - this.mean) * (paramArrayOfdouble[b] - this.mean);
            b++;
        }
        this.variance = d / (this.n - 1);
    }

    public MeanVar(double paramDouble) {
        this.n = 1;
        this.mean = paramDouble;
        this.variance = 0.0D;
    }

    public void addValue(double paramDouble) {
        double d = this.mean;
        this.mean = d + (paramDouble - d) / (this.n + 1.0D);
        this.variance = (1.0D - 1.0D / this.n) * this.variance + (this.mean - d) * (this.mean - d) * (this.n + 1.0D);
        this.n++;
    }

    public Object clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException cloneNotSupportedException) {
            System.out.println("MeanVar can't clone");
        }
        return object;
    }

    public double getMean() {
        return this.mean;
    }

    public int getN() {
        return this.n;
    }

    public double getSd() {
        return Math.sqrt(this.variance);
    }

    public double getVariance() {
        return this.variance;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double[] arrayOfDouble1 = {73.0D, 62.7D, 59.3D, 68.2D};
            MeanVar meanVar1 = new MeanVar(arrayOfDouble1);
            System.out.println("n = " + meanVar1.getN());
            System.out.println("Mean = " + meanVar1.getMean());
            System.out.println("Variance = " + meanVar1.getVariance());
            System.out.println("s.d. = " + meanVar1.getSd());

            MeanVar meanVar2 = new MeanVar(73.0D);
            meanVar2.addValue(62.7D);
            meanVar2.addValue(59.3D);
            meanVar2.addValue(68.2D);
            System.out.println("n = " + meanVar2.getN());
            System.out.println("Mean = " + meanVar2.getMean());
            System.out.println("Variance = " + meanVar2.getVariance());
            System.out.println("s.d. = " + meanVar2.getSd());

            double[] arrayOfDouble2 = {7.000001D, 7.000002D, 7.000003D, 7.000004D, 7.000005D};
            MeanVar meanVar3 = new MeanVar(arrayOfDouble2);
            System.out.println("n = " + meanVar3.getN());
            System.out.println("Mean = " + meanVar3.getMean());
            System.out.println("Variance = " + meanVar3.getVariance());
            System.out.println("s.d. = " + meanVar3.getSd());

            MeanVar meanVar4 = new MeanVar(7.000001D);
            meanVar4.addValue(7.000002D);
            meanVar4.addValue(7.000003D);
            meanVar4.addValue(7.000004D);
            meanVar4.addValue(7.000005D);
            System.out.println("n = " + meanVar4.getN());
            System.out.println("Mean = " + meanVar4.getMean());
            System.out.println("Variance = " + meanVar4.getVariance());
            System.out.println("s.d. = " + meanVar4.getSd());
        }
    }
}

