package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;


import java.util.function.BiFunction;

/**
 * @param <R> the type of the cumulated
 * @param <U> the type of the incremental
 */
@CodeHistory(date = "2023/4/2", project = "Ultramarine")
@CodeHistory(date = "2025/3/6")
@FunctionalInterface
public interface ReduceFunction<R, U> extends BiFunction<R, U, R> {

    @Override
    R apply(R r, U u);
}
