package jsc.swt.plot;

public interface AxisModel extends Cloneable {
  Object clone();
  
  double getLength();
  
  double getMin();
  
  double getMax();
  
  int getTickCount();
  
  double getFirstTickValue();
  
  double getLastTickValue();
  
  String getTickLabel(int paramInt);
  
  double getTickValue(int paramInt);
  
  String getLabel();
  
  void setLabel(String paramString);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/plot/AxisModel.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */