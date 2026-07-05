package mujica.ds.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/4/3")
public class JoinedIntegralAppender implements IntegralAppender {

    @NotNull
    protected final IntegralAppender[] parts;

    @NotNull
    protected final String separator;

    @NotNull
    protected final byte[] separatorBytes;

    public JoinedIntegralAppender(@NotNull IntegralAppender[] parts, @NotNull String separator) {
        super();
        this.parts = parts;
        this.separator = separator;
        this.separatorBytes = separator.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @NotNull
    @Override
    public String stringify(byte value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(short value, @NotNull ByteBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @NotNull
    @Override
    public String stringify(short value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(char value, @NotNull ByteBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(char value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @NotNull
    @Override
    public String stringify(char value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }

    @Override
    public void write(int value, @NotNull ByteBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
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
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
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
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.put(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.writeBytes(separatorBytes);
            }
            parts[index].write(value, out);
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        final int length = parts.length;
        for (int index = 0; index < length; index++) {
            if (index != 0) {
                out.append(separator);
            }
            parts[index].append(value, out);
        }
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        final StringBuilder sb = new StringBuilder();
        append(value, sb);
        return sb.toString();
    }
}
