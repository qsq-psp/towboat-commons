package mujica.algebra.random;

import mujica.reflect.modifier.CodeHistory;

import java.util.function.IntToLongFunction;

@CodeHistory(date = "2018/11/28", project = "mmc", name = "DiscreteRandom")
@CodeHistory(date = "2020/7/20", project = "va")
@CodeHistory(date = "2022/4/2")
@FunctionalInterface
public interface RandomSource extends IntToLongFunction, Cloneable {

    long applyAsLong(int bitCount);
}
