package mujica.json.io;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/1/6", name = "StringJsonWriter")
@CodeHistory(date = "2026/3/21")
public abstract class JsonStringWriter extends JsonWriter {

    @NotNull
    public abstract CharSequence getCharSequence();

    @NotNull
    public String getString() {
        return getCharSequence().toString();
    }
}
