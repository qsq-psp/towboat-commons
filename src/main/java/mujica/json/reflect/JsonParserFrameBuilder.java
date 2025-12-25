package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2022/7/26", project = "Ultramarine", name = "FrameStructure")
@CodeHistory(date = "2025/12/22")
@FunctionalInterface
public interface JsonParserFrameBuilder {

    @NotNull
    JsonParserFrame createFrame(@NotNull JsonParser context, int containerType);
}
