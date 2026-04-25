package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;

@CodeHistory(date = "2025/2/25", name = "IntegralToStringFunction")
@CodeHistory(date = "2026/3/7")
public interface IntegralAppender extends Serializable {

    void acceptByte(byte value, @NotNull StringBuilder out);

    void acceptByte(byte value, @NotNull StringBuffer out);

    void acceptShort(short value, @NotNull StringBuilder out);

    void acceptShort(short value, @NotNull StringBuffer out);

    void acceptChar(char value, @NotNull StringBuilder out);

    void acceptChar(char value, @NotNull StringBuffer out);

    void acceptInt(int value, @NotNull StringBuilder out);

    void acceptInt(int value, @NotNull StringBuffer out);

    void acceptLong(long value, @NotNull StringBuilder out);

    void acceptLong(long value, @NotNull StringBuffer out);

    void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out);

    void acceptBig(@NotNull BigInteger value, @NotNull StringBuffer out);
}
