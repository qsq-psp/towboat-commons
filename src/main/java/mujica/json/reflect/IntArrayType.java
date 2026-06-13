package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/12")
class IntArrayType extends JsonType {

    private static final long serialVersionUID = 0xBE7C48FDF066E68DL;

    @NotNull
    IntType componentType;

    IntArrayType(long flags, @NotNull IntType componentType) {
        super(flags);
        this.componentType = componentType;
    }
}
