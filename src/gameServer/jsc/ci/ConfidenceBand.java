package jsc.ci;

public interface ConfidenceBand {
  double getConfidenceCoeff();
  
  void setConfidenceCoeff(double paramDouble);
  
  int getN();
  
  double getLowerLimit(int paramInt);
  
  double getUpperLimit(int paramInt);
  
  double getX(int paramInt);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/ci/ConfidenceBand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */