package mujica.text.number;

import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/3/6", name = "HexadecimalAppender")
@CodeHistory(date = "2026/4/27")
public class Base16Appender extends DefaultNumberAppender implements Base16Case {

    private static final long serialVersionUID = 0xE3B5B7095A05C245L;

    private static final char[] UPPER_TABLE = new char[0x10];

    private static final char[] LOWER_TABLE = new char[0x10];

    static {
        for (int i = 0; i < 0xa; i++) {
            UPPER_TABLE[i] = (char) ('0' + i);
            LOWER_TABLE[i] = (char) ('0' + i);
        }
        for (int i = 0xa; i < 0x10; i++) {
            UPPER_TABLE[i] = (char) (UPPER_CONSTANT + i);
            LOWER_TABLE[i] = (char) (LOWER_CONSTANT + i);
        }
    }

    @CodeHistory(date = "2026/3/6")
    private interface NibbleAppender {

        @NotNull
        NibbleAppender accept(int value, @NotNull StringBuilder out);

        @NotNull
        NibbleAppender accept(int value, @NotNull StringBuffer out);

        void finish(@NotNull StringBuilder out);

        void finish(@NotNull StringBuffer out);
    }

    private static final NibbleAppender UPPER_SUBSEQUENT = new NibbleAppender() {

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuilder out) {
            out.append(UPPER_TABLE[0xf & value]);
            return this;
        }

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuffer out) {
            out.append(UPPER_TABLE[0xf & value]);
            return this;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            // pass
        }

        @Override
        public String toString() {
            return "upper, pad";
        }
    };

    private static final NibbleAppender UPPER_START = new NibbleAppender() {

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuilder out) {
            value &= 0xf;
            if (value == 0) {
                return this;
            }
            out.append(UPPER_TABLE[value]);
            return UPPER_SUBSEQUENT;
        }

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuffer out) {
            value &= 0xf;
            if (value == 0) {
                return this;
            }
            out.append(UPPER_TABLE[value]);
            return UPPER_SUBSEQUENT;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            out.append('0');
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            out.append('0');
        }

        @Override
        public String toString() {
            return "upper, no-pad";
        }
    };

    private static final NibbleAppender LOWER_SUBSEQUENT = new NibbleAppender() {

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuilder out) {
            out.append(LOWER_TABLE[0xf & value]);
            return this;
        }

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuffer out) {
            out.append(LOWER_TABLE[0xf & value]);
            return this;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            // pass
        }

        @Override
        public String toString() {
            return "lower, pad";
        }
    };

    private static final NibbleAppender LOWER_START = new NibbleAppender() {

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuilder out) {
            value &= 0xf;
            if (value == 0) {
                return this;
            }
            out.append(LOWER_TABLE[value]);
            return LOWER_SUBSEQUENT;
        }

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuffer out) {
            value &= 0xf;
            if (value == 0) {
                return this;
            }
            out.append(LOWER_TABLE[value]);
            return LOWER_SUBSEQUENT;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            out.append('0');
        }

        @Override
        public void finish(@NotNull StringBuffer out) {
            out.append('0');
        }

        @Override
        public String toString() {
            return "lower, no-pad";
        }
    };

    @NotNull
    private final NibbleAppender start;

    public Base16Appender(boolean upperCase, boolean pad) {
        super();
        if (upperCase) {
            if (pad) {
                start = UPPER_SUBSEQUENT;
            } else {
                start = UPPER_START;
            }
        } else {
            if (pad) {
                start = LOWER_SUBSEQUENT;
            } else {
                start = LOWER_START;
            }
        }
    }

    @Override
    public void append(byte value, @NotNull StringBuilder out) {
        start.accept(value >> 4, out.append("0x")).accept(value, out).finish(out);
    }

    @Override
    public void append(byte value, @NotNull StringBuffer out) {
        start.accept(value >> 4, out.append("0x")).accept(value, out).finish(out);
    }

    @Override
    public void append(short value, @NotNull StringBuilder out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void append(short value, @NotNull StringBuffer out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void append(char value, @NotNull StringBuilder out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void append(char value, @NotNull StringBuffer out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void append(int value, @NotNull StringBuilder out) {
        out.append("0x");
        NibbleAppender appender = start;
        int shift = Integer.SIZE;
        do {
            shift -= 4;
            appender = appender.accept(value >> shift, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(int value, @NotNull StringBuffer out) {
        out.append("0x");
        NibbleAppender appender = start;
        int shift = Integer.SIZE;
        do {
            shift -= 4;
            appender = appender.accept(value >> shift, out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(long value, @NotNull StringBuilder out) {
        out.append("0x");
        NibbleAppender appender = start;
        int shift = Long.SIZE;
        do {
            shift -= 4;
            appender = appender.accept((int) (value >> shift), out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(long value, @NotNull StringBuffer out) {
        out.append("0x");
        NibbleAppender appender = start;
        int shift = Long.SIZE;
        do {
            shift -= 4;
            appender = appender.accept((int) (value >> shift), out);
        } while (shift > 0);
        appender.finish(out);
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuilder out) {
        final int pos = out.length();
        out.append("0x").append(value.toString(16));
        if (out.charAt(pos + 2) == '-') {
            // replace "0x-" with "-0x"
            out.setCharAt(pos, '-');
            out.setCharAt(pos + 1, '0');
            out.setCharAt(pos + 2, 'x');
        }
    }

    @Override
    public void append(@NotNull BigInteger value, @NotNull StringBuffer out) {
        final int pos = out.length();
        out.append("0x").append(value.toString(16));
        if (out.charAt(pos + 2) == '-') {
            // replace "0x-" with "-0x"
            out.setCharAt(pos, '-');
            out.setCharAt(pos + 1, '0');
            out.setCharAt(pos + 2, 'x');
        }
    }

    @Override
    public String toString() {
        return "HexadecimalAppender[" + start + "]";
    }
}
