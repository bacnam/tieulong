package jsc.numerical;

public interface IntegratingFunction {
    double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) throws NumericalException;
}

