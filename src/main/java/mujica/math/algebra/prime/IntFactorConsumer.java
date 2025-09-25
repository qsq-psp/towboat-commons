package mujica.math.algebra.prime;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/3/18")
@FunctionalInterface
public interface IntFactorConsumer {

    void accept(int factor, int times);
}
