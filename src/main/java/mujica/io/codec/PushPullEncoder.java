package mujica.io.codec;

import io.netty.buffer.ByteBuf;
import mujica.io.function.IOByteConsumer;
import mujica.io.function.IOIntSupplier;
import mujica.reflect.function.ByteConsumer;
import mujica.reflect.function.CharSupplier;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@CodeHistory(date = "2026/3/25")
public class PushPullEncoder {

    // protected static final int REPLACEMENT = 0x80efbfbd;

    public void reset() {
        // pass
    }

    public void push(char in, @NotNull ByteConsumer out) {
        if (in < 0x80) {
            out.accept((byte) in);
        }
    }

    public void push(char in, @NotNull ByteBuffer out) {
        push(in, (ByteConsumer) out::put);
    }

    public void push(char in, @NotNull ByteBuf out) {
        push(in, (ByteConsumer) out::writeByte);
    }

    public void push(char in, @NotNull IOByteConsumer out) throws IOException {
        if (in < 0x80) {
            out.accept((byte) in);
        }
    }

    public void push(char in, @NotNull OutputStream out) throws IOException {
        push(in, (IOByteConsumer) out::write);
    }

    public void push(char in, @NotNull DataOutput out) throws IOException {
        push(in, (IOByteConsumer) out::writeByte);
    }

    public byte pull(@NotNull CharSupplier in) {
        while (true) {
            int ch = in.getAsChar();
            if (ch < 0x80) {
                return (byte) ch;
            }
        }
    }

    public byte pull(@NotNull CharBuffer in) {
        return pull((CharSupplier) in::get);
    }

    public byte pull(@NotNull IOIntSupplier in) throws IOException {
        while (true) {
            int ch = in.getAsInt();
            if (ch < 0x80) {
                return (byte) ch;
            }
        }
    }

    public byte pull(@NotNull Reader in) throws IOException {
        return pull(() -> {
            int ch = in.read();
            if (ch == -1) {
                throw new EOFException();
            }
            return ch;
        });
    }

    public void finishPush(@NotNull ByteConsumer out) {
        // pass
    }

    public void finishPush(@NotNull ByteBuffer out) {
        finishPush((ByteConsumer) out::put);
    }

    public void finishPush(@NotNull ByteBuf out) {
        finishPush((ByteConsumer) out::writeByte);
    }

    public void finishPush(@NotNull IOByteConsumer out) throws IOException {
        // pass
    }

    public void finishPush(@NotNull OutputStream out) throws IOException {
        finishPush((IOByteConsumer) out::write);
    }

    public void finishPush(@NotNull DataOutput out) throws IOException {
        finishPush((IOByteConsumer) out::writeByte);
    }

    public boolean finishPullHasNext() {
        return false;
    }

    public byte finishPullAsByte() {
        throw new IllegalStateException();
    }

    public int finishPullAsInt() {
        throw new IllegalStateException();
    }
}
