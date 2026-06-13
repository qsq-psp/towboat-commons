package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.f64.F64Slot;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2020/12/24", project = "webbiton", name = "JsonDoubleType")
@CodeHistory(date = "2022/6/11", project = "Ultramarine", name = "JsonDoubleType")
@CodeHistory(date = "2026/6/6", name = "DoubleType")
@CodeHistory(date = "2026/6/8")
class F64Type extends JsonType implements F64Slot {

    private static final long serialVersionUID = 0x79D5392531C49BB6L;

    transient double value;

    F64Type(long flags) {
        super(flags);
    }

    @Override
    public double getDouble() {
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
    public void setDouble(double newValue) {
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
            double doubleValue = (Double) in;
            if ((flags & ((((long) JsonEmpty.FROM_NAN) << UNDEFINED_SHIFT) | (((long) JsonEmpty.FROM_NAN) << NULL_SHIFT))) != 0L && Double.isNaN(doubleValue)) {
                if ((flags & (((long) JsonEmpty.FROM_NAN) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("NaN double to undefined");
                    out.skippedValue();
                } else {
                    context.getLogger().debug("NaN double to null");
                    out.nullValue();
                }
            } else if ((flags & ((((long) JsonEmpty.FROM_INFINITE) << UNDEFINED_SHIFT) | (((long) JsonEmpty.FROM_INFINITE) << NULL_SHIFT))) != 0L && Double.isInfinite(doubleValue)) {
                if ((flags & (((long) JsonEmpty.FROM_INFINITE) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("infinite double to undefined");
                    out.skippedValue();
                } else {
                    context.getLogger().debug("infinite double to null");
                    out.nullValue();
                }
            } else {
                out.numberValue(doubleValue);
            }
        }
    }

    @Override
    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            Object object = getter.get(self);
            if (object != null) {
                value = (Double) object;
                state = CollectionConstant.PRESENT;
            } else {
                state = CollectionConstant.EMPTY;
            }
        } else {
            value = getter.getDouble(self);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    double doubleFrom(@NotNull Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }
        if ((flags & JsonHint.CAST_FROM_STRING) != 0L && object instanceof CharSequence) {
            return Double.parseDouble(object.toString());
        }
        throw new ClassCastException(object.getClass() + " to double");
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
            value = doubleFrom(object);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            value = (byte) (newValue ? 1.0 : 0.0);
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to double");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            value = newValue;
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("int to double");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            value = newValue;
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("long to double");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            value = newValue.doubleValue();
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("BigInteger to double");
        }
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            setter.setDouble(self, value);
        } else if (state == CollectionConstant.EMPTY) {
            setter.set(self, null);
        } else {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            if (Double.isNaN(value)) {
                if ((flags & (((long) JsonEmpty.FROM_NAN) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("NaN double to undefined");
                    out.skippedValue();
                } else if ((flags & (((long) JsonEmpty.FROM_NAN) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("NaN double to null");
                    out.nullValue();
                } else {
                    out.numberValue(value);
                }
            } else if (Double.isInfinite(value)) {
                if ((flags & (((long) JsonEmpty.FROM_INFINITE) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("infinite double to undefined");
                    out.skippedValue();
                } else if ((flags & (((long) JsonEmpty.FROM_INFINITE) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("infinite double to null");
                    out.nullValue();
                } else {
                    out.numberValue(value);
                }
            } else if (value == 0.0) {
                if ((flags & (((long) JsonEmpty.FROM_ZERO_DECIMAL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("zero double to undefined");
                    out.skippedValue();
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_DECIMAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("zero double to null");
                    out.nullValue();
                } else {
                    out.numberValue(value);
                }
            } else {
                out.numberValue(value);
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

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            if (Double.isNaN(value)) {
                if ((flags & (((long) JsonEmpty.FROM_NAN) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("NaN double to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_NAN) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("NaN double to null");
                    return null;
                }
            } else if (Double.isInfinite(value)) {
                if ((flags & (((long) JsonEmpty.FROM_INFINITE) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("infinite double to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_INFINITE) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("infinite double to null");
                    return null;
                }
            } else if (value == 0.0) {
                if ((flags & (((long) JsonEmpty.FROM_ZERO_DECIMAL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("zero double to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_DECIMAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("zero double to null");
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
