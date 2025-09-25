package mujica.math.algebra.random;

import mujica.reflect.function.StateStack;
import mujica.reflect.modifier.CodeHistory;

import java.util.NoSuchElementException;

@CodeHistory(date = "2022/5/8")
public interface StackingRandomSource extends StateStack, RandomSource {

    @Override
    void save();

    @Override
    void restore() throws NoSuchElementException;

    @Override
    void reset() throws NoSuchElementException;

    @Override
    int next(int bitCount);
}
