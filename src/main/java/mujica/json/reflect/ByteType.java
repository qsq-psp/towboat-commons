package mujica.json.reflect;

import io.netty.buffer.ByteBuf;
import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_byte.ByteSlot;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2020/4/1", project = "webbiton", name = "RegistryByteField")
@CodeHistory(date = "2022/8/8", project = "Ultramarine", name = "JsonByteType")
@CodeHistory(date = "2026/4/9")
class ByteType extends JsonType implements ByteSlot {

    private static final long serialVersionUID = 0x9312fa58b4a856d8L;

    transient byte value;

    ByteType() {
        super();
    }

    ByteType(long flags) {
        super(flags);
    }

    @Override
    public byte getByte() {
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
    public void setByte(byte newValue) {
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
            int byteValue = (Byte) in;
            if (byteValue > 0) {
                out.numberValue(byteValue);
            } else if (byteValue < 0) {
                if ((flags & JsonHint.UNSIGNED) == 0L) {
                    out.numberValue(byteValue);
                } else {
                    out.numberValue(0xff & byteValue);
                }
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero byte to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero byte to null");
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
                value = (Byte) object;
                state = CollectionConstant.PRESENT;
            } else {
                state = CollectionConstant.EMPTY;
            }
        } else {
            value = getter.getByte(self);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    protected byte byteFrom(int intValue) {
        return (byte) intValue;
    }

    protected byte byteFrom(long longValue) {
        return (byte) longValue;
    }

    protected byte byteFrom(@NotNull BigInteger bigInteger) {
        return bigInteger.byteValueExact();
    }

    protected byte byteFrom(@NotNull Object object) {
        if (object instanceof Integer) {
            return byteFrom((int) object);
        }
        if (object instanceof Long) {
            return byteFrom((long) object);
        }
        if (object instanceof BigInteger) {
            return byteFrom((BigInteger) object);
        }
        if ((flags & JsonHint.CAST_FROM_STRING) != 0L && object instanceof CharSequence) {
            return byteFrom(Integer.decode(object.toString()));
        }
        throw new ClassCastException(object.getClass() + " to byte");
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
            value = byteFrom(object);
            state = CollectionConstant.PRESENT;
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            value = (byte) (newValue ? 1 : 0);
            state = CollectionConstant.PRESENT;
        } else {
            throw new ClassCastException("boolean to byte");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        value = byteFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        value = byteFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        value = byteFrom(newValue);
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            setter.setByte(self, value);
        } else if (state == CollectionConstant.EMPTY) {
            setter.set(self, null);
        } else {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
    }

    @Deprecated
    void to(@NotNull ByteBuf buf) {
        final CollectionConstant oldState = state;
        state = CollectionConstant.UNDEFINED;
        if (oldState == CollectionConstant.PRESENT) {
            buf.writeByte(value);
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
                    out.numberValue(0xff & value);
                }
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("zero byte to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("zero byte to null");
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

    byte toByte() {
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
                    context.getLogger().debug("zero byte to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_ZERO_INTEGRAL) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("zero byte to null");
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
