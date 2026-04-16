package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

@CodeHistory(date = "2026/4/3")
public class PlainObjectFrame extends NopFrame {

    @NotNull
    final PlainObjectType type;

    @NotNull
    final Object self;

    public PlainObjectFrame(@NotNull NopFrame bottom, @NotNull PlainObjectType type, @NotNull Object self) {
        super(bottom);
        this.type = type;
        this.self = self;
    }

    @Nullable
    private PlainObjectField fieldForKey() {
        if (key instanceof String) {
            return type.fieldForName((String) key);
        } else if (key instanceof FastString) {
            return type.fieldForName(key.toString());
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public NopFrame open() {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                return field.createFrame(this); // pass object reference through key inside
            } catch (Throwable e) {
                context.getLogger().warn("", e);
                return new NopFrame(this);
            } finally {
                setKey(field); // now key can be used to store field
            }
        } else {
            context.getLogger().warn("creating frame bottom {}", this);
            return new NopFrame(this);
        }
    }

    @Nullable
    @Override
    public Object close() {
        return self;
    }

    @Override
    public void structureValue(@Nullable Object value) {
        if (key instanceof PlainObjectField && value != CollectionConstant.UNDEFINED) {
            try {
                ((PlainObjectField) key).setter.set(self, value);
            } catch (Throwable e) {
                context.getLogger().warn("structure value", e);
            }
        } else {
            context.getLogger().warn("structure value {} {}", value, this);
        }
    }

    @Override
    public void simpleValue(@Nullable Object value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("simple value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("simple value {} bottom {}", value, this);
        }
    }

    @Override
    public void nullValue() {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from().to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("null value field {} bottom {}", field, this, e);
            }
        } else {
            context.getLogger().warn("null value bottom {}", this);
        }
    }

    @Override
    public void booleanValue(boolean value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("boolean value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("boolean value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(int value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("int value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("int value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(long value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("long value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("long value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(float value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("float value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("float value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(double value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("double value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("double value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(@NotNull BigInteger value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("BigInteger value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("BigInteger value {} bottom {}", value, this);
        }
    }

    @Override
    public void numberValue(@NotNull FastNumber value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("FastNumber value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("FastNumber value {} bottom {}", value, this);
        }
    }

    @Override
    public void stringValue(@NotNull CharSequence value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("CharSequence value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("CharSequence value {} bottom {}", value, this);
        }
    }

    @Override
    public void stringValue(@NotNull FastString value) {
        final PlainObjectField field = fieldForKey();
        if (field != null) {
            try {
                field.declaredType.from(value).to(field.setter, self);
            } catch (Throwable e) {
                context.getLogger().warn("FastString value {} field {} bottom {}", value, field, this, e);
            }
        } else {
            context.getLogger().warn("FastString value {} bottom {}", value, this);
        }
    }
}
