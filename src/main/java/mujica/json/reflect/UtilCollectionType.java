package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.JsonHandler;
import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.basic.TypeUtil;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

@CodeHistory(date = "2022/8/5", project = "Ultramarine", name = "JsonCollectionType")
@CodeHistory(date = "2026/4/8")
class UtilCollectionType extends BuiltType {

    private static final long serialVersionUID = 0x570001dbf06183f0L;

    @NotNull
    JsonType componentType = JsonType.NOP;

    transient Iterable<?> value;

    UtilCollectionType(long flags) {
        super(flags);
    }

    @NotNull
    @Override
    public NopFrame createFrame(@NotNull NopFrame bottom) {
        if (bottom.shape != NopFrame.StructureShape.ARRAY) {
            bottom.context.getLogger().warn("container mismatch");
            return new NopFrame(bottom);
        }
        Object collection = bottom.key;
        if ((flags & JsonHint.ALWAYS_BUILD) == 0L || collection instanceof Collection) {
            if ((flags & JsonHint.CLEAR_COLLECTION) != 0L) {
                ((Collection<?>) collection).clear();
            }
        } else {
            try {
                collection = builder.get(null);
            } catch (Throwable e) {
                bottom.context.getLogger().warn("", e);
                return super.createFrame(bottom);
            }
        }
        bottom.setKey(null);
        return new UtilCollectionFrame(bottom, componentType, collection);
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
            throw new ClassCastException(object.getClass() + " to collection");
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
            Object collection = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            setter.set(self, collection);
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
        Iterable<?> iterable = value;
        if (iterable != null) {
            value = null;
            Iterator<?> iterator = iterable.iterator();
            if (iterator.hasNext()) {
                out.openArray();
                do {
                    componentType.transform(iterator.next(), out, context);
                } while (iterator.hasNext());
                out.closeArray();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << UNDEFINED_SHIFT)) != 0L) {
                context.getLogger().debug("empty collection to undefined");
                out.skippedValue();
            } else if ((flags & (((long) JsonEmpty.FROM_EMPTY_ARRAY) << NULL_SHIFT)) != 0L) {
                context.getLogger().debug("empty collection to null");
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
            Object collection = value;
            state = CollectionConstant.UNDEFINED;
            value = null;
            return collection;
        } else {
            throw new IllegalStateException();
        }
    }

    @NotNull
    UtilCollectionType derive() {
        if ((flags & JsonHint.DERIVED) == 0L) {
            try {
                UtilCollectionType that = (UtilCollectionType) clone();
                that.flags |= JsonHint.DERIVED;
                return that;
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e);
            }
        } else {
            return this;
        }
    }

    @Override
    boolean needGenericInfo() {
        return componentType == JsonType.NOP;
    }

    @NotNull
    @Override
    JsonType collectGeneric(@NotNull Type genericType, @NotNull JsonContext context) {
        UtilCollectionType that = this;
        final Type genericComponentType = TypeUtil.search(genericType, Collection.class, "E");
        if (genericComponentType instanceof Class) {
            that = that.derive();
            that.componentType = context.forClass((Class<?>) genericComponentType);
            context.getLogger().warn("generic success 1");
        } else if (genericComponentType instanceof ParameterizedType) {
            Type rawGenericComponentType = ((ParameterizedType) genericComponentType).getRawType();
            if (rawGenericComponentType instanceof Class<?>) {
                that = that.derive();
                that.componentType = context.forClass((Class<?>) rawGenericComponentType);
                context.getLogger().warn("generic success 2");
            } else {
                context.getLogger().warn("generic failure 1");
            }
        } else {
            context.getLogger().warn("generic failure 2");
        }
        return that;
    }
}
