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

