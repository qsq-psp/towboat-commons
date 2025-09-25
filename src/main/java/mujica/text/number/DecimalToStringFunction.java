package mujica.text.number;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

/**
 * Created in existence on 2018/7/9, named DoubleToStringConverter.
 * Created on 2025/2/25.
 * Some implementations supports percent sign
 */
public interface DecimalToStringFunction {

    void append(@NotNull CharSequence num, int numRadix, @NotNull StringBuilder out);

    @NotNull
    String stringify(@NotNull CharSequence num, int numRadix);

    void append(@NotNull BigDecimal decimal, @NotNull StringBuilder out);

    @NotNull
    String stringify(@NotNull BigDecimal decimal);
}
