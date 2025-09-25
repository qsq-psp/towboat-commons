package mujica.ds.primitive;

import mujica.reflect.modifier.Assigned;

/**
 * Created on 2025/6/15.
 */
public interface BitwiseContext extends NumericContext {

    void minusOne(@Assigned int dstVariable);

    void bitwiseNot(@Assigned int srcVariable, @Assigned int dstVariable);

    void bitwiseAnd(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    void bitwiseOr(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    void bitwiseXor(@Assigned int srcVariableA, @Assigned int srcVariableB, @Assigned int dstVariable);

    int bitSize(@Assigned int srcVariable);

    int bitCount(@Assigned int srcVariable);

    int trailingZeros(@Assigned int srcVariable);

    int trailingOnes(@Assigned int srcVariable);

    boolean getBit(@Assigned int variable, int bitIndex);

    boolean setBit(@Assigned int variable, int bitIndex, boolean bit);

    boolean flipBit(@Assigned int variable, int bitIndex);
}
