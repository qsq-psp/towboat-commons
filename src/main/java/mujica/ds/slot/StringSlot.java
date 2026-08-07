package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/7/29")
public class StringSlot {

    @NotNull
    String string;

    public StringSlot(@NotNull String string) {
        super();
        this.string = string;
    }

    @NotNull
    public String getString() {
        return string;
    }

    public void setString(@NotNull String string) {
        this.string = string;
    }
}
