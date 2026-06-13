package mujica.reflect.basic;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/6/5")
public class Next<T extends NaturalType> extends NaturalType {

    private static final long serialVersionUID = 0x1081B58F2553956EL;

    @NotNull
    final T previous;

    public Next(@NotNull T previous) {
        super();
        this.previous = previous;
    }

    @Override
    public int ordinal() {
        return previous.ordinal() + 1;
    }
}
