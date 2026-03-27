package mujica.io.codec;

import mujica.io.function.IOIntConsumer;
import mujica.io.function.IOIntSupplier;
import mujica.reflect.function.ByteSupplier;
import mujica.reflect.function.CharConsumer;
import mujica.reflect.modifier.CodeHistory;
import mujica.reflect.modifier.DataType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.ByteBuffer;

@CodeHistory(date = "2026/2/25")
public class PushPullDecoder {

    protected static final char REPLACEMENT = '\ufffd';

    public void reset() {
        // pass
    }
    
    public void push(byte in, @NotNull CharConsumer out) {
        if (in >= 0) {
            out.accept((char) in);
        } else {
            out.accept(REPLACEMENT);
        }
    }
    
    public void push(byte in, @NotNull StringBuilder out) {
        push(in, (CharConsumer) out::append);
    }
    
    public void push(byte in, @NotNull StringBuffer out) {
        push(in, (CharConsumer) out::append);
    }
    
    public void push(@DataType("u8") int in, @NotNull IOIntConsumer out) throws IOException {
        if ((in & 0x7f) == in) {
            out.accept(in);
        } else {
            out.accept(REPLACEMENT);
        }
    }
    
    public void push(@DataType("u8") int in, @NotNull Writer out) throws IOException {
        push(in, out::write);
    }
    
    public char pull(@NotNull ByteSupplier in) {
        final byte octet = in.getAsByte();
        if (octet >= 0) {
            return (char) octet;
        } else {
            return REPLACEMENT;
        }
    }

    public char pull(@NotNull ByteBuffer in) {
        return pull((ByteSupplier) in::get);
    }
    
    @DataType("u16")
    public int pull(@NotNull IOIntSupplier in) throws IOException {
        final int octet = in.getAsInt();
        if ((octet & 0x7f) == octet) {
            return octet;
        } else {
            return REPLACEMENT;
        }
    }

    @DataType("u16")
    public int pull(@NotNull InputStream in) throws IOException {
        return pull(() -> {
            int octet = in.read();
            if (octet == -1) {
                throw new EOFException();
            }
            return octet;
        });
    }

    @DataType("u16")
    public int pull(@NotNull DataInput in) throws IOException {
        return pull(in::readUnsignedByte);
    }
    
    public void finishPush(@NotNull CharConsumer out) {
        // pass
    }

    public void finishPush(@NotNull StringBuilder out) {
        finishPush((CharConsumer) out::append);
    }

    public void finishPush(@NotNull StringBuffer out) {
        finishPush((CharConsumer) out::append);
    }
    
    public void finishPush(@NotNull IOIntConsumer out) throws IOException {
        // pass
    }

    public void finishPush(@NotNull Writer out) throws IOException {
        finishPush((IOIntConsumer) out::write);
    }

    public boolean finishPullHasNext() {
        return false;
    }

    public char finishPullAsChar() {
        throw new IllegalStateException();
    }

    @DataType("u16")
    public int finishPullAsInt() {
        throw new IllegalStateException();
    }
}
