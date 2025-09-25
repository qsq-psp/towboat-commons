package mujica.math.algebra.discrete;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Created on 2025/2/27.
 */
public abstract class ModularMath extends IntegralMath {

    protected static final int ONE = 0;

    protected static final int SMALL = 1;

    protected static final int LARGER_THAN_MAX_INT = 2;

    protected static final int LARGER_THAN_MAX_INT_SUM = 3;

    protected static final int LARGER_THAN_MAX_INT_PRODUCT = 4;

    protected static final int LARGER_THAN_MAX_LONG = 5;

    protected static final int LARGER_THAN_MAX_LONG_SUM = 6;

    protected static final int LARGER_THAN_MAX_LONG_PRODUCT = 7;

    protected abstract int modLevel();

    public abstract Number modNumber();

    @NotNull
    public abstract BigInteger bigModNumber();

    @Override
    public int hashCode() {
        return modNumber().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ModularMath && this.modNumber().equals(((ModularMath) obj).modNumber());
    }

    @Override
    public String toString() {
        return "ModularMath[" + modNumber() + "]";
    }
}
