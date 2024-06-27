package indi.qsq.json.entity;

import indi.qsq.util.value.ReadFlagPole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Created on 2023/5/2.
 */
public class AccessObject extends JsonObject {

    private static final long serialVersionUID = 0x3E2FB7C4DD7567D8L;

    public static class InaccessibleException extends RuntimeException {

        private static final long serialVersionUID = 0x501798D7B251D66CL;
    }

    public enum AccessLevel {

        FREE {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                return addedAccessLevel;
            }
            @Override
            public boolean testExtend() {
                return true;
            }
            @Override
            public boolean testConfigure() {
                return true;
            }
            @Override
            public boolean testWrite() {
                return true;
            }
        },
        SILENT_PREVENT_EXTENSION {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                if (addedAccessLevel != FREE) {
                    return addedAccessLevel;
                } else {
                    return this;
                }
            }
            @Override
            public boolean testExtend() {
                return false;
            }
            @Override
            public boolean testConfigure() {
                return true;
            }
            @Override
            public boolean testWrite() {
                return true;
            }
        },
        STRICT_PREVENT_EXTENSION {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                switch (addedAccessLevel) {
                    default:
                        return this;
                    case SILENT_SEALED:
                        return STRICT_SEALED;
                    case SILENT_FROZEN:
                        return STRICT_FROZEN;
                    case STRICT_SEALED:
                    case STRICT_FROZEN:
                        return addedAccessLevel;
                }
            }
            @Override
            public boolean testExtend() {
                throw new InaccessibleException();
            }
            @Override
            public boolean testConfigure() {
                return true;
            }
            @Override
            public boolean testWrite() {
                return true;
            }
        },
        SILENT_SEALED {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                switch (addedAccessLevel) {
                    default:
                        return this;
                    case STRICT_PREVENT_EXTENSION:
                        return STRICT_SEALED;
                    case SILENT_FROZEN:
                    case STRICT_SEALED:
                    case STRICT_FROZEN:
                        return addedAccessLevel;
                }
            }
            @Override
            public boolean testExtend() {
                return false;
            }
            @Override
            public boolean testConfigure() {
                return false;
            }
            @Override
            public boolean testWrite() {
                return true;
            }
        },
        STRICT_SEALED {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                switch (addedAccessLevel) {
                    default:
                        return this;
                    case SILENT_FROZEN:
                    case STRICT_FROZEN:
                        return STRICT_FROZEN;
                }
            }
            @Override
            public boolean testExtend() {
                throw new InaccessibleException();
            }
            @Override
            public boolean testConfigure() {
                throw new InaccessibleException();
            }
            @Override
            public boolean testWrite() {
                return true;
            }
        },
        SILENT_FROZEN {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                switch (addedAccessLevel) {
                    default:
                        return this;
                    case STRICT_PREVENT_EXTENSION:
                    case STRICT_SEALED:
                    case STRICT_FROZEN:
                        return addedAccessLevel;
                }
            }
            @Override
            public boolean testExtend() {
                return false;
            }
            @Override
            public boolean testConfigure() {
                return false;
            }
            @Override
            public boolean testWrite() {
                return false;
            }
        }, STRICT_FROZEN {
            @Override
            public AccessLevel add(AccessLevel addedAccessLevel) {
                return this;
            }
            @Override
            public boolean testExtend() {
                throw new InaccessibleException();
            }
            @Override
            public boolean testConfigure() {
                throw new InaccessibleException();
            }
            @Override
            public boolean testWrite() {
                throw new InaccessibleException();
            }
        };

        private static final long serialVersionUID = 0x9E5A95D9A5381362L;

        public abstract AccessLevel add(AccessLevel addedAccessLevel);

        public abstract boolean testExtend();

        public abstract boolean testConfigure();

        public abstract boolean testWrite();
    }

    @NotNull
    private AccessLevel accessLevel = AccessLevel.FREE;

    public boolean raiseAccessLevel(AccessLevel addedAccessLevel) {
        final AccessLevel newAccessLevel = accessLevel.add(addedAccessLevel);
        if (accessLevel != newAccessLevel) {
            accessLevel = newAccessLevel;
            return true;
        } else {
            return false;
        }
    }

    @NotNull
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/preventExtensions
     * The Object.preventExtensions() static method prevents new properties from ever being added to an object
     * (i.e. prevents future extensions to the object). It also prevents the object's prototype from being re-assigned.
     */
    public boolean preventExtensions(boolean strict) {
        return raiseAccessLevel(strict ? AccessLevel.STRICT_PREVENT_EXTENSION : AccessLevel.SILENT_PREVENT_EXTENSION);
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/isExtensible
     * The Object.isExtensible() static method determines if an object is extensible (whether it can have new properties
     * added to it).
     */
    public boolean isExtensible() {
        return accessLevel == AccessLevel.FREE;
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/seal
     */
    public boolean seal(boolean strict) {
        return raiseAccessLevel(strict ? AccessLevel.STRICT_SEALED : AccessLevel.SILENT_SEALED);
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/isSealed
     * The Object.isSealed() static method determines if an object is sealed.
     * An object is sealed if it is not extensible and if all its properties are non-configurable and therefore not
     * removable (but not necessarily non-writable).
     */
    public boolean isSealed() {
        return accessLevel.ordinal() >= AccessLevel.SILENT_SEALED.ordinal();
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/freeze
     */
    public boolean freeze(boolean strict) {
        return raiseAccessLevel(strict ? AccessLevel.STRICT_FROZEN : AccessLevel.SILENT_FROZEN);
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/isFrozen
     * The Object.isFrozen() static method determines if an object is frozen.
     * An object is frozen if and only if it is not extensible, all its properties are non-configurable, and all its
     * data properties (that is, properties which are not accessor properties with getter or setter components) are
     * non-writable.
     */
    public boolean isFrozen() {
        return accessLevel.ordinal() >= AccessLevel.SILENT_FROZEN.ordinal();
    }

    @FunctionalInterface
    public interface PropertyOperator {

        Object apply(AccessObject context, String key, Object value);
    }

    public static class PropertyDescriptor implements ReadFlagPole, Serializable {

        private static final long serialVersionUID = 0x83F3F05FC5F2D0B3L;

        public static final int FLAG_WRITABLE           = 0x01;
        public static final int FLAG_CONFIGURABLE       = 0x02;
        public static final int FLAG_ENUMERABLE         = 0x04;
        public static final int FLAG_STRICT             = 0x08;

        public Object value;

        public PropertyOperator get, set;

        public final int flags;

        public PropertyDescriptor(int flags) {
            super();
            this.flags = flags;
        }

        public PropertyDescriptor() {
            this(FLAG_WRITABLE | FLAG_CONFIGURABLE | FLAG_ENUMERABLE);
        }

        public int getFlagValue() {
            return flags;
        }

        public boolean testConfigure() {
            if ((flags & FLAG_CONFIGURABLE) != 0) {
                return true;
            }
            if ((flags & FLAG_STRICT) == 0) {
                return false;
            }
            throw new InaccessibleException();
        }

        public Object getValue(AccessObject context, String name) {
            if (get != null) {
                return get.apply(context, name, value);
            } else {
                return value;
            }
        }

        public boolean setValue(AccessObject context, String name, Object newValue) {
            if ((flags & FLAG_WRITABLE) != 0) {
                if (set != null) {
                    value = set.apply(context, name, newValue);
                } else {
                    value = newValue;
                }
                return true;
            }
            if ((flags & FLAG_STRICT) == 0) {
                return false;
            }
            throw new InaccessibleException();
        }

        public Object putValue(AccessObject context, String name, Object newValue) {
            if ((flags & FLAG_WRITABLE) != 0) {
                Object oldValue = getValue(context, name);
                if (set != null) {
                    value = set.apply(context, name, newValue);
                } else {
                    value = newValue;
                }
                return oldValue;
            }
            if ((flags & FLAG_STRICT) == 0) {
                return getValue(context, name);
            }
            throw new InaccessibleException();
        }
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/defineProperty
     */
    public boolean defineProperty(@NotNull String name, @Nullable PropertyDescriptor descriptor) {
        if (super.containsKey(name)) {
            if (getAccessLevel().testConfigure()) {
                Object object = super.get(name);
                if (!(object instanceof PropertyDescriptor) || ((PropertyDescriptor) object).testConfigure()) {
                    super.put(name, descriptor);
                    return true;
                }
            }
        } else {
            if (getAccessLevel().testExtend()) {
                super.put(name, descriptor);
                return true;
            }
        }
        return false;
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/getOwnPropertyDescriptor
     */
    @Nullable
    public PropertyDescriptor getOwnPropertyDescriptor(@NotNull String name) {
        final Object descriptor = super.get(name);
        if (descriptor instanceof PropertyDescriptor) {
            return (PropertyDescriptor) descriptor;
        } else {
            return null;
        }
    }

    @Override
    public Object get(@NotNull Object name) {
        final Object object = super.get(name);
        if (object instanceof PropertyDescriptor) {
            return ((PropertyDescriptor) object).getValue(this, (String) name);
        } else {
            return object;
        }
    }

    public boolean set(@NotNull String name, @Nullable Object value) {
        if (super.containsKey(name)) {
            if (getAccessLevel().testConfigure()) {
                Object object = super.get(name);
                if (object instanceof PropertyDescriptor) {
                    return ((PropertyDescriptor) object).setValue(this, name, value);
                } else {
                    super.put(name, value);
                    return true;
                }
            }
        } else {
            if (getAccessLevel().testExtend()) {
                super.put(name, value);
                return true;
            }
        }
        return false;
    }

    @Override
    public Object put(@NotNull String name, @Nullable Object value) {
        if (super.containsKey(name)) {
            if (getAccessLevel().testConfigure()) {
                Object object = super.get(name);
                if (object instanceof PropertyDescriptor) {
                    return ((PropertyDescriptor) object).putValue(this, name, value);
                } else {
                    super.put(name, value);
                    return object;
                }
            }
        } else {
            if (getAccessLevel().testExtend()) {
                super.put(name, value);
                return null;
            }
        }
        return get(name);
    }
}
