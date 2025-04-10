package jsc.tests;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import jsc.distributions.Tail;
import jsc.event.StatisticEvent;
import jsc.event.StatisticListener;

public abstract class ResampleTest
implements SignificanceTest
{
private long critCount = 0L;
private long totalRepCount = 0L;
private final double tObs;
private double SP = 0.0D;

private final Tail tail;

protected Random rand;

protected Set statisticListeners;

public ResampleTest(double paramDouble, Tail paramTail, StatisticListener paramStatisticListener) {
this.tail = paramTail;
this.tObs = paramDouble;
this.rand = new Random();

this.statisticListeners = new HashSet(1);
if (paramStatisticListener != null) addStatisticListener(paramStatisticListener);

}

protected abstract double bootstrapSample();

public double calculateSP(int paramInt) {
for (byte b = 0; b < paramInt; b++) {

double d = bootstrapSample();

if (!this.statisticListeners.isEmpty()) {
fireStatisticEvent(new StatisticEvent(this, d));
}
if (this.tail == Tail.UPPER)
{ if (d >= this.tObs) this.critCount++;  }
else if (this.tail == Tail.LOWER)
{ if (d <= this.tObs) this.critCount++;
}
else if (Math.abs(d) >= Math.abs(this.tObs)) { this.critCount++; }

this.totalRepCount++;
} 

this.SP = this.critCount / this.totalRepCount;
return this.SP;
}

public double getSP() {
return this.critCount / this.totalRepCount;
}

public double getTestStatistic() {
return this.tObs;
}

public long getTotalRepCount() {
return this.totalRepCount;
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
}

public void addStatisticListener(StatisticListener paramStatisticListener) {
this.statisticListeners.add(paramStatisticListener);
}

public void removeStatisticListener(StatisticListener paramStatisticListener) {
this.statisticListeners.remove(paramStatisticListener);
}

private void fireStatisticEvent(StatisticEvent paramStatisticEvent) {
Iterator iterator = this.statisticListeners.iterator();
while (iterator.hasNext()) {

StatisticListener statisticListener = iterator.next();
statisticListener.statisticCreated(paramStatisticEvent);
} 
}
}

