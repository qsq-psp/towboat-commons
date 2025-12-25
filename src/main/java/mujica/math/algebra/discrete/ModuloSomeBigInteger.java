package mujica.math.algebra.discrete;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/4/3.
 */
@CodeHistory(date = "2025/4/3")
public class ModuloSomeBigInteger extends ModularMath {

    protected static final BigInteger BIG_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);

    protected static final BigInteger BIG_MAX_LONG_SUM = ModuloI64.BIG_MASK;

    protected static final BigInteger BIG_MAX_LONG_PRODUCT = BIG_MAX_LONG.multiply(BIG_MAX_LONG);

    protected static int level(@NotNull BigInteger value) {
        if (value.compareTo(BIG_MAX_LONG) > 0) {
            if (value.compareTo(BIG_MAX_LONG_PRODUCT) > 0) {
                return LARGER_THAN_MAX_LONG_PRODUCT;
            } else if (value.compareTo(BIG_MAX_LONG_SUM) > 0) {
                return LARGER_THAN_MAX_LONG_SUM;
            } else {
                return LARGER_THAN_MAX_LONG;
            }
        } else {
            return ModuloSomeLong.level(value.longValue());
        }
    }

    @NotNull
    final BigInteger mod;

    private final int level;

    public ModuloSomeBigInteger(@NotNull BigInteger mod) {
        super();
        if (mod.signum() <= 0) {
            throw new IllegalArgumentException();
        }
        this.mod = mod;
        this.level = level(mod);
    }

    @Override
    protected int modLevel() {
        return level;
    }

    @NotNull
    @Override
    public Number modNumber() {
        return mod;
    }

    @NotNull
    @Override
    public BigInteger bigModNumber() {
        return mod;
    }

    @Override
    public int cast2i(long value) {
        if (level < LARGER_THAN_MAX_LONG) {
            BigInteger bigValue = BigInteger.valueOf(value).mod(mod);
            if (bigValue.signum() < 0) {
                bigValue = bigValue.add(mod);
            }
            value = bigValue.longValueExact();
        }
        return super.cast2i(value);
    }

    @Override
    public int increment(int a) {
        return cast2i(a + 1L);
    }

    @Override
    public int decrement(int a) {
        return cast2i(a - 1L);
    }
}
