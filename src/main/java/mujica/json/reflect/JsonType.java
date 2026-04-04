package mujica.json.reflect;

import mujica.json.entity.JsonHandler;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/12/22")
class JsonType implements JsonParserFrameBuilder, JsonContextSerializer {

    static final int FLAG_DYNAMIC = 0x0001;
    static final int FLAG_ALWAYS_BUILD = 0x0002;
    static final int FLAG_STRICT_ORDER = 0x0004;

    int flags;

    @NotNull
    @Override
    public JsonParserFrame createFrame(@NotNull JsonParserFrame bottom) {
        return new JsonParserFrame(bottom);
    }

    @Override
    public void serialize(Object in, @NotNull JsonHandler out, @NotNull JsonContext context) {
        //
    }
}
