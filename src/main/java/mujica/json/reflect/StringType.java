package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.container.FastNumber;
import mujica.json.handler.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.json.modifier.MaxStringLength;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.math.BigInteger;

@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "JsonStringType")
@CodeHistory(date = "2022/8/6", project = "Ultramarine", name = "JsonStringType")
@CodeHistory(date = "2026/4/16")
class StringType extends JsonType {

    private static final long serialVersionUID = 0x00346E309F63DB4FL;

    int maxLength = Integer.MAX_VALUE;

    transient String value;

    StringType(long flags) {
        super(flags);
    }

    @NotNull
    StringType fromString(@NotNull String string) {
        if (string.length() > maxLength) {
            throw new IllegalArgumentException("too long String");
        }
        value = string;
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        from(getter.get(self));
        return this;
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        if (object instanceof CharSequence) {
            return fromString(object.toString());
        }
        if (object == null) {
            return from();
        }
        if (object instanceof Number) {
            if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
                if (object instanceof Integer || object instanceof Long || object instanceof BigInteger) {
                    return fromString(object.toString());
                }
            }
            if ((flags & JsonHint.CAST_FROM_DECIMAL) != 0L) {
                if (object instanceof Float || object instanceof Double) {
                    return fromString(object.toString());
                }
            }
        } else if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L && object instanceof Boolean) {
            return fromString(object.toString());
        }
        throw new ClassCastException(object.getClass() + " to String");
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
            return fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("boolean to String");
        }
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("int to String");
        }
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("long to String");
        }
    }

    @NotNull
    @Override
    JsonType from(float newValue) {
        if ((flags & JsonHint.CAST_FROM_DECIMAL) != 0L) {
            return fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("float to String");
        }
    }

    @NotNull
    @Override
    JsonType from(double newValue) {
        if ((flags & JsonHint.CAST_FROM_DECIMAL) != 0L) {
            return fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("double to String");
        }
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromString(newValue.toString());
        } else {
            throw new ClassCastException("BigInteger to String");
        }
    }

    @NotNull
    @Override
    JsonType from(@NotNull FastNumber newValue) {
        if (newValue.isDecimal()) {
            if ((flags & JsonHint.CAST_FROM_DECIMAL) == 0L) {
                throw new ClassCastException("FastNumber (decimal) to String");
            }
        } else {
            if ((flags & JsonHint.CAST_FROM_INTEGRAL) == 0L) {
                throw new ClassCastException("FastNumber (integral) to String");
            }
        }
        return fromString(newValue.toString());
    }

    @NotNull
    @Override
    JsonType from(@NotNull CharSequence newValue) {
        return fromString(newValue.toString());
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
                        context.getLogger().debug("null String to undefined");
                        out.skippedValue();
                    } else {
                        out.nullValue();
                    }
                } else if (value.isEmpty()) {
                    if ((flags & (((long) JsonEmpty.FROM_EMPTY_STRING) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("empty String to undefined");
                        out.skippedValue();
                    } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_STRING) << NULL_SHIFT)) != 0L) {
                        context.getLogger().debug("empty String to null");
                        out.nullValue();
                    } else {
                        out.stringValue("");
                    }
                } else if (value.isBlank()) {
                    if ((flags & (((long) JsonEmpty.FROM_BLANK_STRING) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("blank String to undefined");
                        out.skippedValue();
                    } else if ((flags & (((long) JsonEmpty.FROM_BLANK_STRING) << NULL_SHIFT)) != 0L) {
                        context.getLogger().debug("blank String to null");
                        out.nullValue();
                    } else {
                        out.stringValue(value);
                    }
                } else {
                    out.stringValue(value);
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
            String string = value;
            value = null;
            state = CollectionConstant.UNDEFINED;
            if (string != null) {
                if (string.isEmpty()) {
                    if ((flags & (((long) JsonEmpty.FROM_EMPTY_STRING) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("empty String to undefined");
                        return CollectionConstant.UNDEFINED;
                    } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_STRING) << NULL_SHIFT)) != 0L) {
                        context.getLogger().debug("empty String to null");
                        return null;
                    }
                } else if (string.isBlank()) {
                    if ((flags & (((long) JsonEmpty.FROM_BLANK_STRING) << UNDEFINED_SHIFT)) != 0L) {
                        context.getLogger().debug("blank String to undefined");
                        return CollectionConstant.UNDEFINED;
                    } else if ((flags & (((long) JsonEmpty.FROM_BLANK_STRING) << NULL_SHIFT)) != 0L) {
                        context.getLogger().debug("blank String to null");
                        return null;
                    }
                }
            }
            return string;
        } else {
            throw new IllegalStateException();
        }
    }

    void collectMaxStringLength(@NotNull AnnotatedElement annotated) {
        final MaxStringLength maxStringLength = annotated.getAnnotation(MaxStringLength.class);
        if (maxStringLength != null) {
            if (maxStringLength.value() < 0) {
                throw new IllegalArgumentException("negative max string length");
            }
            maxLength = maxStringLength.value();
        }
    }
}
