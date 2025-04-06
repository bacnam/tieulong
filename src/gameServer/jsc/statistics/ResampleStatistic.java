package jsc.statistics;

public interface ResampleStatistic extends Statistic {
  double recalculateStatistic(double[] paramArrayOfdouble);
  
  double[] getSample();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/statistics/ResampleStatistic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */