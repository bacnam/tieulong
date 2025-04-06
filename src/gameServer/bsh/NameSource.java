package bsh;

public interface NameSource {
  String[] getAllNames();
  
  void addNameSourceListener(Listener paramListener);
  
  public static interface Listener {
    void nameSourceChanged(NameSource param1NameSource);
  }
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/NameSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */