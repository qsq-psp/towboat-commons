package mujica.ds.primitive;

import mujica.reflect.modifier.Assigned;
import mujica.reflect.modifier.Index;

/**
 * Created on 2025/6/13.
 */
public interface OperationContext {

    @Assigned
    int newVariable();

    @Assigned
    int newVector(int length);

    @Assigned
    int newMatrix(int rowCount, int columnCount);

    void deleteVariable(@Assigned int pointer);

    void deleteVector(@Assigned int pointer);

    void deleteMatrix(@Assigned int pointer);

    void move(@Assigned int srcVariable, @Assigned int dstVariable);

    void loadFromArray(@Assigned int srcVector, @Index int srcIndex, @Assigned int dstVariable);

    void storeIntoArray(@Assigned int srcVariable, @Assigned int dstVector, @Index int dstIndex);

    void loadFromMatrix(@Assigned int srcMatrix, @Index int srcRow, @Index int srcColumn, @Assigned int dstVariable);

    void storeIntoMatrix(@Assigned int srcVariable, @Assigned int dstMatrix, @Index int dstRow, @Index int dstColumn);
}
