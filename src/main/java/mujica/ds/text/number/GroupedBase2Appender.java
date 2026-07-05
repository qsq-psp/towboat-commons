package mujica.ds.text.number;

import io.netty.buffer.ByteBuf;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@CodeHistory(date = "2026/3/9", name = "BinaryAppender")
@CodeHistory(date = "2026/4/26")
public class GroupedBase2Appender implements IntegralAppender {

    private static final long serialVersionUID = 0x2CC9A7B00AAB4E91L;

    @CodeHistory(date = "2026/3/10")
    private interface BitAppender {

        @NotNull
        BitAppender write(boolean value, @NotNull ByteBuffer out);

        @NotNull
        BitAppender write(boolean value, @NotNull ByteBuf out);

        @NotNull
        BitAppender append(boolean value, @NotNull StringBuilder out);

        @NotNull
        BitAppender append(boolean value, @NotNull StringBuffer out);

        void finish(@NotNull ByteBuffer out);

        void finish(@NotNull ByteBuf out);

        void finish(@NotNull StringBuilder out);

        void finish(@NotNull StringBuffer out);
    }

    private static final BitAppender SUBSEQUENT = new BitAppender() {

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuffer out) {
            out.put(value ? (byte) '1' : (byte) '0');
            return this;
        }

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuf out) {
            out.writeByte(value ? '1' : '0');
            return this;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuilder out) {
            out.append(value ? '1' : '0');
            return this;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuffer out) {
            out.append(value ? '1' : '0');
            return this;
        }

        @Override
        public void finish(@NotNull ByteBuffer out) {
            // pass
        }

        @Override
        public void finish(@NotNull ByteBuf out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            // pass
        }
    };

    private static final BitAppender PAD_START = new BitAppender() {

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuffer out) {
            out.put(value ? (byte) '1' : (byte) '0');
            return SUBSEQUENT;
        }

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuf out) {
            out.writeByte(value ? '1' : '0');
            return SUBSEQUENT;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuilder out) {
            out.append(value ? '1' : '0');
            return SUBSEQUENT;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuffer out) {
            out.append(value ? '1' : '0');
            return SUBSEQUENT;
        }

        @Override
        public void finish(@NotNull ByteBuffer out) {
            // pass
        }

        @Override
        public void finish(@NotNull ByteBuf out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            // pass
        }
    };

    private static final BitAppender SKIP_LEADING_ZERO = new BitAppender() {

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuffer out) {
            if (value) {
                out.put((byte) '1');
                return SUBSEQUENT;
            }
            return this;
        }

        @NotNull
        @Override
        public BitAppender write(boolean value, @NotNull ByteBuf out) {
            if (value) {
                out.writeByte('1');
                return SUBSEQUENT;
            }
            return this;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuilder out) {
            if (value) {
                out.append('1');
                return SUBSEQUENT;
            }
            return this;
        }

        @NotNull
        @Override
        public BitAppender append(boolean value, @NotNull StringBuffer out) {
            if (value) {
                out.append('1');
                return SUBSEQUENT;
            }
            return this;
        }

        @Override
        public void finish(@NotNull ByteBuffer out) {
            out.put((byte) '0');
        }

        @Override
        public void finish(@NotNull ByteBuf out) {
            out.writeByte('0');
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            out.append('0');
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            out.append('0');
        }
    };

    @NotNull
    private final BitAppender start;

    private int groupSize;

    public GroupedBase2Appender(boolean pad, int groupSize) {
        super();
        if (groupSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.start = pad ? PAD_START : SKIP_LEADING_ZERO;
        this.groupSize = groupSize;
    }

    public GroupedBase2Appender(boolean pad) {
        this(pad, Integer.MAX_VALUE);
    }

    public GroupedBase2Appender(int groupSize) {
        this(false, groupSize);
    }

    public GroupedBase2Appender() {
        this(false, Integer.MAX_VALUE);
    }

    public boolean isPad() {
        return start == PAD_START;
    }

    public int getGroupSize() {
        return groupSize;
    }

    @Override
    public void write(byte value, @NotNull ByteBuffer out) {
        out.put((byte) '0');
        out.put((byte) 'b');
        BitAppender appender = start;
        int shift = Byte.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.put((byte) '_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void write(byte value, @NotNull ByteBuf out) {
        out.writeByte('0');
        out.writeByte('b');
        BitAppender appender = start;
        int shift = Byte.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.writeByte('_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Byte.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Byte.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
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
        out.put((byte) '0');
        out.put((byte) 'b');
        BitAppender appender = start;
        int shift = Short.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.put((byte) '_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void write(short value, @NotNull ByteBuf out) {
        out.writeByte('0');
        out.writeByte('b');
        BitAppender appender = start;
        int shift = Short.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.writeByte('_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Short.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Short.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
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
        out.put((byte) '0');
        out.put((byte) 'b');
        BitAppender appender = start;
        int shift = Character.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.put((byte) '_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void write(char value, @NotNull ByteBuf out) {
        out.writeByte('0');
        out.writeByte('b');
        BitAppender appender = start;
        int shift = Character.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.writeByte('_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Character.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(char value, @NotNull StringBuffer out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Character.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
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
        out.put((byte) '0');
        out.put((byte) 'b');
        BitAppender appender = start;
        int shift = Integer.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.put((byte) '_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void write(int value, @NotNull ByteBuf out) {
        out.writeByte('0');
        out.writeByte('b');
        BitAppender appender = start;
        int shift = Integer.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.writeByte('_');
            }
            shift--;
            appender = appender.write((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Integer.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Integer.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
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
        out.put((byte) '0');
        out.put((byte) 'b');
        BitAppender appender = start;
        int shift = Long.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.put((byte) '_');
            }
            shift--;
            appender = appender.write((value & (1L << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void write(long value, @NotNull ByteBuf out) {
        out.writeByte('0');
        out.writeByte('b');
        BitAppender appender = start;
        int shift = Long.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.writeByte('_');
            }
            shift--;
            appender = appender.write((value & (1L << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Long.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1L << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Long.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.append((value & (1L << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
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
        final int mark = out.position();
        out.put((byte) '0');
        out.put((byte) 'b');
        out.put(value.toString().getBytes(StandardCharsets.US_ASCII));
        if (out.get(mark + 2) == '-') {
            // replace "0b-" with "-0b"
            out.put(mark, (byte) '-');
            out.put(mark + 1, (byte) '0');
            out.put(mark + 2, (byte) 'b');
        }
    }

    @Override
    public void write(@NotNull BigInteger value, @NotNull ByteBuf out) {
        final int mark = out.writerIndex();
        out.writeByte('0');
        out.writeByte('b');
        out.writeCharSequence(value.toString(), StandardCharsets.US_ASCII);
        if (out.getByte(mark + 2) == '-') {
            // replace "0b-" with "-0b"
            out.setByte(mark, '-');
            out.setByte(mark + 1, '0');
            out.setByte(mark + 2, 'b');
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        final int mark = out.length();
        out.append("0b").append(value.toString(2));
        if (out.charAt(mark + 2) == '-') {
            // replace "0b-" with "-0b"
            out.setCharAt(mark, '-');
            out.setCharAt(mark + 1, '0');
            out.setCharAt(mark + 2, 'b');
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        final int mark = out.length();
        out.append("0b").append(value.toString(2));
        if (out.charAt(mark + 2) == '-') {
            // replace "0b-" with "-0b"
            out.setCharAt(mark, '-');
            out.setCharAt(mark + 1, '0');
            out.setCharAt(mark + 2, 'b');
        }
    }

    @NotNull
    @Override
    public String stringify(@NotNull BigInteger value) {
        if (value.signum() < 0) {
            return "-0b" + value.toString(2).substring(1);
        } else {
            return "0b" + value.toString(2);
        }
    }
}
