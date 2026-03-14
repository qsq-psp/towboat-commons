package mujica.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;

import java.math.BigInteger;

@CodeHistory(date = "2026/2/27")
public final class BigConstants {

    public static final BigInteger HUNDRED = BigInteger.valueOf(100L);

    public static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

    public static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    public static final BigInteger COUNT_LONG = BigInteger.ONE.shiftLeft(Long.SIZE);

    public static final BigInteger MAX_UNSIGNED_LONG = COUNT_LONG.subtract(BigInteger.ONE);
}
