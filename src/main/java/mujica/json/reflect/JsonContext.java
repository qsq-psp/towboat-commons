package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@CodeHistory(date = "2021/1/2", project = "webbiton", name = "JsonLayerStack")
@CodeHistory(date = "2021/12/28", project = "va", name = "JsonParser")
@CodeHistory(date = "2022/1/3", project = "infrastructure", name = "JsonParser")
@CodeHistory(date = "2022/6/9", project = "Ultramarine", name = "JsonSerializer")
@CodeHistory(date = "2022/7/12", project = "Ultramarine", name = "JsonParser")
@CodeHistory(date = "2025/10/28", name = "JsonParser")
@CodeHistory(date = "2026/4/3")
public class JsonContext { // no longer implements JsonConsumer

    final HashMap<Class<?>, JsonType> reflectCache = new HashMap<>();

    @NotNull
    JsonType forClass(@NotNull Class<?> clazz) {
        return reflectCache.get(clazz);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonContext.class);

    @NotNull
    Logger logger = LOGGER;

    @NotNull
    public Logger getLogger() {
        return logger;
    }

    public void setLogger(@NotNull Logger logger) {
        this.logger = logger;
    }

    // create on need
    // final JsonParserStack stack = new JsonParserStack();
}
