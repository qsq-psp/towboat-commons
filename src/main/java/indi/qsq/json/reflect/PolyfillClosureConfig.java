package indi.qsq.json.reflect;

import indi.qsq.json.api.JsonConsumer;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2022/7/12.
 *
 * For classes in libraries
 */
class PolyfillClosureConfig extends ClosureConfig {

    private static final long serialVersionUID = 0x651A6CDA86224A3CL;

    ClosureConfig subClassConfig;

    String serializer;

    PolyfillClosureConfig() {
        super();
    }

    PolyfillClosureConfig(String serializer) {
        super();
        this.serializer = serializer;
    }

    PolyfillClosureConfig setSubClassConfig(ClosureConfig subClassConfig) {
        this.subClassConfig = subClassConfig;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    PolyfillClosureConfig setSubClassConfig() {
        return setSubClassConfig(this);
    }

    @Override
    ClosureConfig getSubClassConfig() {
        return subClassConfig;
    }

    PolyfillClosureConfig setSerializer(String serializer) {
        this.serializer = serializer;
        return this;
    }

    @Override
    String getSerializer() {
        return serializer;
    }

    @Override
    public void toJsonEntries(@NotNull JsonConsumer jc) {
        super.toJsonEntries(jc);
        if (serializer != null) {
            jc.key("serializer");
            jc.stringValue(serializer);
        }
    }
}
