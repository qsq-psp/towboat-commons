package mujica.io.codec;

import mujica.io.hash.ByteFillPolicy;
import mujica.io.hash.DataView;
import mujica.io.hash.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import mujica.text.escape.Quote;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@CodeHistory(date = "2025/4/29", project = "UltraIO")
@CodeHistory(date = "2025/4/30")
public class Base32DecodeInputStream extends FilterInputStream implements Base32StreamingCodec {

    private long buffer;

    private static final int SRC_STEP = CODE_STEP;
    private static final int DST_STEP = MESSAGE_STEP;

    private int srcShift;
    private int dstShift;
    private int stopShift;

    @NotNull
    private BufferingPolicy policy;

    public Base32DecodeInputStream(@NotNull InputStream in, @NotNull BufferingPolicy policy) {
        super(in);
        this.policy = policy;
    }

    public Base32DecodeInputStream(@NotNull InputStream in) {
        this(in, BufferingPolicy.NORMAL);
    }

    @NotNull
    public DataView getDataView() {
        return new LongDataView(this::getBuffer, ByteFillPolicy.MIDDLE_TO_RIGHT, START_SHIFT);
    }

    @Override
    public long getBuffer() {
        return buffer;
    }

    @NotNull
    @Override
    public BufferingPolicy getPolicy() {
        return policy;
    }

    @Override
    public void setPolicyDelayed(@NotNull BufferingPolicy policy) {
        this.policy = policy;
    }

    @Override
    public void setPolicyImmediately(@NotNull BufferingPolicy policy) throws IOException {
        this.policy = policy;
        // todo
    }

    private boolean readEOF() throws IOException {
        int digit = in.read();
        if (digit == -1) {
            return true;
        }
        if ('A' <= digit && digit <= 'Z') {
            digit -= 'A';
        } else if ('a' <= digit && digit <= 'z') {
            digit -= 'z';
        } else if ('2' <= digit && digit <= '7') {
            digit += 26 - '2';
        } else {
            throw new BadCodeException(Quote.DEFAULT.apply((byte) digit) + " not in [2-7A-Za-z]");
        }
        srcShift -= SRC_STEP;
        buffer |= ((long) digit) << srcShift;
        return false;
    }

    private boolean readStop() throws IOException {
        int digit = in.read();
        if (digit == '=') {
            return true;
        }
        if ('A' <= digit && digit <= 'Z') {
            digit -= 'A';
        } else if ('a' <= digit && digit <= 'z') {
            digit -= 'z';
        } else if ('2' <= digit && digit <= '7') {
            digit += 26 - '2';
        } else {
            throw new BadCodeException(Quote.DEFAULT.apply((byte) digit) + " not in [2-7A-Za-z]");
        }
        srcShift -= SRC_STEP;
        buffer |= ((long) digit) << srcShift;
        return false;
    }

    @Override
    public int read() throws IOException {
        while (dstShift - DST_STEP < srcShift) {
            if (stopShift != 0) {
                while (stopShift < srcShift) {
                    int sign = in.read();
                    if (sign != '=') {
                        throw new BadCodeException(Quote.DEFAULT.apply((byte) sign) + " != '='");
                    }
                    stopShift += SRC_STEP;
                }
                buffer = 0L;
                srcShift = START_SHIFT;
                dstShift = START_SHIFT;
                stopShift = 0;
            }
            if (srcShift == 0) {
                assert dstShift == 0;
                buffer = 0L;
                srcShift = START_SHIFT;
                dstShift = START_SHIFT;
            }
            if (srcShift == START_SHIFT) {
                if (readEOF()) {
                    return -1;
                }
            } else {
                if (readStop()) {
                    if (srcShift / DST_STEP == (srcShift + (SRC_STEP - 1)) / DST_STEP) { // test pass trick
                        throw new IOException();
                    }
                    stopShift += SRC_STEP;
                }
            }
        }
        dstShift -= DST_STEP;
        return 0xff & (int) (buffer >> dstShift);
    }

    @Override
    public int read(@NotNull byte[] array, int offset, int length) throws IOException {
        int count = 0;
        for (int limit = Math.addExact(offset, length); offset < limit; offset++) {
            int value = read();
            if (value == -1) {
                if (count == 0) {
                    count = -1;
                }
                break;
            }
            array[offset] = (byte) value;
            count++;
        }
        return count;
    }
}
