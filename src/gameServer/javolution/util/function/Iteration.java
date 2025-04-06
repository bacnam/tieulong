package javolution.util.function;

import java.util.Iterator;

public interface Iteration<E> {
  void run(Iterator<E> paramIterator);
  
  public static interface Sequential<E> extends Iteration<E> {}
  
  public static interface Mutable<E> extends Iteration<E> {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/function/Iteration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */