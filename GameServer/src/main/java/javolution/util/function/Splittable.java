package javolution.util.function;

public interface Splittable<T> {
    void perform(Consumer<T> paramConsumer, T paramT);

    T[] split(int paramInt);

    void update(Consumer<T> paramConsumer, T paramT);
}

