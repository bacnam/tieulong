package javolution.util.function;

import java.util.Comparator;

public interface Equality<T> extends Comparator<T> {
  int hashCodeOf(T paramT);
  
  boolean areEqual(T paramT1, T paramT2);
  
  int compare(T paramT1, T paramT2);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/function/Equality.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */