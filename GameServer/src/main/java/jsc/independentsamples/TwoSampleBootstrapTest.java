package jsc.independentsamples;

import jsc.distributions.Tail;
import jsc.event.StatisticListener;
import jsc.tests.BootstrapTest;

public abstract class TwoSampleBootstrapTest
extends BootstrapTest
{
protected final int N;
protected final int nA;
protected final int nB;
protected TwoSampleStatistic t;

public TwoSampleBootstrapTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail) {
this(paramTwoSampleStatistic, paramTail, null);
}

public TwoSampleBootstrapTest(TwoSampleStatistic paramTwoSampleStatistic, Tail paramTail, StatisticListener paramStatisticListener) {
super(paramTwoSampleStatistic.getStatistic(), paramTail, paramStatisticListener);

this.nA = paramTwoSampleStatistic.sizeA();
this.nB = paramTwoSampleStatistic.sizeB();
this.N = this.nA + this.nB;
this.t = paramTwoSampleStatistic;
}

public int getN() {
return this.N;
}
}

