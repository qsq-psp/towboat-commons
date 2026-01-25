package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/6")
public abstract class StringJsonWriter extends JsonWriter {

    public abstract void reset();

    @NotNull
    public abstract CharSequence getCharSequence();

    @NotNull
    public CharSequence getString() {
        return getCharSequence().toString();
    }
}
