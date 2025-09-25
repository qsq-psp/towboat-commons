package mujica.math.algebra.discrete;

import java.math.BigInteger;

/**
 * Created on 2025/3/15.
 */
public class BaselineCastToZero extends CastToZero {

    @Override
    public int mean(int x, int y) {
        // BigInteger divide 2 is not cast to zero, is cast to floor; shift right is cast to zero
        return BigInteger.valueOf(x).add(BigInteger.valueOf(y)).shiftRight(1).intValueExact();
    }

    @Override
    public long mean(long x, long y) {
        // BigInteger divide 2 is not cast to zero, is cast to floor; shift right is cast to zero
        return BigInteger.valueOf(x).add(BigInteger.valueOf(y)).shiftRight(1).longValueExact();
    }
}
