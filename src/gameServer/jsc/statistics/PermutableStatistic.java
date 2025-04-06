package jsc.statistics;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.Selection;

public interface PermutableStatistic extends Statistic {
  Enumerator getEnumerator();
  
  double permuteStatistic(Selection paramSelection);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/statistics/PermutableStatistic.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */