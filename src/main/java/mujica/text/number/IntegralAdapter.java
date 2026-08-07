package mujica.text.number;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created on 2026/5/7.
 */
public abstract class IntegralAdapter implements IntegralAppender {

    protected IntegralAdapter() {
        super();
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        write((int) value, out);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        write((int) value, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        append((int) value, out);
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        append((int) value, out);
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        return stringify((int) value);
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        write((int) value, out);
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        write((int) value, out);
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        append((int) value, out);
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        append((int) value, out);
    }

    @NotNull
    @Override
    public String stringify(short value) {
        return stringify((int) value);
    }

    @Override
    public void write(char value, @NotNull ByteBuffer out) {
        write((int) value, out);
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        write((int) value, out);
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        append((int) value, out);
    }

    @Override
    public void append(char value, @NotNull StringBuffer out) {
        append((int) value, out);
    }

    @NotNull
    @Override
    public String stringify(char value) {
        return stringify((int) value);
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        out.put(stringify(value).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        out.writeCharSequence(sb, StandardCharsets.UTF_8);
    }

    @Override
    public abstract void append(int value, @NotNull StringBuilder out);

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        out.append(sb);
    }

    @NotNull
    @Override
    public String stringify(int value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(long value, @NotNull ByteBuffer out) {
        out.put(stringify(value).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        out.writeCharSequence(sb, StandardCharsets.UTF_8);
    }

    @Override
    public abstract void append(long value, @NotNull StringBuilder out);

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        out.append(sb);
    }

    @NotNull
    @Override
    public String stringify(long value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuffer out) {
        write(value.longValueExact(), out);
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        write(value.longValueExact(), out);
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        append(value.longValueExact(), out);
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        append(value.longValueExact(), out);
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }
}
