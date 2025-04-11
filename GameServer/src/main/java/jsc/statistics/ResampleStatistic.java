package jsc.statistics;

public interface ResampleStatistic extends Statistic {
    double recalculateStatistic(double[] paramArrayOfdouble);

    double[] getSample();
}

