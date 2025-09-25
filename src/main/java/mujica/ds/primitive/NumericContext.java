package mujica.ds.primitive;

import mujica.reflect.modifier.Assigned;
import mujica.reflect.modifier.CodeHistory;

/**
 * Created on 2025/6/14.
 */
@CodeHistory(date = "2025/6/11", name = "NumberStorage")
@CodeHistory(date = "2025/6/14")
public interface NumericContext extends ComparableContext {

    void zero(@Assigned int dstVariable);

    void one(@Assigned int dstVariable);

    void two(@Assigned int dstVariable);

    int sign(@Assigned int srcVariable);

    void negate(@Assigned int srcVariable, @Assigned int dstVariable);

    void abs(@Assigned int srcVariable, @Assigned int dstVariable);

    void add(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    void subtract(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    void multiply(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    void divideExact(@Assigned int dividendVariable, @Assigned int divisorVariable, @Assigned int quotientVariable);

    void divideAndRemainder(@Assigned int dividendVariable, @Assigned int divisorVariable, @Assigned int quotientVariable, @Assigned int remainderVariable);

    void staticShift(int distance, @Assigned int srcVariable, @Assigned int dstVariable);

    void dynamicShift(@Assigned int distanceVariable, @Assigned int srcVariable, @Assigned int dstVariable);
}
