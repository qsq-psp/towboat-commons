package mujica.json.reflect;

import mujica.json.entity.FastNumber;
import mujica.json.entity.FastString;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * frames are short-lived small objects
 */
@CodeHistory(date = "2020/12/27", project = "webbiton", name = "JsonLayerHandler")
@CodeHistory(date = "2021/3/23", project = "webbiton", name = "JsonDummyLayer")
@CodeHistory(date = "2021/12/28", project = "va", name = "JsonParser.Frame")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "ParserFrame")
@CodeHistory(date = "2022/6/11", project = "Ultramarine", name = "ConverterFrame")
@CodeHistory(date = "2025/12/20")
public class JsonParserFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonParserFrame.class);

    @NotNull
    protected final JsonParser context;

    public JsonParserFrame(@NotNull JsonParser context) {
        super();
        this.context = context;
    }

    @NotNull
    public JsonParserFrame open(@Nullable Object key, int containerType) {
        return new JsonParserFrame(context);
    }

    public void close(@NotNull JsonParserFrame frame) {
        LOGGER.info("close {} ignored in {}", frame, context);
    }

    public void nullValue(@Nullable Object key) {
        LOGGER.info("[{}] null ignored in {}", key, context);
    }

    public void booleanValue(@Nullable Object key, boolean value) {
        LOGGER.info("[{}] boolean {} ignored in {}", key, value, context);
    }

    public void numberValue(@Nullable Object key, long value) {
        LOGGER.info("[{}] long {} ignored in {}", key, value, context);
    }

    public void numberValue(@Nullable Object key, double value) {
        LOGGER.info("[{}] double {} ignored in {}", key, value, context);
    }

    public void numberValue(@Nullable Object key, @NotNull FastNumber value) {
        LOGGER.info("[{}] FastNumber {} ignored in {}", key, value, context);
    }

    public void stringValue(@Nullable Object key, @NotNull String value) {
        LOGGER.info("[{}] String {} ignored in {}", key, value, context);
    }

    public void stringValue(@Nullable Object key, @NotNull FastString value) {
        LOGGER.info("[{}] FastString {} ignored in {}", key, value, context);
    }
}
