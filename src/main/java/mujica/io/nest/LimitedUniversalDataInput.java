package mujica.io.nest;

import java.math.BigInteger;

/**
 * Created on 2025/10/9.
 */
public interface LimitedUniversalDataInput extends LimitedInput, UniversalDataInput {

    BigInteger MASK64 = BigInteger.ONE.shiftLeft(Long.SIZE).subtract(BigInteger.ONE);
}
