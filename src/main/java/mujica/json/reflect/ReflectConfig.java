package mujica.json.reflect;

import mujica.json.modifier.JsonEmpty;
import mujica.json.modifier.JsonHint;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "ConversionConfig")
@CodeHistory(date = "2026/4/7", name = "JsonConfig")
@CodeHistory(date = "2026/4/13")
public class ReflectConfig implements Cloneable, Serializable {

    private static final long serialVersionUID = 0x1d7280cfd1b77115L;

    public static final int UNDEFINED_SHIFT = 32;
    public static final int NULL_SHIFT = 48;

    protected long flags;

    public ReflectConfig() {
        super();
    }

    public ReflectConfig(long flags) {
        super();
        this.flags = flags;
    }

    public boolean any(long testFlag) {
        return (flags & testFlag) != 0L;
    }

    public boolean all(long testFlags) {
        return (flags & testFlags) == testFlags;
    }

    @NotNull
    ReflectConfig collectConfig(@NotNull AnnotatedElement annotated, boolean shouldDerive) {
        long newFlags = flags;
        final JsonHint jsonHint = annotated.getDeclaredAnnotation(JsonHint.class);
        if (jsonHint != null) {
            newFlags = (newFlags & 0xffffffff_00000000L) | (jsonHint.value() & 0x00000000_ffffffffL);
        }
        final JsonEmpty jsonEmpty = annotated.getDeclaredAnnotation(JsonEmpty.class);
        if (jsonEmpty != null) {
            if (jsonEmpty.toUndefined() != -1) {
                newFlags = (newFlags & 0xffff_0000_ffffffffL) | (((long) (jsonEmpty.toUndefined() & 0xffff)) << UNDEFINED_SHIFT);
            }
            if (jsonEmpty.toNull() != -1) {
                newFlags = (newFlags & 0x0000_ffff_ffffffffL) | (((long) (jsonEmpty.toNull() & 0xffff)) << NULL_SHIFT);
            }
        }
        ReflectConfig that = this;
        if (newFlags != flags) {
            if (shouldDerive) {
                that = that.derive();
            }
            that.flags = newFlags;
        }
        return that;
    }

    @NotNull
    ReflectConfig derive() {
        if ((flags & JsonHint.DERIVED) == 0L) {
            try {
                ReflectConfig that = (ReflectConfig) clone();
                that.flags |= JsonHint.DERIVED;
                return that;
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e);
            }
        } else {
            return this;
        }
    }
}
