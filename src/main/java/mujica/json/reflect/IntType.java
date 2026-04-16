package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_int.IntSlot;
import mujica.ds.of_int.list.IntList;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2020/12/24", project = "webbiton", name = "RegistryIntField")
@CodeHistory(date = "2021/12/24", project = "va", name = "JsonIntField")
@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "JsonIntType")
@CodeHistory(date = "2022/6/10", project = "Ultramarine", name = "JsonIntType")
@CodeHistory(date = "2026/1/1")
class IntType extends JsonType implements IntSlot {

    private static final long serialVersionUID = 0x9044dfc48c3175efL;

    transient int value;

    IntType() {
        super();
    }

    IntType(long flags) {
        super(flags);
    }

    @Override
    public int getInt() {
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
    public void setInt(int newValue) {
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
            int intValue = (Integer) in;
            if (intValue > 0) {
                out.numberValue(intValue);
            } else if (intValue < 0) {
                if ((flags & JsonHint.UNSIGNED) == 0L) {
                    out.numberValue(intValue);
                } else {
                    out.numberValue(0xffffffffL & intValue);
                }
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero int to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero int to null");
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
                value = (Integer) object;
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

    protected int intFrom(long longValue) {
        return (int) longValue;
    }

    protected int intFrom(@NotNull BigInteger bigInteger) {
        return bigInteger.intValueExact();
    }

    protected int intFrom(@NotNull Object object) {
        if (object instanceof Integer) {
            return (Integer) object;
        }
        if (object instanceof Long) {
            return intFrom((long) object);
        }
        if (object instanceof BigInteger) {
            return intFrom((BigInteger) object);
        }
        if ((flags & JsonHint.CAST_FROM_STRING) != 0L && object instanceof CharSequence) {
            return Integer.decode(object.toString());
        }
        throw new ClassCastException(object.getClass() + " to int");
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
            value = intFrom(object);
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
            value = newValue ? 1 : 0;
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to int");
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
        value = intFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        value = intFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            setter.setInt(self, value);
        } else if (state == CollectionConstant.EMPTY) {
            setter.set(self, null);
        } else {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
    }

    @Deprecated
    void to(@NotNull IntList list) {
        final CollectionConstant oldState = state;
        state = CollectionConstant.UNDEFINED;
        if (oldState == CollectionConstant.PRESENT) {
            list.offerLast(value);
        } else if (oldState == CollectionConstant.EMPTY) {
            throw new NullPointerException();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            if (value > 0) {
                out.numberValue(value);
            } else if (value < 0) {
                if ((flags & JsonHint.UNSIGNED) == 0L) {
                    out.numberValue(value);
                } else {
                    out.numberValue(0xffffffffL & value);
                }
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero int to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero int to null");
                out.nullValue();
            } else {
                out.numberValue(0);
            }
        } else if (state == CollectionConstant.EMPTY) {
            if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("null to undefined");
                out.skippedValue();
            } else {
                out.nullValue();
            }
        } else {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
    }

    int toInt() {
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

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            if (value == 0) {
                if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("zero int to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("zero int to null");
                    return null;
                }
            }
            return value;
        } else if (state == CollectionConstant.EMPTY) {
            if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("null to undefined");
                return CollectionConstant.UNDEFINED;
            } else {
                return null;
            }
        } else {
            throw new IllegalStateException();
        }
    }
}
