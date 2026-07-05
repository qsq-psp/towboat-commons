package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/12", name = "IntArrayType")
@CodeHistory(date = "2026/6/16")
class I32ArrayType extends JsonType {

    private static final long serialVersionUID = 0xBE7C48FDF066E68DL;

    @NotNull
    I32Type componentType;

    I32ArrayType(long flags, @NotNull I32Type componentType) {
        super(flags);
        this.componentType = componentType;
    }
}
