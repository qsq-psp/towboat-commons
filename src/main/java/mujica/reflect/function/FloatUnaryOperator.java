package mujica.reflect.function;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/11/22")
@FunctionalInterface
public interface FloatUnaryOperator {

    float applyAsFloat(float operand);
}
