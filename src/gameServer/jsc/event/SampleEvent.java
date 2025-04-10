package jsc.event;

public class SampleEvent
extends StatisticEvent
{
protected double[] sample;

public SampleEvent(Object paramObject, double[] paramArrayOfdouble, double paramDouble) {
super(paramObject, paramDouble);
this.sample = paramArrayOfdouble;
}

public double[] getSample() {
return this.sample;
}
}

