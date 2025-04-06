package jsc.numerical;

public interface IntegratingFunction {
  double getIntegral(Function paramFunction, double paramDouble1, double paramDouble2, int paramInt) throws NumericalException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/numerical/IntegratingFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */