package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.JsonHandler;
import mujica.json.entity.TypePreference;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;
import java.math.BigInteger;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/12/22")
class JsonType extends ReflectConfig implements TypePreference, JsonContextTransformer<Object> {

    private static final long serialVersionUID = 0x41cda8faed8e4d97L;

    static final JsonType NOP = new JsonType();

    @NotNull
    protected transient CollectionConstant state = CollectionConstant.UNDEFINED;

    JsonType() {
        super();
    }

    JsonType(long flags) {
        super(flags);
    }

    @Override
    public int typePreference() {
        return 0;
    }

    @NotNull
    public NopFrame createFrame(@NotNull NopFrame bottom) {
        return new NopFrame(bottom);
    }

    @Override
    public void transform(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        from(in).to(out, context);
    }

    JsonType from(@NotNull Getter getter, @Nullable Object self) throws Throwable {
        return from(getter.get(self));
    }

    @NotNull
    JsonType from(@Nullable Object object) {
        return this;
    }

    @NotNull
    JsonType from() {
        return from((Object) null);
    }

    @NotNull
    JsonType from(boolean newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(int newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(long newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(float newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(double newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(@NotNull BigInteger newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(@NotNull FastNumber newValue) {
        return from((Object) newValue);
    }

    @NotNull
    JsonType from(@NotNull CharSequence newValue) {
        return from((Object) newValue);
    }

    void to(@NotNull Setter setter, @Nullable Object self) throws Throwable {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
        } else {
            throw new IllegalStateException();
        }
    }

    void to(@NotNull JsonHandler out, @NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            out.skippedValue();
        } else {
            throw new IllegalStateException();
        }
    }

    Object toObject(@NotNull JsonContext context) {
        if (state == CollectionConstant.PRESENT) {
            state = CollectionConstant.UNDEFINED;
            return CollectionConstant.UNDEFINED;
        } else {
            throw new IllegalStateException();
        }
    }

    @NotNull
    JsonType collectType(@NotNull Class<?> clazz, @NotNull JsonContext context) {
        collectConfig(clazz, false);
        return this;
    }

    @NotNull
    JsonType collectSignAndRange(@NotNull AnnotatedElement annotated) {
        long newFlags = flags;
        final DataType dataType = annotated.getDeclaredAnnotation(DataType.class);
        if (dataType != null) {
            if (dataType.value().toLowerCase().startsWith("u")) {
                newFlags |= JsonHint.UNSIGNED;
            }
        }
        JsonType that = this;
        if (newFlags != flags) {
            that = that.derive();
            that.flags = newFlags;
        }
        return that;
    }

    @NotNull
    JsonType derive() {
        if ((flags & JsonHint.DERIVED) == 0L) {
            try {
                JsonType that = (JsonType) clone();
                that.flags |= JsonHint.DERIVED;
                return that;
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e);
            }
        } else {
            return this;
        }
    }

    boolean needGenericInfo() {
        return false;
    }

    @NotNull
    JsonType collectGeneric(@NotNull Type genericType, @NotNull JsonContext context) {
        return this;
    }
}
