package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;

@CodeHistory(date = "2021/3/22", project = "webbiton", name = "RegistryArrayField")
@CodeHistory(date = "2021/12/31", project = "infrastructure", name = "JsonArrayType")
@CodeHistory(date = "2022/7/23", project = "Ultramarine", name = "JsonArrayType")
@CodeHistory(date = "2026/4/10")
class JavaArrayType extends BuiltType {

    private static final long serialVersionUID = 0xffc61befdc9ace59L;

    @NotNull
    JsonType componentType = JsonType.NOP;

    transient Object value;

    JavaArrayType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    public NopFrame createFrame(@NotNull NopFrame bottom) {
        if (bottom.shape != NopFrame.StructureShape.ARRAY) {
            bottom.context.getLogger().warn("container mismatch");
            return new NopFrame(bottom);
        }
        final JavaArrayFrame frame = new JavaArrayFrame(bottom, this);
        if ((flags & JsonHint.APPEND_TO_ARRAY) != 0L) {
            Object array = bottom.key;
            if (array != null) {
                try {
                    int length = Array.getLength(array);
                    for (int index = 0; index < length; index++) {
                        frame.list.add(Array.get(array, index));
                    }
                } catch (Exception e) {
                    bottom.context.getLogger().warn("", e);
                }
            }
        }
        bottom.setKey(null);
        return frame;
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        if (in != null) {
            int length = Array.getLength(in);
            if (length > 0) {
                out.openArray();
                for (int index = 0; index < length; index++) {
                    componentType.transform(Array.get(in, index), out, context);
                }
                out.closeArray();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("empty array to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("empty array to null");
                out.nullValue();
            } else {
                out.emptyArrayValue();
            }
        } else if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
            context.getLogger().debug("null to undefined");
            out.skippedValue();
        } else {
            out.nullValue();
        }
    }

    @NotNull
    @Override
    JsonType from(@Nullable Object object) {
        if (object == null) {
            if ((flags & JsonHint.NULLABLE) != 0L) {
                value = null;
                state = CollectionConstant.PRESENT;
            } else {
                throw new NullPointerException();
            }
        } else {
            try {
                value = builder.get(1);
                if (value != null) {
                    Array.set(value, 0, object);
                }
                state = CollectionConstant.PRESENT;
            } catch (Throwable e) {
                e.printStackTrace();
                value = null;
                throw new ClassCastException(object.getClass() + " to array");
            }
        }
        return this;
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

    @Override
    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            Object array = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            setter.set(self, array);
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state != CollectionConstant.PRESENT) {
            throw new IllegalStateException();
        }
        state = CollectionConstant.UNDEFINED;
        Object array = value;
        if (array != null) {
            value = null;
            int length = Array.getLength(array);
            if (length > 0) {
                out.openArray();
                for (int index = 0; index < length; index++) {
                    componentType.transform(Array.get(array, index), out, context);
                }
                out.closeArray();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("empty array to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("empty array to null");
                out.nullValue();
            } else {
                out.emptyArrayValue();
            }
        } else if ((flags & (((long) JsonEmpty.FROM_NULL) << UNDEFINED_SHIFT)) != 0L) {
            context.getLogger().debug("null to undefined");
            out.skippedValue();
        } else {
            out.nullValue();
        }
    }

    @Override
    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            Object array = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            return array;
        } else {
            throw new IllegalStateException();
        }
    }

    @NotNull
    @Override
    JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        collectConfig(clazz, false);
        collectConstructor(clazz, context);
        collectComponentType(clazz, context);
        return this;
    }

    @Override
    void collectConstructor(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        builder = new ArrayNewInstance(clazz);
        if ((flags & JsonHint.USE_METHOD_HANDLE) != 0L) {
            try {
                builder = builder.unreflect(MethodHandles.lookup());
            } catch (Throwable e) {
                context.getLogger().warn("", e);
            }
        }
    }

    void collectComponentType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        componentType = context.forClass(clazz.getComponentType());
    }
}
