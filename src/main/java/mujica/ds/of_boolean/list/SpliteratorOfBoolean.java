package mujica.ds.of_boolean.list;

import mujica.reflect.function.BooleanConsumer;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Created on 2026/3/26.
 */
public interface SpliteratorOfBoolean extends Spliterator.OfPrimitive<Boolean, BooleanConsumer, SpliteratorOfBoolean> {

    @Override
    SpliteratorOfBoolean trySplit();

    @Override
    boolean tryAdvance(BooleanConsumer action);

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    default void forEachRemaining(BooleanConsumer action) {
        do { } while (tryAdvance(action));
    }

    @Override
    default boolean tryAdvance(Consumer<? super Boolean> action) {
        if (action instanceof BooleanConsumer) {
            return tryAdvance((BooleanConsumer) action);
        } else {
            return tryAdvance((BooleanConsumer) action::accept);
        }
    }

    @Override
    default void forEachRemaining(Consumer<? super Boolean> action) {
        if (action instanceof BooleanConsumer) {
            forEachRemaining((BooleanConsumer) action);
        } else {
            forEachRemaining((BooleanConsumer) action::accept);
        }
    }
}
