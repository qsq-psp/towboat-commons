package mujica.io.nest;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Arrays;

/**
 * Created on 2025/10/13.
 */
@CodeHistory(date = "2025/10/13")
public class LimitedInputHandle implements LimitedInput, Serializable {

    private static final long serialVersionUID = 0xb3da61e76429543cL;

    private Behavior behavior = Behavior.END;

    private long remaining = Long.MAX_VALUE;

    @NotNull
    @Override
    public Behavior getBehavior() {
        return behavior;
    }

    @Override
    public void setBehavior(@NotNull Behavior newBehavior) {
        behavior = newBehavior;
    }

    @Override
    public long getRemaining() {
        return remaining;
    }

    @Override
    public long setRemaining(long newRemaining) {
        final long oldRemaining = remaining;
        remaining = newRemaining;
        return oldRemaining;
    }

    @NotNull
    public InputStream createInputStream(@NotNull InputStream in) {
        return new SecretInputStream(in);
    }

    @CodeHistory(date = "2025/10/13")
    private class SecretInputStream extends FilterInputStream {

        SecretInputStream(@NotNull InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            if (remaining > 0L) {
                int data = in.read();
                if (data != -1) {
                    remaining--;
                    return data;
                }
                if (behavior == Behavior.PAD) {
                    remaining--;
                    return 0;
                }
                if (behavior == Behavior.THROW) {
                    throw new EOFException("in");
                }
            }
            return -1;
        }

        @Override
        public int read(@NotNull byte[] array, int offset, int length) throws IOException {
            length = (int) Math.min(length, remaining);
            if (length > 0) {
                int count = in.read(array, offset, length);
                if (count > 0) {
                    remaining -= count;
                    return count;
                }
                if (behavior == Behavior.PAD) {
                    Arrays.fill(array, offset, offset + length, (byte) 0);
                    remaining -= length;
                    return length;
                }
                if (behavior == Behavior.THROW) {
                    throw new EOFException("in");
                }
            }
            return -1;
        }

        @Override
        public long skip(long n) throws IOException {
            n = Math.min(n, remaining);
            if (n > 0L) {
                long m = in.skip(n);
                if (m > 0L) {
                    remaining -= m;
                    return m;
                }
                if (behavior == Behavior.PAD) {
                    remaining -= n;
                    return n;
                }
                if (behavior == Behavior.THROW) {
                    throw new EOFException("in");
                }
            }
            return 0L;
        }

        @Override
        public int available() throws IOException {
            long value;
            if (behavior == Behavior.PAD) {
                value = remaining;
            } else {
                value = Math.min(remaining, in.available());
            }
            return (int) Math.min(value, Integer.MAX_VALUE);
        }
    }
}
