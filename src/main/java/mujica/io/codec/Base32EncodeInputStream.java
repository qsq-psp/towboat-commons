package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.LongDataView;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created in UltraIO on 2025/4/28.
 * Moved here on 2025/4/30.
 */
public class Base32EncodeInputStream extends FilterInputStream implements Base32StreamingEncoder {

    private long buffer;

    private static final int SRC_STEP = MESSAGE_STEP; // 8
    private static final int DST_STEP = CODE_STEP; // 5

    private int srcShift;
    private int dstShift;

    private boolean stopped;

    private int alphabetOffset;

    @NotNull
    private BufferingPolicy policy;

    protected Base32EncodeInputStream(@NotNull InputStream in, @MagicConstant(valuesFromClass = Base32Case.class) int alphabetOffset, @NotNull BufferingPolicy policy) {
        super(in);
        this.alphabetOffset = alphabetOffset;
        this.policy = policy;
    }

    public Base32EncodeInputStream(@NotNull InputStream in, boolean upperCase, @NotNull BufferingPolicy policy) {
        super(in);
        setUpperCase(upperCase);
        this.policy = policy;
    }

    public Base32EncodeInputStream(@NotNull InputStream in, boolean upperCase) {
        this(in, upperCase, BufferingPolicy.NORMAL);
    }

    public Base32EncodeInputStream(@NotNull InputStream in, @NotNull BufferingPolicy policy) {
        this(in, true, policy);
    }

    public Base32EncodeInputStream(@NotNull InputStream in) {
        this(in, true, BufferingPolicy.NORMAL);
    }

    @Override
    public boolean isUpperCase() {
        return alphabetOffset == UPPER;
    }

    @Override
    public void setUpperCase(boolean upper) {
        if (upper) {
            alphabetOffset = UPPER;
        } else {
            alphabetOffset = LOWER;
        }
    }

    @NotNull
    @Override
    public DataView getDataView() {
        return new LongDataView(this::getBuffer, ByteFillPolicy.MIDDLE_TO_RIGHT, START_SHIFT);
    }

    @Override
    public long getBuffer() {
        return buffer;
    }

    @NotNull
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
    }

    private boolean readOne() throws IOException {
        final int value = in.read();
        if (value == -1) {
            if (!stopped) {
                stopped = true;
                if (srcShift == START_SHIFT) {
                    srcShift = 0;
                    dstShift = 0;
                }
            }
            return false;
        }
        srcShift -= SRC_STEP;
        buffer |= (0xffL & value) << srcShift;
        return true;
    }

    private int encode() {
        dstShift -= DST_STEP;
        int value = 0x1f & (int) (buffer >> dstShift);
        if (value < 26) {
            value += alphabetOffset;
        } else {
            value += '2' - 26;
        }
        return value;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public int read() throws IOException {
        while (!stopped) {
            if (dstShift - DST_STEP < srcShift) {
                if (srcShift == 0) {
                    assert dstShift == 0;
                    buffer = 0L;
                    srcShift = START_SHIFT;
                    dstShift = START_SHIFT;
                }
                if (policy == BufferingPolicy.MINIMAL) {
                    readOne();
                } else {
                    while (srcShift > 0 && readOne());
                }
            } else {
                int value = encode();
                if (policy == BufferingPolicy.GREEDY && dstShift == 0) {
                    assert srcShift == 0;
                    buffer = 0L;
                    srcShift = START_SHIFT;
                    dstShift = START_SHIFT;
                }
                if (policy != BufferingPolicy.MINIMAL) {
                    while (srcShift > 0 && readOne());
                }
                return value;
            }
        }
        if (dstShift > 0) {
            if (srcShift < dstShift) {
                return encode();
            } else {
                dstShift -= DST_STEP;
                return '=';
            }
        } else {
            return -1;
        }
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

    @Override
    public boolean stop() throws IOException {
        // todo
        return false;
    }
}
