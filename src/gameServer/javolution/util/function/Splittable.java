package javolution.util.function;

public interface Splittable<T> {
  void perform(Consumer<T> paramConsumer, T paramT);
  
  T[] split(int paramInt);
  
  void update(Consumer<T> paramConsumer, T paramT);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/function/Splittable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */