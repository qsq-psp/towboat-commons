package mujica.json.reflect;

import mujica.algebra.discrete.BigConstants;
import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_long.LongSlot;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2026/4/12")
class LongType extends JsonType implements LongSlot {

    private static final long serialVersionUID = 0x203531829aa1ba76L;

    transient long value;

    LongType() {
        super();
    }

    LongType(long flags) {
        super(flags);
    }

    @Override
    public long getLong() {
        if (state == CollectionConstant.PRESENT) {
            return value;
        }
        if (state == CollectionConstant.EMPTY) {
            throw new NullPointerException();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void setLong(long newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        if (in == null) {
            if ((flags & JsonHint.NULLABLE) != 0L) {
                out.nullValue();
            } else {
                throw new NullPointerException();
            }
        } else {
            long longValue = (Long) in;
            if (longValue > 0L) {
                out.numberValue(longValue);
            } else if (longValue < 0L) {
                if ((flags & JsonHint.UNSIGNED) == 0L) {
                    out.numberValue(longValue);
                } else {
                    out.numberValue(BigInteger.valueOf(longValue).add(BigConstants.COUNT_LONG));
                }
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero long to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero long to null");
                out.nullValue();
            } else {
                out.numberValue(0);
            }
        }
    }

    @Override
    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            Object object = getter.get(self);
            if (object != null) {
                value = (Long) object;
                state = CollectionConstant.PRESENT;
            } else {
                state = CollectionConstant.EMPTY;
            }
        } else {
            value = getter.getInt(self);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    protected long longFrom(@NotNull BigInteger bigInteger) {
        return bigInteger.longValueExact();
    }

    protected long longFrom(@NotNull Object object) {
        if (object instanceof Integer) {
            return (Integer) object;
        }
        if (object instanceof Long) {
            return (Long) object;
        }
        if (object instanceof BigInteger) {
            return longFrom((BigInteger) object);
        }
        if ((flags & JsonHint.CAST_FROM_STRING) != 0L && object instanceof CharSequence) {
            return Long.decode(object.toString());
        }
        throw new ClassCastException(object.getClass() + " to long");
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        if (object == null) {
            if ((flags & JsonHint.NULLABLE) != 0L) {
                state = CollectionConstant.EMPTY;
            } else {
                throw new NullPointerException();
            }
        } else {
            value = longFrom(object);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from() {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            state = CollectionConstant.EMPTY;
        } else {
            throw new NullPointerException();
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            value = newValue ? 1L : 0L;
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to long");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        value = newValue;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        value = longFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            setter.setLong(self, value);
        } else if (state == CollectionConstant.EMPTY) {
            setter.set(self, null);
        } else {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
    }

    long toLong() {
        final CollectionConstant oldState = state;
        state = CollectionConstant.UNDEFINED;
        if (oldState == CollectionConstant.PRESENT) {
            return value;
        } else if (oldState == CollectionConstant.EMPTY) {
            throw new NullPointerException();
        } else {
            throw new IllegalStateException();
        }
    }
}
