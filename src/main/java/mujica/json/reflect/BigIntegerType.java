package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2026/4/8")
class BigIntegerType extends JsonType {

    transient BigInteger value;

    BigIntegerType() {
        super();
    }

    BigIntegerType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    JsonType from() {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            value = null;
            state = CollectionConstant.PRESENT;
        } else {
            throw new NullPointerException();
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            value = newValue ? BigInteger.ONE : BigInteger.ZERO;
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to BigInteger");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        value = BigInteger.valueOf(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        value = BigInteger.valueOf(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            try {
                setter.set(self, value);
            } finally {
                value = null;
                state = CollectionConstant.UNDEFINED;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            try {
                if (value == null) {
                    if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("null BigInteger to undefined");
                        out.skippedValue();
                    } else {
                        out.nullValue();
                    }
                } else if (value.signum() == 0) {
                    if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("zero BigInteger to undefined");
                        out.skippedValue();
                    } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                        context.getLogger().debug("zero BigInteger to null");
                        out.nullValue();
                    } else {
                        out.numberValue(BigInteger.ZERO);
                    }
                } else {
                    out.numberValue(value);
                }
            } finally {
                value = null;
                state = CollectionConstant.UNDEFINED;
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            BigInteger bigInteger = value;
            value = null;
            state = CollectionConstant.UNDEFINED;
            if (bigInteger == null) {
                if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("null BigInteger to undefined");
                    return CollectionConstant.UNDEFINED;
                } else {
                    return null;
                }
            } else if (bigInteger.signum() == 0) {
                if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("zero BigInteger to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("zero BigInteger to null");
                    return null;
                } else {
                    return BigInteger.ZERO;
                }
            } else {
                return bigInteger;
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
