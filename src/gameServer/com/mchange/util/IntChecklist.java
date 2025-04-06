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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/IntChecklist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */