package jsc.ci;

public interface ConfidenceBand {
  double getConfidenceCoeff();

  void setConfidenceCoeff(double paramDouble);

  int getN();

  double getLowerLimit(int paramInt);

  double getUpperLimit(int paramInt);

  double getX(int paramInt);
}

