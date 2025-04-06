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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/distributions/Distribution.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */