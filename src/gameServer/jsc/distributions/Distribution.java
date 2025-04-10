package jsc.distributions;

public interface Distribution {
  double cdf(double paramDouble);

  double inverseCdf(double paramDouble);

  boolean isDiscrete();

  double mean();

  double pdf(double paramDouble);

  double random();

  void setSeed(long paramLong);

  double variance();
}

