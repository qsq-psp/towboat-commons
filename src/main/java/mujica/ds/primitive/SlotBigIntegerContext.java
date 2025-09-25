package mujica.ds.primitive;

import mujica.reflect.modifier.CodeHistory;

@CodeHistory(date = "2025/7/3")
public class SlotBigIntegerContext implements OperationContext {

    @Override
    public int newVariable() {
        return 0;
    }

    @Override
    public int newVector(int length) {
        return 0;
    }

    @Override
    public int newMatrix(int rowCount, int columnCount) {
        return 0;
    }

    @Override
    public void deleteVariable(int pointer) {

    }

    @Override
    public void deleteVector(int pointer) {

    }

    @Override
    public void deleteMatrix(int pointer) {

    }

    @Override
    public void move(int srcVariable, int dstVariable) {

    }

    @Override
    public void loadFromArray(int srcVector, int srcIndex, int dstVariable) {

    }

    @Override
    public void storeIntoArray(int srcVariable, int dstVector, int dstIndex) {

    }

    @Override
    public void loadFromMatrix(int srcMatrix, int srcRow, int srcColumn, int dstVariable) {

    }

    @Override
    public void storeIntoMatrix(int srcVariable, int dstMatrix, int dstRow, int dstColumn) {

    }
}
