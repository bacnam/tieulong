package com.mchange.util;

public interface IntChecklist {
  void check(int paramInt);

  void uncheck(int paramInt);

  boolean isChecked(int paramInt);

  void clear();

  int countChecked();

  int[] getChecked();

  IntEnumeration checked();
}

