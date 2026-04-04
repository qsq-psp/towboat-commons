package mujica.json.reflect;

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
public class JsonParserFrame extends JsonHandler { // frames are short-lived small objects

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserFrame.class);

    @NotNull
    final JsonContext context;
    
    final JsonParserFrame bottom;

    @CodeHistory(date = "2026/4/1")
    public enum StructureType {

        ARRAY, OBJECT;

        private static final long serialVersionUID = 0x96632A2729DFE59BL;
    }

    StructureType topType;

    Object key;

    public JsonParserFrame(@NotNull JsonContext context) {
        super();
        this.context = context;
        this.bottom = null;
    }

    public JsonParserFrame(@NotNull JsonParserFrame bottom) {
        super();
        this.context = bottom.context;
        this.bottom = bottom;
    }

    @NotNull
    public JsonContext getContext() {
        return context;
    }

    public JsonParserFrame getBottom() {
        return bottom;
    }

    public StructureType getTopType() {
        return topType;
    }

    public Object getKey() {
        return key;
    }

    @NotNull
    public JsonParserFrame open() {
        return new JsonParserFrame(this);
    }

    @Nullable
    public Object close() {
        return null;
    }

    public void structureValue(@Nullable Object value) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("structureValue({}) context {}", AppenderToStringBuilder.Java.get().apply(value), context);
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
}
