package jsc.statistics;

import jsc.combinatorics.Enumerator;
import jsc.combinatorics.Selection;

public interface PermutableStatistic extends Statistic {
    Enumerator getEnumerator();

    double permuteStatistic(Selection paramSelection);
}

