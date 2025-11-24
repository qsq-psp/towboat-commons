package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;

@CodeHistory(date = "2022/6/5", project = "Ultramarine")
@CodeHistory(date = "2025/11/14")
class Setter {

    Setter() { // for finding usage
        super(); // pass
    }

    protected void invoke(@Nullable Object self, @Nullable Object value) {
        // pass
    }

    @NotNull
    protected Setter tryUnreflect(@NotNull MethodHandles.Lookup lookup) {
        return this; // if fail to unreflect
    }
}
