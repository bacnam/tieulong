package jsc.event;

import java.util.EventObject;

public class StatisticEvent
extends EventObject
{
protected double statistic;

public StatisticEvent(Object paramObject, double paramDouble) {
super(paramObject);
setStatistic(paramDouble);
}

public double getStatistic() {
return this.statistic;
}

public void setStatistic(double paramDouble) {
this.statistic = paramDouble;
}
}

