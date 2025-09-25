package mujica.ds.primitive;

import mujica.ds.generic.list.SlotList;
import mujica.ds.of_long.PublicLongSlot;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2025/6/23")
public class SlotLongContext implements BitwiseContext {

    @NotNull
    final SlotList<Object> memory;

    public SlotLongContext(@NotNull SlotList<Object> memory) {
        super();
        this.memory = memory;
    }

    @Override
    public int newVariable() {
        return memory.store(new PublicLongSlot());
    }

    @Override
    public int newVector(int length) {
        return memory.store(new long[length]);
    }

    @Override
    public int newMatrix(int rowCount, int columnCount) {
        return memory.store(new long[rowCount][columnCount]);
    }

    @Override
    public void deleteVariable(int pointer) {
        final Object o = memory.delete(pointer);
        assert o instanceof PublicLongSlot;
    }

    @Override
    public void deleteVector(int pointer) {
        final Object o = memory.delete(pointer);
        assert o instanceof long[];
    }

    @Override
    public void deleteMatrix(int pointer) {
        final Object o = memory.delete(pointer);
        assert o instanceof long[][];
    }

    @Override
    public void move(int srcVariable, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = ((PublicLongSlot) memory.load(srcVariable)).value;
    }

    @Override
    public void loadFromArray(int srcVector, int srcIndex, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = ((long[]) memory.load(srcVector))[srcIndex];
    }

    @Override
    public void storeIntoArray(int srcVariable, int dstVector, int dstIndex) {
        ((long[]) memory.load(dstVector))[dstIndex] = ((PublicLongSlot) memory.load(srcVariable)).value;
    }

    @Override
    public void loadFromMatrix(int srcMatrix, int srcRow, int srcColumn, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = ((long[][]) memory.load(srcMatrix))[srcRow][srcColumn];
    }

    @Override
    public void storeIntoMatrix(int srcVariable, int dstMatrix, int dstRow, int dstColumn) {
        ((long[][]) memory.load(dstMatrix))[dstRow][dstColumn] = ((PublicLongSlot) memory.load(srcVariable)).value;
    }

    @Override
    public int hashCode(int srcVariable) {
        return Long.hashCode(((PublicLongSlot) memory.load(srcVariable)).value);
    }

    @Override
    public int compare(int variableA, int variableB) {
        return Long.compare(
                ((PublicLongSlot) memory.load(variableA)).value,
                ((PublicLongSlot) memory.load(variableB)).value
        );
    }

    @Override
    public void swapInVector(int vector, int indexA, int indexB) {
        final long[] array = (long[]) memory.load(vector);
        long value = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = value;
    }

    @Override
    public boolean compareAndSwapInVector(int vector, int indexA, int indexB) {
        final long[] array = (long[]) memory.load(vector);
        long valueA = array[indexA];
        long valueB = array[indexB];
        if (valueA > valueB) {
            array[indexA] = valueB;
            array[indexB] = valueA;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void zero(int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = 0;
    }

    @Override
    public void one(int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = 1;
    }

    @Override
    public void two(int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = 2;
    }

    @Override
    public int sign(int srcVariable) {
        return Long.signum(((PublicLongSlot) memory.load(srcVariable)).value);
    }

    @Override
    public void negate(int srcVariable, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = -((PublicLongSlot) memory.load(srcVariable)).value;
    }

    @Override
    public void abs(int srcVariable, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = Math.abs(((PublicLongSlot) memory.load(srcVariable)).value);
    }

    @Override
    public void add(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        + ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public void subtract(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        - ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public void multiply(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        * ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public void divideExact(int dividendVariable, int divisorVariable, int quotientVariable) {
        final long dividend = ((PublicLongSlot) memory.load(dividendVariable)).value;
        final long divisor = ((PublicLongSlot) memory.load(divisorVariable)).value;
        final long quotient = dividend / divisor;
        if (dividend == divisor * quotient) {
            ((PublicLongSlot) memory.load(quotientVariable)).value = quotient;
        } else {
            throw new ArithmeticException("can not divide exact: " + dividend + " / " + divisor);
        }
    }

    @Override
    public void divideAndRemainder(int dividendVariable, int divisorVariable, int quotientVariable, int remainderVariable) {
        final long dividend = ((PublicLongSlot) memory.load(dividendVariable)).value;
        final long divisor = ((PublicLongSlot) memory.load(divisorVariable)).value;
        final long quotient = dividend / divisor;
        final long remainder = dividend - divisor * quotient;
        ((PublicLongSlot) memory.load(quotientVariable)).value = quotient;
        ((PublicLongSlot) memory.load(remainderVariable)).value = remainder;
    }

    @Override
    public void staticShift(int distance, int srcVariable, int dstVariable) {
        long value = ((PublicLongSlot) memory.load(srcVariable)).value;
        if (distance > 0) {
            if (distance >= Long.SIZE) {
                value = 0L;
            } else {
                value <<= distance;
            }
        } else {
            distance = -distance;
            if (distance >= Long.SIZE) {
                if (value < 0) {
                    value = -1L;
                } else {
                    value = 0L;
                }
            } else {
                value >>= distance;
            }
        }
        ((PublicLongSlot) memory.load(dstVariable)).value = value;
    }

    @Override
    public void dynamicShift(int distanceVariable, int srcVariable, int dstVariable) {
        long distance = ((PublicLongSlot) memory.load(distanceVariable)).value;
        long value = ((PublicLongSlot) memory.load(srcVariable)).value;
        if (distance > 0) {
            if (distance >= Long.SIZE) {
                value = 0L;
            } else {
                value <<= distance;
            }
        } else {
            distance = -distance;
            if (distance >= Long.SIZE) {
                if (value < 0) {
                    value = -1L;
                } else {
                    value = 0L;
                }
            } else {
                value >>= distance;
            }
        }
        ((PublicLongSlot) memory.load(dstVariable)).value = value;
    }

    @Override
    public void minusOne(int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = -1;
    }

    @Override
    public void bitwiseNot(int srcVariable, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value = ~((PublicLongSlot) memory.load(srcVariable)).value;
    }

    @Override
    public void bitwiseAnd(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        & ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public void bitwiseOr(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        | ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public void bitwiseXor(int srcVariableA, int srcVariableB, int dstVariable) {
        ((PublicLongSlot) memory.load(dstVariable)).value =
                ((PublicLongSlot) memory.load(srcVariableA)).value
                        ^ ((PublicLongSlot) memory.load(srcVariableB)).value;
    }

    @Override
    public int bitSize(int srcVariable) {
        final long value = ((PublicLongSlot) memory.load(srcVariable)).value;
        return Long.SIZE - Math.max(Long.numberOfLeadingZeros(value), Long.numberOfLeadingZeros(~value));
    }

    @Override
    public int bitCount(int srcVariable) {
        final long value = ((PublicLongSlot) memory.load(srcVariable)).value;
        if (value < 0) {
            return Long.bitCount(value);
        } else {
            return Long.bitCount(~value);
        }
    }

    @Override
    public int trailingZeros(int srcVariable) {
        return Long.numberOfTrailingZeros(((PublicLongSlot) memory.load(srcVariable)).value);
    }

    @Override
    public int trailingOnes(int srcVariable) {
        return Long.numberOfTrailingZeros(~((PublicLongSlot) memory.load(srcVariable)).value);
    }

    @Override
    public boolean getBit(int variable, int bitIndex) {
        if (bitIndex < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (bitIndex >= Long.SIZE) {
            bitIndex = Long.SIZE - 1;
        }
        return (((PublicLongSlot) memory.load(variable)).value & (1L << bitIndex)) != 0;
    }

    @Override
    public boolean setBit(int variable, int bitIndex, boolean bit) {
        return false; // todo
    }

    @Override
    public boolean flipBit(int variable, int bitIndex) {
        return false; // todo
    }
}
