package mujica.json.reflect;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/4/12")
class ContainerFrame extends NopFrame {

    public ContainerFrame(@NotNull JsonContext context) {
        super(context);
    }

    public ContainerFrame(@NotNull NopFrame parent) {
        super(parent);
    }
}
