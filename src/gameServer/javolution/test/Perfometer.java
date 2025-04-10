package javolution.test;

import javolution.context.LogContext;
import javolution.lang.Configurable;
import javolution.lang.MathLib;
import javolution.text.TextBuilder;
import javolution.util.FastTable;

public abstract class Perfometer<T>
{
public static final Configurable<Integer> DURATION_MS = new Configurable<Integer>()
{
public String getName()
{
return getClass().getEnclosingClass().getName() + "#DURATION_MS";
}

protected Integer getDefault() {
return Integer.valueOf(1000);
}
};

public static final Configurable<Boolean> SKIP = new Configurable<Boolean>()
{
public String getName()
{
return getClass().getEnclosingClass().getName() + "#SKIP";
}

protected Boolean getDefault() {
return Boolean.valueOf(false);
}
};

private final String description;

private T input;

private long[] times;

public Perfometer(String description) {
this.description = description;
}

public double getAvgTimeInSeconds() {
if (this.times == null) return Double.NaN; 
long sum = 0L;
for (long time : this.times) {
sum += time;
}
return sum / 1.0E9D / this.times.length;
}

public String getDescription() {
return this.description;
}

public T getInput() {
return this.input;
}

public int getNbrOfIterations() {
return (this.times != null) ? this.times.length : 0;
}

public double[] getTimesInSeconds() {
if (this.times == null) return new double[0]; 
double[] timesSec = new double[this.times.length];
for (int i = 0; i < this.times.length; i++) {
timesSec[i] = this.times[i] / 1.0E9D;
}
return timesSec;
}

public Perfometer<T> measure(T input, int nbrOfIterations) {
if (((Boolean)SKIP.get()).booleanValue()) return this; 
this.input = input;
this.times = new long[nbrOfIterations];
long[] calibrations = longArray(nbrOfIterations, Long.MAX_VALUE);
long[] measures = longArray(nbrOfIterations, Long.MAX_VALUE);
try {
long exitTime = System.currentTimeMillis() + ((Integer)DURATION_MS.get()).intValue();

while (true)
{ initialize(); int i;
for (i = 0; i < nbrOfIterations; i++) {
long start = System.nanoTime();
run(false);
long time = System.nanoTime() - start;
calibrations[i] = MathLib.min(calibrations[i], time);
} 

initialize();
for (i = 0; i < nbrOfIterations; i++) {
long start = System.nanoTime();
run(true);
long time = System.nanoTime() - start;
measures[i] = MathLib.min(measures[i], time);
} 
if (System.currentTimeMillis() >= exitTime)
{ for (i = 0; i < nbrOfIterations; i++) {
this.times[i] = measures[i] - calibrations[i];
}
return this; }  } 
} catch (Exception error) {
throw new RuntimeException("Perfometer Exception", error);
} 
}

public void print() {
if (((Boolean)SKIP.get()).booleanValue())
return;  TextBuilder txt = new TextBuilder();
txt.append(this.description).append(" (").append(getNbrOfIterations()).append(") for ").append(this.input).append(": ");

while (txt.length() < 80)
txt.append(' '); 
txt.append(getAvgTimeInSeconds() * 1.0E9D, 8, false, true);
txt.append(" ns (avg), ");
txt.append(getWCETinSeconds() * 1.0E9D, 8, false, true);
txt.append(" ns (wcet#").append(getWorstCaseNumber()).append(")");
LogContext.info(new Object[] { txt });
}

public void printDetails() {
if (((Boolean)SKIP.get()).booleanValue())
return;  FastTable<Long> measurements = new FastTable();
for (long time : this.times)
measurements.add(Long.valueOf(time)); 
LogContext.debug(new Object[] { measurements });
}

public double getWCETinSeconds() {
if (this.times == null) return Double.NaN; 
long wcet = 0L;
for (long time : this.times) {
if (time > wcet) wcet = time; 
} 
return wcet / 1.0E9D;
}

public int getWorstCaseNumber() {
if (this.times == null) return -1; 
long wcet = 0L;
int j = -1;
for (int i = 0; i < this.times.length; i++) {
if (this.times[i] > wcet) {
wcet = this.times[i];
j = i;
} 
} 
return j;
}

protected abstract void initialize() throws Exception;

protected abstract void run(boolean paramBoolean) throws Exception;

protected void validate() {}

private long[] longArray(int length, long initialValue) {
long[] array = new long[length];
for (int i = 0; i < length; i++)
array[i] = initialValue; 
return array;
}
}

