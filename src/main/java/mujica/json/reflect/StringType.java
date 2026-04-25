package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.json.modifier.MaxStringLength;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.math.BigInteger;

/**
 * Created on 2026/4/16.
 */
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

    void fromString(@NotNull String string) {
        if (string.length() > maxLength) {
            throw new IllegalArgumentException("too long String");
        }
        value = string;
        state = CollectionConstant.PRESENT;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("boolean to String");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("int to String");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("long to String");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(float newValue) {
        if ((flags & JsonHint.CAST_FROM_DECIMAL) != 0L) {
            fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("long to String");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(double newValue) {
        if ((flags & JsonHint.CAST_FROM_DECIMAL) != 0L) {
            fromString(String.valueOf(newValue));
        } else {
            throw new ClassCastException("long to String");
        }
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            fromString(newValue.toString());
        } else {
            throw new ClassCastException("BigInteger to String");
        }
        return this;
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
        fromString(newValue.toString());
        return this;
    }

    @NotNull
    @Override
    JsonType from(@NotNull CharSequence newValue) {
        fromString(newValue.toString());
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
                    out.nullValue();
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
