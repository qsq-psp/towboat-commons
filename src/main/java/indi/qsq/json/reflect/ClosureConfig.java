package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/12.
 *
 * Closure includes class, interface and package
 */
class ClosureConfig extends ConversionConfig {

    private static final long serialVersionUID = 0x48569E5CDB4BECB5L;

    static final int SHIFT_LOGGER                    = 24;
    static final int SHIFT_ORDERED                   = 16;
    static final int SHIFT_RANDOM                    = 8;

    static final int FLAG_COLLECT_FIELDS             = 0x01;
    static final int FLAG_COLLECT_SETTERS            = 0x02;
    static final int FLAG_COLLECT_GETTERS            = 0x04;
    static final int FLAG_COLLECT_NON_PUBLIC         = 0x08;
    static final int FLAG_SUPER_CLASS                = 0x10;
    static final int FLAG_SUPER_INTERFACE            = 0x20;
    static final int FLAG_USE_METHOD_HANDLE          = 0x40;
    static final int FLAG_USE_MAP_BASED              = 0x80;

    int closureConfig;

    ClosureConfig() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ConversionConfig clone() {
        return (new ClosureConfig()).setClosureConfig(this);
    }

    ClosureConfig setClosureConfig(int closureConfig) {
        this.closureConfig = closureConfig;
        return this;
    }

    ClosureConfig setClosureConfig(int closureConfig, int serializeConfig, int parseConfig) {
        setConversionConfig(serializeConfig, parseConfig);
        return setClosureConfig(closureConfig);
    }

    ClosureConfig setClosureConfig(ClosureConfig that) {
        return setClosureConfig(that.closureConfig, that.serializeConfig, that.parseConfig);
    }

    boolean anyClassConfig(int flag) {
        return (closureConfig & flag) != 0;
    }

    ClosureConfig getSubClassConfig() {
        return null;
    }

    String getSerializer() {
        return null;
    }

    protected ReflectClass build() {
        if (anyClassConfig(FLAG_COLLECT_FIELDS | FLAG_COLLECT_SETTERS | FLAG_COLLECT_GETTERS | FLAG_SUPER_CLASS | FLAG_SUPER_INTERFACE)) {
            return new ReflectClass.MapBased();
        } else {
            return new ReflectClass();
        }
    }

    protected ReflectClass load(Class<?> clazz) {
        final ReflectClass loaded = build();
        loaded.setClosureConfig(this);
        final String serializer = getSerializer();
        if (serializer != null) {
            loaded.collectSerializer(serializer);
        }
        loaded.collect(clazz);
        return loaded.simplify();
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        jc.key("closure");
        jc.numberValue(closureConfig);
    }

    @Override
    public int hashCode() {
        return (serializeConfig * 0x2f + parseConfig) * 0x2f + closureConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            ClosureConfig that = (ClosureConfig) obj;
            return this.serializeConfig == that.serializeConfig && this.parseConfig == that.parseConfig && this.closureConfig == that.closureConfig;
        }
        return false;
    }
}
