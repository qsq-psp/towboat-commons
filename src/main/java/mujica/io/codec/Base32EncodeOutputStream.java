package mujica.io.codec;

import mujica.io.view.ByteFillPolicy;
import mujica.io.view.DataView;
import mujica.io.view.LongDataView;
import mujica.reflect.modifier.CodeHistory;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@CodeHistory(date = "2025/4/27", project = "UltraIO")
@CodeHistory(date = "2025/4/30")
public class Base32EncodeOutputStream extends FilterOutputStream implements Base32StreamingEncoder {

    private long buffer;

    private static final int SRC_STEP = MESSAGE_STEP; // 8
    private static final int DST_STEP = CODE_STEP; // 5

    private int srcShift;
    private int dstShift;

    private int alphabetOffset;

    @NotNull
    private BufferingPolicy policy;

    protected Base32EncodeOutputStream(@NotNull OutputStream out, @MagicConstant(valuesFromClass = Base32Case.class) int alphabetOffset, @NotNull BufferingPolicy policy) {
        super(out);
        this.alphabetOffset = alphabetOffset;
        this.policy = policy;
    }

    public Base32EncodeOutputStream(@NotNull OutputStream out, boolean upperCase, @NotNull BufferingPolicy policy) {
        super(out);
        setUpperCase(upperCase);
        this.policy = policy;
    }

    public Base32EncodeOutputStream(@NotNull OutputStream out, boolean upperCase) {
        this(out, upperCase, BufferingPolicy.NORMAL);
    }

    public Base32EncodeOutputStream(@NotNull OutputStream out, @NotNull BufferingPolicy policy) {
        this(out, true, policy);
    }

    public Base32EncodeOutputStream(@NotNull OutputStream out) {
        this(out, true, BufferingPolicy.NORMAL);
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

    @Override
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
        // todo
    }

    private void encode(int value) throws IOException {
        value &= 0x1f;
        if (value < 26) {
            value += alphabetOffset;
        } else {
            value += '2' - 26;
        }
        out.write(value);
    }

    private void encode() throws IOException {
        dstShift -= DST_STEP;
        encode((int) (buffer >> dstShift));
    }

    @Override
    public void write(int octet) throws IOException {
        if (srcShift == 0) {
            while (dstShift > 0) {
                encode();
            }
            buffer = 0L;
            srcShift = START_SHIFT;
            dstShift = START_SHIFT;
        }
        srcShift -= SRC_STEP;
        buffer |= (0xffL & octet) << srcShift;
        if (policy == BufferingPolicy.MINIMAL) {
            while (dstShift - DST_STEP >= srcShift) {
                encode();
            }
        } else if (policy == BufferingPolicy.NORMAL && srcShift == 0) {
            while (dstShift > 0) {
                encode();
            }
            buffer = 0L;
            srcShift = START_SHIFT;
            dstShift = START_SHIFT;
        }
    }

    @Override
    public boolean stop() throws IOException {
        if (srcShift == START_SHIFT) {
            return false;
        }
        while (dstShift > srcShift) {
            encode();
        }
        while (dstShift > 0) {
            dstShift -= DST_STEP;
            out.write('=');
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        if (stop()) {
            out.flush();
        }
        out.close();
    }
}
