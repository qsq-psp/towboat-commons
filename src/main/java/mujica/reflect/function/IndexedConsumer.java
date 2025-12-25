package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Index;

@CodeHistory(date = "2025/3/8")
@FunctionalInterface
public interface IndexedConsumer<C, T> {

    void accept(T item, @Index(of = "container") int index, C container);
}
