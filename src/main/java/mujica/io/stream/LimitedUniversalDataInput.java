package mujica.io.stream;

import mujica.reflect.modifier.CodeHistory;

import java.math.BigInteger;

@CodeHistory(date = "2025/10/9")
public interface LimitedUniversalDataInput extends LimitedInput, UniversalDataInput {

    BigInteger MASK64 = BigInteger.ONE.shiftLeft(Long.SIZE).subtract(BigInteger.ONE);
}
