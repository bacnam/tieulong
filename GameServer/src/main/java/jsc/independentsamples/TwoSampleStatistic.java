package jsc.independentsamples;

import jsc.statistics.Statistic;

public interface TwoSampleStatistic extends Statistic {
  double resampleStatistic(double[] paramArrayOfdouble1, double[] paramArrayOfdouble2);

  double[] getSampleA();

  double[] getSampleB();

  int sizeA();

  int sizeB();
}

