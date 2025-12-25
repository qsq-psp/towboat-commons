package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/12/22")
class JsonType implements JsonParserFrameBuilder {

    @NotNull
    @Override
    public JsonParserFrame createFrame(@NotNull JsonParser context, int containerType) {
        return new JsonParserFrame(context);
    }
}
