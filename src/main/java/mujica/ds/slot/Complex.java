package mujica.ds.slot;

import mujica.reflect.modifier.Name;
import org.jetbrains.annotations.NotNull;

/**
 * Created on 2026/6/5.
 */
public class Complex<S extends Real> {

    @NotNull
    @Name(value = "real part", language = "en")
    @Name(value = "实部", language = "zh")
    final S a;

    @NotNull
    @Name(value = "imaginary part", language = "en")
    @Name(value = "虚部", language = "zh")
    final S b;

    public Complex(@NotNull S a, @NotNull S b) {
        super();
        this.a = a;
        this.b = b;
    }
}
