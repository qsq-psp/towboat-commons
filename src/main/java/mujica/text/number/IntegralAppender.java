package mujica.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;

@CodeHistory(date = "2025/2/25", name = "IntegralToStringFunction")
@CodeHistory(date = "2026/3/7")
public interface IntegralAppender extends Serializable {

    void write(byte value, @NotNull ByteBuffer out);

    void write(byte value, @NotNull ByteBuf out);

    void append(byte value, @NotNull StringBuilder out);

    void append(byte value, @NotNull StringBuffer out);

    @NotNull
    String stringify(byte value);

    void write(short value, @NotNull ByteBuffer out);

    void write(short value, @NotNull ByteBuf out);

    void append(short value, @NotNull StringBuilder out);

    void append(short value, @NotNull StringBuffer out);

    @NotNull
    String stringify(short value);

    void write(char value, @NotNull ByteBuffer out);

    void write(char value, @NotNull ByteBuf out);

    void append(char value, @NotNull StringBuilder out);

    void append(char value, @NotNull StringBuffer out);

    @NotNull
    String stringify(char value);

    void write(int value, @NotNull ByteBuffer out);

    void write(int value, @NotNull ByteBuf out);

    void append(int value, @NotNull StringBuilder out);

    void append(int value, @NotNull StringBuffer out);

    @NotNull
    String stringify(int value);

    void write(long value, @NotNull ByteBuffer out);

    void write(long value, @NotNull ByteBuf out);

    void append(long value, @NotNull StringBuilder out);

    void append(long value, @NotNull StringBuffer out);

    @NotNull
    String stringify(long value);

    void write(@NotNull BigInteger value, @NotNull ByteBuffer out);

    void write(@NotNull BigInteger value, @NotNull ByteBuf out);

    void append(@NotNull BigInteger value, @NotNull StringBuilder out);

    void append(@NotNull BigInteger value, @NotNull StringBuffer out);

    @NotNull
    String stringify(@NotNull BigInteger value);
}
