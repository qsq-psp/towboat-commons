package mujica.ds.slot;

import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

@CodeHistory(date = "2026/9/5")
public class Derivative<S extends Real> {

    @NotNull
    @Name(value = "base variable", language = "en")
    final S x;

    @NotNull
    @Name(value = "derivative", language = "en")
    final S d;

    public Derivative(@NotNull S x, @NotNull S d) {
        super();
        this.x = x;
        this.d = d;
    }
}
