package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.math.BigInteger;
import java.util.HashMap;

@CodeHistory(date = "2022/8/12", project = "Ultramarine", name = "JsonEnumType")
@CodeHistory(date = "2026/4/11")
class JavaEnumType extends JsonType {

    private static final long serialVersionUID = 0x43e916a99d6cce1dL;

    @NotNull
    final HashMap<Object, Object> map = new HashMap<>(); // (Integer | String | Enum<?>) -> (String | Enum<?>)

    protected transient Enum<?> value;

    JavaEnumType(long flags) {
        super(flags);
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        Object nameObject;
        if (in instanceof Enum) {
            nameObject = map.get(in);
        } else {
            nameObject = null;
        }
        if (nameObject instanceof String) {
            out.stringValue((String) nameObject);
        } else if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) == 0L) {
            out.nullValue();
        } else {
            out.skippedValue();
        }
    }

    @Override
    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        final Object enumObject = getter.get(self);
        if (enumObject instanceof Enum && map.containsKey(enumObject)) {
            value = (Enum<?>) enumObject;
        } else {
            value = null;
        }
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        return super.from(object);
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
    JsonType fromOrdinal(int ordinal) {
        final Object enumObject = map.get(ordinal);
        if (enumObject instanceof Enum) {
            value = (Enum<?>) enumObject;
        } else {
            value = null;
        }
        state = CollectionConstant.PRESENT;
        return this;
    }

    @NotNull
    @Override
    JsonType from(boolean newValue) {
        if ((flags & JsonHint.CAST_FROM_BOOLEAN) != 0L) {
            return fromOrdinal(newValue ? 0 : 1);
        } else {
            throw new ClassCastException("boolean to enum");
        }
    }

    @NotNull
    @Override
    JsonType from(int newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromOrdinal(newValue);
        } else {
            throw new ClassCastException("int to enum");
        }
    }

    @NotNull
    @Override
    JsonType from(long newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            if (0L <= newValue && newValue <= Integer.MAX_VALUE) {
                return fromOrdinal((int) newValue);
            } else {
                throw new ClassCastException("long to int to enum");
            }
        } else {
            throw new ClassCastException("long to enum");
        }
    }

    @NotNull
    @Override
    JsonType from(float newValue) {
        throw new ClassCastException("float to enum");
    }

    @NotNull
    @Override
    JsonType from(double newValue) {
        throw new ClassCastException("double to enum");
    }

    @NotNull
    @Override
    JsonType from(@NotNull BigInteger newValue) {
        if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromOrdinal(newValue.intValueExact());
        } else {
            throw new ClassCastException("BigInteger to enum");
        }
    }

    @NotNull
    @Override
    JsonType from(@NotNull FastNumber newValue) {
        if (newValue.isDecimal()) {
            throw new ClassCastException("FastNumber (decimal) to enum");
        } else if ((flags & JsonHint.CAST_FROM_INTEGRAL) != 0L) {
            return fromOrdinal(newValue.intValue());
        } else {
            throw new ClassCastException("BigInteger (integral) to enum");
        }
    }

    @NotNull
    @Override
    JsonType from(@NotNull CharSequence newValue) {
        final Object enumObject = map.get(newValue.toString());
        if (enumObject instanceof Enum) {
            value = (Enum<?>) enumObject;
        } else {
            value = null;
        }
        state = CollectionConstant.PRESENT;
        return this;
    }

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            Object object = value;
            value = null;
            state = CollectionConstant.UNDEFINED;
            setter.set(self, object);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            Object enumObject = value;
            value = null;
            state = CollectionConstant.UNDEFINED;
            Object nameObject = map.get(enumObject);
            if (nameObject instanceof String) {
                out.stringValue((String) nameObject);
            } else if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) == 0L) {
                out.nullValue();
            } else {
                out.skippedValue();
            }
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            Object object = value;
            value = null;
            state = CollectionConstant.UNDEFINED;
            if (object == null && (flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("null enum to undefined");
                return CollectionConstant.UNDEFINED;
            }
            return object;
        } else {
            throw new IllegalStateException();
        }
    }

    @NotNull
    @Override
    JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        collectConfig(clazz, false);
        collectEnumConstants(clazz, context);
        return this;
    }

    void collectEnumConstants(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        try {
            for (Enum<?> value : reflectGetValues(clazz)) {
                String name = value.name();
                // reformat here
                map.put(value.ordinal(), value);
                map.put(name, value);
                map.put(value, name);
            }
        } catch (Throwable e) {
            context.getLogger().warn("collect enum {}", clazz, e);
        }
    }

    @NotNull
    Enum<?>[] reflectGetValues(@NotNull Class<?> clazz) throws Throwable {
        Object returnValue;
        if ((flags & JsonHint.USE_METHOD_HANDLE) != 0L) {
            returnValue = MethodHandles.lookup().findStatic(clazz, "values", MethodType.methodType(clazz.arrayType())).invoke();
        } else {
            returnValue = clazz.getMethod("values").invoke(null);
        }
        if (returnValue instanceof Enum[]) {
            return (Enum<?>[]) returnValue;
        } else {
            throw new ClassCastException();
        }
    }
}
