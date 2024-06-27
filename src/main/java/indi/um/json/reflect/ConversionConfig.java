package indi.um.json.reflect;

import indi.um.json.api.JsonConsumer;
import indi.um.json.api.JsonStructure;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created on 2022/7/12.
 */
public class ConversionConfig implements JsonStructure, Cloneable, Serializable {

    private static final long serialVersionUID = 0x1D7280CFD1B77115L;

    public static final int UNDEFINED_SHIFT = 16;

    int serializeConfig;

    int parseConfig;

    public ConversionConfig() {
        super();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ConversionConfig clone() {
        final ConversionConfig that = new ConversionConfig();
        that.setConversionConfig(this);
        return that;
    }

    void setConversionConfig(int serializeConfig, int parseConfig) {
        this.serializeConfig = serializeConfig;
        this.parseConfig = parseConfig;
    }

    void setConversionConfig(@NotNull ConversionConfig that) {
        setConversionConfig(that.serializeConfig, that.parseConfig);
    }

    public boolean anySerializeConfig(int flag) {
        return (serializeConfig & flag) != 0;
    }

    public boolean anySerializeConfig(int flag, boolean undefined) {
        if (undefined) {
            flag <<= UNDEFINED_SHIFT;
        }
        return anySerializeConfig(flag);
    }

    public boolean allSerializeConfigs(int flag) {
        return (serializeConfig & flag) == flag;
    }

    public boolean allSerializeConfigs(int flag, boolean undefined) {
        if (undefined) {
            flag <<= UNDEFINED_SHIFT;
        }
        return allSerializeConfigs(flag);
    }

    public boolean anyParseConfig(int flag) {
        return (parseConfig & flag) != 0;
    }

    public boolean allParseConfigs(int flag) {
        return (parseConfig & flag) == flag;
    }

    @Override
    public void toJson(@NotNull JsonConsumer jc) {
        jc.openObject();
        toJsonEntries(jc);
        jc.closeObject();
    }

    public void toJsonEntries(@NotNull JsonConsumer jc) {
        jc.key("serialize");
        jc.numberValue(serializeConfig);
        jc.key("parse");
        jc.numberValue(parseConfig);
    }

    @Override
    public int hashCode() {
        return serializeConfig * 0x2f + parseConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            ConversionConfig that = (ConversionConfig) obj;
            return this.serializeConfig == that.serializeConfig && this.parseConfig == that.parseConfig;
        }
        return false;
    }
}
