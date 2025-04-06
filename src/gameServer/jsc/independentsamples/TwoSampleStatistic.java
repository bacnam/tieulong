package jsc.independentsamples;

import jsc.statistics.Statistic;

public interface TwoSampleStatistic extends Statistic {
  double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2);
  
  double[] getSampleA();
  
  double[] getSampleB();
  
  int sizeA();
  
  int sizeB();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/independentsamples/TwoSampleStatistic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */