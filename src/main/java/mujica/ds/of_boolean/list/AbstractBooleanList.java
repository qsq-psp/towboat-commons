package mujica.ds.of_boolean.list;

import mujica.reflect.function.BooleanConsumer;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Created on 2026/3/12.
 */
@CodeHistory(date = "2026/3/12")
public abstract class AbstractBooleanList implements BitList {

    protected AbstractBooleanList() {
        super();
    }

    @NotNull
    @Override
    public AbstractBooleanList duplicate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkHealth(@NotNull Consumer<RuntimeException> consumer) {
        // pass; always healthy
    }

    @Override
    public boolean isEmpty() {
        return bitLength() == 0;
    }

    @Override
    public boolean isFull() {
        return false; // never full
    }

    @Override
    public void forEach(@NotNull BooleanConsumer action) {
        final int n = bitLength();
        for (int i = 0; i < n; i++) {
            action.accept(getBit(i));
        }
    }
}
