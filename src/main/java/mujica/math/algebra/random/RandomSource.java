package mujica.math.algebra.random;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2018/11/28", project = "mmc", name = "DiscreteRandom")
@CodeHistory(date = "2020/7/20", project = "va")
@CodeHistory(date = "2022/4/2")
public interface RandomSource extends Cloneable {

    @NotNull
    RandomSource clone() throws CloneNotSupportedException;

    long next(int bitCount);
}
