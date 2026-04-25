package mujica.json.reflect;

import mujica.ds.generic.set.CollectionConstant;
import mujica.json.entity.FastString;
import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.format.AppenderToStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CodeHistory(date = "2020/12/27", project = "webbiton", name = "JsonLayerHandler")
@CodeHistory(date = "2021/3/23", project = "webbiton", name = "JsonDummyLayer")
@CodeHistory(date = "2021/12/28", project = "va", name = "JsonParser.Frame")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "ParserFrame")
@CodeHistory(date = "2022/6/11", project = "Ultramarine", name = "ConverterFrame")
@CodeHistory(date = "2025/12/20")
class NopFrame extends JsonHandler { // frames are short-lived small objects

    private static final Logger LOGGER = LoggerFactory.getLogger(NopFrame.class);

    @NotNull
    final JsonContext context;
    
    final NopFrame parent;

    @CodeHistory(date = "2026/4/1", name = "StructureType")
    @CodeHistory(date = "2026/4/9")
    public enum StructureShape {

        ARRAY, OBJECT;

        private static final long serialVersionUID = 0x96632A2729DFE59BL;
    }

    StructureShape shape;

    Object key;

    public NopFrame(@NotNull JsonContext context) {
        super();
        this.context = context;
        this.parent = null;
    }

    public NopFrame(@NotNull NopFrame parent) {
        super();
        this.context = parent.context;
        this.parent = parent;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    @NotNull
    NopFrame open() {
        return new NopFrame(this);
    }

    @Nullable
    Object close() {
        return CollectionConstant.UNDEFINED;
    }

    public void structureValue(@Nullable Object value) {
        if (value != CollectionConstant.UNDEFINED && LOGGER.isInfoEnabled()) {
            LOGGER.info("structureValue({}) this {}", AppenderToStringBuilder.Java.get().apply(value), this);
        }
    }

    @Override
    public void openArray() {
        LOGGER.error("openArray() context {}", context);
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeArray() {
        LOGGER.error("closeArray() context {}", context);
        throw new UnsupportedOperationException();
    }

    @Override
    public void openObject() {
        LOGGER.error("openObject() context {}", context);
        throw new UnsupportedOperationException();
    }

    @Override
    public void closeObject() {
        LOGGER.error("closeObject() context {}", context);
        throw new UnsupportedOperationException();
    }

    @Override
    public void stringKey(@NotNull String key) { // a cut JsonHandler that only handle values
        LOGGER.error("stringKey({}) context {}", key, context);
        throw new UnsupportedOperationException();
    }

    @Override
    public void stringKey(@NotNull FastString key) { // a cut JsonHandler that only handle values
        LOGGER.error("stringKey({}) context {}", key, context);
        throw new UnsupportedOperationException();
    }

    @Override
    @NotNull
    public String toString() {
        return getClass().getSimpleName() + "[key = " + key + "]";
    }
}
