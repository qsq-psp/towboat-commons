package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2025/2/25")
public interface IntegralToStringFunction {

    void append(int value, @NotNull StringBuilder out);

    @NotNull
    String stringify(int value);

    void append(long value, @NotNull StringBuilder out);

    @NotNull
    String stringify(long value);

    void append(@NotNull BigInteger value, @NotNull StringBuilder out);

    @NotNull
    String stringify(@NotNull BigInteger value);
}
