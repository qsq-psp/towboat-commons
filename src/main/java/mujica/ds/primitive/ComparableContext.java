package mujica.ds.primitive;

import mujica.reflect.modifier.Assigned;
import mujica.reflect.modifier.Index;

/**
 * Created on 2025/6/14.
 */
public interface ComparableContext extends OperationContext {

    int hashCode(@Assigned int srcVariable);

    int compare(@Assigned int variableA, @Assigned int variableB);

    void swapInVector(@Assigned int vector, @Index int indexA, @Index int indexB);

    boolean compareAndSwapInVector(@Assigned int vector, @Index int indexA, @Index int indexB);
}
