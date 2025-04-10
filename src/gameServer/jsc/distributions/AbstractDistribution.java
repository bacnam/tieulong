package jsc.distributions;

import java.util.Random;

public abstract class AbstractDistribution
implements Distribution
{
protected Random rand = new Random();

public abstract double cdf(double paramDouble);

public abstract double inverseCdf(double paramDouble);

public boolean isDiscrete() {
return false;
}

public abstract double mean();

public abstract double pdf(double paramDouble);

public double random() {
return inverseCdf(this.rand.nextDouble());
}

public double sd() {
return Math.sqrt(variance());
}

public void setSeed(long paramLong) {
this.rand.setSeed(paramLong);
}

public abstract double variance();
}

