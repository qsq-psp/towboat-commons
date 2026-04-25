package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.ds.of_boolean.BooleanSlot;
import mujica.ds.of_boolean.list.BooleanList;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2026/1/2")
class BooleanType extends JsonType implements BooleanSlot {

    protected static final CollectionConstant FALSE = CollectionConstant.REMOVED;

    protected static final CollectionConstant TRUE = CollectionConstant.PRESENT;

    BooleanType() {
        super();
    }

    BooleanType(long flags) {
        super(flags);
    }

    @Override
    public boolean getBoolean() {
        switch (state) {
            default:
            case UNDEFINED:
                throw new IllegalStateException();
            case EMPTY:
                throw new NullPointerException();
            case REMOVED:
                return false;
            case PRESENT:
                return true;
        }
    }

    @Override
    public void setBoolean(boolean newValue) {
        state = newValue ? TRUE : FALSE;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        if (in == null) {
            if ((flags & JsonHint.NULLABLE) != 0L) {
                out.nullValue();
            } else {
                throw new NullPointerException();
            }
        } else if ((Boolean) in) {
            out.booleanValue(true);
        } else if ((flags & (((long) JsonEmpty.FROM_FALSE) << UNDEFINED_SHIFT)) != 0L) {
            context.getLogger().debug("false to undefined");
            out.skippedValue();
        } else if ((flags & (((long) JsonEmpty.FROM_FALSE) << NULL_SHIFT)) != 0L) {
            context.getLogger().debug("false to null");
            out.nullValue();
        } else {
            out.booleanValue(false);
        }
    }

    @Override
    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        if ((flags & JsonHint.NULLABLE) != 0L) {
            Object object = getter.get(self);
            if (object != null) {
                if ((Boolean) object) {
                    state = TRUE;
                } else {
                    state = FALSE;
                }
            } else {
                state = CollectionConstant.EMPTY;
            }
        } else {
            if (getter.getBoolean(self)) {
                state = TRUE;
            } else {
                state = FALSE;
            }
        }
        return this;
    }

    boolean booleanFrom(@NotNull Object object) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            if (object instanceof Integer) {
                return (Integer) object != 0;
            }
            if (object instanceof Long) {
                return (Long) object != 0;
            }
            if (object instanceof BigInteger) {
                return ((BigInteger) object).signum() != 0;
            }
        }
        if ((flags & JsonHint.CAST_FROM_STRING) != 0L && object instanceof CharSequence) {
            return Boolean.parseBoolean(object.toString());
        }
        throw new ClassCastException(object.getClass() + " to boolean");
    }

    @NotNull
    CollectionConstant stateFrom(Object object) {
        if (object == null) {
            if ((flags & JsonHint.NULLABLE) != 0L) {
                return CollectionConstant.EMPTY;
            } else {
                throw new NullPointerException();
            }
        }
        return booleanFrom(object) ? TRUE : FALSE;
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        state = stateFrom(object);
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
    JsonType from(boolean value) {
        state = value ? TRUE : FALSE;
        return this;
    }

    @NotNull
    @Override
    JsonType from(int value) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            state = value != 0 ? TRUE : FALSE;
        } else {
            throw new ClassCastException("int to boolean");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(long value) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            state = value != 0L ? TRUE : FALSE;
        } else {
            throw new ClassCastException("long to boolean");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger value) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            state = value.signum() != 0 ? TRUE : FALSE;
        } else {
            throw new ClassCastException("BigInteger to boolean");
        }
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        switch (state) {
            default:
            case UNDEFINED:
                throw new IllegalStateException();
            case EMPTY:
                setter.set(self, null);
                break;
            case REMOVED:
                setter.setBoolean(self, false);
                break;
            case PRESENT:
                setter.setBoolean(self, true);
                break;
        }
        state = CollectionConstant.UNDEFINED;
    }

    void to(@NotNull BooleanList list) {
        final CollectionConstant oldState = state;
        state = CollectionConstant.UNDEFINED;
        switch (oldState) {
            default:
            case UNDEFINED:
                throw new IllegalStateException();
            case EMPTY:
                throw new NullPointerException();
            case REMOVED:
                list.offerLast(false);
                break;
            case PRESENT:
                list.offerLast(true);
                break;
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        switch (state) {
            default:
            case UNDEFINED:
                throw new IllegalStateException();
            case EMPTY:
                if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("null to undefined");
                    out.skippedValue();
                } else {
                    out.nullValue();
                }
                break;
            case REMOVED:
                if ((flags & (((long) JsonEmpty.FROM_FALSE) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("false to undefined");
                    out.skippedValue();
                } else if ((flags & (((long) JsonEmpty.FROM_FALSE) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("false to null");
                    out.nullValue();
                } else {
                    out.booleanValue(false);
                }
                break;
            case PRESENT:
                out.booleanValue(true);
                break;
        }
        state = CollectionConstant.UNDEFINED;
    }

    @Override
    Object toObject(@NotNull JsonContext context) {
        switch (state) {
            default:
            case UNDEFINED:
                throw new IllegalStateException();
            case EMPTY:
                state = CollectionConstant.UNDEFINED;
                if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("null to undefined");
                    return CollectionConstant.UNDEFINED;
                } else {
                    return null;
                }
            case REMOVED:
                state = CollectionConstant.UNDEFINED;
                if ((flags & (((long) JsonEmpty.FROM_FALSE) << UNDEFINED_SHIFT)) != 0L) {
                    context.getLogger().debug("false to undefined");
                    return CollectionConstant.UNDEFINED;
                } else if ((flags & (((long) JsonEmpty.FROM_FALSE) << NULL_SHIFT)) != 0L) {
                    context.getLogger().debug("false to null");
                    return null;
                } else {
                    return false;
                }
            case PRESENT:
                state = CollectionConstant.UNDEFINED;
                return true;
        }
    }
}
