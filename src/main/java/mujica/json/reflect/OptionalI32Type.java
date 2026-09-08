package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/8/16")
class OptionalI32Type extends JsonType {

    @NotNull
    I32Type contentType;

    OptionalI32Type(long flags) {
        super(flags);
        contentType = new I32Type(flags);
    }
}
