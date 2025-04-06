package jsc.descriptive;

public interface DoubleFrequencyTable {
  int addValue(double paramDouble);
  
  int addValues(double[] paramArrayOfdouble);
  
  double getBoundary(int paramInt);
  
  void clearData();
  
  int getCumulativeFrequency(int paramInt);
  
  double getCumulativeProportion(int paramInt);
  
  int[] getFrequencies();
  
  int getFrequency(int paramInt);
  
  int getMaximumFreq();
  
  double getMaximumProportion();
  
  int getNumberOfBins();
  
  int getN();
  
  double getProportion(int paramInt);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/descriptive/DoubleFrequencyTable.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */