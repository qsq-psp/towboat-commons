package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Some implementations supports percent sign
 */
@CodeHistory(date = "2018/7/9", project = "existence", name = "DoubleToStringConverter")
@CodeHistory(date = "2025/2/25")
public interface DecimalToStringFunction {

    void append(@NotNull CharSequence num, int numRadix, @NotNull StringBuilder out);

    @NotNull
    String stringify(@NotNull CharSequence num, int numRadix);

    void append(@NotNull BigDecimal decimal, @NotNull StringBuilder out);

    @NotNull
    String stringify(@NotNull BigDecimal decimal);
}
