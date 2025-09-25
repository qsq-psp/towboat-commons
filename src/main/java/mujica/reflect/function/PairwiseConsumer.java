package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.ReferencePage;

import java.util.function.BiConsumer;

@CodeHistory(date = "2025/3/21")
@FunctionalInterface
@ReferencePage(title = "itertools", href = "https://docs.python.org/3/library/itertools.html#itertools.pairwise")
public interface PairwiseConsumer<T> extends BiConsumer<T, T> {

    @Override
    void accept(T previous, T current);
}
