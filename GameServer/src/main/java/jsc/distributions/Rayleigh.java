package jsc.distributions;

public class Rayleigh
        extends Weibull {
    public Rayleigh(double paramDouble) {
        super(paramDouble * Math.sqrt(2.0D), 2.0D);
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            double d = 1.0D;
            double[] arrayOfDouble = {0.1D, 0.25D, 0.5D, 0.75D, 1.0D, 2.0D, 3.0D, 10.0D};

            Rayleigh rayleigh = new Rayleigh(d);
            System.out.println("Rayleigh distribution: b = " + d);
            byte b;
            for (b = 0; b < arrayOfDouble.length; b++) {
                System.out.println("X=" + arrayOfDouble[b] + " pdf=" + rayleigh.pdf(arrayOfDouble[b]) + " cdf=" + rayleigh.cdf(arrayOfDouble[b]) + " X=" + rayleigh.inverseCdf(rayleigh.cdf(arrayOfDouble[b])));
            }

            System.out.println("Random numbers");
            for (b = 0; b <= 10; ) {
                System.out.println(rayleigh.random());
                b++;
            }

        }
    }
}

