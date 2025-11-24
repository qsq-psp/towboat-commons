package mujica.reflect.function;

/**
 * Created on 2025/11/22.
 */
public interface WildIterator<E> {
    
    boolean hasNext() throws Throwable;
    
    E next() throws Throwable;

    @SuppressWarnings("RedundantThrows")
    default void remove() throws Throwable {
        throw new UnsupportedOperationException();
    }
}
