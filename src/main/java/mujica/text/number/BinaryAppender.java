package mujica.text.number;

import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/3/9")
public class BinaryAppender extends IntegralAppender {

    private static final long serialVersionUID = 0x2CC9A7B00AAB4E91L;

    @CodeHistory(date = "2026/3/10")
    private interface BitAppender {

        @NotNull
        BitAppender accept(boolean value, @NotNull StringBuilder out);

        void finish(@NotNull StringBuilder out);
    }

    private static final BitAppender SUBSEQUENT = new BitAppender() {

        @NotNull
        @Override
        public BitAppender accept(boolean value, @NotNull StringBuilder out) {
            out.append(value ? '1' : '0');
            return this;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }
    };

    private static final BitAppender PAD_START = new BitAppender() {

        @NotNull
        @Override
        public BitAppender accept(boolean value, @NotNull StringBuilder out) {
            out.append(value ? '1' : '0');
            return SUBSEQUENT;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }
    };

    private static final BitAppender SKIP_LEADING_ZERO = new BitAppender() {

        @NotNull
        @Override
        public BitAppender accept(boolean value, @NotNull StringBuilder out) {
            if (value) {
                out.append('1');
                return SUBSEQUENT;
            }
            return this;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            out.append('0');
        }
    };

    @NotNull
    private final BitAppender start;

    private int groupSize;

    public BinaryAppender(boolean pad, int groupSize) {
        super();
        this.start = pad ? PAD_START : SKIP_LEADING_ZERO;
        setGroupSize(groupSize);
    }

    public BinaryAppender(boolean pad) {
        this(pad, Integer.MAX_VALUE);
    }

    public BinaryAppender(int groupSize) {
        this(false, groupSize);
    }

    public BinaryAppender() {
        this(false, Integer.MAX_VALUE);
    }

    public boolean isPad() {
        return start == PAD_START;
    }

    public int getGroupSize() {
        return groupSize;
    }

    @NotNull
    public BinaryAppender setGroupSize(int groupSize) {
        if (groupSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.groupSize = groupSize;
        return this;
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Byte.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.accept((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void acceptInteger(int value, @NotNull StringBuilder out) {
        out.append("0b");
        BitAppender appender = start;
        int shift = Integer.SIZE;
        do {
            if (appender != start && shift % groupSize == 0) {
                out.append('_');
            }
            shift--;
            appender = appender.accept((value & (1 << shift)) != 0, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append("0b").append(value.toString(2));
    }
}
