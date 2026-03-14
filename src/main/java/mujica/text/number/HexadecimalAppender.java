package mujica.text.number;

import mujica.io.codec.Base16Case;
import mujica.reflect.modifier.CodeHistory;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@CodeHistory(date = "2026/3/6")
public class HexadecimalAppender extends IntegralAppender implements Base16Case {

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

    @NotNull
    private final char[] table;

    @CodeHistory(date = "2026/3/6")
    private interface NibbleAppender {

        @NotNull
        NibbleAppender accept(int value, @NotNull StringBuilder out);

        void finish(@NotNull StringBuilder out);

        // StringBuffer ...
    }

    @NotNull
    private final NibbleAppender subsequent = new NibbleAppender() {

        @NotNull
        @Override
        public NibbleAppender accept(int value, @NotNull StringBuilder out) {
            out.append(table[0xf & value]);
            return this;
        }

        @Override
        public void finish(@NotNull StringBuilder out) {
            // pass
        }
    };

    @NotNull
    private NibbleAppender createLeadingZero() {
        return new NibbleAppender() {

            @NotNull
            @Override
            public NibbleAppender accept(int value, @NotNull StringBuilder out) {
                value &= 0xf;
                if (value == 0) {
                    return this;
                }
                out.append(table[value]);
                return subsequent;
            }

            @Override
            public void finish(@NotNull StringBuilder out) {
                out.append('0');
            }
        };
    }

    @NotNull
    private final NibbleAppender start;

    public HexadecimalAppender(boolean upperCase, boolean pad) {
        super();
        this.table = upperCase ? UPPER_TABLE : LOWER_TABLE;
        this.start = pad ? subsequent : createLeadingZero();
    }

    public boolean isUpperCase() {
        return this.table == UPPER_TABLE;
    }

    public boolean isPad() {
        return start == subsequent;
    }

    @Override
    public void acceptByte(byte value, @NotNull StringBuilder out) {
        start.accept(value >> 4, out.append("0x")).accept(value, out).finish(out);
    }

    @Override
    public void acceptShort(short value, @NotNull StringBuilder out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void acceptCharacter(char value, @NotNull StringBuilder out) {
        start.accept(value >> 12, out.append("0x")).accept(value >> 8, out).accept(value >> 4, out).accept(value, out).finish(out);
    }

    @Override
    public void acceptInteger(int value, @NotNull StringBuilder out) {
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
    public void acceptLong(long value, @NotNull StringBuilder out) {
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
    public void acceptBig(@NotNull BigInteger value, @NotNull StringBuilder out) {
        out.append("0x").append(value.toString(16));
    }
}
