package jsc.combinatorics;

public interface Enumerator {
  double countSelections();
  
  boolean hasNext();
  
  Selection nextSelection();
  
  Selection randomSelection();
  
  void reset();
  
  void setSeed(long paramLong);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/combinatorics/Enumerator.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */