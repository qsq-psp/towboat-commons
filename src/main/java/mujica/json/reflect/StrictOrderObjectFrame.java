package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/4/19.
 */
@CodeHistory(date = "2026/4/19")
class StrictOrderObjectFrame extends PlainObjectFrame {

    StrictOrderObjectFrame(@NotNull NopFrame bottom, @NotNull PlainObjectType type, @NotNull Object self) {
        super(bottom, type, self);
    }

    //
}
